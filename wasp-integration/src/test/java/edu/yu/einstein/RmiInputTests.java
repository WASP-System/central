package edu.yu.einstein;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.messages.WaspMessageType;
import edu.yu.einstein.wasp.messages.WaspRunStatusMessage;
import edu.yu.einstein.wasp.messages.WaspStatus;

@ContextConfiguration(locations={"classpath:test-launch-context.xml", "classpath:RmiMessageSend-context.xml"})

public class RmiInputTests extends AbstractTestNGSpringContextTests implements MessageHandler{
	
	@Autowired
	ApplicationContext context;
	
	private final Logger logger = Logger.getLogger(RunFlowTests.class);
	
	private Message<?> message = null;
	
	private final Integer PU_ID = 1;
	private final String PU_KEY = "platformUnitId";
	private final Integer RUN_ID = 10;
	private final String RUN_KEY = "runId";
	
	@Test (groups = "unit-tests")
	public void testAutowiringOk() {
		Assert.assertNotNull(context);
	}
	
	@Test(groups = "unit-tests", dependsOnMethods = "testAutowiringOk")
	public void testSendMessage() throws Exception{
		
		//try{ 
			// listen in on the waspRunPublishSubscribeChannel for messages
			SubscribableChannel waspRunPublishSubscribeChannel = context.getBean("wasp.channel.notification.run", SubscribableChannel.class);
			waspRunPublishSubscribeChannel.subscribe(this); // register as a message handler on the waspRunPublishSubscribeChannel
			
			DirectChannel outboundRmiChannel = context.getBean("wasp.channel.rmi.outbound", DirectChannel.class);
			DirectChannel replyChannel = context.getBean("wasp.channel.rmi.internal.reply", DirectChannel.class);
			replyChannel.subscribe(this);
			
			// send run started message into outboundRmiChannel
			message =  WaspRunStatusMessage.build(RUN_ID, PU_ID, WaspStatus.STARTED);
			logger.debug("Sending message via 'wasp.channel.rmi.outbound': "+message.toString());
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
			Assert.assertTrue(message.getHeaders().containsKey(WaspMessageType.MESSAGE_TYPE_FIELD));
			Assert.assertEquals(message.getHeaders().get(WaspMessageType.MESSAGE_TYPE_FIELD), WaspMessageType.RUN);
			Assert.assertTrue(message.getHeaders().containsKey(RUN_KEY));
			Assert.assertEquals(message.getHeaders().get(RUN_KEY), RUN_ID);
			
			// check payload as expected (don't bother checking headers this time around)
			Assert.assertEquals(message.getPayload(), WaspStatus.STARTED);
			
		//} catch (Exception e){
		//	// caught an unexpected exception
		//	Assert.fail("Caught Exception: "+e.getMessage());
		//}
	}
	
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		logger.debug("Message recieved by handleMessage(): "+message.toString());
		this.message = message; 
	}

	
	
}
