package edu.yu.einstein.wasp.integration.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.integration.rmi.RmiOutboundGateway;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.integration.messages.WaspMessageType;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.templates.GenericStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;

@ContextConfiguration(locations={"classpath:integration/integration-test-context.xml"})
public class RmiInputTests extends AbstractTestNGSpringContextTests implements MessageHandler{ // AbstractTestNGSpringContextTests extension for @Autowired to work
	
	@Autowired
	MessageChannelRegistry channelRegistry;
	
	@Autowired
	private RmiOutboundGateway outboundGateway;
	
	private final Logger logger = LoggerFactory.getLogger(RmiInputTests.class);
	
	private Message<?> message = null;
	
	private final Integer PU_ID = 1;
	private final Integer RUN_ID = 10;
	private SubscribableChannel listeningChannel;
	
	@BeforeClass
	private void setup(){
		Assert.assertNotNull(channelRegistry);
		Assert.assertNotNull(outboundGateway);
		listeningChannel = channelRegistry.getChannel("wasp.channel.notification.batch", SubscribableChannel.class);
		listeningChannel.subscribe(this); // register as a message handler on the listeningChannel
	}
	
	@AfterClass 
	private void tearDown(){
		listeningChannel.unsubscribe(this);
	}

	
	@Test(groups = "unit-tests-batch-integration")
	public void testSendMessage() {
		try{ 
			// send run started message into outboundRmiChannel
			GenericStatusMessageTemplate messageTemplate = new GenericStatusMessageTemplate();
			messageTemplate.setStatus(WaspStatus.STARTED);
			Message<WaspStatus> messageToSend =  messageTemplate.build();
			logger.info("Sending message via 'outbound rmi gateway': "+messageToSend.toString());
			Message<?> replyMessage = (Message<?>) outboundGateway.handleRequestMessage(messageToSend);
			if (replyMessage == null)
				Assert.fail("Did not get expected reply message");
			Assert.assertEquals((WaspStatus) replyMessage.getPayload(), WaspStatus.COMPLETED);
			
			// Delay to allow message receiving and transitions. Time out after 20s.
			int repeat = 0;
			while (message == null && repeat < 40){
				Thread.sleep(500);
				repeat++;
			}
			if (outboundGateway == null)
				Assert.fail("Timeout waiting to receive message on 'wasp.channel.notification.run'");
			
			// verify message headers
			Assert.assertTrue(message.getHeaders().containsKey(WaspMessageType.HEADER_KEY));
			Assert.assertEquals(message.getHeaders().get(WaspMessageType.HEADER_KEY), WaspMessageType.GENERIC);
			Assert.assertFalse(message.getHeaders().containsKey("unknown-target"));
			
			// check payload as expected (don't bother checking headers this time around)
			Assert.assertEquals(message.getPayload(), WaspStatus.STARTED);
			
		} catch (Exception e){
			// caught an unexpected exception
			Assert.fail("Caught Exception: "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Test(groups = "unit-tests-integration", dependsOnMethods = "testSendMessage")
	public void testUnknownTarget() throws Exception{
		try{ 
			// send run started message into outboundRmiChannel
			Message<WaspStatus> messageToSend = MessageBuilder.withPayload(WaspStatus.STARTED)
					.setHeader(WaspMessageType.HEADER_KEY, WaspMessageType.RUN)
					.setHeader("target", "foo") //unknown target foo
					.setHeader("runId", RUN_ID)
					.setHeader("platformUnitId", PU_ID)
					.setPriority(WaspStatus.STARTED.getPriority())
					.build();
			logger.info("Sending message via 'wasp.channel.remoting.outbound': "+messageToSend.toString());
			logger.info("Sending message via 'outbound rmi gateway': "+messageToSend.toString());
			Message<?> replyMessage = (Message<?>) outboundGateway.handleRequestMessage(messageToSend);
			if (replyMessage == null)
				Assert.fail("Message recieve timed out with no message returned");
			Assert.assertTrue(replyMessage.getHeaders().containsKey("unknown-target"));
			Assert.assertEquals(replyMessage.getHeaders().get("unknown-target"), "true");
			
			// check payload as expected (don't bother checking headers this time around)
			Assert.assertEquals(replyMessage.getPayload(), WaspStatus.STARTED);
			
		} catch (Exception e){
			// caught an unexpected exception
			Assert.fail("Caught Exception: "+e.getMessage());
		}
	}
	
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		logger.info("Message recieved by handleMessage(): "+message.toString());
		this.message = message; 
	}

	
	
}
