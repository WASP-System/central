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
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.integration.rmi.RmiOutboundGateway;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.daemon.test.stubs.StubSampleDao;
import edu.yu.einstein.wasp.integration.messages.JobStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.LibraryStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.SampleStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.WaspJobTask;
import edu.yu.einstein.wasp.integration.messages.WaspSampleTask;
import edu.yu.einstein.wasp.integration.messages.payload.WaspStatus;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.model.SampleType;

@ContextConfiguration(locations={"/daemon-test-launch-context.xml"})

public class SampleFlowTests extends AbstractTestNGSpringContextTests implements MessageHandler {
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private MessageChannelRegistry channelRegistry;
	
	@Autowired 
	private JobRegistry jobRegistry;
	
	@Autowired
	private RmiOutboundGateway outboundGateway;	
		
	@Autowired
	private StubSampleDao stubSampleDao;
	
	private final Logger logger = LoggerFactory.getLogger(SampleFlowTests.class);
	
	private Message<?> message = null;
	
	private final String JOB_ID_KEY = "jobId";
	
	private final String SAMPLE_ID_KEY = "sampleId";
	
	private final Integer JOB_ID = 1;
	
	// need to use different sampleId for each test as database not reset and 
	// it is not possible to re-submit a batch job with an identical signature (parameters)
	private final Integer SAMPLE_ID = 1;
	private final Integer SAMPLE_ID2 = 2;
	private final Integer SAMPLE_ID3 = 3;
	
	private SubscribableChannel listeningChannel;
	private SubscribableChannel waspAbortChannel;
	
	
	@BeforeClass
	private void setup() throws SecurityException, NoSuchMethodException{
		Assert.assertNotNull(channelRegistry);
		Assert.assertNotNull(jobLauncher);
		Assert.assertNotNull(jobRegistry);
		waspAbortChannel = channelRegistry.getChannel("wasp.channel.notification.abort", SubscribableChannel.class);
		waspAbortChannel.subscribe(this);
	}
	
	@AfterClass
	private void tearDown(){
		waspAbortChannel.unsubscribe(this);
	}
	
	@AfterMethod
	private void unsubscribe(){
		listeningChannel.unsubscribe(this);
	}
	
		
	/**
	 * This test exercises the normal sample flow with a DNA sample received.
	 * The method sets up a listeningChannel and listens on it. It then launches the wasp.default.sample.mainFlow.v1 flow.
	 */
	@Test (groups = "unit-tests-batch-integration")
	public void testDNASampleReceived() throws Exception{
		try{
			listeningChannel = channelRegistry.getChannel("wasp.channel.notification.sample", SubscribableChannel.class);
			listeningChannel.subscribe(this); // register as a message handler on the listeningChannel
			// set a sampleType of 'dna' for the sample hardwired into the stub SampleDao
			SampleType sampleType = new SampleType();
			sampleType.setSampleTypeId(1);
			sampleType.setIName("dna");
			stubSampleDao.sample.setSampleType(sampleType);
			
			// setup job execution for the 'wasp.default.sample.mainFlow.v1' job
			Job job = jobRegistry.getJob("wasp.sample.jobflow.v1"); // get the 'wasp.default.sample.mainFlow.v1' job from the context
			Map<String, JobParameter> parameterMap = new HashMap<String, JobParameter>();
			parameterMap.put( JOB_ID_KEY, new JobParameter(JOB_ID.toString()) );
			parameterMap.put( SAMPLE_ID_KEY, new JobParameter(SAMPLE_ID.toString()) );
			JobExecution jobExecution = jobLauncher.run(job, new JobParameters(parameterMap));
			Thread.sleep(500); // allow some time for flow initialization
			
			// send CREATED sample message (simulating button presses in web view when sample received)
			SampleStatusMessageTemplate sampleTemplate = new SampleStatusMessageTemplate(SAMPLE_ID);
			sampleTemplate.setStatus(WaspStatus.CREATED);
			Message<WaspStatus> sampleCreatedNotificationMessage = sampleTemplate.build();
			logger.info("Sending message via 'outbound rmi gateway': "+sampleCreatedNotificationMessage.toString());
			Message<?> replyMessage = (Message<?>) outboundGateway.handleRequestMessage(sampleCreatedNotificationMessage);
			if (replyMessage != null)
				Assert.fail("Got unexpected reply message: "+ replyMessage.toString());
			
			// send ACCEPTED message (simulating job approval tasks completed by wasp job flow)
			JobStatusMessageTemplate jobTemplate = new JobStatusMessageTemplate(JOB_ID);
			jobTemplate.setStatus(WaspStatus.ACCEPTED);
			Message<WaspStatus> jobAcceptedNotificationMessage = jobTemplate.build();
			logger.info("Sending message via 'outbound rmi gateway': "+jobAcceptedNotificationMessage.toString());
			replyMessage = (Message<?>) outboundGateway.handleRequestMessage(jobAcceptedNotificationMessage);
			if (replyMessage != null)
				Assert.fail("Got unexpected reply message: "+ replyMessage.toString());
			
			Thread.sleep(500); // delay to allow processing of messages
			
			// send COMPLETED message (simulating job approval tasks completed by wasp job flow)
			sampleTemplate.setStatus(WaspStatus.COMPLETED);
			sampleTemplate.setTask(WaspSampleTask.QC);
			Message<WaspStatus> qcPassedNotificationMessage = sampleTemplate.build();
			logger.info("Sending message via 'outbound rmi gateway': "+qcPassedNotificationMessage.toString());
			replyMessage = (Message<?>) outboundGateway.handleRequestMessage(qcPassedNotificationMessage);
			if (replyMessage != null)
				Assert.fail("Got unexpected reply message: "+ replyMessage.toString());
			
			// Delay to allow message receiving and transitions. Time out after 40s.
			int repeat = 0;
			while ((message == null || 
					(! SampleStatusMessageTemplate.actUponMessage(message, SAMPLE_ID, WaspJobTask.NOTIFY_STATUS)) || 
					!message.getPayload().equals(WaspStatus.ACCEPTED)) && repeat < 40){
				message = null;
				Thread.sleep(1000);
				repeat++;
			}
			if (message == null)
				Assert.fail("Timeout waiting to receive message on 'wasp.channel.notification.sample'");
			
			Thread.sleep(500); // wait for message receiving and job completion events
			
			// check BatchStatus and ExitStatus are as expected
			Assert.assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
			Assert.assertEquals(jobExecution.getExitStatus().getExitCode(), ExitStatus.COMPLETED.getExitCode());
			
		} catch (Exception e){
			// caught an unexpected exception
			Assert.fail("Caught Exception: "+e.getMessage());
		}
	}
	
	/**
	 * This test exercises the normal sample flow with a DNA sample received.
	 * The method sets up a listeningChannel and listens on it. It then launches the wasp.default.sample.mainFlow.v1 flow.
	 */
	@Test (groups = "unit-tests-batch-integration")
	public void testLibrarySampleReceived() throws Exception{
		try{
			listeningChannel = channelRegistry.getChannel("wasp.channel.notification.library", SubscribableChannel.class);
			listeningChannel.subscribe(this); // register as a message handler on the listeningChannel
			// set a sampleType of 'library' for the sample hardwired into the stub SampleDao
			SampleType sampleType = new SampleType();
			sampleType.setSampleTypeId(1);
			sampleType.setIName("library");
			stubSampleDao.sample.setSampleType(sampleType);
			
			// setup job execution for the 'wasp.default.sample.mainFlow.v1' job
			Job job = jobRegistry.getJob("wasp.sample.jobflow.v1"); // get the 'wasp.default.sample.mainFlow.v1' job from the context
			Map<String, JobParameter> parameterMap = new HashMap<String, JobParameter>();
			parameterMap.put( JOB_ID_KEY, new JobParameter(JOB_ID.toString()) );
			parameterMap.put( SAMPLE_ID_KEY, new JobParameter(SAMPLE_ID2.toString()) );
			JobExecution jobExecution = jobLauncher.run(job, new JobParameters(parameterMap));
			Thread.sleep(500); // allow some time for flow initialization
			
			// send CREATED sample message (simulating button presses in web view when sample received)
			SampleStatusMessageTemplate sampleTemplate = new SampleStatusMessageTemplate(SAMPLE_ID2);
			sampleTemplate.setStatus(WaspStatus.CREATED);
			Message<WaspStatus> sampleCreatedNotificationMessage = sampleTemplate.build();
			logger.info("Sending message via 'outbound rmi gateway': "+sampleCreatedNotificationMessage.toString());
			Message<?> replyMessage = (Message<?>) outboundGateway.handleRequestMessage(sampleCreatedNotificationMessage);
			if (replyMessage != null)
				Assert.fail("Got unexpected reply message: "+ replyMessage.toString());
			
			// send ACCEPTED message (simulating job approval tasks completed by wasp job flow)
			JobStatusMessageTemplate jobTemplate = new JobStatusMessageTemplate(JOB_ID);
			jobTemplate.setStatus(WaspStatus.ACCEPTED);
			Message<WaspStatus> jobAcceptedNotificationMessage = jobTemplate.build();
			logger.info("Sending message via 'outbound rmi gateway': "+jobAcceptedNotificationMessage.toString());
			replyMessage = (Message<?>) outboundGateway.handleRequestMessage(jobAcceptedNotificationMessage);
			if (replyMessage != null)
				Assert.fail("Got unexpected reply message: "+ replyMessage.toString());
			
			int repeat = 0;
			while ((message == null || 
					(! LibraryStatusMessageTemplate.actUponMessage(message, SAMPLE_ID2, WaspJobTask.NOTIFY_STATUS)) || 
					!message.getPayload().equals(WaspStatus.CREATED)) && repeat < 40){
				message = null;
				Thread.sleep(1000);
				repeat++;
			}
			if (message == null)
				Assert.fail("Timeout waiting to receive message on 'wasp.channel.notification.library'");
			
			Thread.sleep(500); // wait for message receiving and job completion events
			
			// check BatchStatus and ExitStatus are as expected
			Assert.assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
			Assert.assertEquals(jobExecution.getExitStatus().getExitCode(), ExitStatus.COMPLETED.getExitCode());
			
		} catch (Exception e){
			// caught an unexpected exception
			Assert.fail("Caught Exception: "+e.getMessage());
		}
	}
	
	/**
	 * Tests aborting of batch job when a sample is aborted because of failing QC
	 * @throws Exception
	 */
	@Test (groups = "unit-tests-batch-integration")
	public void testSampleFailedQC() throws Exception{
		try{
			listeningChannel = channelRegistry.getChannel("wasp.channel.notification.sample", SubscribableChannel.class);
			listeningChannel.subscribe(this); // register as a message handler on the listeningChannel
			// set a sampleType of 'library' for the sample hardwired into the stub SampleDao
			SampleType sampleType = new SampleType();
			sampleType.setSampleTypeId(1);
			sampleType.setIName("sample");
			stubSampleDao.sample.setSampleType(sampleType);
			
			// setup job execution for the 'wasp.default.sample.mainFlow.v1' job
			Job job = jobRegistry.getJob("wasp.sample.jobflow.v1"); // get the 'wasp.default.sample.mainFlow.v1' job from the context
			Map<String, JobParameter> parameterMap = new HashMap<String, JobParameter>();
			parameterMap.put( JOB_ID_KEY, new JobParameter(JOB_ID.toString()) );
			parameterMap.put( SAMPLE_ID_KEY, new JobParameter(SAMPLE_ID3.toString()) );
			JobExecution jobExecution = jobLauncher.run(job, new JobParameters(parameterMap));
			Thread.sleep(500); // allow some time for flow initialization
			
			// send ACCEPTED message (simulating job approval tasks completed by wasp job flow)
			JobStatusMessageTemplate jobTemplate = new JobStatusMessageTemplate(JOB_ID);
			jobTemplate.setStatus(WaspStatus.ACCEPTED);
			Message<WaspStatus> jobAcceptedNotificationMessage = jobTemplate.build();
			logger.info("Sending message via 'outbound rmi gateway': "+jobAcceptedNotificationMessage.toString());
			Message<?> replyMessage = (Message<?>) outboundGateway.handleRequestMessage(jobAcceptedNotificationMessage);
			if (replyMessage != null)
				Assert.fail("Got unexpected reply message: "+ replyMessage.toString());
			
			// send CREATED sample message (simulating button presses in web view when sample not going to be received)
			SampleStatusMessageTemplate sampleTemplate = new SampleStatusMessageTemplate(SAMPLE_ID3);
			sampleTemplate.setStatus(WaspStatus.CREATED);
			Message<WaspStatus> sampleCreatedNotificationMessage = sampleTemplate.build();
			logger.info("Sending message via 'outbound rmi gateway': "+sampleCreatedNotificationMessage.toString());
			replyMessage = (Message<?>) outboundGateway.handleRequestMessage(sampleCreatedNotificationMessage);
			if (replyMessage != null)
				Assert.fail("Got unexpected reply message: "+ replyMessage.toString());
						
			Thread.sleep(500); // delay to allow processing of messages
			
			// send FAILED message (simulating QC fail notification completed by wasp job flow)
			sampleTemplate.setStatus(WaspStatus.FAILED);
			sampleTemplate.setTask(WaspSampleTask.QC);
			Message<WaspStatus> qcFailedNotificationMessage = sampleTemplate.build();
			logger.info("Sending message via 'outbound rmi gateway': "+qcFailedNotificationMessage.toString());
			replyMessage = (Message<?>) outboundGateway.handleRequestMessage(qcFailedNotificationMessage);
			if (replyMessage != null)
				Assert.fail("Got unexpected reply message: "+ replyMessage.toString());
			
			// Delay to allow message receiving and transitions. Time out after 40s.
			int repeat = 0;
			while ((message == null || 
					(! SampleStatusMessageTemplate.actUponMessage(message, SAMPLE_ID3, WaspJobTask.NOTIFY_STATUS)) || 
					!message.getPayload().equals(WaspStatus.ABANDONED)) && repeat < 40){
				message = null;
				Thread.sleep(1000);
				repeat++;
			}
			if (message == null)
				Assert.fail("Timeout waiting to receive message on 'wasp.channel.notification.abort'");
			
			Thread.sleep(500); // wait for message receiving and job completion events
			
			// check BatchStatus and ExitStatus are as expected
			Assert.assertEquals(jobExecution.getStatus(), BatchStatus.STOPPED);
			Assert.assertEquals(jobExecution.getExitStatus().getExitCode(), ExitStatus.STOPPED.getExitCode());
			
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
