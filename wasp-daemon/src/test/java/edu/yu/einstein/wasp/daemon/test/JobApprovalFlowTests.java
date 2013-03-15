package edu.yu.einstein.wasp.daemon.test;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.integration.messages.WaspMessageType;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;
import edu.yu.einstein.wasp.integration.messages.templates.JobStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.model.User;


@ContextConfiguration(locations={"/daemon-test-launch-context.xml"})

public class JobApprovalFlowTests extends AbstractTestNGSpringContextTests implements MessageHandler {
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private MessageChannelRegistry channelRegistry;
	
	@Autowired
	private MessageChannelRegistry messageChannelRegistry;
	
	@Autowired 
	private JobRegistry jobRegistry;
	
	private MessagingTemplate messagingTemplate;
	
	private final String OUTBOUND_MESSAGE_CHANNEL = "wasp.channel.remoting.outbound";
	
	private final Logger logger = LoggerFactory.getLogger(JobApprovalFlowTests.class);
	
	private Message<?> message = null;
	
	private final String JOB_ID_KEY = "jobId";
	
	// need to use different jobId for each test as database not reset and 
	// it is not possible to re-submit a batch job with an identical signature (parameters)
	private final Integer JOB_ID = 1;
	private final Integer JOB_ID2 = 2;
	
	private SubscribableChannel listeningChannel;
	
	private SubscribableChannel abortChannel;
	
	@BeforeClass
	private void setup(){
		Assert.assertNotNull(channelRegistry);
		Assert.assertNotNull(jobLauncher);
		Assert.assertNotNull(jobRegistry);
		Assert.assertNotNull(messageChannelRegistry);
		listeningChannel = channelRegistry.getChannel("wasp.channel.notification.job", SubscribableChannel.class);
		listeningChannel.subscribe(this); // register as a message handler on the listeningChannel
		abortChannel = channelRegistry.getChannel("wasp.channel.notification.abort", SubscribableChannel.class);
		abortChannel.subscribe(this); // register as a message handler on the listeningChannel
		messagingTemplate = new MessagingTemplate();
		messagingTemplate.setReceiveTimeout(2000);

	}
	
	@AfterClass 
	private void tearDown(){
		listeningChannel.unsubscribe(this);
		abortChannel.unsubscribe(this);
	}
	
		
	/**
	 * This test exercises the approvalFlow.
	 * The method sets up a listeningChannel and listens on it. it then launches the default.waspJob.jobflow.v1.
	 */
	@Test (groups = "unit-tests-batch-integration")
	public void testJobApproved() throws Exception{
		try{
			// setup job execution for the 'default.waspJob.jobflow.v1' job
			Job job = jobRegistry.getJob("default.waspJob.jobflow.v1"); // get the 'default.waspJob.jobflow.v1' job from the context
			Map<String, JobParameter> parameterMap = new HashMap<String, JobParameter>();
			parameterMap.put( JOB_ID_KEY, new JobParameter(JOB_ID.toString()) );
			JobExecution jobExecution = jobLauncher.run(job, new JobParameters(parameterMap));
			Thread.sleep(500); // allow some time for flow initialization
			
			// send approval messages (simulating button presses in web view)
			JobStatusMessageTemplate template = new JobStatusMessageTemplate(JOB_ID);
			template.setStatus(WaspStatus.COMPLETED);
			
			template.setTask(WaspJobTask.QUOTE);
			Message<WaspStatus> quoteApprovedMessage = template.build();
			logger.info("testJobApproved(): Sending message via 'outbound rmi gateway': "+quoteApprovedMessage.toString());
			Message<?> replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class), quoteApprovedMessage);
			if (replyMessage != null)
				Assert.fail("testJobApproved(): Got unexpected reply message: "+ replyMessage.toString());
			
			template.setTask(WaspJobTask.PI_APPROVE);
			Message<WaspStatus> piApprovedMessage = template.build();
			logger.info("testJobApproved(): Sending message via 'outbound rmi gateway': "+piApprovedMessage.toString());
			replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class), piApprovedMessage);
			if (replyMessage != null)
				Assert.fail("testJobApproved(): Got unexpected reply message: "+ replyMessage.toString());
			
			template.setTask(WaspJobTask.DA_APPROVE);
			Message<WaspStatus> daApprovedMessage = template.build();
			logger.info("testJobApproved(): Sending message via 'outbound rmi gateway': "+daApprovedMessage.toString());
			replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class), daApprovedMessage);
			if (replyMessage != null)
				Assert.fail("testJobApproved(): Got unexpected reply message: "+ replyMessage.toString());
			
			template.setTask(WaspJobTask.FM_APPROVE);
			Message<WaspStatus> fmApprovedMessage = template.build();
			logger.info("testJobApproved(): Sending message via 'outbound rmi gateway': "+fmApprovedMessage.toString());
			replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class), fmApprovedMessage);
			if (replyMessage != null)
				Assert.fail("testJobApproved(): Got unexpected reply message: "+ replyMessage.toString());
			
			// Delay to allow message receiving and transitions. Time out after 10s.
			int repeat = 0;
			while ((message == null || (! JobStatusMessageTemplate.actUponMessage(message, JOB_ID, WaspJobTask.NOTIFY_STATUS))) && repeat < 10){
				message = null;
				Thread.sleep(1000);
				repeat++;
			}
			if (message == null)
				Assert.fail("testJobApproved(): Timeout waiting to receive message on 'wasp.channel.notification.job'");
			
			// check headers as expected
			Assert.assertTrue(message.getHeaders().containsKey(JOB_ID_KEY));
			Assert.assertEquals(message.getHeaders().get(JOB_ID_KEY), JOB_ID);
			Assert.assertTrue(message.getHeaders().containsKey(WaspMessageType.HEADER_KEY));
			Assert.assertEquals(message.getHeaders().get(WaspMessageType.HEADER_KEY), WaspMessageType.JOB);
			
			// check payload as expected
			Assert.assertEquals(WaspStatus.class, message.getPayload().getClass());
			Assert.assertEquals(message.getPayload(), WaspStatus.ACCEPTED);
			
			// check BatchStatus and ExitStatus is as expected
			Assert.assertEquals(jobExecution.getStatus(), BatchStatus.STARTED);
			
			// send message to tell exit condition monitoring tasklet that job is finished
			template.setTask(WaspJobTask.NOTIFY_STATUS);
			Message<WaspStatus> completeNotificationMessage = template.build();
			logger.info("testJobApproved(): Sending message via 'outbound rmi gateway': "+completeNotificationMessage.toString());
			replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class), completeNotificationMessage);
			if (replyMessage != null)
				Assert.fail("testJobApproved(): Got unexpected reply message: "+ replyMessage.toString());
			
			Thread.sleep(500); // wait for message receiving and job completion events
			// check BatchStatus and ExitStatus are as expected
			Assert.assertEquals(jobExecution.getStatus(), BatchStatus.STARTED);
			jobExecution.stop();
		} catch (Exception e){
			// caught an unexpected exception
			Assert.fail("Caught Exception: "+e.getMessage());
		}
	}
	
	/**
	 * This test exercises the approvalFlow. In this case one approval task receives an ABANDONED signal to signify rejection.
	 * The method sets up a listeningChannel and listens on it. it then launches the default.waspJob.jobflow.v1.
	 */
	@Test (groups = "unit-tests-batch-integration")
	public void testJobNotApproved() throws Exception{
		try{
			// setup job execution for the 'default.waspJob.jobflow.v1' job
			Job job = jobRegistry.getJob("default.waspJob.jobflow.v1"); // get the 'default.waspJob.jobflow.v1' job from the context
			Map<String, JobParameter> parameterMap = new HashMap<String, JobParameter>();
			parameterMap.put( JOB_ID_KEY, new JobParameter(JOB_ID2.toString()) );
			JobExecution jobExecution = jobLauncher.run(job, new JobParameters(parameterMap));
			Thread.sleep(500);
			
			// send approval messages (simulating button presses in web view)
			JobStatusMessageTemplate template = new JobStatusMessageTemplate(JOB_ID2);
			template.setStatus(WaspStatus.COMPLETED);
			

			
			template.setTask(WaspJobTask.QUOTE);
			Message<WaspStatus> quoteApprovedMessage = template.build();
			logger.info("testJobNotApproved(): Sending message via 'outbound rmi gateway': "+quoteApprovedMessage.toString());
			Message<?> replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class), quoteApprovedMessage);
			if (replyMessage != null)
				Assert.fail("testJobNotApproved(): Got unexpected reply message: "+ replyMessage.toString());
			
			template.setTask(WaspJobTask.DA_APPROVE);
			Message<WaspStatus> daApprovedMessage = template.build();
			logger.info("testJobNotApproved(): Sending message via 'outbound rmi gateway': "+daApprovedMessage.toString());
			replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class), daApprovedMessage);
			if (replyMessage != null)
				Assert.fail("testJobNotApproved(): Got unexpected reply message: "+ replyMessage.toString());
			
			template.setTask(WaspJobTask.FM_APPROVE);
			Message<WaspStatus> fmApprovedMessage = template.build();
			logger.info("testJobNotApproved(): Sending message via 'outbound rmi gateway': "+fmApprovedMessage.toString());
			replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class), fmApprovedMessage);
			if (replyMessage != null)
				Assert.fail("testJobNotApproved(): Got unexpected reply message: "+ replyMessage.toString());
			
			template.setTask(WaspJobTask.PI_APPROVE);
			template.setStatus(WaspStatus.ABANDONED);
			template.setComment("A comment");
			template.setUserCreatingMessage(new User());
			Message<WaspStatus> piApprovedMessage = template.build();
			logger.info("testJobNotApproved(): Sending message via 'outbound rmi gateway': "+piApprovedMessage.toString());
			replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class), piApprovedMessage);
			if (replyMessage != null)
				Assert.fail("testJobNotApproved(): Got unexpected reply message: "+ replyMessage.toString());
			
			// Delay to allow message receiving and transitions. Timeout after 10s.
			int repeat = 0;
			while ((message == null || (! JobStatusMessageTemplate.actUponMessage(message, JOB_ID2, WaspJobTask.NOTIFY_STATUS))) && repeat < 10){
				message = null;
				Thread.sleep(1000);
				repeat++;
			}
			if (message == null)
				Assert.fail("testJobNotApproved(): Timeout waiting to receive message on 'wasp.channel.notification.job'");
			
			// check headers as expected
			Assert.assertTrue(message.getHeaders().containsKey(JOB_ID_KEY));
			Assert.assertEquals(message.getHeaders().get(JOB_ID_KEY), JOB_ID2);
			Assert.assertTrue(message.getHeaders().containsKey(WaspMessageType.HEADER_KEY));
			Assert.assertEquals(message.getHeaders().get(WaspMessageType.HEADER_KEY), WaspMessageType.JOB);
			
			// check payload as expected
			Assert.assertEquals(WaspStatus.class, message.getPayload().getClass());
			Assert.assertEquals(message.getPayload(), WaspStatus.ABANDONED);
			
			Thread.sleep(1000); // allow batch to wrap up
			// check BatchStatus and ExitStatus are as expected
			Assert.assertEquals(jobExecution.getStatus(), BatchStatus.STOPPED);
			Assert.assertEquals(jobExecution.getExitStatus().getExitCode(), ExitStatus.STOPPED.getExitCode());
			jobExecution.stop();
		} catch (Exception e){
			// caught an unexpected exception
			Assert.fail("testJobNotApproved(): Caught Exception: "+e.getMessage());
		}
	}
	

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		logger.debug("Message recieved by handleMessage(): "+message.toString());
		this.message = message; 
	}
	
}
