package edu.yu.einstein.wasp.gatk.batch.tasklet;


/**
 * 
 */

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionExponential;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet;
import edu.yu.einstein.wasp.gatk.software.GATKSoftwareComponent;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;


public class CreatTargetTasklet extends WaspTasklet implements StepExecutionListener {

	private Integer cellLibraryId;
	
	@Autowired
	private FileService fileService;
	

	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private GridHostResolver gridHostResolver;
	
	@Autowired
	private FileType bamFileType;
	@Autowired
	private FileType fastqFileType;
	
	private StepExecution stepExecution;
	
	@Autowired
	private GATKSoftwareComponent gatk;

	public CreatTargetTasklet() {
		// proxy
	}

	public CreatTargetTasklet(String cellLibraryIds) {
		List<Integer> cids = WaspSoftwareJobParameters.getLibraryCellIdList(cellLibraryIds);
		Assert.assertTrue(cids.size() == 1);
		this.cellLibraryId = cids.get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet#execute(org.
	 * springframework.batch.core.StepContribution,
	 * org.springframework.batch.core.scope.context.ChunkContext)
	 */
	@Override
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		// if the work has already been started, then check to see if it is finished
		// if not, throw an exception that is caught by the repeat policy.
		RepeatStatus repeatStatus = super.execute(contrib, context);
		if (repeatStatus.equals(RepeatStatus.FINISHED))
			return RepeatStatus.FINISHED;
		
		SampleSource cellLib = sampleService.getSampleSourceDao().findById(cellLibraryId);
		
		ExecutionContext stepContext = this.stepExecution.getExecutionContext();
		stepContext.put("cellLibId", cellLib.getId()); //place in the step context
		
		Job job = sampleService.getJobOfLibraryOnCell(cellLib);
		
		logger.debug("Beginning GATK creat re-alignment target step for cellLibrary " + cellLib.getId() + " from job " + job.getId());
		
		Set<FileGroup> fileGroups = fileService.getFilesForCellLibraryByType(cellLib, fastqFileType); // TODO: change to bamFileType later
		
		logger.debug("ffileGroups.size()="+fileGroups.size());
		Assert.assertTrue(fileGroups.size() == 1);
		FileGroup fg = fileGroups.iterator().next();
		
		logger.debug("file group: " + fg.getId() + ":" + fg.getDescription());
		
		Map<String,Object> jobParameters = context.getStepContext().getJobParameters();
		
		for (String key : jobParameters.keySet()) {
			logger.debug("Key: " + key + " Value: " + jobParameters.get(key).toString());
		}
		
		// TODO: temporary, fix me
		//WorkUnit w = new WorkUnit();
		WorkUnit w = gatk.getCreatTarget(cellLib, fg);
		
		w.setResultsDirectory(WorkUnit.RESULTS_DIR_PLACEHOLDER + "/" + job.getId());
   
		GridResult result = gridHostResolver.execute(w);
		
		//place the grid result in the step context
		storeStartedResult(context, result);
		
		// place scratch directory in step execution context, to be promoted
		// to the job context at run time.
        stepContext.put("scrDir", result.getWorkingDirectory());
        stepContext.put("creatTargetName", result.getId());
        

		return RepeatStatus.CONTINUABLE;
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
		logger.debug("StepExecutionListener beforeStep saving StepExecution");
		this.stepExecution = stepExecution;
		
	}

}
