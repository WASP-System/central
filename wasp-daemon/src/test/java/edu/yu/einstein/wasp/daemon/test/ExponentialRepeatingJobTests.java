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
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;

@ContextConfiguration(locations={"/daemon-test-launch-context.xml","/daemon-test-batchJob.xml"})
public class ExponentialRepeatingJobTests extends BatchDatabaseIntegrationTest {
	
	private final Logger logger = LoggerFactory.getLogger(ExponentialRepeatingJobTests.class);

	private final String BATCH_JOB_NAME = "test.exponentialRepeatJob";
	
	private final String STEP_NAME = "test.repeatedStep";
	
	@BeforeMethod
	protected void beforeMethodSetup() throws Exception{
		super.cleanDB();
	}
	
	@BeforeClass
	@Override
	protected void setup(){
		super.setup();
	}
	
	@AfterClass
	protected boolean afterClassTeardown(){
		return super.stopRunningJobExecutions();
	}
		
	/**
	 * Test exponentially repeating 
	 */
	@Test (groups = "unit-tests-batch-integration")
	public void testSuccessfulExponentiallyRepeatingJob() throws Exception{
		Job job = jobRegistry.getJob(BATCH_JOB_NAME); 
		Map<String, JobParameter> parameterMap = new HashMap<String, JobParameter>();
		JobExecution jobExecution = jobLauncher.run(job, new JobParameters(parameterMap));
		int repeat = 0;
		while (repeat < 120){
			try{
				Thread.sleep(2000); // allow some time for flow initialization
			} catch (InterruptedException e){};
			StepExecution stepExecution = jobRepository.getLastStepExecution(jobExecution.getJobInstance(), STEP_NAME);
			if (stepExecution == null){
				logger.warn("StepExecution == null");
				continue;
			}
			Long timeInterval = BatchJobHibernationManager.getWakeTimeInterval(stepExecution);
			if (timeInterval == null){
				logger.warn("timeInterval == null");
				continue;
			}
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
	}
	
	
	
}
