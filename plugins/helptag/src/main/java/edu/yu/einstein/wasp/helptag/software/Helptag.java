/**
 * Created by Wasp System Eclipse Plugin
 * @author AJ
 */
package edu.yu.einstein.wasp.helptag.software;

import java.util.ArrayList;
import java.util.List;

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
import edu.yu.einstein.wasp.service.JobService;
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
	JobService jobService;

	@Autowired
	private GenomeService genomeService;	
	
	@Autowired
	private FileType bamFileType;

	@Autowired
	private FileType fastqFileType;

	/**
	 * 
	 */
	private static final long serialVersionUID = -6995954039217461596L;

	public static final String HELPTAG_AREA = "helptag";

	/**
	 * 
	 */
	public Helptag() {
	}

	private WorkUnit prepareWorkUnit(List<FileHandle> fhlist) {
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();

		c.setProcessMode(ProcessMode.MAX);
		c.setMode(ExecutionMode.PROCESS);

		// require 4GB memory
		c.setMemoryRequirements(4);
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(this);
		c.setSoftwareDependencies(sd);
		c.setWorkingDirectory(WorkUnitGridConfiguration.SCRATCH_DIR_PLACEHOLDER);
		WorkUnit w = new WorkUnit(c);
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

	@Transactional("entityManager")
	public WorkUnit getHpaiiCounter(Integer cellLibraryId) {
		WorkUnit w = null;
		SampleSource cl;
		try {
			cl = sampleService.getCellLibraryBySampleSourceId(cellLibraryId);
		
			List<FileHandle> inputBamFiles = new ArrayList<FileHandle>();
			for(FileGroup fg : fileService.getFilesForCellLibraryByType(cl, bamFileType)) {
				inputBamFiles.addAll(fg.getFileHandles());
			}
			logger.info("Helptag pipeline # of input files: "+inputBamFiles.size());
			for (FileHandle fh : inputBamFiles) {
				logger.info("Helptag pipeline input files: "+fh.getFileURI().toString());
			}
			// create work unit with preset sge params and input bam files
			w = prepareWorkUnit(inputBamFiles);

			Job job = sampleService.getJobOfLibraryOnCell(cl);
			w.getConfiguration().setResultsDirectory(fileService.generateJobSoftwareBaseFolderName(job, this) + "/" + cellLibraryId);
			
			StringBuilder mergeBamCmd;
			String mergedBamFile = "";
			if(inputBamFiles.size()==1){
				mergedBamFile = "${" + WorkUnit.INPUT_FILE + "[0]}";
			}
			else if(inputBamFiles.size() > 1){
				mergedBamFile = "tmpMergedBamFile.bam";
				
				mergeBamCmd = new StringBuilder();
				mergeBamCmd.append("samtools merge " + mergedBamFile + " ");
				//tmpMergedBamFile.bam is the output of the merge; note that merge requires sorted files
				
				for(int i = 0; i < inputBamFiles.size(); i++){				
					mergeBamCmd.append("${" + WorkUnit.INPUT_FILE + "["+i+"]} ");				
				}

				w.addCommand(new String(mergeBamCmd));
			}
			
			// output hcount file name
			String hcountFile = fileService.generateUniqueBaseFileName(cl) + "hcount";
			
			// set the command
			String cmd = "bam2hcount.pl " +
					"-i " + mergedBamFile + " " +
					"-o " + hcountFile + " " +
					"-g " + getGenomeBuild(cl).getGenome().getAlias();
			w.addCommand(cmd);
		} catch (SampleTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return w;
	}
}
