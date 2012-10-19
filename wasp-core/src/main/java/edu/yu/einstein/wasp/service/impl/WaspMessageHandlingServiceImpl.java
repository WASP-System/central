package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.channel.DirectChannel;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.exception.WaspMessageChannelNotFoundException;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.service.WaspMessageHandlingService;

public abstract class WaspMessageHandlingServiceImpl extends WaspServiceImpl implements	WaspMessageHandlingService {
	
	public static final String OUTBOUND_MSG_CHANNEL_NAME = "wasp.channel.rmi.outbound";
	
	public static final String REPLY_MSG_CHANNEL_NAME = "wasp.channel.rmi.outbound.reply";
	
	public static final Long MESSAGE_SEND_TIMEOUT = new Long(5000); // 5s
	
	@Autowired
	private MessageChannelRegistry channelRegistry;
	
	private DirectChannel replyChannel; // when sending messages we should check for a response and handle it
	
	private DirectChannel outboundRmiChannel; // channel to send messages out of system
	
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
	 * Send an outbound message via Spring Integration
	 * @param message
	 * @throws WaspMessageBuildingException
	 */
	public void sendOutboundMessage(final Message<?> message) throws WaspMessageBuildingException{
		logger.debug("Sending message via '" + OUTBOUND_MSG_CHANNEL_NAME + "': "+message.toString());
		if (! outboundRmiChannel.send(message, MESSAGE_SEND_TIMEOUT) )
			throw new WaspMessageBuildingException("Failed to send message " + message.toString());
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
