package edu.yu.einstein.wasp.daemon.test;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;

@ContextConfiguration(locations={"/daemon-test-launch-context.xml","/daemon-test-batchJob.xml"})

public class ExponentialRepeatingJobTests extends AbstractTestNGSpringContextTests  {
	
	private final Logger logger = LoggerFactory.getLogger(ExponentialRepeatingJobTests.class);
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired 
	private JobRegistry jobRegistry;
	
	@Autowired
	private JobRepository jobRepository;
	
	private final String BATCH_JOB_NAME = "test.exponentialRepeatJob";
	
	private final String STEP_NAME = "test.repeatedStep";
	
		
	@BeforeClass
	private void setup() throws SecurityException, NoSuchMethodException{
		Assert.assertNotNull(jobRepository);
		Assert.assertNotNull(jobLauncher);
		Assert.assertNotNull(jobRegistry);
	}
	
		
	/**
	 * Test exponentially repeating 
	 */
	@Test (groups = "unit-tests-batch-integration")
	public void testSuccessfulExponentiallyRepeatingJob() throws Exception{
		try{
			Job job = jobRegistry.getJob(BATCH_JOB_NAME); 
			Map<String, JobParameter> parameterMap = new HashMap<String, JobParameter>();
			JobExecution jobExecution = jobLauncher.run(job, new JobParameters(parameterMap));
			int repeat = 0;
			while (repeat < 120){
				try{
					Thread.sleep(1000); // allow some time for flow initialization
				} catch (InterruptedException e){};
				StepExecution stepExecution = jobRepository.getLastStepExecution(jobExecution.getJobInstance(), STEP_NAME);
				if (stepExecution == null){
					logger.warn("StepExecution == null");
					continue;
				}
				Long timeInterval = BatchJobHibernationManager.getWakeTimeInterval(stepExecution);
				logger.debug("Current time interval = " + timeInterval);
				if (timeInterval >= 400L){
					logger.debug("Repeats completed");
					JobExecution freshJe = jobRepository.getLastJobExecution(jobExecution.getJobInstance().getJobName(), jobExecution.getJobParameters());
					// wait for job to complete
					if (freshJe.getStatus().equals(BatchStatus.COMPLETED)){
						logger.debug("Job completed: " + freshJe);
						break;
					}
				}
				repeat++;
			}
			if (repeat == 120){
				logger.debug("Timeout waiting for completion of tasks");
				Assert.fail();
			}
		} catch (Exception e){
			 // caught an unexpected exception
			Assert.fail("testSuccessfulJobLaunch(): Caught Exception: "+e.getMessage());
		}
	}
	
}
