package edu.yu.einstein.wasp.daemon.test;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.daemon.test.stubs.StubSampleDao;
import edu.yu.einstein.wasp.integration.messages.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspTask;
import edu.yu.einstein.wasp.integration.messages.payload.WaspStatus;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;

@ContextConfiguration(locations={"/daemon-test-launch-context.xml"})

public class JobLaunchTests extends AbstractTestNGSpringContextTests implements MessageHandler {
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private MessageChannelRegistry channelRegistry;
	
	@Autowired 
	private JobRegistry jobRegistry;
	
		
	@Autowired
	private StubSampleDao stubSampleDao;
	
	private final Logger logger = LoggerFactory.getLogger(JobLaunchTests.class);
	
	private Message<?> message = null;
	
	
	// need to use different sampleId for each test as database not reset and 
	// it is not possible to re-submit a batch job with an identical signature (parameters)
	private final Integer JOB_ID = 1;
	private final String BATCH_JOB_NAME = "default.waspJob.jobflow.v1";
	private final String BATCH_JOB_NAME_WRONG = "notARealJob";
	
	
	private DirectChannel outboundRmiChannel;
	private DirectChannel replyChannel;
	
	private static final Long MESSAGE_SEND_TIMEOUT = new Long(5000); // 5s
	
	private static final String OUTBOUND_MSG_CHANNEL_NAME = "wasp.channel.rmi.outbound";
	
	private static final String REPLY_MSG_CHANNEL_NAME = "wasp.channel.rmi.outbound.reply";
	
	
	
	
	@BeforeClass
	private void setup() throws SecurityException, NoSuchMethodException{
		Assert.assertNotNull(channelRegistry);
		Assert.assertNotNull(jobLauncher);
		Assert.assertNotNull(jobRegistry);
		outboundRmiChannel = channelRegistry.getChannel(OUTBOUND_MSG_CHANNEL_NAME, DirectChannel.class);
		replyChannel = channelRegistry.getChannel(REPLY_MSG_CHANNEL_NAME, DirectChannel.class);
		replyChannel.subscribe(this);
		
	}
	
	@AfterClass
	private void tearDown(){
		replyChannel.unsubscribe(this);
	}
	
	@BeforeMethod
	private void resetMessage(){
		message = null;
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
			logger.debug("Sending message via '" + OUTBOUND_MSG_CHANNEL_NAME + "': "+messageToSend.toString());
			if (! outboundRmiChannel.send(messageToSend, MESSAGE_SEND_TIMEOUT) )
				Assert.fail("Failed to send message " + messageToSend.toString() + " within timeout period");
			
			// Delay to allow message receiving and transitions. Time out after 40s.
			int repeat = 0;
			while (message == null && repeat < 40){
				Thread.sleep(1000);
				repeat++;
			}
			if (message == null)
				Assert.fail("Timeout waiting to receive message on " + REPLY_MSG_CHANNEL_NAME);
			if (BatchJobLaunchContext.class.isInstance(message.getPayload()))
					Assert.fail("Message bouced");
			if (message.getHeaders().containsKey(WaspTask.EXCEPTION))
				Assert.fail("Failed to launch job. Returned message: " + message.toString());
			Assert.assertEquals(message.getPayload(), WaspStatus.COMPLETED);
			
		} catch (Exception e){
			 // caught an unexpected exception
			Assert.fail("Caught Exception: "+e.getMessage());
		}
	}
	
	/**
	 * Test getting correct reply from WaspBatchJobLauncher service activator after sending a message to start a batch job 
	 * containing an non-existent batch job
	 */
	@Test (groups = "unit-tests-batch-integration")
	public void testFailedJobLaunch1() throws Exception{
		try{
			Map<String, String> jobParameters = new HashMap<String, String>();
			jobParameters.put(WaspJobParameters.JOB_ID, JOB_ID.toString());
			BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate( new BatchJobLaunchContext(BATCH_JOB_NAME_WRONG, jobParameters) );
			Message<BatchJobLaunchContext> messageToSend = batchJobLaunchMessageTemplate.build();
			logger.debug("Sending message via '" + OUTBOUND_MSG_CHANNEL_NAME + "': "+messageToSend.toString());
			if (! outboundRmiChannel.send(messageToSend, MESSAGE_SEND_TIMEOUT) )
				Assert.fail("Failed to send message " + messageToSend.toString() + " within timeout period");
			
			// Delay to allow message receiving and transitions. Time out after 40s.
			int repeat = 0;
			while (message == null && repeat < 40){
				Thread.sleep(1000);
				repeat++;
			}
			if (message == null)
				Assert.fail("Timeout waiting to receive message on " + REPLY_MSG_CHANNEL_NAME);
			if (BatchJobLaunchContext.class.isInstance(message.getPayload()))
					Assert.fail("Message bouced");
			Assert.assertEquals(message.getPayload(), WaspStatus.FAILED);
			if (!message.getHeaders().containsKey(WaspTask.EXCEPTION))
				Assert.fail("Failed to return an exception in the header");
			logger.debug("Returned an exception as expected in the headers. Value = " + message.getHeaders().get(WaspTask.EXCEPTION));
			
		} catch (Exception e){
			 // caught an unexpected exception
			Assert.fail("Caught Exception: "+e.getMessage());
		}
	}
	

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		logger.debug("Message recieved by handleMessage(): "+message.toString());
		this.message = message; 
	}
	
}
