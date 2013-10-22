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
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.StartLimitExceededException;
import org.springframework.batch.core.job.AbstractJob;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionException;
import org.springframework.batch.core.job.flow.FlowJob;
import org.springframework.batch.core.job.flow.JobFlowExecutor;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.support.ExitCodeMapper;
import org.springframework.batch.core.listener.CompositeJobExecutionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.repeat.RepeatException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;

/**
 * @author asmclellan
 *
 */
public class WaspBatchJob extends FlowJob {

        protected static final Logger logger = LoggerFactory.getLogger(WaspBatchJob.class);

        /**
         * Create a {@link WaspBatchJob} with null name and no flow (invalid state).
         */
        public WaspBatchJob() {
                super();
        }

        /**
         * Create a {@link WaspBatchJob} with provided name and no flow (invalid state).
         */
        public WaspBatchJob(String name) {
                super(name);
        }
        
        public WaspBatchJob(FlowJob job) {
        	setJobParametersIncrementer(job.getJobParametersIncrementer());
            setJobParametersValidator(job.getJobParametersValidator());
            setName(job.getName());
            setRestartable(job.isRestartable());
            try {
             	// set listener. Unfortunately no getters for this so use reflection.
             	Field listenerField = AbstractJob.class.getDeclaredField("listener");
             	listenerField.setAccessible(true); // because field is private
             	listenerField.set(this, listenerField.get((AbstractJob) job));
 			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
 				logger.warn("Unable to set value for 'listener' from value provided Job object by reflection: " + e.getLocalizedMessage());
 				e.printStackTrace();
 			}
            try {
             	// set jobRepository. Unfortunately no getters for these so use reflection.
             	Field jobRepositoryField = AbstractJob.class.getDeclaredField("jobRepository");
             	jobRepositoryField.setAccessible(true); // because field is private
 				setJobRepository( (JobRepository) jobRepositoryField.get((AbstractJob) job) );
 			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
 				logger.warn("Unable to obtain value for 'jobRepository' from provided Job object by reflection: " + e.getLocalizedMessage());
 				e.printStackTrace();
 			}
            try {
             	// set flow. Unfortunately no getters for this so use reflection.
             	Field flowField = FlowJob.class.getDeclaredField("flow");
             	flowField.setAccessible(true); // because field is private
 				setFlow((Flow) flowField.get(job));
 			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
 				logger.warn("Unable to obtain value for 'flow' from provided Job object by reflection: " + e.getLocalizedMessage());
 				e.printStackTrace();
 			}
            try {
             	// set stepMap. Unfortunately no getters for this so use reflection.
             	Field stepMapField = FlowJob.class.getDeclaredField("stepMap");
             	stepMapField.setAccessible(true); // because field is private
             	stepMapField.set(this, stepMapField.get(job));
 			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
 				logger.warn("Unable to obtain value for 'stepMap' from provided Job object by reflection: " + e.getLocalizedMessage());
 				e.printStackTrace();
 			}
            try {
             	// set initialized. Unfortunately no getters for this so use reflection.
             	Field initializedField = FlowJob.class.getDeclaredField("initialized");
             	initializedField.setAccessible(true); // because field is private
             	initializedField.set(this, initializedField.get(job));
 			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
 				logger.warn("Unable to obtain value for 'initialized' from provided Job object by reflection: " + e.getLocalizedMessage());
 				e.printStackTrace();
 			}
        }
        

        /**
         * Alternative of the execute() function of Abstract Job for hibernating jobs
         * Run the specified job, handling all listener and repository calls, and
         * delegating the actual processing to {@link #doExecuteForHibernation(JobExecution)}.
         *
         * @see Job#execute(JobExecution)
         * @throws StartLimitExceededException
         * if start limit of one of the steps was exceeded
         */
        public void executeForHibernation(JobExecution execution) {
        		boolean hasHibernatingExitStatus = false;
        	    if (execution.getExitStatus().getExitCode().equals(WaspBatchExitStatus.HIBERNATING.getExitCode())){
        	    	hasHibernatingExitStatus = true;
        	    	logger.debug("Job execution being awoken from hibernation: " + execution);
        	    } else{
        	    	logger.debug("Job execution starting: " + execution);
        	    }
                try {
                		if (!hasHibernatingExitStatus)
                			getJobParametersValidator().validate(execution.getJobParameters());

                        if (execution.getStatus() != BatchStatus.STOPPING) {
                        		if (!hasHibernatingExitStatus){
	                                execution.setStartTime(new Date());
	                                getListener().beforeJob(execution);
                        		}
                        		updateStatus(execution, BatchStatus.STARTED);
                        		updateExitStatus(execution, ExitStatus.EXECUTING);
                                try {
                                	doExecuteForHibernation(execution);
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
                                execution.setExitStatus(exitStatus.and(ExitStatus.NOOP
                                                .addExitDescription("All steps already completed or no steps configured for this job.")));
                        }
                        if (!wasHibernationRequested(execution)){
                        	execution.setEndTime(new Date());

	                        try {
	                        	getListener().afterJob(execution);
	                        } catch (Exception e) {
	                                logger.error("Exception encountered in afterStep callback", e);
	                        }
                        }

                        getJobRepository().update(execution);
                }

        }
        
        /**
         * @see AbstractJob#doExecute(JobExecution)
         */
        protected void doExecuteForHibernation(final JobExecution execution) throws JobExecutionException {
                try {
                        JobFlowExecutor executor = new JobFlowExecutor(getJobRepository(),
                                        new WaspStepHandler(getJobRepository()), execution);
                        executor.updateJobExecutionStatus(getFlow().start(executor).getStatus());
                }
                catch (FlowExecutionException e) {
                        if (e.getCause() instanceof JobExecutionException) {
                                throw (JobExecutionException) e.getCause();
                        }
                        throw new JobExecutionException("Flow execution ended unexpectedly", e);
                }
        }
        
        
        /**
         * No getter was declared in Abstract Job so we need to use reflection to extract the private value
         * @return
         */
        private CompositeJobExecutionListener getListener(){
        	Field listenerField = null;
			try {
				listenerField = AbstractJob.class.getDeclaredField("listener");
	        	listenerField.setAccessible(true);
	        	return (CompositeJobExecutionListener) listenerField.get((AbstractJob) this);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				logger.debug("Unable to obtain CompositeJobExecutionListener from super via reflection");
				e.printStackTrace();
			}
			return null;
        }
        
        /**
         * No getter was declared in FlowJob so we need to use reflection to extract the private value
         * @return
         */
        private Flow getFlow(){
        	Field flowField = null;
			try {
				flowField = FlowJob.class.getDeclaredField("flow");
				flowField.setAccessible(true);
	        	return (Flow) flowField.get((FlowJob) this);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				logger.debug("Unable to obtain Flow from super via reflection");
				e.printStackTrace();
			}
			return null;
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
                getJobRepository().update(jobExecution);
        }
        
        private void updateExitStatus(JobExecution jobExecution,ExitStatus status) {
            jobExecution.setExitStatus(status);
            getJobRepository().update(jobExecution);
        }
        
        /**
         * Assert mandatory properties: {@link JobRepository}.
         *
         * @see InitializingBean#afterPropertiesSet()
         */
        @Override
        public void afterPropertiesSet() throws Exception {
                Assert.notNull(getJobRepository(), "JobRepository must be set");
        }
        
       
       

}
