package edu.yu.einstein.wasp.daemon.test;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.daemon.test.stubs.StubSampleDao;
import edu.yu.einstein.wasp.integration.messages.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.WaspTask;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;

@ContextConfiguration(locations={"/daemon-test-launch-context.xml","/dummyBatchJobFlow.xml"})

public class JobLaunchTests extends AbstractTestNGSpringContextTests  {
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired 
	private JobRegistry jobRegistry;
	
	@Autowired
	private MessageChannelRegistry messageChannelRegistry;
		
	@Autowired
	private StubSampleDao stubSampleDao;
	
	private final Logger logger = LoggerFactory.getLogger(JobLaunchTests.class);
	
	private MessagingTemplate messagingTemplate;
	
	
	// need to use different sampleId for each test as database not reset and 
	// it is not possible to re-submit a batch job with an identical signature (parameters)
	private final Integer JOB_ID = 10;
	private final String BATCH_JOB_NAME = "dummyBatchJob";
	private final String BATCH_JOB_NAME_WRONG = "notARealJob";
	
	private final String OUTBOUND_MESSAGE_CHANNEL = "wasp.channel.remoting.outbound";
		
	
	@BeforeClass
	private void setup() throws SecurityException, NoSuchMethodException{
		Assert.assertNotNull(messageChannelRegistry);
		Assert.assertNotNull(jobLauncher);
		Assert.assertNotNull(jobRegistry);
		messagingTemplate = new MessagingTemplate();
		messagingTemplate.setReceiveTimeout(2000);
	}
	
		
	/**
	 * Test getting correct reply from BatchJobLaunchServiceImpl service activator after sending a message to start a batch job 
	 */
	@Test (groups = "unit-tests-batch-integration")
	public void testSuccessfulJobLaunch() throws Exception{
		try{
			Map<String, String> jobParameters = new HashMap<String, String>();
			jobParameters.put(WaspJobParameters.JOB_ID, JOB_ID.toString());
			BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate( new BatchJobLaunchContext(BATCH_JOB_NAME, jobParameters) );
			Message<BatchJobLaunchContext> messageToSend = batchJobLaunchMessageTemplate.build();
			logger.debug("testSuccessfulJobLaunch(): Sending message : "+messageToSend.toString());
			Message<?> replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class), messageToSend);
			if (replyMessage == null)
				Assert.fail("testSuccessfulJobLaunch(): Failed to send message " + messageToSend.toString() + " within timeout period");
			if (BatchJobLaunchContext.class.isInstance(replyMessage.getPayload()))
				Assert.fail("testSuccessfulJobLaunch(): Message bouced");
			if (replyMessage.getHeaders().containsKey(WaspTask.EXCEPTION))
				Assert.fail("testSuccessfulJobLaunch(): Failed to launch job. Returned message: " + replyMessage.toString());
			Assert.assertEquals(replyMessage.getPayload(), WaspStatus.COMPLETED);
		} catch (Exception e){
			 // caught an unexpected exception
			Assert.fail("testSuccessfulJobLaunch(): Caught Exception: "+e.getMessage());
		}
	}
	
	/**
	 * Test getting correct reply from BatchJobLaunchServiceImpl service activator after sending a message to start a batch job 
	 * containing an non-existent batch job
	 */
	@Test (groups = "unit-tests-batch-integration", dependsOnMethods="testSuccessfulJobLaunch")
	public void testFailedJobLaunch() throws Exception{
		try{
			Map<String, String> jobParameters = new HashMap<String, String>();
			jobParameters.put(WaspJobParameters.JOB_ID, JOB_ID.toString());
			BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate( new BatchJobLaunchContext(BATCH_JOB_NAME_WRONG, jobParameters) );
			Message<BatchJobLaunchContext> messageToSend = batchJobLaunchMessageTemplate.build();
			logger.debug("testFailedJobLaunch(): Sending message : "+messageToSend.toString());
			Message<?> replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class), messageToSend);
			if (replyMessage == null)
				Assert.fail("testFailedJobLaunch(): Failed to send message " + messageToSend.toString() + " within timeout period");
			if (replyMessage == null)
				Assert.fail("testFailedJobLaunch(): Timeout waiting to receive message");
			if (BatchJobLaunchContext.class.isInstance(replyMessage.getPayload()))
					Assert.fail("testFailedJobLaunch(): Message bouced");
			Assert.assertEquals(replyMessage.getPayload(), WaspStatus.FAILED);
			if (!replyMessage.getHeaders().containsKey(WaspTask.EXCEPTION))
				Assert.fail("testFailedJobLaunch(): Failed to return an exception in the header");
			logger.debug("testFailedJobLaunch(): Returned an exception as expected in the headers. Value = " + replyMessage.getHeaders().get(WaspTask.EXCEPTION));
			
		} catch (Exception e){
			 // caught an unexpected exception
			Assert.fail("testFailedJobLaunch(): Caught Exception: "+e.getMessage());
		}
	}
	
}
