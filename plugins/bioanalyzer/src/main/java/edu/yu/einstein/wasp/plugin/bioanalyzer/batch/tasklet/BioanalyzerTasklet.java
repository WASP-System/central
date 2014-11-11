/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package edu.yu.einstein.wasp.plugin.bioanalyzer.batch.tasklet;

import java.util.Map;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.service.JobService;

/**
 * @author 
 * 
 */
public class BioanalyzerTasklet extends WaspRemotingTasklet {
	
	@Autowired
	private JobService jobService;
	
	private Integer waspJobId;
	
	@Autowired
	private GridHostResolver gridHostResolver;
	

	public BioanalyzerTasklet() {
		// proxy
	}

	public BioanalyzerTasklet(String waspJobId) {
		this.waspJobId = Integer.parseInt(waspJobId);
	}
	
	/**
	 * Setup work to be run remotely. This method is called during execution of the super.execute(contrib, context) method. 
	 * You must either call the super.execute method from the locally overridden method (as is the default below) or remove the local method 
	 * below to use the method in the parent class.
	 * @param context
	 * @throws Exception
	 */
	@Override
	@Transactional("entityManager") // transactional for wasp system entities (e.g. Job)
	public void doExecute(ChunkContext context) throws Exception {
		
		// get step execution for this tasklet step
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		
		// get execution context for the step. Store key / value entries in here to maintain state in db
		ExecutionContext stepExecutionContext = stepExecution.getExecutionContext();
		
		// get job execution for the wrapper batch job and its context
		JobExecution jobExecution = stepExecution.getJobExecution();
		ExecutionContext jobExecutionContext = jobExecution.getExecutionContext();
		
		
		// get managed entity object for a wasp job by id 
		// Note: class or method must be public and be annotated with @Transactional("entityManager") otherwise
		// it will not be possible to inflate linked entities.
		Job waspJob = jobService.getJobByJobId(waspJobId);
		
		// configure work unit. Configuration information may be used to select a resource best suited to running the 
		// batch job
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
		c.setResultsDirectory(WorkUnitGridConfiguration.RESULTS_DIR_PLACEHOLDER + "/" + waspJobId);
		
		// prepare work unit (unit of work to be executed on configured resources e.g. a local cluster)
		WorkUnit w = new WorkUnit(c);
   
		// get grid result which can be used to determine the state of an executing job 
		GridResult result = gridHostResolver.execute(w);
		
		// persist the grid result in the step context (saves state in the batch db)
		saveGridResult(context, result);
		
		logger.info("Batch job execution submitted with id=" + result.getGridJobId() + 
				" on host '" + result.getHostname() + "' from step (name='" + stepExecution.getStepName() +
							"', id=" +  stepExecution.getId() + ")");
	}
	
	/**
	 * After remote task is finished you may need to execute some further business logic. Such work is specified here.
	 */
	@Override
	@Transactional("entityManager")
	public void doPreFinish(ChunkContext context) throws Exception {
		// do work post completion of remote task. 
		// e.g. get stored result to access output of task (GridResult result = getGridResult(context);)
	}
	
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void beforeStep(StepExecution stepExecution){
		super.beforeStep(stepExecution);
		// any pre-step logic goes here
		
		// here we can print out some information about the batch job to the info log
		if (logger.isInfoEnabled()){ // no point looking in here otherwise
			Map<String, JobParameter> jobParameters = stepExecution.getJobExecution().getJobParameters().getParameters();
			JobExecution batchJobExec = stepExecution.getJobExecution();
			
			logger.info("Starting batch job (name='" + batchJobExec.getJobInstance().getJobName() +
					"', id=" +  batchJobExec.getId() + "), step (name='" + stepExecution.getStepName() +
							"', id=" +  stepExecution.getId() + ")");
			
			for (String key : jobParameters.keySet()) {
				logger.info("Parameter for batch job id= " + batchJobExec.getId() + 
						": " + key + "=" + jobParameters.get(key).getValue().toString());
			}
		}
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		ExitStatus exitStatus = super.afterStep(stepExecution);
		// any post-step logic goes here
	
		// here we can print out some information about the batch job status to the info log
		logger.info("Finished executing step (name='" + stepExecution.getStepName() +
							"', id=" +  stepExecution.getId() + ") with ExitStatus=" + exitStatus);
		return exitStatus;
	}
	
}
