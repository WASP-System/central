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

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.job.flow.FlowJob;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.util.Assert;

import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;

/**
 * Largely based on {@link SimpleJobLauncher} 2.2.2.RELEASE with modifications
 * (https://github.com/spring-projects/spring-batch/blob/2.2.2.RELEASE/spring-batch-core/src/main/java/org/springframework/batch/core/launch/support/SimpleJobLauncher.java)
 * @author asmclellan
 *
 */
public class WaspBatchJobLauncher extends SimpleJobLauncher implements JobLauncherWasp {
	
	protected static final Logger logger = LoggerFactory.getLogger(WaspBatchJobLauncher.class);
	
	public WaspBatchJobLauncher() {
		super();
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		 Assert.state(getJobRepository() != null, "A JobRepository has not been set.");
         if (getTaskExecutor() == null) {
                 logger.info("No TaskExecutor has been set, defaulting to synchronous executor.");
                 setTaskExecutor(new SyncTaskExecutor());
         }
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public JobExecution run(final Job job, final JobParameters jobParameters)
                    throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
                    JobParametersInvalidException {
		WaspFlowJob waspFlowJob = new WaspFlowJob((FlowJob) job);
		return super.run(waspFlowJob, jobParameters);
	}
	
	
	/**
	 * Wake hibernating job
	 */
	@Override
	public JobExecution wake(final WaspFlowJob job, final JobParameters jobParameters) throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		Assert.notNull(job, "The Job must not be null.");
        Assert.notNull(jobParameters, "The JobParameters must not be null.");
        final JobExecution jobExecution;
        JobExecution lastExecution = getJobRepository().getLastJobExecution(job.getName(), jobParameters);
        
        if (lastExecution != null) {
                if (!job.isRestartable()) {
                        throw new JobRestartException("JobInstance already exists and is not restartable");
                }
                if (!lastExecution.getExitStatus().getExitCode().equals(WaspBatchExitStatus.HIBERNATING.getExitCode())) {
                    throw new JobRestartException("JobExecution id=" + lastExecution.getJobId() + " is not of ExitStatus HIBERNATING");
                }
                                       
        }
        logger.debug("Waking up JobExecution : " + lastExecution);
        
        // Check the validity of the parameters before doing creating anything
        // in the repository...
        job.getJobParametersValidator().validate(jobParameters);
        jobExecution = getJobRepository().createJobExecution(job.getName(), jobParameters);
        jobExecution.setExecutionContext(lastExecution.getExecutionContext());
        jobExecution.getExecutionContext().put(BatchJobHibernationManager.WAS_HIBERNATING, true);
        getJobRepository().updateExecutionContext(jobExecution);
        lastExecution.setExitStatus(new ExitStatus(ExitStatus.STOPPED.getExitCode(), BatchJobHibernationManager.WAS_HIBERNATING));
        getJobRepository().update(lastExecution);
        logger.debug("JobExecution has " + jobExecution.getStepExecutions().size() + " steps");
        logger.debug("JobExecutionContext contains " + jobExecution.getExecutionContext().size() + " values in it");
        try {
        	getTaskExecutor().execute(new Runnable() {

                        @Override
                        public void run() {
                                try {
                                        logger.info("Job: [" + job + "] re-launched with the following parameters: [" + jobParameters
                                                        + "]");
                                        job.execute(jobExecution);
                                        logger.info("Job: [" + job + "] completed with the following parameters: [" + jobParameters
                                                        + "] and the following status: [" + jobExecution.getStatus() + "]");
                                }
                                catch (Throwable t) {
                                        logger.info("Job: [" + job
                                                        + "] failed unexpectedly and fatally with the following parameters: [" + jobParameters
                                                        + "]", t);
                                        rethrow(t);
                                }
                        }

                        private void rethrow(Throwable t) {
                                if (t instanceof RuntimeException) {
                                        throw (RuntimeException) t;
                                }
                                else if (t instanceof Error) {
                                        throw (Error) t;
                                }
                                throw new IllegalStateException(t);
                        }
                });
        }
        catch (TaskRejectedException e) {
                jobExecution.upgradeStatus(BatchStatus.FAILED);
                if (jobExecution.getExitStatus().equals(ExitStatus.UNKNOWN)) {
                        jobExecution.setExitStatus(ExitStatus.FAILED.addExitDescription(e));
                }
                getJobRepository().update(jobExecution);
        }

        return jobExecution;
	}
	
	/**
     * No getter was declared in SimpleJobLauncher so we need to use reflection to extract the private value
     * @return
     */
    private JobRepository getJobRepository(){
    	Field jobRepositoryField = null;
		try {
			jobRepositoryField = SimpleJobLauncher.class.getDeclaredField("jobRepository");
			jobRepositoryField.setAccessible(true);
        	return (JobRepository) jobRepositoryField.get((SimpleJobLauncher) this);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			logger.debug("Unable to obtain JobRepository from super via reflection");
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * No getter was declared in SimpleJobLauncher so we need to use reflection to extract the private value
     * @return
     */
    private TaskExecutor getTaskExecutor(){
    	Field taskExecutorField = null;
		try {
			taskExecutorField = SimpleJobLauncher.class.getDeclaredField("taskExecutor");
			taskExecutorField.setAccessible(true);
        	return (TaskExecutor) taskExecutorField.get((SimpleJobLauncher) this);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			logger.debug("Unable to obtain TaskExecutor from super via reflection");
			e.printStackTrace();
		}
		return null;
    }

}
