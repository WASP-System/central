
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

import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.StartLimitExceededException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.AbstractJob;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.SimpleStepHandler;
import org.springframework.batch.core.job.StepHandler;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.support.ExitCodeMapper;
import org.springframework.batch.core.listener.CompositeJobExecutionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.StepLocator;
import org.springframework.batch.repeat.RepeatException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;

/**
 * Based on {@link AbstractJob} 2.2.2.RELEASE
 * (https://github.com/spring-projects/spring-batch/blob/2.2.2.RELEASE/spring-batch-core/src/main/java/org/springframework/batch/core/job/AbstractJob.java)
 * @author asmclellan
 */
public abstract class AbstractWaspBatchJob implements Job, StepLocator, BeanNameAware, InitializingBean {

        protected static final Logger logger = LoggerFactory.getLogger(AbstractWaspBatchJob.class);

        protected String name;

        protected boolean restartable = true;

        protected JobRepository jobRepository;
        
        protected CompositeJobExecutionListener listener = new CompositeJobExecutionListener();

        protected JobParametersIncrementer jobParametersIncrementer;

        protected JobParametersValidator jobParametersValidator = new DefaultJobParametersValidator();

        protected StepHandler stepHandler;
        
        private final Integer STEP_STATUS_RETRY_TIMEOUT = 10; // ms 

        /**
         * Default constructor.
         */
        public AbstractWaspBatchJob() {
                super();
        }

        /**
         * Convenience constructor to immediately add name (which is mandatory but
         * not final).
         *
         * @param name
         */
        public AbstractWaspBatchJob(String name) {
                super();
                this.name = name;
        }

        /**
         * A validator for job parameters. Defaults to a vanilla
         * {@link DefaultJobParametersValidator}.
         *
         * @param jobParametersValidator
         * a validator instance
         */
        public void setJobParametersValidator(
                        JobParametersValidator jobParametersValidator) {
                this.jobParametersValidator = jobParametersValidator;
        }

        /**
         * Assert mandatory properties: {@link JobRepository}.
         *
         * @see InitializingBean#afterPropertiesSet()
         */
        @Override
        public void afterPropertiesSet() throws Exception {
                Assert.notNull(jobRepository, "JobRepository must be set");
        }

        /**
         * Set the name property if it is not already set. Because of the order of
         * the callbacks in a Spring container the name property will be set first
         * if it is present. Care is needed with bean definition inheritance - if a
         * parent bean has a name, then its children need an explicit name as well,
         * otherwise they will not be unique.
         *
         * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
         */
        @Override
        public void setBeanName(String name) {
                if (this.name == null) {
                        this.name = name;
                }
        }

        /**
         * Set the name property. Always overrides the default value if this object
         * is a Spring bean.
         *
         * @see #setBeanName(java.lang.String)
         */
        public void setName(String name) {
                this.name = name;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.springframework.batch.core.domain.IJob#getName()
         */
        @Override
        public String getName() {
                return name;
        }

        /**
         * Retrieve the step with the given name. If there is no Step with the given
         * name, then return null.
         *
         * @param stepName
         * @return the Step
         */
        @Override
        public abstract Step getStep(String stepName);

        /**
         * Retrieve the step names.
         *
         * @return the step names
         */
        @Override
        public abstract Collection<String> getStepNames();

        public CompositeJobExecutionListener getListener() {
			return listener;
		}

		public void setListener(CompositeJobExecutionListener listener) {
			this.listener = listener;
		}

		public StepHandler getStepHandler() {
			return stepHandler;
		}

		public void setStepHandler(StepHandler stepHandler) {
			this.stepHandler = stepHandler;
		}

		@Override
        public JobParametersValidator getJobParametersValidator() {
                return jobParametersValidator;
        }

        /**
         * Boolean flag to prevent categorically a job from restarting, even if it
         * has failed previously.
         *
         * @param restartable
         * the value of the flag to set (default true)
         */
        public void setRestartable(boolean restartable) {
                this.restartable = restartable;
        }

        /**
         * @see Job#isRestartable()
         */
        @Override
        public boolean isRestartable() {
                return restartable;
        }

        /**
         * Public setter for the {@link JobParametersIncrementer}.
         *
         * @param jobParametersIncrementer
         * the {@link JobParametersIncrementer} to set
         */
        public void setJobParametersIncrementer(
                        JobParametersIncrementer jobParametersIncrementer) {
                this.jobParametersIncrementer = jobParametersIncrementer;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.springframework.batch.core.Job#getJobParametersIncrementer()
         */
        @Override
        public JobParametersIncrementer getJobParametersIncrementer() {
                return this.jobParametersIncrementer;
        }

       /**
         * Public setter for injecting {@link JobExecutionListener}s. They will all
         * be given the listener callbacks at the appropriate point in the job.
         *
         * @param listeners
         * the listeners to set.
         */
        public void setJobExecutionListeners(JobExecutionListener[] listeners) {
                for (int i = 0; i < listeners.length; i++) {
                        this.listener.register(listeners[i]);
                }
        }

        /**
         * Register a single listener for the {@link JobExecutionListener}
         * callbacks.
         *
         * @param listener
         * a {@link JobExecutionListener}
         */
        public void registerJobExecutionListener(JobExecutionListener listener) {
                this.listener.register(listener);
        }

        /**
         * Public setter for the {@link JobRepository} that is needed to manage the
         * state of the batch meta domain (jobs, steps, executions) during the life
         * of a job.
         *
         * @param jobRepository
         */
        public void setJobRepository(JobRepository jobRepository) {
                this.jobRepository = jobRepository;
                stepHandler = new SimpleStepHandler(jobRepository);
        }

        /**
         * Convenience method for subclasses to access the job repository.
         *
         * @return the jobRepository
         */
        protected JobRepository getJobRepository() {
                return jobRepository;
        }

        /**
         * Extension point for subclasses allowing them to concentrate on processing
         * logic and ignore listeners and repository calls. Implementations usually
         * are concerned with the ordering of steps, and delegate actual step
         * processing to {@link #handleStep(Step, JobExecution)}.
         *
         * @param execution
         * the current {@link JobExecution}
         *
         * @throws JobExecutionException
         * to signal a fatal batch framework error (not a business or
         * validation exception)
         */
        abstract protected void doExecute(JobExecution execution, boolean wasHibernating)
                        throws JobExecutionException;

        /**
         * Run the specified job, handling all listener and repository calls, and
         * delegating the actual processing to {@link #doExecute(JobExecution)}.
         *
         * @see Job#execute(JobExecution)
         * @throws StartLimitExceededException
         * if start limit of one of the steps was exceeded
         */
        @Override
        public final void execute(JobExecution execution) {
        	logger.debug("JobExecution: " + execution);
    		logger.debug("execution.getStatus()=" + execution.getStatus());
    		logger.debug("# step executions=" + execution.getStepExecutions().size());
    		boolean isWakingFromHibernation = false;
    	    if (execution.getExecutionContext().containsKey(BatchJobHibernationManager.WAS_HIBERNATING) && 
    	    		(boolean) execution.getExecutionContext().get(BatchJobHibernationManager.WAS_HIBERNATING)){
    	    	isWakingFromHibernation = true;
    	    	execution.getExecutionContext().put(BatchJobHibernationManager.WAS_HIBERNATING, true);
    	        jobRepository.updateExecutionContext(execution);
    	    	logger.info("Job execution being awoken from hibernation: " + execution);
    	    } else{
    	    	logger.info("Job execution starting: " + execution);
    	    }
    	    boolean hibernationRequested = false;
    	    BatchStatus finalStatus = BatchStatus.STOPPED;
            try {
            		if (!isWakingFromHibernation)
            			getJobParametersValidator().validate(execution.getJobParameters());

                    if (execution.getStatus() != BatchStatus.STOPPING) {
                    		execution.setStartTime(new Date());
                    		if (!isWakingFromHibernation){
                    			// do not handle beforeJob listener if waking from hibernation
                                listener.beforeJob(execution);
                    		}
                    		updateStatus(execution, BatchStatus.STARTED);
                            try {
                            	doExecute(execution, isWakingFromHibernation);
                                logger.info("Job execution complete: " + execution);
                            } catch (RepeatException e) {
                                    throw e.getCause();
                            }
                    } else {
                            // The job was already stopped before we even got this far. Deal
                            // with it in the same way as any other interruption.
                            execution.setStatus(finalStatus);
                            if (isHibernationRequested(execution)){
                            	execution.setExitStatus(WaspBatchExitStatus.HIBERNATING);
                            	logger.info("Job execution was hibernated: " + execution);
                            } else {
                            	execution.setExitStatus(ExitStatus.COMPLETED);
                            	logger.info("Job execution was stopped: " + execution);
                            }

                    }

            } catch (JobInterruptedException e) {
            		hibernationRequested = isHibernationRequested(execution);
            		logger.debug("caught JobInterrupt and wasHibernationRequested=" + hibernationRequested);
	            	logger.debug("JobExecution: " + execution);
	        		logger.debug("execution.getStatus()=" + execution.getStatus());
	        		logger.debug("# step executions=" + execution.getStepExecutions().size());
            		if (hibernationRequested)
            			logger.info("Encountered hibernation request executing job: " + e.getMessage());
            		else
            			logger.info("Encountered interruption executing job: " + e.getMessage());
                    logger.trace("Full exception", e);
                    execution.setExitStatus(getDefaultExitStatusForFailure(e, hibernationRequested));
                    finalStatus = BatchStatus.max(BatchStatus.STOPPED, e.getStatus());
                    execution.setStatus(finalStatus);

                    if (!hibernationRequested) {
	                    execution.addFailureException(e);
                    }
                    logger.info(String.format("JobExecution updated with status=%s and exitStatus=%s", execution.getStatus(), execution.getExitStatus()));
            } catch (Throwable t) {
                    logger.error("Encountered fatal error executing job", t);
                    execution.setExitStatus(getDefaultExitStatusForFailure(t, hibernationRequested));
                    execution.setStatus(BatchStatus.FAILED);
                    execution.addFailureException(t);
            } finally {
            		logger.debug("in finally block");
            		logger.debug("execution.getStatus()=" + execution.getStatus());
            		logger.debug("# step executions=" + execution.getStepExecutions().size());
                    if (execution.getStatus().isLessThanOrEqualTo(BatchStatus.STOPPED)
                                    && execution.getStepExecutions().isEmpty()) {
                            ExitStatus exitStatus = execution.getExitStatus();
                            execution.setExitStatus(exitStatus.and(ExitStatus.NOOP
                                            .addExitDescription("All steps already completed or no steps configured for this job.")));
                    }
                    if (hibernationRequested){
                    	boolean allStepsComplete = false;
                    	do{
                    		allStepsComplete = true;
                    		// get stepExecution status from freshest JobExecution
                    		for (StepExecution se : execution.getStepExecutions()){
	                    		WaspBatchExitStatus currentExitStatus = new WaspBatchExitStatus(se.getExitStatus());
	                    		logger.debug("Current exit status of StepExecution id=" + se.getId() + " : " + currentExitStatus);
	                    		if (currentExitStatus.isStopped()){
		                    		se.setExitStatus(WaspBatchExitStatus.HIBERNATING);
		                        	logger.debug("Step execution id=" + se.getId() + " was marked as hibernated: " + se);
		                        	jobRepository.update(se);
	                    		} else if (currentExitStatus.isRunningAndAwake()){
	                    			allStepsComplete = false;
	                    		}
	                    	}
	                    	if (!allStepsComplete){
	                    		// should not get here if all steps were started before a step initiated job stopping
		                    	logger.info("Not all steps have been stopped. Trying again in " + STEP_STATUS_RETRY_TIMEOUT + "ms...");
	                			try {
									Thread.sleep(STEP_STATUS_RETRY_TIMEOUT);
								} catch (InterruptedException e) {}
	                    	}
                    	} while (!allStepsComplete);
                    } else {
                    	try {
                    		// do not handle afterJob listener if entering hibernation
                        	listener.afterJob(execution);
                        } catch (Exception e) {
                        	logger.error("Exception encountered in afterStep callback", e);
                        }
                    }
                    execution.setEndTime(new Date());
                    jobRepository.update(execution);
                    logger.info(String.format("JobExecution completed with status=%s and exitStatus=%s", execution.getStatus(), execution.getExitStatus()));
            }
        }
        
        private JobExecution getLatestJobExecution(JobExecution currentExecution){
        	return jobRepository.getLastJobExecution(currentExecution.getJobInstance().getJobName(), currentExecution.getJobParameters());
        }
        
        
        /**
         * Check ExitStatus of latest JobExecution to see if the description implies hibernation is requested
         * @param execution
         * @return
         */
        private boolean isHibernationRequested(JobExecution execution){
        	execution = getLatestJobExecution(execution); // check latest
        	boolean hibernationStatus = false;
        	if (execution.getExitStatus().getExitDescription().equals(BatchJobHibernationManager.HIBERNATION_REQUESTED))
        		hibernationStatus = true;
        	logger.debug("Checking hibernation status for JobExecution id=" + execution.getId() + ": requested=" + hibernationStatus);
        	return hibernationStatus;
        }


        /**
         * Convenience method for subclasses to delegate the handling of a specific
         * step in the context of the current {@link JobExecution}. Clients of this
         * method do not need access to the {@link JobRepository}, nor do they need
         * to worry about populating the execution context on a restart, nor
         * detecting the interrupted state (in job or step execution).
         *
         * @param step
         * the {@link Step} to execute
         * @param execution
         * the current {@link JobExecution}
         * @return the {@link StepExecution} corresponding to this step
         *
         * @throws JobInterruptedException
         * if the {@link JobExecution} has been interrupted, and in
         * particular if {@link BatchStatus#ABANDONED} or
         * {@link BatchStatus#STOPPING} is detected
         * @throws StartLimitExceededException
         * if the start limit has been exceeded for this step
         * @throws JobRestartException
         * if the job is in an inconsistent state from an earlier
         * failure
         */
        protected final StepExecution handleStep(Step step, JobExecution execution)
                        throws JobInterruptedException, JobRestartException,
                        StartLimitExceededException {
                return stepHandler.handleStep(step, execution);

        }

        /**
         * Default mapping from throwable to {@link ExitStatus}.
         *
         * @param ex
         * the cause of the failure
         * @return an {@link ExitStatus}
         */
        private ExitStatus getDefaultExitStatusForFailure(Throwable ex, boolean hibernationRequested) {
            ExitStatus exitStatus;
            if (ex instanceof JobInterruptedException
                            || ex.getCause() instanceof JobInterruptedException) {
            	logger.debug("checking hibernation status");
            	if (hibernationRequested)
            			exitStatus = WaspBatchExitStatus.HIBERNATING;
            		else
            			exitStatus = ExitStatus.STOPPED.addExitDescription(JobInterruptedException.class.getName());
            } else if (ex instanceof NoSuchJobException
                            || ex.getCause() instanceof NoSuchJobException) {
                    exitStatus = new ExitStatus(ExitCodeMapper.NO_SUCH_JOB, ex
                                    .getClass().getName());
            } else {
                    exitStatus = ExitStatus.FAILED.addExitDescription(ex);
            }

            return exitStatus;
    }


        /**
         * update the status of given JobExecution
         * @param jobExecution
         * @param status
         */
        private void updateStatus(JobExecution jobExecution, BatchStatus status) {
                jobExecution.setStatus(status);
                jobRepository.update(jobExecution);
        }
       

        @Override
        public String toString() {
                return ClassUtils.getShortName(getClass()) + ": [name=" + name + "]";
        }

}
