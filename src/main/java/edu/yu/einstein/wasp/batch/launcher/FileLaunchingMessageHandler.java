package edu.yu.einstein.wasp.batch.launcher;

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
import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.model.State;

import java.util.HashSet; 
import java.util.Set; 


public class FileLaunchingMessageHandler {
	private final Log logger = LogFactory.getLog(getClass());

	private final JobRegistry jobRegistry;
	private final JobLauncher jobLauncher;
	private final JobRepository jobRepository;
	private final JobOperator jobOperator;
	private final String jobName;

	@Autowired
	private JobExplorer jobExplorer; 


	/**
	 * this is a set to determine what this context has seen
	 * seen assumptions made
	 * - the launcher has seen this job, so it is going fine
	 * within spring batch, so don't sweat it.
	 *
	 * not seen assumes that the task is not running
	 * 	further tests on the system will determine if that is
	 * 	true or not
	 */
	private Set<String> seenFilenameSet = new HashSet<String>();


	/**
	 * Handle job launching request for spring batch from SI
	 * 
	 * @param jobRegistry
	 * @param jobLauncher
	 * @param jobRepository
	 * @param jobOperator
	 * @param jobName
	 */
	public FileLaunchingMessageHandler (JobRegistry jobRegistry, JobLauncher jobLauncher, JobRepository jobRepository, JobOperator jobOperator, String jobName) {
		super();
		this.jobRegistry = jobRegistry;
		this.jobLauncher = jobLauncher;
		this.jobRepository = jobRepository;
		this.jobOperator = jobOperator;
		this.jobName = jobName;
	}

	public void launch(List<String> files) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
			JobParametersInvalidException, NoSuchJobException, NoSuchJobExecutionException, org.springframework.batch.core.launch.JobExecutionNotRunningException {

		// null files
		if (files == null)	{ return; }

		logger.info("Launch Called with " + files.size() + " files"); 

		Job job = jobRegistry.getJob(this.jobName);

		for (String filename: files) {

			logger.info("Launch Called with Job " + job + " Filename " + filename + ""); 

			JobExecution jobToken=null;

			JobParametersBuilder builder = new JobParametersBuilder();
			builder.addString("filename", ""+ filename);

			JobParameters params = builder.toJobParameters();

			if (seenFilenameSet.contains(filename)) { 
				logger.debug("FilenameLaunch Seen " + filename);
				// already running internally?
				continue; 
			}

			// clean run
			if (! jobRepository.isJobInstanceExists(this.jobName, params)) {
				jobToken = jobLauncher.run(job, builder.toJobParameters());
				seenFilenameSet.add(filename);

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
			seenFilenameSet.add(filename);
		}

		return;
	}


	/**
	 * resetStaleJobs
	 * for a job execution set it to a stopped file so it has
	 * a chance of being reexecuted
	 *
	 * @param JobExecution - job to be cleaned
	 * @return clean up succeeded?
	 *
	 */
	public boolean resetStaleJob(JobExecution jobExecution) {
		if (jobExecution == null) {
			return false;
		}
	
		final BatchStatus status = jobExecution.getStatus();

		// if it is already complete don't reset it
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
