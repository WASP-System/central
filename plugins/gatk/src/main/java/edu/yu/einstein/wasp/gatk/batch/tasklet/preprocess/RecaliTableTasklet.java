package edu.yu.einstein.wasp.gatk.batch.tasklet.preprocess;


import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.gatk.software.GATKSoftwareComponent;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.SampleService;


/**
 * @author jcai
 * @author asmclellan
 */
public class RecaliTableTasklet extends WaspRemotingTasklet implements StepExecutionListener {

	@Autowired
	private SampleService sampleService;

	@Autowired
	private FileService fileService;

	@Autowired
	private GridHostResolver gridHostResolver;

	@Autowired
	private FileType bamFileType;

	@Autowired
	private GATKSoftwareComponent gatk;
	
	public RecaliTableTasklet() {
		// proxy
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		// retrieve stored properties
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		ExecutionContext jobExecutionContext = stepExecution.getJobExecution().getExecutionContext();
		String scratchDirectory = jobExecutionContext.getString("scrDir");
		String localAlignJobName = jobExecutionContext.getString("localAlignJobName");
		
		SampleSource cellLib = sampleService.getSampleSourceDao().findById(jobExecutionContext.getInt("cellLibId"));

		Job job = sampleService.getJobOfLibraryOnCell(cellLib);

		logger.debug("Beginning GATK calculate recalibration table step for cellLibrary " + cellLib.getId() + " from job " + job.getId());
		logger.debug("Starting from previously local re-align'd scratch directory " + scratchDirectory);

		WorkUnit w = gatk.getRecaliTable(cellLib, scratchDirectory, localAlignJobName);
		w.setResultsDirectory(WorkUnit.RESULTS_DIR_PLACEHOLDER + "/" + job.getId());

		GridResult result = gridHostResolver.execute(w);

		// place the grid result in the step context
		storeStartedResult(context, result);

		// place recaliTableName in execution context, to be promoted
		// to the job context at run time.
		stepExecution.getExecutionContext().put("recaliTableName", result.getId());

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
	}
}

