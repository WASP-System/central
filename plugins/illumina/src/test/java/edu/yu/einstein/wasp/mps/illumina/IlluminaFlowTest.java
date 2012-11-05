package edu.yu.einstein.wasp.mps.illumina;

import mockit.Expectations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.impl.SampleServiceImpl;

//"/illumina-test-application-context.xml", "classpath*:message-routing-internal.xml"
//"classpath*:/RmiMessageSend-context.xml", "/illumina-test-application-context.xml" }

@ContextConfiguration(locations = { "classpath*:/daemon-test-launch-context.xml", "classpath*:/RmiMessageSend-context.xml",
		"classpath*:/batch-integration-common-config.xml"})
public class IlluminaFlowTest extends AbstractTestNGSpringContextTests
		implements MessageHandler {

	// Autowired
	private JobLauncher jobLauncher;

	// Autowired
	private MessageChannelRegistry channelRegistry;

	// Autowired
	private JobRegistry jobRegistry;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private Message<?> message = null;

	private DirectChannel outboundRmiChannel;
	private DirectChannel replyChannel;
	private SubscribableChannel listeningChannel;
	private SubscribableChannel waspAbortChannel;

	@BeforeClass
	public void setUp() {
//		Assert.assertNotNull(channelRegistry);
//		Assert.assertNotNull(jobLauncher);
//		Assert.assertNotNull(jobRegistry);
//		outboundRmiChannel = channelRegistry.getChannel("wasp.channel.rmi.outbound", DirectChannel.class);
//		replyChannel = channelRegistry.getChannel("wasp.channel.rmi.outbound.reply", DirectChannel.class);
//		replyChannel.subscribe(this);
//		waspAbortChannel = channelRegistry.getChannel("wasp.channel.notification.abort", SubscribableChannel.class);
//		for (String x : channelRegistry.getNames()) {
//			System.out.println("thisisatest: " + x);
//		}
		//waspAbortChannel.subscribe(this);

	}

	@AfterClass
	public void tearDown() {
		listeningChannel.unsubscribe(this);
		replyChannel.unsubscribe(this);

	}

	// Test(groups = "unit-tests-batch-integration")
	public void runIlluminaFlow() throws Exception {
		
//		SampleService ss = new SampleServiceImpl();
//		
//		new Expectations() {
//			Sample s;
//			SampleDao sd;
//			
//			{
//				s.getName(); result = "moo";
//				sd.getSampleBySampleId(1); result = s;
//			}
//			
//		};
		
		//System.out.println(ss.getSampleById(1).getName());
		
//		Job job = jobRegistry.getJob("illumina.mainFlow.v1"); 
//		
//		Map<String, JobParameter> parameterMap = new HashMap<String, JobParameter>();
//		parameterMap.put(WaspPlatformUnitTask.RUN_ID_KEY, new JobParameter(run.getRunId().toString()));
//		JobExecution jobExecution = jobLauncher.run(job, new JobParameters(
//				parameterMap));
//		Thread.sleep(5000);
	}

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		logger.debug("Message recieved by handleMessage(): "
				+ message.toString());
		this.message = message;
	}
}
