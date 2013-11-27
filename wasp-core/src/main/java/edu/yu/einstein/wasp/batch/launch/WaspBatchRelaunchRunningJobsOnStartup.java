package edu.yu.einstein.wasp.batch.launch;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

import edu.yu.einstein.wasp.batch.core.extension.WaspBatchExitStatus;
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

	public WaspBatchRelaunchRunningJobsOnStartup(JobRepository jobRepository, JobExplorer jobExplorer, JobOperator jobOperator, BatchJobHibernationManager hibernationManager) {
		this.jobRepository = jobRepository;
		this.jobExplorer = (WaspJobExplorer) jobExplorer;
		this.jobOperator = jobOperator;
		this.hibernationManager = hibernationManager;
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
	public void doLaunchAllRunningJobs(){
		long oneSecondAgo = System.currentTimeMillis() - 1000; // set date one second in the past to avoid possible last execution job conflict
		
		// re-populate hibernation manager with all persisted messages to wake steps
		logger.debug("Re-populate hibernation manager...");
		for (StepExecution se : jobExplorer.getStepExecutions(WaspBatchExitStatus.HIBERNATING)){
			hibernationManager.addMessageTemplatesForWakingJobStep(se.getJobExecutionId(), se.getId());
			hibernationManager.addMessageTemplatesForAbandoningJobStep(se.getJobExecutionId(), se.getId());
		}
		// First clean up all existing step executions in ExitStatus UNKNOWN or EXECUTING. We should set these to FAILED
		Set<StepExecution> stepExecutionsToRestart = new HashSet<StepExecution>();
		stepExecutionsToRestart.addAll(jobExplorer.getStepExecutions(ExitStatus.UNKNOWN));
		stepExecutionsToRestart.addAll(jobExplorer.getStepExecutions(ExitStatus.EXECUTING));
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
	}

}
