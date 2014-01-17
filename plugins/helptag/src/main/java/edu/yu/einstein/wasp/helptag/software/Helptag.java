/**
 * Created by Wasp System Eclipse Plugin
 * @author AJ
 */
package edu.yu.einstein.wasp.helptag.software;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.exception.NullResourceException;
import edu.yu.einstein.wasp.exception.ParameterValueRetrievalException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;
// Un-comment the following if using the plugin service
// import org.springframework.beans.factory.annotation.Autowired;
// import package edu.yu.einstein.wasp.helptag.service. HelptagService;




/**
 * @author
 */
public class Helptag extends SoftwarePackage{

	// Un-comment the following if using the plugin service
	//@Autowired
	//HelptagService  helptagService;
	
	@Autowired
	FileService fileService;
	
	@Autowired
	SampleService sampleService;
	
	
	@Autowired
	private GenomeService genomeService;	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6995954039217461596L;

	public static final String HELPTAG_AREA = "helptag";
	
	public static final String OUTPUT_FOLDER = "helptagResults";

	/**
	 * 
	 */
	public Helptag() {
		setSoftwareVersion("0.0.1"); // TODO: Set this value. This default may also be overridden in wasp.site.properties
	}

	public WorkUnit getHelptag(Integer libraryCellId) {
		WorkUnit w = new WorkUnit();
		
		// require fastqc
		List<SoftwarePackage> software = new ArrayList<SoftwarePackage>();
		software.add(this);
		w.setSoftwareDependencies(software);
		
		// require 1GB memory
		w.setMemoryRequirements(1);
		
		// require a single thread, execution mode PROCESS
		// indicates this is a vanilla exectuion.
		w.setProcessMode(ProcessMode.SINGLE);
		w.setMode(ExecutionMode.PROCESS);
		
		// set working directory to scratch
		w.setWorkingDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		
		// we aren't actually going to retain any files, so we will set the output
		// directory to the scratch directory.  Also set "secure results" to
		// false to indicate that we don't care about the output.
		w.setResultsDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		w.setSecureResults(false);
		
		
		// set the command
		//String cmd = "java -jar $GATK_ROOT/GenomeAnalysisTK.jar -nt 4 -I ${" + WorkUnit.INPUT_FILE + "} -R " + getGenomeIndexPath(getGenomeBuild(libraryCell)) + "genome.fasta -T RealignerTargetCreator -o ${" + WorkUnit.JOB_NAME + "}.realign.intervals -known /cork/jcai/GATK_bundle_2.2/1000G_phase1.indels.hg19.vcf -known /cork/jcai/GATK_bundle_2.2/Mills_and_1000G_gold_standard.indels.hg19.vcf";
//		String cmd = "/home/epigenscripts/bin/htg_aln2hcount.sh ${" + WorkUnit.INPUT_FILE + "} out.${" + WorkUnit.INPUT_FILE + "} " + getGenomeIndexPath(getGenomeBuild(libraryCell));
//		logger.debug("Will conduct helptag hcount generation with command: " + cmd);
		
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
//		FileGroup fg = fileService.getFileGroupById(filegroupId);
		
		FileType ft = fileService.getFileType("fastq");
		Set<FileGroup> fgSet = new HashSet<FileGroup>();
		SampleSource cl;
		try {
			cl = sampleService.getCellLibraryBySampleSourceId(libraryCellId);
			fgSet.addAll(fileService.getFilesForCellLibraryByType(cl, ft));
		} catch (SampleTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<FileHandle> files = new ArrayList<FileHandle>();
		for(FileGroup fg : fgSet) {
			files.addAll(fg.getFileHandles());
		}
//		for (FileHandle fh : files) {
//			logger.info(fh.getFileURI().toString());
//		}
		//Collections.sort(files, new FastqComparator(fastqService));
		w.setRequiredFiles(files);
		
		// set the command
		String cmd = getCommand(fgSet);
		w.setCommand(cmd);
		
		return w;
	}

	/**
	 * Set the helptag command. 
	 * 
	 * @param fileGroup
	 * @return String
	 */
	private String getCommand(Set<FileGroup> fileGroupSet) {
		
		String command = "";

		String optStr = "--base 27 --sort ";
		
		command += "mkdir " + OUTPUT_FOLDER + "\n";
		logger.info("WorkUnit.INPUT_FILE: " + WorkUnit.INPUT_FILE);
		//command += "/home/epigenscripts/bin/helptag_script.pl ${" + WorkUnit.INPUT_FILE + "[@]} " + optStr + " --outdir " + OUTPUT_FOLDER + "\n";
		return command;
	}

	
	
	private WorkUnit prepareWorkUnit(FileGroup fg) {
		WorkUnit w = new WorkUnit();
		
		w.setMode(ExecutionMode.PROCESS);
	
		// require 4GB memory
		w.setMemoryRequirements(4);

		List<FileHandle> fhlist = new ArrayList<FileHandle>();
		fhlist.addAll(fg.getFileHandles());
		w.setRequiredFiles(fhlist);
		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(this);
		w.setSoftwareDependencies(sd);
		w.setSecureResults(false);
	
		return w;
	}
	
	private Build getGenomeBuild(SampleSource cellLibrary) {
		Build build = null;
		try {
			Sample library = sampleService.getLibrary(cellLibrary);
			logger.debug("looking for genome build associated with sample: " + library.getId());
			build = genomeService.getBuild(library);
			if (build == null) {
				String mess = "cell library does not have associated genome build metadata annotation";
				logger.error(mess);
				throw new NullResourceException(mess);
			}
			logger.debug("genome build: " + build.getGenome().getName() + "::" + build.getName());
		} catch (ParameterValueRetrievalException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return build;
	}
	
	private String getGenomeIndexPath(Build build) {
		String index = genomeService.getRemoteBuildPath(build) + "gatk/";
		return index;
	}

}
