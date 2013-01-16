package edu.yu.einstein.wasp.daemon.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PriorityChannel;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.integration.messages.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.RunStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.service.RunService;

@ContextConfiguration(locations={"/daemon-test-launch-context.xml","/daemon-test-wiretap.xml"})

public class RunSuccessTests extends AbstractTestNGSpringContextTests implements MessageHandler {
	
	// mockRunService and mockRunDao are mocked in context to keep Spring happy when resolving dependencies on bean creation
	// but MUST be re-mocked here (not @Autowied in) otherwise there is autowiring issues with dependencies such as Entitymanager etc.
	@Mock private RunService mockRunService; 
	
	@Mock private RunDao mockRunDao;
	
	@Mock private WaspPluginRegistry mockWaspPluginRegistry;
	
	private MessagingTemplate messagingTemplate;
	
	private final Logger logger = LoggerFactory.getLogger(RunSuccessTests.class);
	
	private List<Message<?>> messages = null;
	
	private final Integer RUN_ID = 1;
	
	@Autowired
	@Qualifier("wasp.channel.priority.run")
	private PriorityChannel outboundMessageChannel;
	
	
	@Autowired
	@Qualifier("test.channel.queue.launch.wiretap")
	private DirectChannel listeningChannel;
	
	@Autowired
	@Qualifier("wasp.channel.notification.abort")
	private SubscribableChannel abortChannel;
	
	@BeforeTest
	public void setupMocks(){
		MockitoAnnotations.initMocks(this);
		Assert.assertNotNull(mockRunDao);
		Assert.assertNotNull(mockRunService);
		Assert.assertNotNull(mockWaspPluginRegistry);
	}
	
	@BeforeMethod
	public void beforeMethod() {
		
		listeningChannel.subscribe(this); // register as a message handler on the listeningChannel
		abortChannel.subscribe(this); // register as a message handler on the listeningChannel
		messagingTemplate = new MessagingTemplate();
		messagingTemplate.setReceiveTimeout(2000);
		messages = new ArrayList<Message<?>>();
		
	}

	@AfterMethod
	public void afterMethod() {
		  listeningChannel.unsubscribe(this);
		  abortChannel.unsubscribe(this);
	}

	
	@Test (groups = "unit-tests-batch-integration")
	public void runSuccessTest() throws Exception{
		// send run complete messages
		try {
			Run run = new Run();
			
			
			Workflow wf = new Workflow();
			wf.setIName("test_workflow");
			
			Sample library = new Sample();
			library.setSampleId(1);
			Job job = new Job();
			job.setJobId(1);
			job.setWorkflow(wf);
			Map<Sample, Job> libraryJob = new HashMap<Sample, Job>();
			libraryJob.put(library, job);
			PowerMockito.when(mockRunService.getRunDao()).thenReturn(mockRunDao);
			PowerMockito.when(mockRunService.getLibraryJobPairsOnSuccessfulRunCellsWithoutControls(run)).thenReturn(libraryJob);
			PowerMockito.when(mockRunDao.getRunByRunId(1)).thenReturn(run);
			WaspPlugin plugin = new WaspPlugin("test-plugin", null, null){
				@Override public void destroy() throws Exception {}
				@Override public void afterPropertiesSet() throws Exception {}
				@Override public String getBatchJobNameByArea(String BatchJobType, String area) {return null;}
				@Override public String getBatchJobName(String BatchJobType) {return "test_flow_name";}
			};
			Set<WaspPlugin> plugins = new HashSet<WaspPlugin>();
			plugins.add(plugin);
			PowerMockito.when(mockWaspPluginRegistry.getPluginsHandlingArea("test_workflow")).thenReturn(plugins);
			
			RunStatusMessageTemplate template = new RunStatusMessageTemplate(RUN_ID);
			template.setStatus(WaspStatus.COMPLETED);
			Message<WaspStatus> runCompletedMessage = template.build();
			logger.info("runSuccessTest(): Sending message via 'outbound rmi gateway': "+runCompletedMessage.toString());
			Message<?> replyMessage = messagingTemplate.sendAndReceive(outboundMessageChannel, runCompletedMessage);
			if (replyMessage != null)
				logger.debug("testJobApproved(): Got reply message: "+ replyMessage.toString());
			Thread.sleep(500); // wait for message receiving and job completion events
			Assert.assertEquals(messages.size(), 2);
			Assert.assertTrue(BatchJobLaunchMessageTemplate.isMessageOfCorrectType(messages.get(0)));
			Assert.assertTrue(BatchJobLaunchMessageTemplate.isMessageOfCorrectType(messages.get(1)));
		} catch (Exception e){
			// caught an unexpected exception
			Assert.fail("Caught Exception: "+e.getMessage());
		}
					
	}

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		logger.debug("Message recieved by handleMessage(): "+message.toString());
		messages.add(message);
	}

}
