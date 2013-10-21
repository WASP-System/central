/*
* Copyright 2006-2013 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package edu.yu.einstein.wasp.batch.core.extension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;

/**
 * Largely derived from {@link SimpleJobOperator} with modifications
 * @author asmclellan
 *
 */
public class WaspBatchJobOperator extends SimpleJobOperator implements JobOperatorWasp {
	
	private static final String ILLEGAL_STATE_MSG = "Illegal state (only happens on a race condition): %s with name=%s and parameters=%s";
	
	private JobRegistry jobRegistry;
	
	private JobExplorer jobExplorer;

    private JobLauncherWasp jobLauncher;

    private JobRepository jobRepository;

    private Logger logger = LoggerFactory.getLogger(getClass());

	public WaspBatchJobOperator() {
		super();
	}

	
	public void setJobRegistry(JobRegistry jobRegistry) {
		this.jobRegistry = jobRegistry;
		super.setJobRegistry(jobRegistry);
	}


	public void setJobLauncher(JobLauncherWasp jobLauncher) {
		this.jobLauncher = jobLauncher;
		super.setJobLauncher(jobLauncher);
	}
	
	@Override
	public void setJobExplorer(JobExplorer jobExplorer) {
		this.jobExplorer = jobExplorer;
		super.setJobExplorer(jobExplorer);
	}

	@Override
	public void setJobRepository(JobRepository jobRepository) {
		this.jobRepository = jobRepository;
		super.setJobRepository(jobRepository);
	}

	@Override
	public Long wake(long executionId) throws JobInstanceAlreadyCompleteException, NoSuchJobExecutionException, NoSuchJobException, JobRestartException, JobParametersInvalidException {
		logger.info("Checking status of job execution with id=" + executionId);
        JobExecution jobExecution = findExecutionById(executionId);

        String jobName = jobExecution.getJobInstance().getJobName();
        Job job = new WaspBatchJob(jobRegistry.getJob(jobName));
        JobParameters parameters = jobExecution.getJobParameters();

        logger.info(String.format("Attempting to resume job with name=%s and parameters=%s", jobName, parameters));
        try {
                return jobLauncher.wake(job, parameters).getId();
        }
        catch (JobExecutionAlreadyRunningException e) {
                throw new UnexpectedJobExecutionException(String.format(ILLEGAL_STATE_MSG, "job execution already running",
                                jobName, parameters), e);
        }
	}

	@Override
	@Transactional
	public boolean hibernate(long executionId) throws NoSuchJobExecutionException, JobExecutionNotRunningException {
		stop(executionId);
		JobExecution jobExecution = findExecutionById(executionId);
		jobExecution.getExecutionContext().put(BatchJobHibernationManager.HIBERNATING, true);
		jobRepository.updateExecutionContext(jobExecution);
		return true;
	}
	
	 private JobExecution findExecutionById(long executionId) throws NoSuchJobExecutionException {
         JobExecution jobExecution = jobExplorer.getJobExecution(executionId);
         
         if (jobExecution == null) {
                 throw new NoSuchJobExecutionException("No JobExecution found for id: [" + executionId + "]");
         }
         return jobExecution;

 }

}
