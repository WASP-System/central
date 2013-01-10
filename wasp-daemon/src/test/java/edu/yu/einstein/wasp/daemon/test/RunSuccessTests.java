package edu.yu.einstein.wasp.daemon.test;

import java.util.ArrayList;
import java.util.List;

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
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.integration.messages.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.RunStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;

@ContextConfiguration(locations={"/daemon-test-launch-context.xml","/daemon-test-wiretap.xml"})

public class RunSuccessTests extends AbstractTestNGSpringContextTests implements MessageHandler {
	
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
	
	@BeforeMethod
	private void setup(){
		listeningChannel.subscribe(this); // register as a message handler on the listeningChannel
		abortChannel.subscribe(this); // register as a message handler on the listeningChannel
		messagingTemplate = new MessagingTemplate();
		messagingTemplate.setReceiveTimeout(2000);
		messages = new ArrayList<Message<?>>();
	}
	
	@AfterMethod
	private void tearDown(){
		listeningChannel.unsubscribe(this);
		abortChannel.unsubscribe(this);
	}
	
	@Test (groups = "unit-tests-batch-integration")
	public void runSuccessTest() throws Exception{
		// send run complete messages
		try {
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
