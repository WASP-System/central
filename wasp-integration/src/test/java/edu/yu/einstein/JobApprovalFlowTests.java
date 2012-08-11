package edu.yu.einstein;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.core.PollableChannel;
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.messages.WaspJobStatusMessageTemplate;
import edu.yu.einstein.wasp.messages.WaspJobTask;
import edu.yu.einstein.wasp.messages.WaspMessageType;
import edu.yu.einstein.wasp.messages.WaspRunStatusMessageTemplate;
import edu.yu.einstein.wasp.messages.WaspStatus;
import edu.yu.einstein.wasp.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.tasklets.WaspTasklet;


@ContextConfiguration(locations={"classpath:test-launch-context.xml", "classpath:RmiMessageSend-context.xml"})

public class JobApprovalFlowTests extends AbstractTestNGSpringContextTests implements MessageHandler {
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	MessageChannelRegistry channelRegistry;
	
	@Autowired 
	JobRegistry jobRegistry;
	
	private final Logger logger = Logger.getLogger(JobApprovalFlowTests.class);
	
	private Message<?> message = null;
	
	private final String JOB_ID_KEY = "jobId";
	
	private final Integer JOB_ID = 1;
	
	private DirectChannel outboundRmiChannel;
	private DirectChannel replyChannel;
	private SubscribableChannel waspRunPublishSubscribeChannel;
	
	@BeforeClass
	private void setup(){
		Assert.assertNotNull(channelRegistry);
		Assert.assertNotNull(jobLauncher);
		Assert.assertNotNull(jobRegistry);
		outboundRmiChannel = channelRegistry.getChannel("wasp.channel.rmi.outbound", DirectChannel.class);
		replyChannel = channelRegistry.getChannel("wasp.channel.rmi.outbound.reply", DirectChannel.class);
		replyChannel.subscribe(this);
	}
	
	@AfterClass 
	private void tearDown(){
		//waspRunPublishSubscribeChannel.unsubscribe(this);
		replyChannel.unsubscribe(this);
	}
	
		
	/**
	 * This test exercises the approvalFlow.
	 * The method sets up a waspRunPublishSubscribeChannel and listens on it. it then launches the chipSeq.waspJob.jobflow.v1.
	 * The method verifies that a WaspStatus.STARTED is sent by the flow logic and a WaspStatus.COMPLETED 8s later.
	 * The headers and payload of the received messages are checked as is the order received.
	 */
	@Test (groups = "unit-tests")
	public void testJobApproved() throws Exception{
		//try{
			// setup job execution for the 'chipSeq.waspJob.jobflow.v1' job
		waspRunPublishSubscribeChannel = channelRegistry.getChannel("wasp.channel.notification.job", SubscribableChannel.class);
		waspRunPublishSubscribeChannel.subscribe(this); // register as a message handler on the waspRunPublishSubscribeChannel
		
			Job job = jobRegistry.getJob("chipSeq.waspJob.jobflow.v1"); // get the 'chipSeq.waspJob.jobflow.v1' job from the context
			Map<String, JobParameter> parameterMap = new HashMap<String, JobParameter>();
			parameterMap.put( JOB_ID_KEY, new JobParameter(JOB_ID.toString()) );
			JobExecution jobExecution = jobLauncher.run(job, new JobParameters(parameterMap));
			Thread.sleep(5000);
			
			// send approval messages (simulating button presses in web view)
			Message<WaspStatus> quoteApprovedMessage = WaspJobStatusMessageTemplate.build(JOB_ID, WaspStatus.COMPLETED, WaspJobTask.QUOTE);
			logger.debug("Sending message: "+quoteApprovedMessage);
			outboundRmiChannel.send(quoteApprovedMessage);
			
			Message<WaspStatus> piApprovedMessage = WaspJobStatusMessageTemplate.build(JOB_ID, WaspStatus.COMPLETED, WaspJobTask.PI_APPROVE);
			logger.debug("Sending message: "+piApprovedMessage);
			outboundRmiChannel.send(piApprovedMessage);
			
			Message<WaspStatus> adminApprovedMessage = WaspJobStatusMessageTemplate.build(JOB_ID, WaspStatus.COMPLETED, WaspJobTask.ADMIN_APPROVE);
			logger.debug("Sending message: "+adminApprovedMessage);
			outboundRmiChannel.send(adminApprovedMessage);
			
			
			// Delay to allow message receiving and transitions. Time out after 40s.
			int repeat = 0;
			while (message == null && repeat < 40){
				Thread.sleep(1000);
				repeat++;
			}
			if (message == null)
				Assert.fail("Timeout waiting to receive message on 'wasp.channel.notification.run'");
			
			// check headers as expected
			Assert.assertTrue(message.getHeaders().containsKey(JOB_ID_KEY));
			Assert.assertEquals(message.getHeaders().get(JOB_ID_KEY), JOB_ID);
			Assert.assertTrue(message.getHeaders().containsKey(WaspMessageType.HEADER));
			Assert.assertEquals(message.getHeaders().get(WaspMessageType.HEADER), WaspMessageType.JOB);
			
			// check payload as expected
			Assert.assertEquals(WaspStatus.class, message.getPayload().getClass());
			Assert.assertEquals(message.getPayload(), WaspStatus.CREATED);
			
			// check BatchStatus is as expected
			Assert.assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
		//} catch (Exception e){
			// caught an unexpected exception
		//	Assert.fail("Caught Exception: "+e.getMessage());
		//}
	}
	

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		logger.debug("Message recieved by handleMessage(): "+message.toString());
		if (WaspJobStatusMessageTemplate.actUponMessage(message, JOB_ID, WaspJobTask.NOTIFY_JOB_STATUS))
			this.message = message; 
	}
	
}
