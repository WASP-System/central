package edu.yu.einstein.wasp.gatk.batch.tasklet;




/**
 * 
 */

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.gatk.software.GATKSoftwareComponent;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.SampleService;


/**
 * 
 */

public class HardFilterTasklet extends WaspRemotingTasklet implements StepExecutionListener {

	private String scratchDirectory;
	private String callVariantJobName;
	private String jobId;
	private Integer cellLibId;

	private StepExecution stepExecution;

	@Autowired
	private SampleService sampleService;

	@Autowired
	private FileService fileService;

	@Autowired
	private GridHostResolver gridHostResolver;

	@Autowired
	private FileType bamFileType;
	@Autowired
	private FileType fastqFileType;

	@Autowired
	private GATKSoftwareComponent gatk;
	
	public HardFilterTasklet() {
		// proxy
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doExecute(ChunkContext context) throws Exception {

		
		logger.debug("Beginning GATK hard filtering part based on the best practice ");
		logger.debug("Starting from previously scratch directory " + scratchDirectory);

		SampleSource sampleSource=sampleService.getSampleSourceDao().findById(cellLibId);
		WorkUnit w = gatk.getHardFilter(sampleSource, scratchDirectory, callVariantJobName);
		w.setResultsDirectory(WorkUnit.RESULTS_DIR_PLACEHOLDER + "/" + jobId);

		GridResult result = gridHostResolver.execute(w);

		// place the grid result in the step context
		storeStartedResult(context, result);

		// place scratch directory in execution context, to be promoted
		// to the job context at run time.
		ExecutionContext stepContext = this.stepExecution.getExecutionContext();

		stepContext.put("scrDir", result.getWorkingDirectory());
		stepContext.put("hardFilterName", result.getId());

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
		this.stepExecution = stepExecution;
		JobExecution jobExecution = stepExecution.getJobExecution();
		ExecutionContext jobContext = jobExecution.getExecutionContext();
		this.scratchDirectory = jobContext.get("scrDir").toString();
		this.callVariantJobName = jobContext.get("callVariantName").toString();
		this.jobId = jobContext.get("jobId").toString();
		this.cellLibId = (Integer) jobContext.get("cellLibId");
	}
}

