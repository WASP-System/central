package edu.yu.einstein.wasp.batch.core.extension;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.flow.FlowJob;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 *
 * @author asmclellan
 *
 */
public class WaspBatchJobOperator extends SimpleJobOperator implements JobOperatorWasp {
	
	private static final String ILLEGAL_STATE_MSG = "Illegal state (only happens on a race condition): %s with name=%s and parameters=%s";

    private Logger logger = LoggerFactory.getLogger(getClass());

	public WaspBatchJobOperator() {
		super();
	}
	
	/**
     * Check mandatory properties.
     *
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
            Assert.notNull(getJobLauncher(), "JobLauncher must be provided");
            Assert.notNull(getJobRegistry(), "JobLocator must be provided");
            Assert.notNull(getJobExplorer(), "JobExplorer must be provided");
            Assert.notNull(getJobRepository(), "JobRepository must be provided");
    }

    /**
	 * Wake up job from hibernation
	 */
	@Override
	public Long wake(long executionId) throws JobInstanceAlreadyCompleteException, NoSuchJobExecutionException, NoSuchJobException, JobRestartException, JobParametersInvalidException {
		logger.info("Checking status of job execution with id=" + executionId);
        JobExecution jobExecution = findExecutionById(executionId);

        String jobName = jobExecution.getJobInstance().getJobName();
        WaspFlowJob job = new WaspFlowJob((FlowJob) getJobRegistry().getJob(jobName));
        JobParameters parameters = jobExecution.getJobParameters();

        logger.info(String.format("Attempting to wake hibernating job with name=%s and parameters=%s", jobName, parameters));
        try {
                return getJobLauncher().wake(job, parameters).getId();
        }
        catch (JobExecutionAlreadyRunningException e) {
                throw new UnexpectedJobExecutionException(String.format(ILLEGAL_STATE_MSG, "job execution already running", jobName, parameters), e);
        }
	}

	/**
	 * Hibernate job
	 */
	@Override
	@Transactional
	public boolean hibernate(long executionId) throws NoSuchJobExecutionException, JobExecutionNotRunningException {
		// based on stop() method but with exit status modified within transaction
		JobExecution jobExecution = findExecutionById(executionId);
        // Indicate the execution should be stopped by setting it's status to
        // 'STOPPING'. It is assumed that
        // the step implementation will check this status at chunk boundaries.
        BatchStatus status = jobExecution.getStatus();
        if (!(status == BatchStatus.STARTED || status == BatchStatus.STARTING)) {
                throw new JobExecutionNotRunningException("JobExecution must be running so that it can be stopped: "+jobExecution);
        }
        logger.info(String.format("Attempting to hibernate job with name=%s and parameters=%s", jobExecution.getJobInstance().getJobName(), jobExecution.getJobParameters()));
        jobExecution.setStatus(BatchStatus.STOPPING);
        getJobRepository().update(jobExecution);

		return true;
	}
	

	
	 private JobExecution findExecutionById(long executionId) throws NoSuchJobExecutionException {
         JobExecution jobExecution = getJobExplorer().getJobExecution(executionId);
         
         if (jobExecution == null) {
                 throw new NoSuchJobExecutionException("No JobExecution found for id: [" + executionId + "]");
         }
         return jobExecution;

	 }
	 
	 /**
      * No getter was declared in SimpleJobOperator so we need to use reflection to extract the private value
      * @return
      */
      private JobRepository getJobRepository(){
    	  	Field jobRepositoryField = null;
			try {
				jobRepositoryField = SimpleJobOperator.class.getDeclaredField("jobRepository");
				jobRepositoryField.setAccessible(true);
	        	return (JobRepository) jobRepositoryField.get((SimpleJobOperator) this);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				logger.debug("Unable to obtain JobRepository from super via reflection");
				e.printStackTrace();
			}
			return null;
     }
      
      /**
       * No getter was declared in SimpleJobOperator so we need to use reflection to extract the private value
       * @return
       */
       private WaspBatchJobLauncher getJobLauncher(){
     	  	Field jobLauncherField = null;
 			try {
 				jobLauncherField = SimpleJobOperator.class.getDeclaredField("jobLauncher");
 				jobLauncherField.setAccessible(true);
 	        	return (WaspBatchJobLauncher) jobLauncherField.get((SimpleJobOperator) this);
 			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
 				logger.debug("Unable to obtain JobLauncher from super via reflection");
 				e.printStackTrace();
 			}
 			return null;
      }
       
       /**
        * No getter was declared in SimpleJobOperator so we need to use reflection to extract the private value
        * @return
        */
        private JobRegistry getJobRegistry(){
      	  	Field field = null;
  			try {
  				field = SimpleJobOperator.class.getDeclaredField("jobRegistry");
  				field.setAccessible(true);
  	        	return (JobRegistry) field.get((SimpleJobOperator) this);
  			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
  				logger.debug("Unable to obtain JobRegistry from super via reflection");
  				e.printStackTrace();
  			}
  			return null;
       }
        
        /**
         * No getter was declared in SimpleJobOperator so we need to use reflection to extract the private value
         * @return
         */
         private JobExplorer getJobExplorer(){
       	  	Field field = null;
   			try {
   				field = SimpleJobOperator.class.getDeclaredField("jobExplorer");
   				field.setAccessible(true);
   	        	return (JobExplorer) field.get((SimpleJobOperator) this);
   			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
   				logger.debug("Unable to obtain JobExplorer from super via reflection");
   				e.printStackTrace();
   			}
   			return null;
        }

}
