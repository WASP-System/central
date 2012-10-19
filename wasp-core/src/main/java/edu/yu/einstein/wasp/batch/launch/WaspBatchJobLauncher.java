package edu.yu.einstein.wasp.batch.launch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import edu.yu.einstein.wasp.exception.InvalidParameterException;
import edu.yu.einstein.wasp.exception.WaspBatchJobExecutionException;

public class WaspBatchJobLauncher implements BatchJobLauncher{
	
	private BatchJobLaunchContext batchJobLaunchContext;
	
	private JobLauncher jobLauncher;
	
	private JobRegistry jobRegistry;
	
	
	
	public WaspBatchJobLauncher(BatchJobLaunchContext batchJobLaunchContext, JobLauncher jobLauncher, JobRegistry jobRegistry) {
		this.batchJobLaunchContext = batchJobLaunchContext;
		this.jobLauncher = jobLauncher;
		this.jobRegistry = jobRegistry;
	}

	public JobRegistry getJobRegistry() {
		return jobRegistry;
	}

	public void setJobRegistry(JobRegistry jobRegistry) {
		this.jobRegistry = jobRegistry;
	}

	public BatchJobLaunchContext getBatchJobLaunchContext() {
		return batchJobLaunchContext;
	}

	public void setBatchJobLaunchContext( BatchJobLaunchContext batchJobLaunchContext) {
		this.batchJobLaunchContext = batchJobLaunchContext;
	}

	public JobLauncher getjobLauncher() {
		return jobLauncher;
	}

	public void setjobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}

	@Override
	public void launch() throws WaspBatchJobExecutionException{
		launchBatchJob(batchJobLaunchContext, jobLauncher, jobRegistry);
	}
	
	public static JobExecution launchBatchJob(BatchJobLaunchContext batchJobLaunchContext, JobLauncher jobLauncher, JobRegistry jobRegistry) throws WaspBatchJobExecutionException{
		try{
			if (jobLauncher == null)
				throw new InvalidParameterException("No JobLauncher set");
			if (jobRegistry == null)
				throw new InvalidParameterException("No JobRegistry set");
			if (batchJobLaunchContext == null)
				throw new InvalidParameterException("No BatchJobLaunchContext set");
		} catch (InvalidParameterException e){
			throw new WaspBatchJobExecutionException("Method parameters incomplete: " + e.getMessage(), e);
		}
		String jobName = batchJobLaunchContext.getJobName();
		Job job = null;
		try{
			job = jobRegistry.getJob(jobName);
		} catch(NoSuchJobException e){
			throw new WaspBatchJobExecutionException("Unable to find a job with name '" + jobName + "' in the job registry");
		}
		Map<String, JobParameter> jobParameters = new HashMap<String, JobParameter>();
		for (String paramName: batchJobLaunchContext.getJobParameters().keySet())
			jobParameters.put(paramName, new JobParameter(batchJobLaunchContext.getJobParameters().get(paramName)));
		try {
			return jobLauncher.run(job, new JobParameters(jobParameters));
		} catch (JobExecutionAlreadyRunningException e) {
			throw new WaspBatchJobExecutionException("Job Execution already running: " + e.getMessage(), e);
		} catch (JobRestartException e) {
			throw new WaspBatchJobExecutionException("Job Execution restart error: " + e.getMessage(), e);
		} catch (JobInstanceAlreadyCompleteException e) {
			throw new WaspBatchJobExecutionException("Job instance complete error: " + e.getMessage(), e);
		} catch (JobParametersInvalidException e) {
			throw new WaspBatchJobExecutionException("Job parameter error: " + e.getMessage(), e);
		}
	}

}
