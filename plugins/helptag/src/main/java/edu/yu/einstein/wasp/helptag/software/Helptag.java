/**
 * Created by Wasp System Eclipse Plugin
 * @author AJ
 */
package edu.yu.einstein.wasp.helptag.software;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ProcessMode;
import edu.yu.einstein.wasp.helptag.service.impl.HelptagServiceImpl.HelptagIndexType;
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

	@Transactional("entityManager")
	public WorkUnitGridConfiguration prepareWorkUnitConfiguration(FileGroup fg) {
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();

		c.setMode(ExecutionMode.TASK_ARRAY);
		c.setNumberOfTasks(fg.getFileHandles().size());

		c.setProcessMode(ProcessMode.MAX);

		c.setMemoryRequirements(8);

		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(this);
		c.setSoftwareDependencies(sd);
		c.setResultsDirectory(WorkUnitGridConfiguration.SCRATCH_DIR_PLACEHOLDER);
		return c;
	}

	@Transactional("entityManager")
	public WorkUnit buildWorkUnit(FileGroup fg) {
		WorkUnit w = new WorkUnit(prepareWorkUnitConfiguration(fg));
		List<FileHandle> fhlist = new ArrayList<FileHandle>();
		fhlist.addAll(fg.getFileHandles());
		// Collections.sort(fhlist, new FastqComparator(fastqService));
		w.setRequiredFiles(fhlist);
		w.setSecureResults(true);
		return w;
	}


	@Transactional("entityManager")
	public Build getGenomeBuild(SampleSource cellLibrary) throws ParameterValueRetrievalException {
		logger.debug("getting genome build for cellLibrary id=" + cellLibrary.getId());
		Build build = null;
		try {
			Sample library = sampleService.getLibrary(cellLibrary);
			logger.debug("looking for genome build associated with sample: " + library.getId());
			build = genomeService.getBuild(library);
			logger.debug("genome build: " + genomeService.getDelimitedParameterString(build));
		} catch (ParameterValueRetrievalException e) {
			String mess = "cell library " + cellLibrary.getId() + " does not have associated genome build metadata annotation";
			logger.info(mess);
			throw e;
		}
		return build;
	}

	protected String getGenomeIndexPath(Build build, HelptagIndexType type) {
		String indexType = type.toString().toLowerCase();
		String index = genomeService.getRemoteBuildPath(build) + "/helptag/" + indexType + "/" + build.getGenomeBuildNameString();
		return index;
	}

	protected String getGenomeIndexPath(Build build) {
		return getGenomeIndexPath(build, HelptagIndexType.GENOME);
	}

	@Transactional("entityManager")
	public WorkUnit getHpaiiCounter(SampleSource cl, FileGroup fg, Map<String, JobParameter> jobParameters) throws ParameterValueRetrievalException {
		WorkUnit w = buildWorkUnit(fg);
		
		Job job = sampleService.getJobOfLibraryOnCell(cl);
		w.getConfiguration().setResultsDirectory(fileService.generateJobSoftwareBaseFolderName(job, this) + "/" + cl.getId());

		String checkIndex = "if [ ! -e " + getGenomeIndexPath(getGenomeBuild(cl)) + ".hpa ]; then\n  exit 101;\nfi";
		w.setCommand(checkIndex);

		String bamFile = "${" + WorkUnit.INPUT_FILE + "[0]}";
		String outputHcountFilename = "${" + WorkUnit.OUTPUT_FILE + "[0]}";

		// set the command
		String cmd = "bam2hcount.pl -i " + bamFile + " -o " + outputHcountFilename + " -g " + getGenomeIndexPath(getGenomeBuild(cl)) + ".hpa";
		w.addCommand(cmd);

		logger.info("Helptag Hpaii Counter commands: \n" + w.getCommand());
		
		return w;
	}
}
