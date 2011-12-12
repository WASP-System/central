package edu.yu.einstein.wasp.batch;

import java.io.File;
import java.util.Map;
import java.util.List;

import edu.yu.einstein.wasp.model.State;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

public class StateJobLaunchingMessageHandler {

	private final JobRegistry jobRegistry;
	private final JobLauncher jobLauncher;
	private final JobRepository jobRepository;

	private final String jobName;

	/**
	 * Handle job launching request for spring batch from SI
	 * 
	 * @param jobRegistry
	 * @param jobLauncher
	 * @param jobRepository
	 * @param jobName
	 */
	public StateJobLaunchingMessageHandler(JobRegistry jobRegistry, JobLauncher jobLauncher, JobRepository jobRepository, String jobName) {
		super();
		this.jobRegistry = jobRegistry;
		this.jobLauncher = jobLauncher;
		this.jobRepository = jobRepository;
		this.jobName = jobName;
	}

	public JobExecution launch(State state) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
			JobParametersInvalidException, NoSuchJobException {

		if (state == null)	{ return null; }
		
		Job job = jobRegistry.getJob(this.jobName);
		JobParametersBuilder builder = new JobParametersBuilder();
		builder.addString("state", ""+ state.getStateId());
		
		try {
			JobExecution jobToken=null;

			if (jobRepository.isJobInstanceExists(this.jobName, builder.toJobParameters())) {
				jobToken = jobRepository.getLastJobExecution(this.jobName, builder.toJobParameters());
			} else {
				jobToken = jobLauncher.run(job, builder.toJobParameters());
			}

			return jobToken;
		} catch (JobInstanceAlreadyCompleteException e) {
			
		}
		
	  return null;	
	}
}
