/*
* Copyright 2006-2013 the original author or authors.
* @author Lucas Ward
* @author Dave Syer
* @author Ben Hale
* @author Robert Kasanicky
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.StartLimitExceededException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.AbstractJob;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.SimpleJob;
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
 * Code modified from {@link AbstractJob} and {@link SimpleJob}
 * @author asmclellan
 *
 */
public class WaspBatchJob implements Job, StepLocator, BeanNameAware, InitializingBean {

        protected static final Logger logger = LoggerFactory.getLogger(WaspBatchJob.class);

        private String name;

        private boolean restartable = true;

        private JobRepository jobRepository;

        private CompositeJobExecutionListener listener = new CompositeJobExecutionListener();

        private JobParametersIncrementer jobParametersIncrementer;

        private JobParametersValidator jobParametersValidator = new DefaultJobParametersValidator();

        private WaspStepHandler stepHandler;
        
        private List<Step> steps = new ArrayList<Step>();

        /**
         * Default constructor.
         */
        public WaspBatchJob() {
                super();
        }

        /**
         * Convenience constructor to immediately add name (which is mandatory but
         * not final).
         *
         * @param name
         */
        public WaspBatchJob(String name) {
                super();
                this.name = name;
        }
        
        public WaspBatchJob(Job job) {
            this.jobParametersIncrementer = job.getJobParametersIncrementer();
            this.jobParametersValidator = job.getJobParametersValidator();
            this.name = job.getName();
            this.restartable = job.isRestartable();
            try {
            	// set listener. Unfortunately no getters for this so use reflection.
            	Field listenerField = job.getClass().getDeclaredField("listener");
            	listenerField.setAccessible(true); // because field is private
				this.listener = (CompositeJobExecutionListener) listenerField.get(job);
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				logger.warn("Unable to obtain value for 'listener' from provided Job object by reflection: " + e.getLocalizedMessage());
			}
            try {
            	// set jobRepository and stepHandler. Unfortunately no getters for these so use reflection.
            	Field jobRepositoryField = job.getClass().getDeclaredField("jobRepository");
            	jobRepositoryField.setAccessible(true); // because field is private
				setJobRepository( (JobRepository) jobRepositoryField.get(job) );
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				logger.warn("Unable to obtain value for 'jobRepository' from provided Job object by reflection: " + e.getLocalizedMessage());
			}
            if (SimpleJob.class.isInstance(job)){
            	this.steps.clear();
            	SimpleJob sj = (SimpleJob) job;
            	for ( String stepName : sj.getStepNames() )
            		this.steps.add(sj.getStep(stepName));
            }
        }

        /**
         * A validator for job parameters. Defaults to a vanilla
         * {@link DefaultJobParametersValidator}.
         *
         * @param jobParametersValidator
         * a validator instance
         */
        public void setJobParametersValidator(JobParametersValidator jobParametersValidator) {
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
                stepHandler = new WaspStepHandler(jobRepository);
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
         * Run the specified job, handling all listener and repository calls, and
         * delegating the actual processing to {@link #doExecute(JobExecution)}.
         *
         * @see Job#execute(JobExecution)
         * @throws StartLimitExceededException
         * if start limit of one of the steps was exceeded
         */
        @Override
        public void execute(JobExecution execution) {
        		boolean hasHibernatingExitStatus = false;
        	    if (execution.getExitStatus().getExitCode().equals(WaspBatchExitStatus.HIBERNATING.getExitCode())){
        	    	hasHibernatingExitStatus = true;
        	    	logger.debug("Job execution being awoken from hibernation: " + execution);
        	    } else{
        	    	logger.debug("Job execution starting: " + execution);
        	    }
                try {
                		if (!hasHibernatingExitStatus)
                			jobParametersValidator.validate(execution.getJobParameters());

                        if (execution.getStatus() != BatchStatus.STOPPING) {
                        		if (!hasHibernatingExitStatus){
	                                execution.setStartTime(new Date());
	                                listener.beforeJob(execution);
	                                updateStatus(execution, BatchStatus.STARTED);
                        		} else 
                        			execution.setEndTime(null);

                                try {
                                        doExecute(execution);
                                        logger.debug("Job execution complete: " + execution);
                                } catch (RepeatException e) {
                                        throw e.getCause();
                                }
                        } else {
                                // The job was already stopped before we even got this far. Deal
                                // with it in the same way as any other interruption.
                                execution.setStatus(BatchStatus.STOPPED);
                                if (wasHibernationRequested(execution)){
                                	execution.setExitStatus(WaspBatchExitStatus.HIBERNATING);
                                	logger.debug("Job execution was hibernated: " + execution);
                                } else {
                                	execution.setExitStatus(ExitStatus.COMPLETED);
                                	logger.debug("Job execution was stopped: " + execution);
                                }

                        }

                } catch (JobInterruptedException e) {
                		if (wasHibernationRequested(execution))
                			logger.info("Encountered hibernation request executing job: " + e.getMessage());
                		else
                			logger.info("Encountered interruption executing job: " + e.getMessage());
                        if (logger.isDebugEnabled()) {
                                logger.debug("Full exception", e);
                        }
                        execution.setExitStatus(getDefaultExitStatusForFailure(e, execution));
                        execution.setStatus(BatchStatus.max(BatchStatus.STOPPED, e.getStatus()));
                        execution.addFailureException(e);
                } catch (Throwable t) {
                        logger.error("Encountered fatal error executing job", t);
                        execution.setExitStatus(getDefaultExitStatusForFailure(t, execution));
                        execution.setStatus(BatchStatus.FAILED);
                        execution.addFailureException(t);
                } finally {

                        if (execution.getStatus().isLessThanOrEqualTo(BatchStatus.STOPPED)
                                        && execution.getStepExecutions().isEmpty()) {
                                ExitStatus exitStatus = execution.getExitStatus();
                                execution
                                .setExitStatus(exitStatus.and(ExitStatus.NOOP
                                                .addExitDescription("All steps already completed or no steps configured for this job.")));
                        }
                        if (!wasHibernationRequested(execution)){
                        	execution.setEndTime(new Date());

	                        try {
	                                listener.afterJob(execution);
	                        } catch (Exception e) {
	                                logger.error("Exception encountered in afterStep callback", e);
	                        }
                        }

                        jobRepository.update(execution);
                }

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
        protected StepExecution handleStep(Step step, JobExecution execution)
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
        private ExitStatus getDefaultExitStatusForFailure(Throwable ex, JobExecution execution) {
                ExitStatus exitStatus;
                if (ex instanceof JobInterruptedException
                                || ex.getCause() instanceof JobInterruptedException) {
                	if (wasHibernationRequested(execution))
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
        
        private boolean wasHibernationRequested(JobExecution execution){
        	return (execution.getExecutionContext().containsKey(BatchJobHibernationManager.HIBERNATING) && 
    				(boolean) execution.getExecutionContext().get(BatchJobHibernationManager.HIBERNATING));
        }

        private void updateStatus(JobExecution jobExecution, BatchStatus status) {
                jobExecution.setStatus(status);
                jobRepository.update(jobExecution);
        }
        
       /**
         * Public setter for the steps in this job. Overrides any calls to
         * {@link #addStep(Step)}.
         *
         * @param steps the steps to execute
         */
        public void setSteps(List<Step> steps) {
                this.steps.clear();
                this.steps.addAll(steps);
        }

        /**
         * Convenience method for clients to inspect the steps for this job.
         *
         * @return the step names for this job
         */
        @Override
        public Collection<String> getStepNames() {
                List<String> names = new ArrayList<String>();
                for (Step step : steps) {
                        names.add(step.getName());
                }
                return names;
        }

        /**
         * Convenience method for adding a single step to the job.
         *
         * @param step a {@link Step} to add
         */
        public void addStep(Step step) {
                this.steps.add(step);
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.springframework.batch.core.job.AbstractJob#getStep(java.lang.String)
         */
        @Override
        public Step getStep(String stepName) {
                for (Step step : this.steps) {
                        if (step.getName().equals(stepName)) {
                                return step;
                        }
                }
                return null;
        }

        /**
         * Handler of steps sequentially as provided, checking each one for success
         * before moving to the next. Returns the last {@link StepExecution}
         * successfully processed if it exists, and null if none were processed.
         *
         * @param execution the current {@link JobExecution}
         *
         * @see AbstractJob#handleStep(Step, JobExecution)
         */
        protected void doExecute(JobExecution execution) throws JobInterruptedException, JobRestartException, StartLimitExceededException {

                StepExecution stepExecution = null;
                for (Step step : steps) {
                        stepExecution = handleStep(step, execution);
                        if (stepExecution.getStatus() != BatchStatus.COMPLETED) {
                                //
                                // Terminate the job if a step fails
                                //
                                break;
                        }
                }

                //
                // Update the job status to be the same as the last step
                //
                if (stepExecution != null) {
                        logger.debug("Upgrading JobExecution status: " + stepExecution);
                        execution.upgradeStatus(stepExecution.getStatus());
                        execution.setExitStatus(stepExecution.getExitStatus());
                }
        }

        @Override
        public String toString() {
                return ClassUtils.getShortName(getClass()) + ": [name=" + name + "]";
        }

}
