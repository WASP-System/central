package edu.yu.einstein.wasp.daemon.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;
import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager.LockType;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspLibraryTask;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspSampleTask;
import edu.yu.einstein.wasp.integration.messages.templates.JobStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.LibraryStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.SampleStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleType;

@ContextConfiguration(locations={"/daemon-test-launch-context.xml"})
public class SampleFlowTests extends BatchDatabaseIntegrationTest implements MessageHandler {
	
	@Autowired
	private MessageChannelRegistry messageChannelRegistry;
	
	@Autowired
	BatchJobHibernationManager batchJobHibernationManager;
	
	private final String OUTBOUND_MESSAGE_CHANNEL = "wasp.channel.remoting.outbound";
	
	@Mock private SampleDao mockSampleDao;
	
	private MessagingTemplate messagingTemplate;
	
	private final Logger logger = LoggerFactory.getLogger(SampleFlowTests.class);
	
	private SubscribableChannel jobListeningChannel;
	
	private SubscribableChannel sampleListeningChannel;
	
	private SubscribableChannel libraryListeningChannel;
	
	private volatile List<Message<?>> messages = new ArrayList<>();
	
	private final String JOB_ID_KEY = "jobId";
	
	private final String SAMPLE_ID_KEY = "sampleId";
	
	private final Integer JOB_ID = 1;
	
	// need to use different sampleId for each test as database not reset and 
	// it is not possible to re-submit a batch job with an identical signature (parameters)
	private final Integer SAMPLE_ID = 1;
	private final Integer SAMPLE_ID2 = 500;
	private final Integer SAMPLE_ID3 = 600;
	private final Integer SAMPLE_ID4 = 700;

	@BeforeClass
	@Override
	protected void setup(){
		super.setup();
		MockitoAnnotations.initMocks(this);
		Assert.assertNotNull(messageChannelRegistry);
		jobListeningChannel = messageChannelRegistry.getChannel("wasp.channel.notification.job", SubscribableChannel.class);
		jobListeningChannel.subscribe(this); // register as a message handler on the listeningChannel
		sampleListeningChannel = messageChannelRegistry.getChannel("wasp.channel.notification.sample", SubscribableChannel.class);
		sampleListeningChannel.subscribe(this); // register as a message handler on the listeningChannel
		libraryListeningChannel = messageChannelRegistry.getChannel("wasp.channel.notification.library", SubscribableChannel.class);
		libraryListeningChannel.subscribe(this); // register as a message handler on the listeningChannel
		messagingTemplate = new MessagingTemplate();
		messagingTemplate.setReceiveTimeout(10000);
	}
	
	@AfterClass
	private void afterClassTeardown(){
		jobListeningChannel.unsubscribe(this);
		sampleListeningChannel.unsubscribe(this);
		libraryListeningChannel.unsubscribe(this);
	}
	
	@BeforeMethod
	protected void beforeMethodSetup() throws Exception{
		super.cleanDB();
	}
	
	@AfterMethod
	private void afterMethodTearDown(){
		stopRunningJobExecutions();
	}
	
	private void testSamplesReceived(int numSamples, int startId) throws Exception{
		messages = new ArrayList<>();
		Job job = jobRegistry.getJob("wasp.sample.jobflow.v1") ; // get the 'wasp.default.sample.mainFlow.v1' job from the context
		Map<String, JobParameter> parameterMap = new HashMap<String, JobParameter>();
		parameterMap.put( JOB_ID_KEY, new JobParameter(JOB_ID.toString()) );
		Set<Sample> samples = new HashSet<>();
		Set<JobExecution> jes = new HashSet<>();
		for (int i=0; i < numSamples; i++){ 
			Integer id = i+startId;
			SampleType sampleType = new SampleType();
			sampleType.setId(id);
			sampleType.setIName("dna");
			
			Sample sample = new Sample();
			sample.setId(SAMPLE_ID);
			sample.setSampleType(sampleType);
			
			samples.add(sample);
			Mockito.when(mockSampleDao.getSampleBySampleId(id)).thenReturn(sample);
		
			// setup job execution for the 'wasp.default.sample.mainFlow.v1' job
		
			parameterMap.put( SAMPLE_ID_KEY, new JobParameter(id.toString()));
			JobExecution jobExecution = jobLauncher.run(job, new JobParameters(parameterMap));
			jes.add(jobExecution);
		}
		try{
			Thread.sleep(Math.max(500, 50 * numSamples));
		} catch (InterruptedException e){}; // allow some time for flow initialization
		for (int i=0; i < numSamples; i++){ 
			Integer id = i+startId;
			// send CREATED sample message (simulating button presses in web view when sample received)
			SampleStatusMessageTemplate sampleTemplate = new SampleStatusMessageTemplate(id);
			sampleTemplate.setStatus(WaspStatus.CREATED);
			Message<WaspStatus> sampleCreatedNotificationMessage = sampleTemplate.build();
			logger.info("testSamplesReceived(): Sending message via 'outbound rmi gateway': "+sampleCreatedNotificationMessage.toString());
			Message<?> replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class), sampleCreatedNotificationMessage);
			if (replyMessage == null)
				Assert.fail("testSamplesReceived(): Failed to receive reply message");
			
		}
		try{
			Thread.sleep(2000);
		} catch (InterruptedException e){}; // delay to allow processing of messages
		// send ACCEPTED message (simulating job approval tasks completed by wasp job flow)
		JobStatusMessageTemplate jobTemplate = new JobStatusMessageTemplate(JOB_ID);
		jobTemplate.setStatus(WaspStatus.ACCEPTED);
		Message<WaspStatus> jobAcceptedNotificationMessage = jobTemplate.build();
		logger.info("testSamplesReceived(): Sending message via 'outbound rmi gateway': "+jobAcceptedNotificationMessage.toString());
		Message<?> replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class), jobAcceptedNotificationMessage);
		if (replyMessage == null)
			Assert.fail("testSamplesReceived(): Failed to receive reply message");
		
		try{
			Thread.sleep(Math.max(500, 50 * numSamples));
		} catch (InterruptedException e){}; // delay to allow processing of messages
		messages = new ArrayList<>();
		// send COMPLETED message (simulating job approval tasks completed by wasp job flow)
		int expectedMessages = numSamples *2;
		for (int i=0; i < numSamples; i++){ 
			Integer id = i+startId;
			SampleStatusMessageTemplate sampleTemplate = new SampleStatusMessageTemplate(id);
			sampleTemplate.setStatus(WaspStatus.COMPLETED);
			sampleTemplate.setTask(WaspSampleTask.QC);
			Message<WaspStatus> qcPassedNotificationMessage = sampleTemplate.build();
			logger.info("testSamplesReceived(): Sending message via 'outbound rmi gateway': "+qcPassedNotificationMessage.toString());
			replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class), qcPassedNotificationMessage);
			if (replyMessage == null)
				Assert.fail("testSamplesReceived(): Failed to receive reply message");
			
		}
		int repeat = 0;
		while (messages.size() < expectedMessages && repeat < 20){
			try{
				Thread.sleep(500);
			} catch (InterruptedException e){};
			repeat++;
		}
		if (messages.size() < expectedMessages){
			for (JobExecution je: jes){
				JobExecution freshJe = jobRepository.getLastJobExecution(je.getJobInstance().getJobName(), je.getJobParameters());
				logger.debug("JobExecution id=" + 
						freshJe.getId() + 
						", name=" + freshJe.getJobInstance().getJobName() + 
						", status=" + freshJe.getStatus() + 
						", exitStatus=" + freshJe.getExitStatus());
				for (StepExecution se : je.getStepExecutions()){
					StepExecution freshSe = jobExplorer.getStepExecution(freshJe.getId(), se.getId());
					logger.debug("-> StepExecution id=" + 
							se.getId() + 
							", name=" + se.getStepName() + 
							", status=" + se.getStatus() + 
							", exitStatus=" + se.getExitStatus());
				}
			}
			Assert.fail("testSamplesReceived(): Timeout waiting to receive messages on 'wasp.channel.notification.sample', Got " + messages.size() + 
					" messages but expected " + expectedMessages);
		}
		Set<Message<?>> testMessages = new HashSet<>(messages);
		for (Message<?> message : testMessages){
			for (int i=0; i < numSamples; i++){ 
				Integer id = i+startId;
				if (SampleStatusMessageTemplate.actUponMessage(message, id, WaspSampleTask.QC) || 
						(SampleStatusMessageTemplate.actUponMessage(message, id, WaspJobTask.NOTIFY_STATUS) && 
						message.getPayload().equals(WaspStatus.ACCEPTED)) )
					messages.remove(message);
				}
		}
		
		if (messages.size() != 0)
			Assert.fail("testSamplesReceived(): Did not get expected messsages. Unexpected messages remaining: " + testMessages);
		
		// check BatchStatus and ExitStatus are as expected
		repeat = 0;
		boolean allJobsComplete = true;
		JobExecution freshJe;
		while (!allJobsComplete && repeat++ < 10){
			for (JobExecution je: jes){
				freshJe = jobRepository.getLastJobExecution(je.getJobInstance().getJobName(), je.getJobParameters());
				if (!freshJe.getExitStatus().isCompleted()){
					allJobsComplete = false;
					break;
				}
			}
		}
		if (repeat == 10){
			stopRunningJobExecutions();
			Assert.fail("Not all jobs completed with ExistStatus.COMPLETED within suitable time interval");
		}
		if (!batchJobHibernationManager.getMessageTemplatesWakingStepExecutions().isEmpty())
			Assert.fail("MessageTemplatesWakingStepExecutions is not empty: " + batchJobHibernationManager.getMessageTemplatesWakingStepExecutions());
		
		if (!batchJobHibernationManager.getMessageTemplatesAbandoningStepExecutions().isEmpty())
			Assert.fail("MessageTemplatesAbandoningStepExecutions is not empty: " + batchJobHibernationManager.getMessageTemplatesAbandoningStepExecutions());

		for (JobExecution je: jes)
			if (BatchJobHibernationManager.isLockedJobExecution(je, LockType.ANY))
				Assert.fail("A lock is still present for JobExecution id=" + je.getId());
		
		for (JobExecution je: jes){
			freshJe = jobRepository.getLastJobExecution(je.getJobInstance().getJobName(), je.getJobParameters());
			logger.debug("JobExecution at end: " + freshJe.toString());
		}
	}
	
		
	/**
	 * This test exercises the normal sample flow with a DNA sample received.
	 * The method sets up a listeningChannel and listens on it. It then launches the wasp.default.sample.mainFlow.v1 flow.
	 */
	@Test (groups = "unit-tests-batch-integration")
	public void testDNASampleReceived() throws Exception{
		testSamplesReceived(1, 1);
	}
	
	/**
	 * This test exercises the normal sample flow with a DNA sample received.
	 * The method sets up a listeningChannel and listens on it. It then launches the wasp.default.sample.mainFlow.v1 flow.
	 */
	@Test (groups = "unit-tests-batch-integration")
	public void testManyDNASamplesReceived() throws Exception{
		testSamplesReceived(50, 2);
	}
	
	/**
	 * This test exercises the normal sample flow with a DNA sample received with many simultaneous messages.
	 * The method sets up a listeningChannel and listens on it. It then launches the wasp.default.sample.mainFlow.v1 flow.
	 */
	@Test (groups = "unit-tests-batch-integration")
	public void testLibrarySampleReceived() throws Exception{
		// set a sampleType of 'library' for the sample hardwired into the stub SampleDao
		SampleType sampleType = new SampleType();
		sampleType.setId(1);
		sampleType.setIName("library");
		
		Sample sample = new Sample();
		sample.setId(SAMPLE_ID2);
		sample.setSampleType(sampleType);
		Mockito.when(mockSampleDao.getSampleBySampleId(SAMPLE_ID2)).thenReturn(sample);
		
		// setup job execution for the 'wasp.default.sample.mainFlow.v1' job
		Job job = jobRegistry.getJob("wasp.userLibrary.jobflow.v1"); // get the 'wasp.default.sample.mainFlow.v1' job from the context
		Map<String, JobParameter> parameterMap = new HashMap<String, JobParameter>();
		parameterMap.put( JOB_ID_KEY, new JobParameter(JOB_ID.toString()) );
		parameterMap.put( SAMPLE_ID_KEY, new JobParameter(SAMPLE_ID2.toString()) );
		JobExecution jobExecution = jobLauncher.run(job, new JobParameters(parameterMap));
		try{
			Thread.sleep(500);
		} catch (InterruptedException e){}; // allow some time for flow initialization
		
		// send CREATED sample message (simulating button presses in web view when sample received)
		LibraryStatusMessageTemplate sampleTemplate = new LibraryStatusMessageTemplate(SAMPLE_ID2);
		sampleTemplate.setStatus(WaspStatus.CREATED);
		Message<WaspStatus> sampleCreatedNotificationMessage = sampleTemplate.build();
		logger.info("testLibrarySampleReceived(): Sending message via 'outbound rmi gateway': "+sampleCreatedNotificationMessage.toString());
		Message<?> replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class), sampleCreatedNotificationMessage);
		if (replyMessage == null)
			Assert.fail("testLibrarySampleReceived(): Failed to receive reply message");
		
		// send ACCEPTED message (simulating job approval tasks completed by wasp job flow)
		JobStatusMessageTemplate jobTemplate = new JobStatusMessageTemplate(JOB_ID);
		jobTemplate.setStatus(WaspStatus.ACCEPTED);
		Message<WaspStatus> jobAcceptedNotificationMessage = jobTemplate.build();
		logger.info("testLibrarySampleReceived(): Sending message via 'outbound rmi gateway': "+jobAcceptedNotificationMessage.toString());
		replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class), jobAcceptedNotificationMessage);
		if (replyMessage == null)
			Assert.fail("testLibrarySampleReceived(): Failed to receive reply message");
		try{
			Thread.sleep(100);
		} catch (InterruptedException e){};
		sampleTemplate.setTask(WaspLibraryTask.QC);
		sampleTemplate.setStatus(WaspStatus.COMPLETED);
		Message<WaspStatus> sampleQCNotificationMessage = sampleTemplate.build();
		logger.info("testLibrarySampleReceived(): Sending message via 'outbound rmi gateway': "+sampleQCNotificationMessage.toString());
		replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class), sampleQCNotificationMessage);
		if (replyMessage == null)
			Assert.fail("testLibrarySampleReceived(): Failed to receive reply message");
		
		int repeat = 0;
		while ((messages.size() == 0 || 
				(! LibraryStatusMessageTemplate.actUponMessage(messages.get(0), SAMPLE_ID2, WaspJobTask.NOTIFY_STATUS)) || 
				!messages.get(0).getPayload().equals(WaspStatus.ACCEPTED)) && repeat < 10){
			try{
				Thread.sleep(500);
			} catch (InterruptedException e){};
			repeat++;
		}
		if (messages.size() == 0)
			Assert.fail("testLibrarySampleReceived(): Timeout waiting to receive message on 'wasp.channel.notification.library'");
		
		repeat = 0;
		JobExecution freshJe;
		do {
			try{
				Thread.sleep(500); // allow some time for stopping
			} catch (InterruptedException e){};
			freshJe = jobRepository.getLastJobExecution(jobExecution.getJobInstance().getJobName(), jobExecution.getJobParameters());
		} while (!freshJe.getExitStatus().isCompleted() && repeat++ < 10);
		if (repeat == 10){
			Assert.fail("Did not complete with ExistStatus.COMPLETED within suitable time interval");
		}
		jobExecution.stop();
	}
	
	/**
	 * Tests aborting of batch job when a sample is aborted because of failing QC
	 * @throws Exception
	 */
	@Test (groups = "unit-tests-batch-integration")
	public void testSampleFailedQC() throws Exception{
		messages = new ArrayList<>();
		// set a sampleType of 'library' for the sample hardwired into the stub SampleDao
		SampleType sampleType = new SampleType();
		sampleType.setId(1);
		sampleType.setIName("sample");
		
		Sample sample = new Sample();
		sample.setId(SAMPLE_ID3);
		sample.setSampleType(sampleType);
		Mockito.when(mockSampleDao.getSampleBySampleId(SAMPLE_ID3)).thenReturn(sample);
		
		// setup job execution for the 'wasp.default.sample.mainFlow.v1' job
		Job job = jobRegistry.getJob("wasp.sample.jobflow.v1"); // get the 'wasp.default.sample.mainFlow.v1' job from the context
		Map<String, JobParameter> parameterMap = new HashMap<String, JobParameter>();
		parameterMap.put( JOB_ID_KEY, new JobParameter(JOB_ID.toString()) );
		parameterMap.put( SAMPLE_ID_KEY, new JobParameter(SAMPLE_ID3.toString()) );
		JobExecution jobExecution = jobLauncher.run(job, new JobParameters(parameterMap));
		try{
			Thread.sleep(500);
		} catch (InterruptedException e){}; // allow some time for flow initialization
		
		// send ACCEPTED message (simulating job approval tasks completed by wasp job flow)
		JobStatusMessageTemplate jobTemplate = new JobStatusMessageTemplate(JOB_ID);
		jobTemplate.setStatus(WaspStatus.ACCEPTED);
		Message<WaspStatus> jobAcceptedNotificationMessage = jobTemplate.build();
		logger.info("testSampleFailedQC(): Sending message via 'outbound rmi gateway': "+jobAcceptedNotificationMessage.toString());
		Message<?> replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class), jobAcceptedNotificationMessage);
		if (replyMessage == null)
			Assert.fail("testSampleFailedQC(): Failed to receive reply message");
		
		// send CREATED sample message (simulating button presses in web view when sample not going to be received)
		SampleStatusMessageTemplate sampleTemplate = new SampleStatusMessageTemplate(SAMPLE_ID3);
		sampleTemplate.setStatus(WaspStatus.CREATED);
		Message<WaspStatus> sampleCreatedNotificationMessage = sampleTemplate.build();
		logger.info("testSampleFailedQC(): Sending message via 'outbound rmi gateway': "+sampleCreatedNotificationMessage.toString());
		replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class), sampleCreatedNotificationMessage);
		if (replyMessage == null)
			Assert.fail("testSampleFailedQC(): Failed to receive reply message");
					
		try{
			Thread.sleep(500);
		} catch (InterruptedException e){}; // delay to allow processing of messages
		
		// send FAILED message (simulating QC fail notification completed by wasp job flow)
		sampleTemplate.setStatus(WaspStatus.FAILED);
		sampleTemplate.setTask(WaspSampleTask.QC);
		Message<WaspStatus> qcFailedNotificationMessage = sampleTemplate.build();
		logger.info("testSampleFailedQC(): Sending message via 'outbound rmi gateway': "+qcFailedNotificationMessage.toString());
		replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class), qcFailedNotificationMessage);
		if (replyMessage == null)
			Assert.fail("testSampleFailedQC(): Failed to receive reply message");
		
		// Delay to allow message receiving and transitions. Time out after 5.
		int repeat = 0;
		while ((messages.size() == 0 || 
				(! SampleStatusMessageTemplate.actUponMessage(messages.get(0), SAMPLE_ID3, WaspJobTask.NOTIFY_STATUS)) || 
				!messages.get(0).getPayload().equals(WaspStatus.ABANDONED)) && repeat < 10){
			try{
				Thread.sleep(500);
			} catch (InterruptedException e){};
			repeat++;
		}
		if (messages.size() == 0)
			Assert.fail("testSampleFailedQC(): Timeout waiting to receive message on 'wasp.channel.notification.abort'");
		
		repeat = 0;
		JobExecution freshJe;
		do {
			try{
				Thread.sleep(500); // allow some time for stopping
			} catch (InterruptedException e){};
			freshJe = jobRepository.getLastJobExecution(jobExecution.getJobInstance().getJobName(), jobExecution.getJobParameters());
		} while (!freshJe.getExitStatus().isCompleted() && repeat++ < 10);
		if (repeat == 10){
			Assert.fail("Did not complete with ExistStatus.COMPLETED within suitable time interval");
		}
		logger.debug("JobExecution at end: " + freshJe.toString());
	}
	
	/**
	 * This test exercises the normal sample flow with a DNA sample received.
	 * The method sets up a listeningChannel and listens on it. It then launches the wasp.default.sample.mainFlow.v1 flow.
	 */
	@Test (groups = "unit-tests-batch-integration")
	public void testJobAbandoned() throws Exception{
		messages = new ArrayList<>();
		SampleType sampleType = new SampleType();
		sampleType.setId(1);
		sampleType.setIName("dna");
		
		Sample sample = new Sample();
		sample.setId(SAMPLE_ID4);
		sample.setSampleType(sampleType);
		Mockito.when(mockSampleDao.getSampleBySampleId(SAMPLE_ID4)).thenReturn(sample);
		
		// setup job execution for the 'wasp.default.sample.mainFlow.v1' job
		Job job = jobRegistry.getJob("wasp.sample.jobflow.v1"); // get the 'wasp.default.sample.mainFlow.v1' job from the context
		Map<String, JobParameter> parameterMap = new HashMap<String, JobParameter>();
		parameterMap.put( JOB_ID_KEY, new JobParameter(JOB_ID.toString()) );
		parameterMap.put( SAMPLE_ID_KEY, new JobParameter(SAMPLE_ID4.toString()) );
		JobExecution jobExecution = jobLauncher.run(job, new JobParameters(parameterMap));
		try{
			Thread.sleep(500);
		} catch (InterruptedException e){}; // allow some time for flow initialization
		
		// send CREATED sample message (simulating button presses in web view when sample received)
		SampleStatusMessageTemplate sampleTemplate = new SampleStatusMessageTemplate(SAMPLE_ID4);
		sampleTemplate.setStatus(WaspStatus.CREATED);
		Message<WaspStatus> sampleCreatedNotificationMessage = sampleTemplate.build();
		logger.info("testJobAbandoned(): Sending message via 'outbound rmi gateway': "+sampleCreatedNotificationMessage.toString());
		Message<?> replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class), sampleCreatedNotificationMessage);
		if (replyMessage == null)
			Assert.fail("testJobAbandoned(): Failed to receive reply message");
		try{
			Thread.sleep(1);
		} catch (InterruptedException e){}; // delay to allow processing of messages
		// send ABANDONED message (simulating job abandonment)
		JobStatusMessageTemplate jobTemplate = new JobStatusMessageTemplate(JOB_ID);
		jobTemplate.setStatus(WaspStatus.ABANDONED);
		Message<WaspStatus> jobAcceptedNotificationMessage = jobTemplate.build();
		logger.info("testJobAbandoned(): Sending message via 'outbound rmi gateway': "+jobAcceptedNotificationMessage.toString());
		replyMessage = messagingTemplate.sendAndReceive(messageChannelRegistry.getChannel(OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class), jobAcceptedNotificationMessage);
		if (replyMessage == null)
			Assert.fail("testJobAbandoned(): Failed to receive reply message");
		int repeat = 0;
		JobExecution freshJe;
		do {
			try{
				Thread.sleep(500); // allow some time for stopping
			} catch (InterruptedException e){};
			freshJe = jobRepository.getLastJobExecution(jobExecution.getJobInstance().getJobName(), jobExecution.getJobParameters());
		} while (!freshJe.getExitStatus().isTerminated() && repeat++ < 10);
		if (repeat == 10){
			Assert.fail("Did not complete with ExistStatus.TERMINATED within suitable time interval");
		}
		logger.debug("JobExecution at end: " + freshJe.toString());
	}
	
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		logger.debug("Message received by handleMessage(): "+message.toString());
		this.messages.add(message); 
	}
	
}
