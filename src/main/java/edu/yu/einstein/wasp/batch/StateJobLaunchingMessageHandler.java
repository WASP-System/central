package edu.yu.einstein.wasp.batch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.batch.core.explore.JobExplorer;
import java.util.Date;
import org.springframework.batch.core.BatchStatus;

import java.util.List;
import edu.yu.einstein.wasp.model.State;

import java.util.HashSet; 
import java.util.Set; 


public class StateJobLaunchingMessageHandler {
	private final Log logger = LogFactory.getLog(getClass());

	private final JobRegistry jobRegistry;
	private final JobLauncher jobLauncher;
	private final JobRepository jobRepository;
	private final JobOperator jobOperator;
	private final String jobName;

	@Autowired
	private JobExplorer jobExplorer; 

	private Set<Integer> seenStateSet = new HashSet<Integer>();


	/**
	 * Handle job launching request for spring batch from SI
	 * 
	 * @param jobRegistry
	 * @param jobLauncher
	 * @param jobRepository
	 * @param jobOperator
	 * @param jobName
	 */
	public StateJobLaunchingMessageHandler(JobRegistry jobRegistry, JobLauncher jobLauncher, JobRepository jobRepository, JobOperator jobOperator, String jobName) {
		super();
		this.jobRegistry = jobRegistry;
		this.jobLauncher = jobLauncher;
		this.jobRepository = jobRepository;
		this.jobOperator = jobOperator;
		this.jobName = jobName;
	}

	public void launch(List<State> states) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
			JobParametersInvalidException, NoSuchJobException, NoSuchJobExecutionException, org.springframework.batch.core.launch.JobExecutionNotRunningException {

		// null state
		if (states == null)	{ return; }

		logger.info("Launch Called with " + states.size() + " states"); 

		Job job = jobRegistry.getJob(this.jobName);

		for (State state: states) {

			logger.info("Launch Called with Job " + job + " State " + state.getStateId() + ""); 

			JobExecution jobToken=null;

			JobParametersBuilder builder = new JobParametersBuilder();
			builder.addString("state", ""+ state.getStateId());

			JobParameters params = builder.toJobParameters();

			if (seenStateSet.contains(state.getStateId())) { 
				logger.debug("Launch Seen " + state.getStateId());
				// already running internally?
				continue; 
			}

			// clean run
			if (! jobRepository.isJobInstanceExists(this.jobName, params)) {
				jobToken = jobLauncher.run(job, builder.toJobParameters());
				seenStateSet.add(state.getStateId());

				continue;
			}

			// failover condition
			jobToken = jobRepository.getLastJobExecution(this.jobName, params);


			// in the process of stopping, already failed or 
			//	started but not registered 
			if (jobToken.isStopping() ||
						jobToken.getStatus() == BatchStatus.FAILED ||
						jobToken.getStatus() == BatchStatus.STARTED) {
				resetStaleJob(jobToken);

				// waiting until the next run. 
				continue;
			}

			// restart the job
			jobOperator.restart(jobToken.getJobId());
			seenStateSet.add(state.getStateId());

		}




		return;
	}

/*
	public JobExecution launch(State state) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
			JobParametersInvalidException, NoSuchJobException, NoSuchJobExecutionException, org.springframework.batch.core.launch.JobExecutionNotRunningException {

		if (state == null)	{ return null; }

System.out.println("\n\n\n\n" + "AAAA launch called\n"); 
		
		Job job = jobRegistry.getJob(this.jobName);
		JobParametersBuilder builder = new JobParametersBuilder();
		builder.addString("state", ""+ state.getStateId());
System.out.println("job " + job + " state " + state.getStateId() + "\n\n"); 
		
		try {
			JobExecution jobToken=null;

			if (jobRepository.isJobInstanceExists(this.jobName, param)) {

System.out.println("EXISTS\n");


				if (seenStateSet.contains(state.getStateId())) { 
System.out.println("   - SEEN\n");
					// already running internally?
					return null;
				}

				if (jobToken.isStopping()) {
System.out.println(" * STOPPING\n");
					resetStaleJob(jobToken);
				} 
				// failed
				if (jobToken.getStatus() == BatchStatus.FAILED) {
					resetStaleJob(jobToken);
				}
				if (jobToken.getStatus() == BatchStatus.STARTED) {
					resetStaleJob(jobToken);
				}

				/ *
				if (jobToken.isRunning()) {
					jobOperator.restart(jobToken.getJobId());
					return jobToken;
				}
				* /

System.out.println("  - Other " + jobToken.getJobId() + " " + jobToken.getStatus() + "\n");
System.out.println("   " + jobToken.getAllFailureExceptions().size());

				jobOperator.restart(jobToken.getJobId());

			} else {
				jobToken = jobLauncher.run(job, builder.toJobParameters());

			}

			seenStateSet.add(state.getStateId());
			return jobToken;

		} catch (JobInstanceAlreadyCompleteException e) {
		}
		
	  return null;	
	}
*/

	public boolean resetStaleJob(JobExecution jobExecution) {
		if (jobExecution == null) {
			return false;
		}
	
		final BatchStatus status = jobExecution.getStatus();

		if (status.equals(BatchStatus.COMPLETED)) {
			return false;
		}
/*
		if (status.equals(BatchStatus.STARTED)) {
			return false;
		}
*/
	
		jobExecution.setStatus(BatchStatus.STOPPED);

		// TODO SET THIS SLIGHTLY IN THE PAST to avoid last execution job conflict
		jobExecution.setEndTime(new Date());
		// jobExecution.setLastUpdated(new Date());

		jobRepository.update(jobExecution);
	
		return true;
	}



}
