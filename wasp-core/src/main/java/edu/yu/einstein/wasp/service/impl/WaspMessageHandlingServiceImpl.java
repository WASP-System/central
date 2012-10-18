package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.channel.DirectChannel;

import edu.yu.einstein.wasp.exception.WaspMessageChannelNotFoundException;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.service.WaspMessageHandlingService;

public abstract class WaspMessageHandlingServiceImpl extends WaspServiceImpl implements	WaspMessageHandlingService {
	
	public static final String OUTBOUND_MSG_CHANNEL_NAME = "wasp.channel.rmi.outbound";
	
	public static final String REPLY_MSG_CHANNEL_NAME = "wasp.channel.rmi.outbound.reply";
	
	@Autowired
	protected MessageChannelRegistry channelRegistry;
	
	protected DirectChannel replyChannel; // when sending messages we should check for a response and handle it
	
	protected DirectChannel outboundRmiChannel; // channel to send messages out of system
	
	/**
	 * Sets up message channels. You MUST override this in each subclass like so:
	 *     {@literal @}PostConstruct 
	 *     {@literal @}Override
	 *     protected void initialize(){ super.initialize(); }
	 */
	protected void initialize(){
		// subscribe to outbound message channel after dependency injection
		outboundRmiChannel = channelRegistry.getChannel(OUTBOUND_MSG_CHANNEL_NAME, DirectChannel.class);
		if (outboundRmiChannel == null)
			throw new WaspMessageChannelNotFoundException("Cannot obtain a message channel called '" + OUTBOUND_MSG_CHANNEL_NAME + "'. No such channel in registry");
		outboundRmiChannel.subscribe(this);
		
		// subscribe to reply channel after dependency injection
		replyChannel = channelRegistry.getChannel(REPLY_MSG_CHANNEL_NAME, DirectChannel.class);
		if (replyChannel == null)
			throw new WaspMessageChannelNotFoundException("Cannot obtain a message channel called '" + REPLY_MSG_CHANNEL_NAME + "'. No such channel in registry");
		replyChannel.subscribe(this);
	}

	/**
	 * Handle any messages from subscribed message channels.
	 */
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		// reply messages usually mean rejection by the consumer. We should log a warning.
		logger.warn("Received unexpected reply message: " + message.toString());
	}
	
	

}
