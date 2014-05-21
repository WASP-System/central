package edu.yu.einstein.wasp.batch.launch;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.wasp.WaspJobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;

import edu.yu.einstein.wasp.batch.SimpleManyJobRecipient;
import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;

/**
 * Re-launches all batch jobs in state STARTED or STARTING 
 * @author asmclellan
 *
 */
public class WaspBatchRelaunchRunningJobsOnStartup implements BatchRelaunchRunningJobsOnStartup{
	
	private static Logger logger = LoggerFactory.getLogger(WaspBatchRelaunchRunningJobsOnStartup.class);
	
	private WaspJobExplorer jobExplorer;
	
	private JobOperator jobOperator;
	
	private JobRepository jobRepository;
	
	private BatchJobHibernationManager hibernationManager;
	
	private Long initialExponentialInterval;

	public WaspBatchRelaunchRunningJobsOnStartup(JobRepository jobRepository, JobExplorer jobExplorer, JobOperator jobOperator, 
			BatchJobHibernationManager hibernationManager, Long initialExponentialInterval) {
		this.jobRepository = jobRepository;
		this.jobExplorer = (WaspJobExplorer) jobExplorer;
		this.jobOperator = jobOperator;
		this.hibernationManager = hibernationManager;
		this.initialExponentialInterval = initialExponentialInterval;
	}

	public JobRepository getJobRepository() {
		return jobRepository;
	}

	public void setJobRepository(JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	public JobExplorer getJobExplorer() {
		return jobExplorer;
	}

	public void setJobExplorer(JobExplorer jobExplorer) {
		this.jobExplorer = (WaspJobExplorer) jobExplorer;
	}

	public JobOperator getJobOperator() {
		return jobOperator;
	}

	public void setJobOperator(JobOperator jobOperator) {
		this.jobOperator = jobOperator;
	}

	
	public BatchJobHibernationManager getHibernationManager() {
		return hibernationManager;
	}

	public void setHibernationManager(BatchJobHibernationManager hibernationManager) {
		this.hibernationManager = hibernationManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	// don't use @Transactional here as otherwise prevents restarts
	public void doLaunchAllRunningJobs(){
		long oneSecondAgo = System.currentTimeMillis() - 1000; // set date one second in the past to avoid possible last execution job conflict
		List<StepExecution> hibernatingStepExecutions = jobExplorer.getStepExecutions(ExitStatus.HIBERNATING); 
		
		Set<StepExecution> stepExecutionsToRestart = new HashSet<StepExecution>();
		stepExecutionsToRestart.addAll(jobExplorer.getStepExecutions(ExitStatus.UNKNOWN));
		stepExecutionsToRestart.addAll(jobExplorer.getStepExecutions(ExitStatus.EXECUTING));
		
		// First clean up all existing step executions in ExitStatus UNKNOWN or EXECUTING. We should set these to FAILED
		for (StepExecution stepExecution: stepExecutionsToRestart){
			stepExecution.setStatus(BatchStatus.FAILED);
        	stepExecution.setExitStatus(new ExitStatus("FAILED", "Failed because wasp-daemon was shutdown inproperly (was found in an active state on startup)"));
        	stepExecution.setEndTime(new Date(oneSecondAgo));
        	jobRepository.update(stepExecution);
		}
		
		// Now we can clean up all existing job executions in ExitStatus UNKNOWN or EXECUTING. We should set these to FAILED
		// then restart them.
		Set<JobExecution> jobExecutionsToRestart = new HashSet<JobExecution>();
		jobExecutionsToRestart.addAll(jobExplorer.getJobExecutions(ExitStatus.UNKNOWN));
		jobExecutionsToRestart.addAll(jobExplorer.getJobExecutions(ExitStatus.EXECUTING));
		for (JobExecution jobExecution: jobExecutionsToRestart){
			String jobName = jobExecution.getJobInstance().getJobName();
			JobParameters jobParameters = jobExecution.getJobParameters();
			logger.info("Restarting running job '" + jobName + "' with parameters: " + jobParameters);
			try{
				// set jobExecution status to stopped to allow restart
				jobExecution.setStatus(BatchStatus.FAILED);
				jobExecution.setExitStatus(new ExitStatus("FAILED", "Failed because wasp-daemon was shutdown inproperly (was found in an active state on startup)"));
                
                jobExecution.setEndTime(new Date(oneSecondAgo));
                jobRepository.update(jobExecution);
                // now we can restart the job
				jobOperator.restart(jobExecution.getId());
			} catch(Exception e){
				logger.warn("Failed to restart job '" + jobName + "' [" + jobExecution + "] : " + e.getMessage());
			}
		}
		
		// re-populate hibernation manager with all persisted messages to wake steps
		logger.debug("Re-populate hibernation manager...");
		for (StepExecution se : hibernatingStepExecutions){
			if (se.getExecutionContext().containsKey(BatchJobHibernationManager.MANY_JOB_RECIPIENT_KEY)){
				ObjectMapper mapper = new ObjectMapper();
				try{
					SimpleManyJobRecipient jobRecipient = mapper.readValue(se.getExecutionContext().getString(BatchJobHibernationManager.MANY_JOB_RECIPIENT_KEY),
							SimpleManyJobRecipient.class);
					hibernationManager.registerManyStepCompletionListener(jobRecipient);
				} catch(IOException e){
					throw new JSONException("Cannot create object of type ManyJobRecipient from JSON. Caught exception of type " + 
							e.getClass().getName() + " : " + e.getLocalizedMessage());
				}
			}
			hibernationManager.addMessageTemplatesForWakingJobStep(se.getJobExecutionId(), se.getId());
			hibernationManager.addMessageTemplatesForAbandoningJobStep(se.getJobExecutionId(), se.getId());
			if (hibernationManager.getWakeTimeInterval(se.getJobExecutionId(), se.getId()) != null){
				hibernationManager.setWakeTimeInterval(se.getJobExecutionId(), se.getId(), initialExponentialInterval);
				hibernationManager.addTimeIntervalForJobStep(se.getJobExecutionId(), se.getId(), initialExponentialInterval);
			}
		}
	}

}
