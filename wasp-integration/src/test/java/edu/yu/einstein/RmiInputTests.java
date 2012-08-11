package edu.yu.einstein;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.messages.WaspMessageType;
import edu.yu.einstein.wasp.messages.WaspRunStatusMessageTemplate;
import edu.yu.einstein.wasp.messages.WaspStatus;
import edu.yu.einstein.wasp.messaging.MessageChannelRegistry;

@ContextConfiguration(locations={"classpath:test-launch-context.xml", "classpath:RmiMessageSend-context.xml"})
public class RmiInputTests extends AbstractTestNGSpringContextTests implements MessageHandler{ // AbstractTestNGSpringContextTests extension for @Autowired to work
	
	@Autowired
	MessageChannelRegistry channelRegistry;
	
	private final Logger logger = Logger.getLogger(JobApprovalFlowTests.class);
	
	private Message<?> message = null;
	
	private final Integer PU_ID = 1;
	private final String PU_KEY = "platformUnitId";
	private final Integer RUN_ID = 10;
	private final String RUN_KEY = "runId";
	private DirectChannel outboundRmiChannel;
	private DirectChannel replyChannel;
	private SubscribableChannel waspRunPublishSubscribeChannel;
	
	@BeforeClass
	private void setup(){
		Assert.assertNotNull(channelRegistry);
		waspRunPublishSubscribeChannel = channelRegistry.getChannel("wasp.channel.notification.run", SubscribableChannel.class);
		waspRunPublishSubscribeChannel.subscribe(this); // register as a message handler on the waspRunPublishSubscribeChannel
		outboundRmiChannel = channelRegistry.getChannel("wasp.channel.rmi.outbound", DirectChannel.class);
		replyChannel = channelRegistry.getChannel("wasp.channel.rmi.outbound.reply", DirectChannel.class);
		replyChannel.subscribe(this);
	}
	
	@AfterClass 
	private void tearDown(){
		waspRunPublishSubscribeChannel.unsubscribe(this);
		replyChannel.unsubscribe(this);
	}

	
	@Test(groups = "unit-tests")
	public void testSendMessage() {
		try{ 
			// send run started message into outboundRmiChannel
			message =  WaspRunStatusMessageTemplate.build(RUN_ID, PU_ID, WaspStatus.STARTED);
			logger.info("Sending message via 'wasp.channel.rmi.outbound': "+message.toString());
			outboundRmiChannel.send(message);
			
			
			// Delay to allow message receiving and transitions. Time out after 20s.
			int repeat = 0;
			while (message == null && repeat < 40){
				Thread.sleep(500);
				repeat++;
			}
			if (message == null)
				Assert.fail("Timeout waiting to receive message on 'wasp.channel.notification.run'");
			
			// verify message headers
			Assert.assertTrue(message.getHeaders().containsKey(PU_KEY));
			Assert.assertEquals(message.getHeaders().get(PU_KEY), PU_ID);
			Assert.assertTrue(message.getHeaders().containsKey(WaspMessageType.HEADER));
			Assert.assertEquals(message.getHeaders().get(WaspMessageType.HEADER), WaspMessageType.RUN);
			Assert.assertTrue(message.getHeaders().containsKey(RUN_KEY));
			Assert.assertEquals(message.getHeaders().get(RUN_KEY), RUN_ID);
			Assert.assertFalse(message.getHeaders().containsKey("unknown-target"));
			
			// check payload as expected (don't bother checking headers this time around)
			Assert.assertEquals(message.getPayload(), WaspStatus.STARTED);
			
		} catch (Exception e){
			// caught an unexpected exception
			Assert.fail("Caught Exception: "+e.getMessage());
		}
	}
	
	@Test(groups = "unit-tests", dependsOnMethods = "testSendMessage")
	public void testUnknownTarget() throws Exception{
		try{ 
			// send run started message into outboundRmiChannel
			message = MessageBuilder.withPayload(WaspStatus.STARTED)
					.setHeader(WaspMessageType.HEADER, WaspMessageType.RUN)
					.setHeader("target", "foo") //unknown target foo
					.setHeader("runId", RUN_ID)
					.setHeader("platformUnitId", PU_ID)
					.setPriority(WaspStatus.STARTED.getPriority())
					.build();
			logger.info("Sending message via 'wasp.channel.rmi.outbound': "+message.toString());
			outboundRmiChannel.send(message);
			
			
			// Delay to allow message receiving and transitions. Time out after 20s.
			int repeat = 0;
			while (message == null && repeat < 40){
				Thread.sleep(500);
				repeat++;
			}
			if (message == null)
				Assert.fail("Timeout waiting to receive message on 'wasp.channel.rmi.outbound.reply'");
			
			// verify message headers
			Assert.assertTrue(message.getHeaders().containsKey("unknown-target"));
			Assert.assertEquals(message.getHeaders().get("unknown-target"), "true");
			
			// check payload as expected (don't bother checking headers this time around)
			Assert.assertEquals(message.getPayload(), WaspStatus.STARTED);
			
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
