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
import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.explore.wasp.WaspJobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PriorityChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.daemon.batch.tasklets.analysis.WaspJobSoftwareLaunchTasklet;
import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.endpoints.RunSuccessSplitter;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.templates.AnalysisStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.RunStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.interfacing.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobSoftware;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;

@ContextConfiguration(locations={"/daemon-test-launch-context.xml","/daemon-test-wiretap.xml","/daemon-test-batchJob.xml"})
public class PostRunAnalysisJobLaunchTests extends BatchDatabaseIntegrationTest implements MessageHandler {
	
	// mockRunService and mockRunDao are mocked in context to keep Spring happy when resolving dependencies on bean creation
	// but MUST be re-mocked here (not @Autowired in) otherwise there is autowiring issues with dependencies such as Entitymanager etc.
	
	@Mock private SampleService mockSampleService; 
	
	@Mock private RunService mockRunService; 
	
	@Mock private RunDao mockRunDao;
	
	@Mock private JobService mockJobService;
	
	@Mock private WaspPluginRegistry mockWaspPluginRegistry;
	
	@Autowired
	private RunSuccessSplitter runSuccessSplitter;
	
	@Autowired
	private WaspJobSoftwareLaunchTasklet waspJobSoftwareLaunchTasklet;
	
	@Autowired
	private ResourceType softwareResourceType;
	
	@Autowired
	@Qualifier("wasp.channel.priority.run")
	private PriorityChannel outboundMessageChannelRun;
	
	@Autowired
	@Qualifier("wasp.channel.priority.analysis")
	private PriorityChannel outboundMessageChannelAnalysis;
	
	@Autowired
	@Qualifier(MessageChannelRegistry.LAUNCH_MESSAGE_CHANNEL)
	private QueueChannel outboundMessageChannelLaunch;
	
	
	@Autowired
	@Qualifier("test.channel.queue.launch.wiretap")
	private DirectChannel launchChannelWireTap;
	
	@Autowired
	@Qualifier("wasp.channel.notification.analysis")
	private SubscribableChannel analysisChannel;
	
	@Autowired
	@Qualifier("wasp.channel.notification.abort")
	private SubscribableChannel abortChannel;
	
	private MessagingTemplate messagingTemplate;
	
	private final Logger logger = LoggerFactory.getLogger(PostRunAnalysisJobLaunchTests.class);
	
	private List<Message<?>> messages = null;
	
	private final Integer RUN_ID = 1;
	
	private final String CELL_LIBRARY_ID = "1";
	
	private Set<SampleSource> cellLibrarys;
	
	private SampleSource cellLibrary;
	
	private Job job;
	
	@BeforeMethod
	protected void beforeMethodSetup() throws Exception{
		super.cleanDB();
		messages = new ArrayList<Message<?>>();
	}
	
	@AfterMethod
	protected boolean afterMethodTeardown(){
		return super.stopRunningJobExecutions();
	}
	
	@BeforeClass
	@Override
	public void setup(){
		super.setup();
		MockitoAnnotations.initMocks(this);
		Assert.assertNotNull(mockRunDao);
		Assert.assertNotNull(mockRunService);
		Assert.assertNotNull(mockSampleService);
		Assert.assertNotNull(mockJobService);
		Assert.assertNotNull(mockWaspPluginRegistry);
		Assert.assertNotNull(runSuccessSplitter);
		Assert.assertNotNull(waspJobSoftwareLaunchTasklet);
		Assert.assertNotNull(softwareResourceType);
		Assert.assertNotNull(outboundMessageChannelRun);
		Assert.assertNotNull(outboundMessageChannelAnalysis);
		Assert.assertNotNull(outboundMessageChannelLaunch);
		Assert.assertNotNull(launchChannelWireTap);
		Assert.assertNotNull(analysisChannel);
		Assert.assertNotNull(abortChannel);
				
		launchChannelWireTap.subscribe(this); // register as a message handler on the launchChannelWireTap
		abortChannel.subscribe(this); // register as a message handler on the launchChannelWireTap
		analysisChannel.subscribe(this);
		messagingTemplate = new MessagingTemplate();
		messagingTemplate.setReceiveTimeout(2000);
		
		
		Workflow wf = new Workflow();
		wf.setIName("test_workflow");
		
		Sample library = new Sample();
		library.setId(1);
		
		Sample cell = new Sample();
		cell.setId(2);
		
		cellLibrary = new SampleSource();
		cellLibrary.setId(Integer.valueOf(CELL_LIBRARY_ID));
		cellLibrary.setSample(cell);
		cellLibrary.setSourceSample(library);
		
		cellLibrarys = new HashSet<SampleSource>();
		cellLibrarys.add(cellLibrary);
		
		job = new Job();
		job.setId(1);
		job.setWorkflow(wf);
		
		// add mocks to runSuccessSplitter (replacing Autowired versions)
		// could also use ReflectionTestUtils.setField(runSuccessSplitter, "runService", mockRunService) - essential if no setters
		runSuccessSplitter.setRunService(mockRunService);
		runSuccessSplitter.setWaspPluginRegistry(mockWaspPluginRegistry);
		runSuccessSplitter.setSampleService(mockSampleService);
		
		// add mocks to waspJobSoftwareLaunchTasklet (replacing Autowired versions)
		waspJobSoftwareLaunchTasklet.setJobService(mockJobService);
		waspJobSoftwareLaunchTasklet.setSampleService(mockSampleService);
		waspJobSoftwareLaunchTasklet.setWaspPluginRegistry(mockWaspPluginRegistry);
	}
	
	@AfterClass
	public void afterClass(){
		launchChannelWireTap.unsubscribe(this);
		abortChannel.unsubscribe(this);
		analysisChannel.unsubscribe(this);
	}
	
	@Test (groups = "unit-tests-batch-integration")
	public void runSuccessTest() throws Exception{
		// send run complete messages
		// add mocks to runSuccessSplitter (replacing Autowired versions)
		// could also use ReflectionTestUtils.setField(runSuccessSplitter, "runService", mockRunService) - essential if no setters
		Run run = new Run();
		run.setId(1);
		
		PowerMockito.when(mockRunService.getRunDao()).thenReturn(mockRunDao);
		PowerMockito.when(mockRunDao.getRunByRunId(1)).thenReturn(run);
		PowerMockito.when(mockRunService.getCellLibrariesOnSuccessfulRunCellsWithoutControls(Mockito.any(Run.class))).thenReturn(cellLibrarys);
		PowerMockito.when(mockSampleService.getJobOfLibraryOnCell(cellLibrary)).thenReturn(job);
		
		BatchJobProviding plugin = new BatchJobProviding() {
			@Override public String getBatchJobName(String BatchJobType) {return "skipTaskletJob";}
			@Override public Set<?> getProvides() { return null;	}
			@Override public Set<?> getHandles() { return null;	}
			@Override public String getIName() { return null; }
			@Override public String getName() { return null; }
			@Override public String getDescription() { return null; }
		};
		
		List<BatchJobProviding> plugins = new ArrayList<BatchJobProviding>();
		plugins.add(plugin);
		PowerMockito.when(mockWaspPluginRegistry.getPluginsHandlingArea("test_workflow", BatchJobProviding.class)).thenReturn(plugins);
		RunStatusMessageTemplate template = new RunStatusMessageTemplate(RUN_ID);
		template.setStatus(WaspStatus.COMPLETED);
		Message<WaspStatus> runCompletedMessage = template.build();
		logger.info("runSuccessTest(): Sending message via 'outbound rmi gateway': "+runCompletedMessage.toString());
		Message<?> replyMessage = messagingTemplate.sendAndReceive(outboundMessageChannelRun, runCompletedMessage);
		if (replyMessage != null)
			logger.debug("testJobApproved(): Got reply message: "+ replyMessage.toString());
		try{
			Thread.sleep(500);
		} catch (InterruptedException e){}; // wait for message receiving and job completion events
		Assert.assertEquals(messages.size(), 1);
		Assert.assertTrue(BatchJobLaunchMessageTemplate.isMessageOfCorrectType(messages.get(0)));
					
	}

	@Test (groups = "unit-tests-batch-integration")
	public void softwareLaunch() throws WaspMessageBuildingException {
		final String LAUNCH_JOB_NAME = "test.launchSoftwareJob";
		final String ALIGN_JOB_NAME = "test.doAlign";
		BatchJobProviding testPlugin = new BatchJobProviding() {
			@Override public String getBatchJobName(String BatchJobType) {return ALIGN_JOB_NAME;}
			@Override public Set<?> getProvides() { return null;	}
			@Override public Set<?> getHandles() { return null;	}
			@Override public String getIName() { return null; }
			@Override public String getName() { return null; }
			@Override public String getDescription() { return null; }
		};
		
		List<JobMeta> jobMetaList = new ArrayList<JobMeta>();
		JobMeta jobMeta1 = new JobMeta();
		jobMeta1.setId(1);
		jobMeta1.setJobId(1);
		jobMeta1.setK("testAligner.p1");
		jobMeta1.setV("p1Val");
		jobMetaList.add(jobMeta1);
		JobMeta jobMeta2 = new JobMeta();
		jobMeta2.setId(2);
		jobMeta2.setJobId(1);
		jobMeta2.setK("testAligner.p2");
		jobMeta2.setV("p2Val");
		jobMetaList.add(jobMeta2);
		job.setJobMeta(jobMetaList);
	
		Software software = new Software();
		software.setId(1);
		software.setIName("testAligner");
		software.setResourceType(softwareResourceType);
		JobSoftware jobSoftware = new JobSoftware();
		jobSoftware.setSoftware(software);
		jobSoftware.setJob(job);
		List<JobSoftware> jobSoftwares = new ArrayList<JobSoftware>();
		jobSoftwares.add(jobSoftware);
		job.setJobSoftware(jobSoftwares);
		
		
		PowerMockito.when(mockJobService.getJobByJobId(job.getId())).thenReturn(job);
		PowerMockito.when(mockWaspPluginRegistry.getPlugin(Mockito.anyString(), Mockito.eq(BatchJobProviding.class))).thenReturn(testPlugin);
		try {
			Map<String, String> jobParameters = new HashMap<String, String>();
			jobParameters.put(WaspJobParameters.CELL_LIBRARY_ID, CELL_LIBRARY_ID);
			BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate( 
				new BatchJobLaunchContext(LAUNCH_JOB_NAME, jobParameters) );
		
			Message<BatchJobLaunchContext> launchMessage = batchJobLaunchMessageTemplate.build();
			logger.debug("Sending the following launch message via channel " + MessageChannelRegistry.OUTBOUND_MESSAGE_CHANNEL + " : " + launchMessage);
			Message<?> replyMessage = messagingTemplate.sendAndReceive(outboundMessageChannelLaunch, launchMessage);
			if (replyMessage == null)
				logger.debug("testJobApproved(): Failed to receive reply message");

			int repeat = 0;
			boolean done = false;
			while (!done){
				for (Message<?> m: messages){
					if (AnalysisStatusMessageTemplate.actUponMessage(m, 1))
						done = true;
				}
				try{
					Thread.sleep(500);
				} catch (InterruptedException e){};
				repeat++;
				if (repeat == 40)
					Assert.fail("Timeout waiting to receive messages");
			}
			// validate proper completion of alignment step and that it was called with expected parameters
			JobExecution je = ((WaspJobExplorer) jobExplorer).getMostRecentlyStartedJobExecutionInList(((WaspJobExplorer) jobExplorer).getJobExecutions(ALIGN_JOB_NAME));
			ExitStatus status = je.getExitStatus();
			Assert.assertTrue(status.isCompleted());
			JobParameters params = je.getJobParameters();
			Assert.assertEquals(params.getParameters().size(), 4);
			Assert.assertNotNull(params.getString("cellLibraryIdList"));
			Assert.assertNotNull(params.getString("p1"));
			Assert.assertNotNull(params.getString("p2"));
				
		} catch (Exception e){
			// caught an unexpected exception
			 Assert.fail("Caught Exception of type "+ e.getClass().getName() + ": " + e.getMessage());
			 e.printStackTrace();
		}
	}

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		logger.debug("Message received by handleMessage(): "+message.toString());
		messages.add(message);
	}

}
