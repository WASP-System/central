/**
 * 
 */
package edu.yu.einstein.wasp.plugin.babraham.software;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.plugin.babraham.batch.service.impl.BabrahamBatchServiceImpl;
import edu.yu.einstein.wasp.plugin.babraham.charts.BabrahamQCParseModule;
import edu.yu.einstein.wasp.plugin.babraham.exception.BabrahamDataParseException;
import edu.yu.einstein.wasp.plugin.babraham.service.BabrahamService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * @author calder / asmclellan
 *
 */
@Transactional("entityManager")
public class FastQScreen extends SoftwarePackage {
	
	@Autowired
	BabrahamService babrahamService;
	
	@Autowired
	FileService fileService;
	
	@Autowired
	private MessageService messageService;

	/**
	 * 
	 */
	private static final long serialVersionUID = -2863597794926320890L;

	/**
	 * Don't use the whole sequence file to search when performing fastq_screen; instead create a temporary dataset of size subset
	 */
	private static final long subset = 500000L;
	
	public static final String FASTQ_SCREEN_AREA = "fastq_screen";
	
	public static final String COMBINATION_FASTQ_FILE = "all.fastq";
	
	public static final String OUTPUT_FOLDER = "screenResults";
	
	/**
	 * 
	 */
	public FastQScreen() {
	}


	public WorkUnit getFastQScreen(Integer fileGroupId) {
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
				
		// require fastqc
		List<SoftwarePackage> software = new ArrayList<SoftwarePackage>();
		software.add(this);
		c.setSoftwareDependencies(software);
		
		// require 1GB memory
		c.setMemoryRequirements(1);
		
		// require a single thread, execution mode PROCESS
		// indicates this is a vanilla exectuion.
		c.setProcessMode(ProcessMode.SINGLE);
		c.setMode(ExecutionMode.PROCESS);
		
		// set working directory to scratch
		c.setWorkingDirectory(WorkUnitGridConfiguration.SCRATCH_DIR_PLACEHOLDER);
		
		// we aren't actually going to retain any files, so we will set the output
		// directory to the scratch directory.  Also set "secure results" to
		// false to indicate that we don't care about the output.
		c.setResultsDirectory(WorkUnitGridConfiguration.SCRATCH_DIR_PLACEHOLDER);
		WorkUnit w = new WorkUnit(c);
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
		//we'll just use the forward reads for fastq_screen
		FileGroup fileGroup = fileService.getFileGroupById(fileGroupId);
		List<FileHandle> files = babrahamService.getUpToFiveRandomForwardReadFiles(fileGroup); 
		w.setRequiredFiles(files);
		
		// set the command
		w.setCommand(getCommand(fileGroup));
		
		return w;
	}
	
	
	
	/**
	 * Set the fastqscreen command. 
	 * 
	 * @param fileGroup
	 * @return String
	 */
	private String getCommand(FileGroup fileGroup) {
		
		String command = "";

		String opts = "--aligner bowtie2 --quiet --subset " + subset;
		
		command = "";
		command += "mkdir " + OUTPUT_FOLDER + "\n";
		command += "zcat ${" + WorkUnit.INPUT_FILE + "[@]} >> " + COMBINATION_FASTQ_FILE + " && fastq_screen " + opts + " --outdir " + 
				OUTPUT_FOLDER + " " + COMBINATION_FASTQ_FILE + " && rm " + COMBINATION_FASTQ_FILE + "\n";
		return command;
	}
	
	/**
	 * This method takes a grid result of a successfully run FastQScreen job, gets the working directory
	 * and uses it to parse the <em>*_screen.txt</em> file into a JSONObject representing the
	 * data.  
	 * @param result
	 * @return
	 * @throws GridException
	 * @throws BabrahamDataParseException
	 * @throws JSONException 
	 */
	public JSONObject parseOutput(String resultsDir) throws GridException, BabrahamDataParseException, JSONException {
		return BabrahamQCParseModule.getParsedFastqScreenStatistics(((BabrahamBatchServiceImpl) babrahamService).parseFastQScreenOutput(resultsDir), messageService);
	}
}
