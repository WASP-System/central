package edu.yu.einstein;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
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
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.test.stubs.StubSampleDao;
import edu.yu.einstein.wasp.messages.JobStatusMessageTemplate;
import edu.yu.einstein.wasp.messages.LibraryStatusMessageTemplate;
import edu.yu.einstein.wasp.messages.SampleStatusMessageTemplate;
import edu.yu.einstein.wasp.messages.WaspJobTask;
import edu.yu.einstein.wasp.messages.WaspSampleTask;
import edu.yu.einstein.wasp.messages.WaspStatus;
import edu.yu.einstein.wasp.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleType;




@ContextConfiguration(locations={"classpath:test-launch-context.xml", "classpath:RmiMessageSend-context.xml"})

public class SampleFlowTests extends AbstractTestNGSpringContextTests implements MessageHandler {
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private MessageChannelRegistry channelRegistry;
	
	@Autowired 
	private JobRegistry jobRegistry;
	
	@Autowired
	private StubSampleDao stubSampleDao;
	
	private final Logger logger = Logger.getLogger(SampleFlowTests.class);
	
	private Message<?> message = null;
	
	private final String JOB_ID_KEY = "jobId";
	
	private final String SAMPLE_ID_KEY = "sampleId";
	
	private final Integer JOB_ID = 1;
	
	// need to use different sampleId for each test as database not reset and 
	// it is not possible to re-submit a batch job with an identical signature (parameters)
	private final Integer SAMPLE_ID = 1;
	private final Integer SAMPLE_ID2 = 2;
	private final Integer SAMPLE_ID3 = 3;
	
	private DirectChannel outboundRmiChannel;
	private DirectChannel replyChannel;
	private SubscribableChannel listeningChannel;
	private SubscribableChannel waspAbortChannel;
	
	
	@BeforeClass
	private void setup() throws SecurityException, NoSuchMethodException{
		Assert.assertNotNull(channelRegistry);
		Assert.assertNotNull(jobLauncher);
		Assert.assertNotNull(jobRegistry);
		outboundRmiChannel = channelRegistry.getChannel("wasp.channel.rmi.outbound", DirectChannel.class);
		replyChannel = channelRegistry.getChannel("wasp.channel.rmi.outbound.reply", DirectChannel.class);
		replyChannel.subscribe(this);
		waspAbortChannel = channelRegistry.getChannel("wasp.channel.notification.abort", SubscribableChannel.class);
		waspAbortChannel.subscribe(this);
	}
	
	@AfterClass
	private void tearDown(){
		waspAbortChannel.unsubscribe(this);
		replyChannel.unsubscribe(this);
	}
	
	@AfterMethod
	private void unsubscribe(){
		listeningChannel.unsubscribe(this);
	}
	
		
	/**
	 * This test exercises the normal sample flow with a DNA sample received.
	 * The method sets up a listeningChannel and listens on it. It then launches the wasp.sample.mainFlow.v1 flow.
	 */
	@Test (groups = "unit-tests")
	public void testDNASampleReceived() throws Exception{
		try{
			listeningChannel = channelRegistry.getChannel("wasp.channel.notification.sample", SubscribableChannel.class);
			listeningChannel.subscribe(this); // register as a message handler on the listeningChannel
			// set a sampleType of 'dna' for the sample hardwired into the stub SampleDao
			SampleType sampleType = new SampleType();
			sampleType.setSampleTypeId(1);
			sampleType.setIName("dna");
			stubSampleDao.sample.setSampleType(sampleType);
			
			// setup job execution for the 'wasp.sample.mainFlow.v1' job
			Job job = jobRegistry.getJob("wasp.sample.jobflow.v1"); // get the 'wasp.sample.mainFlow.v1' job from the context
			Map<String, JobParameter> parameterMap = new HashMap<String, JobParameter>();
			parameterMap.put( JOB_ID_KEY, new JobParameter(JOB_ID.toString()) );
			parameterMap.put( SAMPLE_ID_KEY, new JobParameter(SAMPLE_ID.toString()) );
			JobExecution jobExecution = jobLauncher.run(job, new JobParameters(parameterMap));
			Thread.sleep(5000); // allow some time for flow initialization
			
			// send CREATED sample message (simulating button presses in web view when sample received)
			SampleStatusMessageTemplate sampleTemplate = new SampleStatusMessageTemplate(SAMPLE_ID);
			sampleTemplate.setStatus(WaspStatus.CREATED);
			Message<WaspStatus> sampleCreatedNotificationMessage = sampleTemplate.build();
			logger.debug("Sending message: "+sampleCreatedNotificationMessage);
			outboundRmiChannel.send(sampleCreatedNotificationMessage);
			
			// send ACCEPTED message (simulating job approval tasks completed by wasp job flow)
			JobStatusMessageTemplate jobTemplate = new JobStatusMessageTemplate(JOB_ID);
			jobTemplate.setStatus(WaspStatus.ACCEPTED);
			Message<WaspStatus> jobAcceptedNotificationMessage = jobTemplate.build();
			logger.debug("Sending message: "+jobAcceptedNotificationMessage);
			outboundRmiChannel.send(jobAcceptedNotificationMessage);
			
			Thread.sleep(5000); // delay to allow processing of messages
			
			// send COMPLETED message (simulating job approval tasks completed by wasp job flow)
			sampleTemplate.setStatus(WaspStatus.COMPLETED);
			sampleTemplate.setTask(WaspSampleTask.QC);
			Message<WaspStatus> qcPassedNotificationMessage = sampleTemplate.build();
			logger.debug("Sending message: "+qcPassedNotificationMessage);
			outboundRmiChannel.send(qcPassedNotificationMessage);
			
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
			
			Thread.sleep(5000); // wait for message receiving and job completion events
			
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
	 * The method sets up a listeningChannel and listens on it. It then launches the wasp.sample.mainFlow.v1 flow.
	 */
	@Test (groups = "unit-tests")
	public void testLibrarySampleReceived() throws Exception{
		try{
			listeningChannel = channelRegistry.getChannel("wasp.channel.notification.library", SubscribableChannel.class);
			listeningChannel.subscribe(this); // register as a message handler on the listeningChannel
			// set a sampleType of 'library' for the sample hardwired into the stub SampleDao
			SampleType sampleType = new SampleType();
			sampleType.setSampleTypeId(1);
			sampleType.setIName("library");
			stubSampleDao.sample.setSampleType(sampleType);
			
			// setup job execution for the 'wasp.sample.mainFlow.v1' job
			Job job = jobRegistry.getJob("wasp.sample.jobflow.v1"); // get the 'wasp.sample.mainFlow.v1' job from the context
			Map<String, JobParameter> parameterMap = new HashMap<String, JobParameter>();
			parameterMap.put( JOB_ID_KEY, new JobParameter(JOB_ID.toString()) );
			parameterMap.put( SAMPLE_ID_KEY, new JobParameter(SAMPLE_ID2.toString()) );
			JobExecution jobExecution = jobLauncher.run(job, new JobParameters(parameterMap));
			Thread.sleep(5000); // allow some time for flow initialization
			
			// send CREATED sample message (simulating button presses in web view when sample received)
			SampleStatusMessageTemplate sampleTemplate = new SampleStatusMessageTemplate(SAMPLE_ID2);
			sampleTemplate.setStatus(WaspStatus.CREATED);
			Message<WaspStatus> sampleCreatedNotificationMessage = sampleTemplate.build();
			logger.debug("Sending message: "+sampleCreatedNotificationMessage);
			outboundRmiChannel.send(sampleCreatedNotificationMessage);
			
			// send ACCEPTED message (simulating job approval tasks completed by wasp job flow)
			JobStatusMessageTemplate jobTemplate = new JobStatusMessageTemplate(JOB_ID);
			jobTemplate.setStatus(WaspStatus.ACCEPTED);
			Message<WaspStatus> jobAcceptedNotificationMessage = jobTemplate.build();
			logger.debug("Sending message: "+jobAcceptedNotificationMessage);
			outboundRmiChannel.send(jobAcceptedNotificationMessage);
			
			Thread.sleep(5000); // delay to allow processing of messages
			
			// send COMPLETED message (simulating QC complete task completed by wasp job flow)
			sampleTemplate.setStatus(WaspStatus.COMPLETED);
			sampleTemplate.setTask(WaspSampleTask.QC);
			Message<WaspStatus> qcPassedNotificationMessage = sampleTemplate.build();
			logger.debug("Sending message: "+qcPassedNotificationMessage);
			outboundRmiChannel.send(qcPassedNotificationMessage);
			
			// Delay to allow message receiving and transitions. Time out after 40s.
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
			
			Thread.sleep(5000); // wait for message receiving and job completion events
			
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
	@Test (groups = "unit-tests")
	public void testSampleFailedQC() throws Exception{
		try{
			listeningChannel = channelRegistry.getChannel("wasp.channel.notification.sample", SubscribableChannel.class);
			listeningChannel.subscribe(this); // register as a message handler on the listeningChannel
			// set a sampleType of 'library' for the sample hardwired into the stub SampleDao
			SampleType sampleType = new SampleType();
			sampleType.setSampleTypeId(1);
			sampleType.setIName("sample");
			stubSampleDao.sample.setSampleType(sampleType);
			
			// setup job execution for the 'wasp.sample.mainFlow.v1' job
			Job job = jobRegistry.getJob("wasp.sample.jobflow.v1"); // get the 'wasp.sample.mainFlow.v1' job from the context
			Map<String, JobParameter> parameterMap = new HashMap<String, JobParameter>();
			parameterMap.put( JOB_ID_KEY, new JobParameter(JOB_ID.toString()) );
			parameterMap.put( SAMPLE_ID_KEY, new JobParameter(SAMPLE_ID3.toString()) );
			JobExecution jobExecution = jobLauncher.run(job, new JobParameters(parameterMap));
			Thread.sleep(5000); // allow some time for flow initialization
			
			// send ACCEPTED message (simulating job approval tasks completed by wasp job flow)
			JobStatusMessageTemplate jobTemplate = new JobStatusMessageTemplate(JOB_ID);
			jobTemplate.setStatus(WaspStatus.ACCEPTED);
			Message<WaspStatus> jobAcceptedNotificationMessage = jobTemplate.build();
			logger.debug("Sending message: "+jobAcceptedNotificationMessage);
			outboundRmiChannel.send(jobAcceptedNotificationMessage);
			
			// send CREATED sample message (simulating button presses in web view when sample not going to be received)
			SampleStatusMessageTemplate sampleTemplate = new SampleStatusMessageTemplate(SAMPLE_ID3);
			sampleTemplate.setStatus(WaspStatus.CREATED);
			Message<WaspStatus> sampleCreatedNotificationMessage = sampleTemplate.build();
			logger.debug("Sending message: "+sampleCreatedNotificationMessage);
			outboundRmiChannel.send(sampleCreatedNotificationMessage);
						
			Thread.sleep(5000); // delay to allow processing of messages
			
			// send FAILED message (simulating QC fail notification completed by wasp job flow)
			sampleTemplate.setStatus(WaspStatus.FAILED);
			sampleTemplate.setTask(WaspSampleTask.QC);
			Message<WaspStatus> qcFailedNotificationMessage = sampleTemplate.build();
			logger.debug("Sending message: "+qcFailedNotificationMessage);
			outboundRmiChannel.send(qcFailedNotificationMessage);
			
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
			
			Thread.sleep(5000); // wait for message receiving and job completion events
			
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
