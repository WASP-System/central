package edu.yu.einstein.wasp.batch.launch;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;

import edu.yu.einstein.wasp.batch.core.extension.WaspBatchJobExplorer;

/**
 * Re-launches all batch jobs in state STARTED or STARTING 
 * @author andymac
 *
 */
public class WaspBatchRelaunchRunningJobsOnStartup implements BatchRelaunchRunningJobsOnStartup{
	
	private static Logger logger = LoggerFactory.getLogger(WaspBatchRelaunchRunningJobsOnStartup.class);
	
	private WaspBatchJobExplorer jobExplorer;
	
	private JobOperator jobOperator;
	
	private JobRepository jobRepository;

	public WaspBatchRelaunchRunningJobsOnStartup(JobRepository jobRepository, JobExplorer jobExplorer, JobOperator jobOperator) {
		this.jobRepository = jobRepository;
		this.jobExplorer = (WaspBatchJobExplorer) jobExplorer;
		this.jobOperator = jobOperator;
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
		this.jobExplorer = (WaspBatchJobExplorer) jobExplorer;
	}

	public JobOperator getJobOperator() {
		return jobOperator;
	}

	public void setJobOperator(JobOperator jobOperator) {
		this.jobOperator = jobOperator;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doLaunchAllRunningJobs(){
		Set<JobExecution> jobExecutionsToRestart = new HashSet<JobExecution>();
		jobExecutionsToRestart.addAll(jobExplorer.getJobExecutions(BatchStatus.STARTING));
		jobExecutionsToRestart.addAll(jobExplorer.getJobExecutions(BatchStatus.STARTED));
		for (JobExecution jobExecution: jobExecutionsToRestart){
			String jobName = jobExecution.getJobInstance().getJobName();
			JobParameters jobParameters = jobExecution.getJobInstance().getJobParameters();
			logger.info("Restarting running job '" + jobName + "' with parameters: " + jobParameters);
			try{
				// set jobExecution status to stopped to allow restart
				jobExecution.setStatus(BatchStatus.STOPPED);
                // set date one second in the past to avoid possible last execution job conflict
				long oneSecondAgo = System.currentTimeMillis() - 1000;
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
