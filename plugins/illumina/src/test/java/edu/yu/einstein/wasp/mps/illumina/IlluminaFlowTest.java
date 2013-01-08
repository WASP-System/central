package edu.yu.einstein.wasp.mps.illumina;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.JobLauncherTestUtils;
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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.grid.file.SshFileService;
import edu.yu.einstein.wasp.grid.work.GridTransportService;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.plugin.wasp.illumina.WaspIlluminaPlugin;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;

@PrepareForTest
// PowerMockito
// TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, StepScopeTestExecutionListener.class })
// injection for step scope
@ContextConfiguration(locations = { "/illumina-test-application-context.xml" })
public class IlluminaFlowTest extends AbstractTestNGSpringContextTests
		implements MessageHandler {

	@Autowired
	private JobLauncherTestUtils jltu;

	private JobLauncher jobLauncher;

	@Autowired
	public void setJobLauncher(JobLauncher j) {
		this.jobLauncher = j;
	}

	private MessageChannelRegistry channelRegistry;

	@Autowired
	public void setChannelRegistry(MessageChannelRegistry m) {
		this.channelRegistry = m;
	}

	private JobRegistry jobRegistry;

	@Autowired
	public void setJobRegistry(JobRegistry j) {
		this.jobRegistry = j;
	}
	
	private SoftwarePackage casava;
	
	@Autowired
	private void setCasava(SoftwarePackage casava) {
		this.casava = casava;
	}

	private Message<?> message = null;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private DirectChannel outboundRmiChannel;
	private DirectChannel replyChannel;
	private SubscribableChannel abortChannel;
	private SubscribableChannel runChannel;
	
	@Autowired
	private GridHostResolver hostResolver;
	@Autowired
	private RunService runService;
	
	private SampleService sampleService;
	private AdaptorService adaptorService;
	private GridWorkService workService;
	private GridFileService fileService;
	
	private GridTransportService transportService;
	
	private Run run;

	@BeforeClass
	public void setUp() {
		Assert.assertNotNull(channelRegistry);
		Assert.assertNotNull(jobLauncher);
		Assert.assertNotNull(jobRegistry);
		outboundRmiChannel = channelRegistry.getChannel(MessageChannelRegistry.OUTBOUND_MESSAGE_CHANNEL, DirectChannel.class);
		replyChannel = channelRegistry.getChannel(MessageChannelRegistry.REPLY_MESSAGE_CHANNEL, DirectChannel.class);
		replyChannel.subscribe(this);
		abortChannel = channelRegistry.getChannel(MessageChannelRegistry.ABORT_MESSAGE_CHANNEL, SubscribableChannel.class);
		abortChannel.subscribe(this);
		runChannel = channelRegistry.getChannel(MessageChannelRegistry.RUN_MESSAGE_CHANNEL, SubscribableChannel.class);
		runChannel.subscribe(this);
	}
	
	@BeforeMethod
	public void prepare() {
		workService = PowerMockito.mock(GridWorkService.class);
		transportService = PowerMockito.mock(GridTransportService.class);
		fileService = new SshFileService(transportService);
		fileService = PowerMockito.spy(fileService);
		sampleService = PowerMockito.mock(SampleService.class);
		adaptorService = PowerMockito.mock(AdaptorService.class);
		run = PowerMockito.mock(Run.class);
	}

	@AfterClass
	public void tearDown() {
		// listeningChannel.unsubscribe(this);
		// replyChannel.unsubscribe(this);

	}
	
	private int jobcount = 0;

	private JobParameters getTestParameters() {
		return new JobParametersBuilder()
				.addString(WaspJobParameters.RUN_ID, "7")
				.addString(WaspJobParameters.RUN_NAME, "101010_TESTRUN")
				.addString("TestRunInstance", new Integer(jobcount++).toString())
				.toJobParameters();
	}

	// @Test
	public void testNotifyStart() throws Exception {
			jltu.launchStep(WaspIlluminaPlugin.STEP_NOTIFY_RUN_START, getTestParameters());
	}
	
	// @Test
	public void testListenRunStart() throws Exception {
		
		PowerMockito.when(hostResolver.getGridWorkService(Mockito.any(WorkUnit.class))).thenReturn(workService);
		PowerMockito.when(workService.getGridFileService()).thenReturn(fileService);
		PowerMockito.when(workService.getTransportService()).thenReturn(transportService);
		PowerMockito.when(transportService.getConfiguredSetting(Mockito.anyString())).thenReturn("configuredSetting");
		PowerMockito.when(hostResolver.getHostname(Mockito.any(WorkUnit.class))).thenReturn("remote.host");
		PowerMockito.when(fileService.exists(Mockito.anyString())).thenReturn(true);
		
		JobExecution je = jltu.launchStep(WaspIlluminaPlugin.STEP_LISTEN_FOR_RUN_START, getTestParameters());
		
		Thread.sleep(1000);
		
		StepExecution se = (StepExecution) je.getStepExecutions().toArray()[0];
		
		//file exists, step completes
		Assert.assertEquals(se.getStatus(), BatchStatus.COMPLETED);
	}

	// @Test
	public void testListenRunStartNotExists() throws Exception {
		
		PowerMockito.when(hostResolver.getGridWorkService(Mockito.any(WorkUnit.class))).thenReturn(workService);
		PowerMockito.when(workService.getGridFileService()).thenReturn(fileService);
		PowerMockito.when(workService.getTransportService()).thenReturn(transportService);
		PowerMockito.when(transportService.getConfiguredSetting(Mockito.anyString())).thenReturn("configuredSetting");
		PowerMockito.when(hostResolver.getHostname(Mockito.any(WorkUnit.class))).thenReturn("remote.host");
		PowerMockito.when(fileService.exists(Mockito.anyString())).thenReturn(false);
		
		JobExecution je = jltu.launchStep(WaspIlluminaPlugin.STEP_LISTEN_FOR_RUN_START, getTestParameters());
		
		Thread.sleep(1000);
		
		StepExecution se = (StepExecution) je.getStepExecutions().toArray()[0];
		
		//file does not exist, step repeats.
		Assert.assertEquals(se.getStatus(), BatchStatus.STARTED);
	}
	
	// @Test
	public void testListenRunComplete() throws Exception {
		PowerMockito.when(hostResolver.getGridWorkService(Mockito.any(WorkUnit.class))).thenReturn(workService);
		PowerMockito.when(workService.getGridFileService()).thenReturn(fileService);
		PowerMockito.when(workService.getTransportService()).thenReturn(transportService);
		PowerMockito.when(transportService.getConfiguredSetting(Mockito.anyString())).thenReturn("configuredSetting");
		PowerMockito.when(hostResolver.getHostname(Mockito.any(WorkUnit.class))).thenReturn("remote.host");
		PowerMockito.when(fileService.exists(Mockito.anyString())).thenReturn(true);
		
		JobExecution je = jltu.launchStep(WaspIlluminaPlugin.STEP_LISTEN_FOR_RUN_COMPLETION, getTestParameters());
		
		Thread.sleep(1000);
		
		StepExecution se = (StepExecution) je.getStepExecutions().toArray()[0];
		
		//file exists, step completes
		Assert.assertEquals(se.getStatus(), BatchStatus.COMPLETED);
		
	}
	
	@Test
	public void testListenForQC() throws Exception {
		jltu.launchStep(WaspIlluminaPlugin.STEP_LISTEN_FOR_QC, getTestParameters());
	}
	
	// @Test
	public void testSampleSheet() throws Exception {
		PowerMockito.when(run.getRunId()).thenReturn(2);
		PowerMockito.when(run.getName()).thenReturn("TESTRUN");
		PowerMockito.when(runService.getRunById(Mockito.anyInt())).thenReturn(run);
		Sample pu;
		pu = PowerMockito.mock(Sample.class);
		PowerMockito.when(run.getPlatformUnit()).thenReturn(pu);
		PowerMockito.when(sampleService.isPlatformUnit(pu)).thenReturn(true);
		PowerMockito.when(pu.getName()).thenReturn("TEST_PLATFORM_UNIT");
		PowerMockito.when(hostResolver.getGridWorkService(Mockito.any(WorkUnit.class))).thenReturn(workService);
		PowerMockito.when(workService.getGridFileService()).thenReturn(fileService);
		PowerMockito.when(hostResolver.getHostname(Mockito.any(WorkUnit.class))).thenReturn("test.host");
		PowerMockito.when(workService.getTransportService()).thenReturn(transportService);
		PowerMockito.when(transportService.getConfiguredSetting(Mockito.any(String.class))).thenReturn("TESTVALUE");
		
		Sample cell = new Sample();
		cell.setName("testcell");
		Map<Integer,Sample> map = new HashMap<Integer, Sample>();
		map.put(0,cell);
		PowerMockito.when(sampleService.getIndexedCellsOnPlatformUnit(pu)).thenReturn(map);
		List<Sample> li = new ArrayList<Sample>();
		Sample lib = new Sample();
		SampleType st = new SampleType();
		st.setIName("library");
		lib.setSampleType(st);
		Sample sample = new Sample();
		sample.setSampleId(2);
		sample.setName("samplename");
		lib.setParent(sample);
		li.add(lib);
		PowerMockito.when(sampleService.getLibrariesOnCell(cell)).thenReturn(li);
		PowerMockito.when(sampleService.getLibrariesOnCellWithoutControls(cell)).thenReturn(li);
		
		Adaptor ad = new Adaptor();
		ad.setBarcodesequence("AAAAA");
		PowerMockito.when(adaptorService.getAdaptor(lib)).thenReturn(ad);
		
		
		PowerMockito.doNothing().when(fileService, "put", Mockito.any(File.class), Mockito.anyString());
		
		jltu.launchStep(WaspIlluminaPlugin.STEP_CREATE_SAMPLE_SHEET, getTestParameters());
		
	}
	
	// @Test(groups = "integration-tests")
	// public void runIlluminaFlow() throws Exception {
	//
	// Run r = PowerMockito.mock(Run.class);
	//
	// String runname = "010101_TESTRUN";
	//
	// String flow = WaspIlluminaPlugin.FLOW_NAME;
	//
	// for (String x :jobRegistry.getJobNames()) {
	// logger.info("found registered job: " + x);
	// }
	//
	// PowerMockito.when(r.getRunId()).thenReturn(1);
	// PowerMockito.when(r.getName()).thenReturn(runname);
	//
	// Job job = jobRegistry.getJob(flow);
	//
	// Map<String, JobParameter> parameterMap = new HashMap<String,
	// JobParameter>();
	// parameterMap.put(WaspJobParameters.RUN_ID, new
	// JobParameter(r.getRunId().toString()));
	// parameterMap.put(WaspJobParameters.RUN_NAME, new
	// JobParameter(r.getName().toString()));
	// JobExecution jobExecution = jobLauncher.run(job, new
	// JobParameters(parameterMap));
	// Thread.sleep(20000);
	//
	//
	//
	// //verify
	// PowerMockito.verifyPrivate(r, Mockito.times(1)).invoke("getName");
	//
	// if (jobExecution.isRunning()) {
	// logger.info("end");
	// jobExecution.stop();
	// }
	//
	// Assert.assertEquals(jobExecution.getStatus(), BatchStatus.STOPPED);
	// }
	// @Test
	// public void testSkipStep() throws Exception {
	//
	// //= prepareMocksAndJob("wasp-illumina.mainFlow.listenForQCCompletion");
	//
	// jltu.launchStep(")
	//
	// logger.debug("starting");
	// while (je.isRunning()) {
	// logger.debug("waiting");
	// Thread.sleep(2000);
	// }
	// logger.debug("ending");
	//
	// }
	// @Test
	// public void testSampleSheet() throws Exception {
	// JobExecution je =
	// prepareMocksAndJob("wasp-illumina.mainFlow.stageResults");
	//
	// je.getStepExecutions();
	//
	// logger.debug("starting");
	//
	// while (je.isRunning()) {
	// logger.debug("waiting");
	// Thread.sleep(2000);
	// }
	// logger.debug("ending");
	// }
	//
	// public JobExecution prepareMocksAndJob(String stepName) {
	// Run r = PowerMockito.mock(Run.class);
	//
	// String runname = "010101_TESTRUN";
	//
	// String flow = WaspIlluminaPlugin.FLOW_NAME;
	//
	// for (String x : jobRegistry.getJobNames()) {
	// logger.info("found registered job: " + x);
	// }
	//
	// PowerMockito.when(r.getRunId()).thenReturn(1);
	// PowerMockito.when(r.getName()).thenReturn(runname);
	//
	// try {
	// Job job = jobRegistry.getJob(flow);
	// } catch (NoSuchJobException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// Map<String, JobParameter> parameterMap = new HashMap<String,
	// JobParameter>();
	// parameterMap.put(WaspJobParameters.RUN_ID, new
	// JobParameter(r.getRunId().toString()));
	// parameterMap.put(WaspJobParameters.RUN_NAME, new
	// JobParameter(r.getName().toString()));
	// JobParameters jp = new JobParameters(parameterMap);
	// return jltu.launchStep(stepName, jp);
	// }
	
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		logger.debug("Message recieved by handleMessage(): "
				+ message.toString());
		this.message = message;
	}
}
