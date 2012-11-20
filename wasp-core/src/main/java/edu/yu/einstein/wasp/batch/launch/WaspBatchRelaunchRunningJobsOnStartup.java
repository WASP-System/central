package edu.yu.einstein.wasp.batch.launch;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;

import edu.yu.einstein.wasp.batch.core.extension.WaspBatchJobExplorer;

public class WaspBatchRelaunchRunningJobsOnStartup implements BatchRelaunchRunningJobsOnStartup{
	
	private static Logger logger = LoggerFactory.getLogger(WaspBatchRelaunchRunningJobsOnStartup.class);
	
	private WaspBatchJobExplorer jobExplorer;
	
	private JobOperator jobOperator;

	public WaspBatchRelaunchRunningJobsOnStartup(JobExplorer jobExplorer, JobOperator jobOperator) {
		this.jobExplorer = (WaspBatchJobExplorer) jobExplorer;
		this.jobOperator = jobOperator;
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
			String jobName = jobExplorer.getJobName(jobExecution);
			JobParameters jobParameters = jobExplorer.getJobParameters(jobExecution);
			logger.info("Restarting running job '" + jobName + "' with parameters: " + jobParameters);
			try{
				jobExecution.stop();
				jobOperator.restart(jobExecution.getId());
			} catch(Exception e){
				logger.warn("Failed to restart job '" + jobName + "' : " + e.getMessage());
			}
		}
	}

}
