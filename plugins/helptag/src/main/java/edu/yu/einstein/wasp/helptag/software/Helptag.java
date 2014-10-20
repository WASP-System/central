/**
 * Created by Wasp System Eclipse Plugin
 * @author AJ
 */
package edu.yu.einstein.wasp.helptag.software;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.NullResourceException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
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
	
	@Autowired
	private FileType samFileType;
	
	@Autowired
	private FileType fastqFileType;

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
	}

	@Transactional("entityManager")
	public WorkUnit getHelptag(Integer cellLibraryId) {
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
			
		// require fastqc
		List<SoftwarePackage> software = new ArrayList<SoftwarePackage>();
		//software.add(this);
		//software.add(samtools);
		c.setSoftwareDependencies(software);
		
		// require 4GB memory
		c.setMemoryRequirements(4);
		
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
		
		
		// set the command
		//String cmd = "java -jar $GATK_ROOT/GenomeAnalysisTK.jar -nt 4 -I ${" + WorkUnit.INPUT_FILE + "} -R " + getGenomeIndexPath(getGenomeBuild(cellLibrary)) + "genome.fasta -T RealignerTargetCreator -o ${" + WorkUnit.JOB_NAME + "}.realign.intervals -known /cork/jcai/GATK_bundle_2.2/1000G_phase1.indels.hg19.vcf -known /cork/jcai/GATK_bundle_2.2/Mills_and_1000G_gold_standard.indels.hg19.vcf";
//		String cmd = "/home/epigenscripts/bin/htg_aln2hcount.sh ${" + WorkUnit.INPUT_FILE + "} out.${" + WorkUnit.INPUT_FILE + "} " + getGenomeIndexPath(getGenomeBuild(cellLibrary));
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
		
		//FileType ft = fileService.getFileType("bam");
		Set<FileGroup> fgSet = new HashSet<FileGroup>();
		SampleSource cl;
		
		try {
			cl = sampleService.getCellLibraryBySampleSourceId(cellLibraryId);
			fgSet.addAll(fileService.getFilesForCellLibraryByType(cl, fastqFileType));

			Job job = sampleService.getJobOfLibraryOnCell(cl);
			c.setResultsDirectory(fileService.generateJobSoftwareBaseFolderName(job, this) + "/" + cellLibraryId);
		
			List<FileHandle> files = new ArrayList<FileHandle>();
			for(FileGroup fg : fgSet) {
				files.addAll(fg.getFileHandles());
			}
			logger.info("Helptag pipeline # of input files: "+files.size());
			for (FileHandle fh : files) {
				logger.info("Helptag pipeline input files: "+fh.getFileURI().toString());
			}
			//Collections.sort(files, new FastqComparator(fastqService));
			w.setRequiredFiles(files);
			
			// set the command
			String cmd = "module load helptag/0.1.0;" + 
					"bam2hcount.pl " +
					"-i ${" + WorkUnit.INPUT_FILE + "} " +
					"-o ${" + WorkUnit.INPUT_FILE + "}.hcount " +
					"-g " + getGenomeBuild(cl).getGenome().getAlias();
			w.setCommand(cmd);
		} catch (SampleTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return w;
	}

	
	private WorkUnit prepareWorkUnit(FileGroup fg) {
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
			
		c.setMode(ExecutionMode.PROCESS);
	
		// require 4GB memory
		c.setMemoryRequirements(4);
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(this);
		c.setSoftwareDependencies(sd);
		WorkUnit w = new WorkUnit(c);
		List<FileHandle> fhlist = new ArrayList<FileHandle>();
		fhlist.addAll(fg.getFileHandles());
		w.setRequiredFiles(fhlist);
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
