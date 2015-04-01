/**
 * 
 */
package edu.yu.einstein.wasp.helptag.batch.tasklet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ProcessMode;
import edu.yu.einstein.wasp.helptag.service.HelptagService;
import edu.yu.einstein.wasp.helptag.software.Helptag;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.FastqComparator;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;
import edu.yu.einstein.wasp.plugin.genomemetadata.batch.tasklet.TestForGenomeIndexTasklet;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * @author AJ
 *
 */
public class HpaiiCountTasklet extends TestForGenomeIndexTasklet implements StepExecutionListener {

	@Autowired
	private FileService fileService;

	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private HelptagService helptagService;

	@Autowired
	private Helptag helptag;
	
	@Autowired
	private GridHostResolver gridHostResolver;
	
	@Autowired
	private FileType hcountFileType;

	@Autowired
	private FileType bamFileType;

	private Integer cellLibraryId;
	
	private Integer fgId;

	private boolean skip = false;

	/**
	 * 
	 */
	public HpaiiCountTasklet() {
		// proxy
	}

	public HpaiiCountTasklet(String cellLibraryId) {
		Assert.assertParameterNotNull(cellLibraryId);
		this.cellLibraryId = Integer.valueOf(cellLibraryId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional("entityManager")
	public GridResult doExecute(ChunkContext context) throws Exception {
		ExecutionContext stepExecutionContext = context.getStepContext().getStepExecution().getExecutionContext();

		try {
			WorkUnit w = buildWorkUnit(context.getStepContext().getStepExecution());
			GridResult result = executeWorkUnit(context, w);
			// place properties for use in later steps into the step execution context, to be promoted
			// to the job context at run time.
			stepExecutionContext.put("cellLibId", cellLibraryId);
			stepExecutionContext.put("scratchDir", result.getWorkingDirectory());
			stepExecutionContext.put("hcountJobName", result.getId());
			stepExecutionContext.put("hcountCommand", w.getCommand());
			return result;
		} catch (ParameterValueRetrievalException e) {
			logger.info("Hpa2Count requested but got ParameterValueRetrievalException: " + e.getLocalizedMessage()
						+ ". Assume Hpa2Count not possible, returning SKIP.");
			skip = true;
			return null;
		}

		WorkUnit w = helptag.getHpaiiCounter(cellLibraryId);
		
		LinkedHashSet<FileHandle> files = new LinkedHashSet<FileHandle>();
		SampleSource cl = sampleService.getCellLibraryBySampleSourceId(cellLibraryId);

		String hcountFileName = fileService.generateUniqueBaseFileName(cl) + "hcount";

		FileGroup hcountG = new FileGroup();
		FileHandle hcountF = new FileHandle();
		hcountF.setFileName(hcountFileName);
		hcountF.setFileType(hcountFileType);
		hcountF = fileService.addFile(hcountF);
		hcountG.setIsActive(1);
		hcountG.addFileHandle(hcountF);
		files.add(hcountF);

		hcountG.setFileType(hcountFileType);
		hcountG.setDerivedFrom(fileService.getFilesForCellLibraryByType(cl, bamFileType));
		hcountG.setDescription(hcountFileName);
		hcountG.setSoftwareGeneratedById(helptag.getId());
		hcountG = fileService.addFileGroup(hcountG);
		fileService.setSampleSourceFile(hcountG, cl);

		// save in step context for use later
		stepExecutionContext.put("hcountGID", hcountG.getId());

		w.setResultFiles(files);

		logger.info("Before submitting hpaii count batch job");

		GridResult result = gridHostResolver.execute(w);

		logger.info("Batch job execution submitted with id=" + result.getGridJobId() + " on host '" + result.getHostname());

		return result;
	}
	
	public static void doWork(int cellLibraryId) {
		
	}
	
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return super.afterStep(stepExecution);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		super.beforeStep(stepExecution);
		logger.debug("StepExecutionListener beforeStep saving StepExecution");
	}

	@Override
	public void doCleanupBeforeRestart(StepExecution stepExecution) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transactional("entityManager")
	public GenomeIndexStatus getGenomeIndexStatus(StepExecution stepExecution) {
		try {
			SampleSource cellLib = sampleService.getSampleSourceDao().findById(cellLibraryId);
			Build build = helptag.getGenomeBuild(cellLib);
			return helptagService.getGenomeIndexStatus(getGridWorkService(getStepExecutionContext(stepExecution)), build);
		} catch (ParameterValueRetrievalException | GridUnresolvableHostException e) {
			String mess = "Unable to determine build or build status " + e.getLocalizedMessage();
			logger.error(mess);
			throw new WaspRuntimeException(mess);
		}
	}

	@Override
	@Transactional("entityManager")
	public WorkUnitGridConfiguration configureWorkUnit(StepExecution stepExecution) throws Exception {
		FileGroup fg = fileService.getFileGroupById(fgId);

		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();

		c.setMode(ExecutionMode.TASK_ARRAY);
		c.setNumberOfTasks(fg.getFileHandles().size());

		c.setProcessMode(ProcessMode.MAX);

		c.setMemoryRequirements(8);

		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(helptag);
		c.setSoftwareDependencies(sd);
		c.setResultsDirectory(WorkUnitGridConfiguration.SCRATCH_DIR_PLACEHOLDER);
		return c;
	}

	@Override
	@Transactional("entityManager")
	public WorkUnit buildWorkUnit(StepExecution stepExecution) throws Exception {
		SampleSource cellLib = sampleService.getSampleSourceDao().findById(cellLibraryId);
		FileGroup fg = fileService.getFileGroupById(fgId);
		logger.debug("file group: " + fg.getId() + ":" + fg.getDescription());

		Map<String, JobParameter> jobParameters = stepExecution.getJobExecution().getJobParameters().getParameters();
		if (logger.isDebugEnabled()) {
			for (String key : jobParameters.keySet()) {
				logger.debug("Key: " + key + " Value: " + jobParameters.get(key).getValue().toString());
			}
		}

		WorkUnit w = helptag.getAln(cellLib, fg, jobParameters);
		List<FileHandle> fhlist = new ArrayList<FileHandle>();
		fhlist.addAll(fg.getFileHandles());
		Collections.sort(fhlist, new FastqComparator(fastqService));
		w.setRequiredFiles(fhlist);
		w.setSecureResults(false);
		return w;
	}

}
