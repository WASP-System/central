/**
 * 
 */
package edu.yu.einstein.wasp.helptag.batch.tasklet;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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
import edu.yu.einstein.wasp.helptag.service.HelptagService;
import edu.yu.einstein.wasp.helptag.software.Helptag;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;
import edu.yu.einstein.wasp.plugin.genomemetadata.batch.tasklet.TestForGenomeIndexTasklet;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.SampleService;

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

			w.setResultFiles(files);

			logger.info("Before submitting hpaii count batch job");
			GridResult result = executeWorkUnit(context, w);
			logger.info("Batch job execution submitted with id=" + result.getGridJobId() + " on host '" + result.getHostname());

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
	}
	
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	@Transactional("entityManager")
	public void beforeStep(StepExecution stepExecution) {
		SampleSource cellLib = sampleService.getSampleSourceDao().findById(cellLibraryId);
		Job job = sampleService.getJobOfLibraryOnCell(cellLib);

		logger.debug("Beginning Helptag hpa2count step for cellLibrary " + cellLib.getId() + " from job " + job.getId());

		Set<FileGroup> fileGroups = new HashSet<FileGroup>();
		;

		for (FileGroup fg : fileService.getFilesForCellLibraryByType(cellLib, bamFileType)) {
			fileGroups.add(fg);
		}

		Assert.assertTrue(fileGroups.size() == 1, "No BAM type filegroups returned for cellLibrary " + cellLib.getId() + " from job " + job.getId());
		fgId = fileGroups.iterator().next().getId();
		super.beforeStep(stepExecution);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if (skip == true) {
			return new ExitStatus("SKIP");
		}
		return super.afterStep(stepExecution);
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

		WorkUnit w = helptag.getHpaiiCounter(cellLib, fg, jobParameters);

		return w;
	}

	@Override
	@Transactional("entityManager")
	public WorkUnitGridConfiguration configureWorkUnit(StepExecution stepExecution) throws Exception {
		FileGroup fg = fileService.getFileGroupById(fgId);

		return helptag.prepareWorkUnitConfiguration(fg);
	}

}
