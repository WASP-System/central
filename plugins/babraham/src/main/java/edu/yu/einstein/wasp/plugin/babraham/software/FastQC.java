/**
 * 
 */
package edu.yu.einstein.wasp.plugin.babraham.software;


import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.SoftwareDao;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.plugin.babraham.batch.service.impl.BabrahamBatchServiceImpl;
import edu.yu.einstein.wasp.plugin.babraham.charts.BabrahamQCParseModule;
import edu.yu.einstein.wasp.plugin.babraham.exception.BabrahamDataParseException;
import edu.yu.einstein.wasp.plugin.babraham.service.BabrahamService;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.FastqComparator;
import edu.yu.einstein.wasp.plugin.fileformat.service.FastqService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.software.SoftwarePackage;



/**
 * @author calder / asmclellan
 *
 */
@Transactional("entityManager")
public class FastQC extends SoftwarePackage{

	
	@Autowired
	private FastqService fastqService;
	
	@Autowired
	private MessageService messageService;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7075104587205964069L;
	
	public static final String COMBINATION_FASTQ_FILE_PREFIX = "all";
	
	public static final String COMBINATION_FASTQ_FILE_NAME = COMBINATION_FASTQ_FILE_PREFIX + ".fastq";
	
	public static final String OUTPUT_ZIP_FILE_NAME = COMBINATION_FASTQ_FILE_PREFIX + "_fastqc.zip";
	
	public static final String OUTPUT_DATA_FILE_TO_EXTRACT = COMBINATION_FASTQ_FILE_PREFIX + "_fastqc/fastqc_data.txt";
	
	public static final String OUTPUT_FOLDER = "fastqcResults";
	
	public static final String QC_ANALYSIS_RESULT_KEY = "result";
	
	public static final String QC_ANALYSIS_RESULT_PASS = "pass";
	
	public static final String QC_ANALYSIS_RESULT_WARN = "warn";
	
	public static final String QC_ANALYSIS_RESULT_FAIL = "fail";
	
	public static class PlotType{
		// meta keys for charts produced
		public static final String QC_RESULT_SUMMARY = "result_summary";
		public static final String BASIC_STATISTICS = "fastqc_basic_statistics";
		public static final String DUPLICATION_LEVELS = "fastqc_duplication_levels";
		public static final String KMER_PROFILES = "fastqc_kmer_profiles";
		public static final String PER_BASE_GC_CONTENT = "fastqc_per_base_gc_content";
		public static final String PER_BASE_N_CONTENT = "fastqc_per_base_n_content";
		public static final String PER_BASE_QUALITY = "fastqc_per_base_quality";
		public static final String PER_BASE_SEQUENCE_CONTENT = "fastqc_per_base_sequence_content";
		public static final String PER_SEQUENCE_GC_CONTENT = "fastqc_per_sequence_gc_content";
		public static final String PER_SEQUENCE_QUALITY = "fastqc_per_sequence_quality";
		public static final String SEQUENCE_LENGTH_DISTRIBUTION = "fastqc_sequence_length_distribution"; 
		public static final String OVERREPRESENTED_SEQUENCES = "fastqc_overrepresented_sequences";
	}
	
	@Autowired
	BabrahamService babrahamService;
	
	@Autowired
	FileService fileService;
	
	@Autowired
	SoftwareDao softwareDao;
	
	/**
	 * 
	 */
	public FastQC() {
	}

	
	/**
	 * Takes a FileGroup and returns a configured WorkUnit to run FastQC on the file group.
	 * The output will be placed in folders that are numbered by their read segment number.
	 * e.g. a paired end run will generate two output folders, 1 and 2.
	 * 
	 * @param fileGroup
	 * @return
	 */
	public WorkUnit getFastQC(Integer fileGroupId) {
		
		WorkUnit w = new WorkUnit();
		
		// require fastqc
		List<SoftwarePackage> software = new ArrayList<SoftwarePackage>();
		software.add(this);
		w.setSoftwareDependencies(software);
		
		// require 1GB memory
		w.setMemoryRequirements(1);
		
		// require a single thread, execution mode PROCESS
		// indicates this is a vanilla execution.
		w.setProcessMode(ProcessMode.SINGLE);
		w.setMode(ExecutionMode.PROCESS);
		
		// set working directory to scratch
		w.setWorkingDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		
		// we aren't actually going to retain any files, so we will set the output
		// directory to the scratch directory.  Also set "secure results" to
		// false to indicate that we don't care about the output.
		w.setResultsDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		w.setSecureResults(false);
		
		// add the files to the work unit
		// files will be represented as bash variables in the work unit and
		// are sorted using the FastqComparator.  For single samples (what we have here)
		// this ensures that they will be in groups of read segments.
		// e.g.
		// s1.R1.001
		// s1.R2.001
		// s1.R1.002
		// s1.R2.002
		FileGroup fileGroup = fileService.getFileGroupById(fileGroupId);
		List<FileHandle> files = new ArrayList<FileHandle>(fileGroup.getFileHandles());
		Collections.sort(files, new FastqComparator(fastqService));
		w.setRequiredFiles(files);
		
		// set the command
		w.setCommand(getCommand(fileGroup));
		
		return w;
	}
	
	/**
	 * Set the fastqc command.  Choose casava mode if the data was generated by the Illumina plugin.
	 * 
	 * @param fileGroup
	 * @param workUnit
	 * @return
	 */
	private String getCommand(FileGroup fileGroup) {
		
		String command = "";
		int segments = fastqService.getNumberOfReadSegments(fileGroup);
		int files = fileGroup.getFileHandles().size();
		
		// fileList is an array of file name refs
		// 1 for each read segment.
		String[] fileList = new String[segments];
		
		for (int i = 0; i < segments; i++) {
			for (int j = 0; j < (files/segments); j++) {
				int index = ((j-1) * segments) + (i-1);
				if(fileList[i]==null)
					fileList[i] = " ${" + WorkUnit.INPUT_FILE + "[" + index + "]}";
				else
					fileList[i] += " ${" + WorkUnit.INPUT_FILE + "[" + index + "]}";
			}
		}
		
		String opts = "--noextract --nogroup --quiet";
		if (fileGroup.getSoftwareGeneratedBy().equals(getSoftwareDependencyByIname("casava")))
			opts += " --casava";
		
		command += "mkdir " + OUTPUT_FOLDER + "\n";
		command += "zcat ${" + WorkUnit.INPUT_FILE + "[@]} >> " + COMBINATION_FASTQ_FILE_NAME + " && fastqc " + opts + " --outdir " + 
				OUTPUT_FOLDER + " " + COMBINATION_FASTQ_FILE_NAME + " && rm " + COMBINATION_FASTQ_FILE_NAME + "\n";
		return command;
	}
	
	/**
	 * This method takes a grid result of a successfully run FastQC job, gets the working directory
	 * and uses it to parse the <em>fastqc_data.txt</em> file into a Map which contains
	 * static Strings defining the output keys (see above) and JSONObjects representing the
	 * data.  
	 * @param result
	 * @return
	 * @throws GridException
	 * @throws BabrahamDataParseException
	 * @throws JSONException 
	 */
	public Map<String,JSONObject> parseOutput(String resultsDir) throws GridException, BabrahamDataParseException, JSONException {
		Map<String,JSONObject> output = new LinkedHashMap<String, JSONObject>();
		Map<String, FastQCDataModule> mMap = ((BabrahamBatchServiceImpl) babrahamService).parseFastQCOutput(resultsDir);
		output.put(PlotType.QC_RESULT_SUMMARY, BabrahamQCParseModule.getParsedQCResults(mMap, messageService));
		output.put(PlotType.BASIC_STATISTICS, BabrahamQCParseModule.getParsedBasicStatistics(mMap, messageService));
		output.put(PlotType.PER_BASE_QUALITY, BabrahamQCParseModule.getParsedPerBaseQualityData(mMap, messageService));
		output.put(PlotType.PER_SEQUENCE_QUALITY, BabrahamQCParseModule.getPerSequenceQualityScores(mMap, messageService));
		output.put(PlotType.PER_BASE_SEQUENCE_CONTENT, BabrahamQCParseModule.getPerBaseSequenceContent(mMap, messageService));
		output.put(PlotType.PER_BASE_GC_CONTENT, BabrahamQCParseModule.getPerBaseGcContent(mMap, messageService));
		output.put(PlotType.PER_SEQUENCE_GC_CONTENT, BabrahamQCParseModule.getPerSequenceGcContent(mMap, messageService));
		output.put(PlotType.PER_BASE_N_CONTENT, BabrahamQCParseModule.getPerBaseNContent(mMap, messageService));
		output.put(PlotType.SEQUENCE_LENGTH_DISTRIBUTION, BabrahamQCParseModule.getSequenceLengthDist(mMap, messageService));
		output.put(PlotType.DUPLICATION_LEVELS, BabrahamQCParseModule.getSequenceDuplicationLevels(mMap, messageService));
		output.put(PlotType.OVERREPRESENTED_SEQUENCES, BabrahamQCParseModule.getOverrepresentedSequences(mMap, messageService));
		output.put(PlotType.KMER_PROFILES, BabrahamQCParseModule.getOverrepresentedKmers(mMap, messageService));
		return output;
	}
	



}
