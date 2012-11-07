package edu.yu.einstein.wasp.daemon.test;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.rmi.RmiOutboundGateway;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.daemon.test.stubs.StubSampleDao;
import edu.yu.einstein.wasp.integration.messages.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspTask;
import edu.yu.einstein.wasp.integration.messages.payload.WaspStatus;

@ContextConfiguration(locations={"/daemon-test-launch-context.xml"})

public class JobLaunchTests extends AbstractTestNGSpringContextTests  {
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private RmiOutboundGateway outboundGateway;
	
	@Autowired 
	private JobRegistry jobRegistry;
	
		
	@Autowired
	private StubSampleDao stubSampleDao;
	
	private final Logger logger = LoggerFactory.getLogger(JobLaunchTests.class);
	
	
	// need to use different sampleId for each test as database not reset and 
	// it is not possible to re-submit a batch job with an identical signature (parameters)
	private final Integer JOB_ID = 1;
	private final String BATCH_JOB_NAME = "default.waspJob.jobflow.v1";
	private final String BATCH_JOB_NAME_WRONG = "notARealJob";
		
	
	@BeforeClass
	private void setup() throws SecurityException, NoSuchMethodException{
		Assert.assertNotNull(outboundGateway);
		Assert.assertNotNull(jobLauncher);
		Assert.assertNotNull(jobRegistry);
	}
	

		
	/**
	 * Test getting correct reply from WaspBatchJobLauncher service activator after sending a message to start a batch job 
	 */
	@Test (groups = "unit-tests-batch-integration")
	public void testSuccessfulJobLaunch() throws Exception{
		try{
			Map<String, String> jobParameters = new HashMap<String, String>();
			jobParameters.put(WaspJobParameters.JOB_ID, JOB_ID.toString());
			BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate( new BatchJobLaunchContext(BATCH_JOB_NAME, jobParameters) );
			Message<BatchJobLaunchContext> messageToSend = batchJobLaunchMessageTemplate.build();
			logger.debug("Sending message : "+messageToSend.toString());
			Message<?> replyMessage = (Message<?>) outboundGateway.handleRequestMessage(messageToSend);
			if (replyMessage == null)
				Assert.fail("Failed to send message " + messageToSend.toString() + " within timeout period");
			if (BatchJobLaunchContext.class.isInstance(replyMessage.getPayload()))
				Assert.fail("Message bouced");
			if (replyMessage.getHeaders().containsKey(WaspTask.EXCEPTION))
				Assert.fail("Failed to launch job. Returned message: " + replyMessage.toString());
			Assert.assertEquals(replyMessage.getPayload(), WaspStatus.COMPLETED);
			
		} catch (Exception e){
			 // caught an unexpected exception
			Assert.fail("Caught Exception: "+e.getMessage());
		}
	}
	
	/**
	 * Test getting correct reply from WaspBatchJobLauncher service activator after sending a message to start a batch job 
	 * containing an non-existent batch job
	 */
	@Test (groups = "unit-tests-batch-integration", dependsOnMethods="testSuccessfulJobLaunch")
	public void testFailedJobLaunch1() throws Exception{
		try{
			Map<String, String> jobParameters = new HashMap<String, String>();
			jobParameters.put(WaspJobParameters.JOB_ID, JOB_ID.toString());
			BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate( new BatchJobLaunchContext(BATCH_JOB_NAME_WRONG, jobParameters) );
			Message<BatchJobLaunchContext> messageToSend = batchJobLaunchMessageTemplate.build();
			logger.debug("Sending message : "+messageToSend.toString());
			Message<?> replyMessage = (Message<?>) outboundGateway.handleRequestMessage(messageToSend);
			if (replyMessage == null)
				Assert.fail("Failed to send message " + messageToSend.toString() + " within timeout period");
			if (replyMessage == null)
				Assert.fail("Timeout waiting to receive message");
			if (BatchJobLaunchContext.class.isInstance(replyMessage.getPayload()))
					Assert.fail("Message bouced");
			Assert.assertEquals(replyMessage.getPayload(), WaspStatus.FAILED);
			if (!replyMessage.getHeaders().containsKey(WaspTask.EXCEPTION))
				Assert.fail("Failed to return an exception in the header");
			logger.debug("Returned an exception as expected in the headers. Value = " + replyMessage.getHeaders().get(WaspTask.EXCEPTION));
			
		} catch (Exception e){
			 // caught an unexpected exception
			Assert.fail("Caught Exception: "+e.getMessage());
		}
	}
	
}
