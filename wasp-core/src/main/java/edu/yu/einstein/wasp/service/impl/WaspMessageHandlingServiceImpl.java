package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHandlingException;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessagingTemplate;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspTask;
import edu.yu.einstein.wasp.integration.messages.payload.WaspStatus;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;

public abstract class WaspMessageHandlingServiceImpl extends WaspServiceImpl{
	
	private int messageTimeoutInMillis = 5000;
	
	/**
	 * Set the timeout when waiting for reply (in millis).  Default 5000 (5s).
	 */
	@Value(value="${wasp.message.timeout:5000}")
	public void setMessageTimeoutInMillis(int messageTimeout) {
		this.messageTimeoutInMillis = messageTimeout;
	}
	
	@Autowired
	@Qualifier(MessageChannelRegistry.OUTBOUND_MESSAGE_CHANNEL)
	private DirectChannel outboundRemotingChannel; // channel to send messages out of system
	
	/**
	 * Send an outbound message via Spring Integration
	 * @param message
	 * @throws WaspMessageBuildingException
	 */
	public void sendOutboundMessage(final Message<?> message) throws WaspMessageBuildingException{
		logger.debug("Sending message via '" + MessageChannelRegistry.OUTBOUND_MESSAGE_CHANNEL + "': "+message.toString());
		MessagingTemplate messagingTemplate = new MessagingTemplate();
		messagingTemplate.setReceiveTimeout(messageTimeoutInMillis);
		Message<?> replyMessage  = null;
		try{
			replyMessage = messagingTemplate.sendAndReceive(outboundRemotingChannel, message);
		} catch(MessageHandlingException e){
			throw new WaspMessageBuildingException("Problem encountered sending message '" + message.toString() + ": " + e.getLocalizedMessage());
		}
		if(replyMessage == null){
			// TODO: send exception
			logger.warn("Did not receive a reply on sending outbound message :"+ message.toString());
			return;
		}
		if (replyMessage.getHeaders().containsKey(WaspTask.EXCEPTION))
			throw new WaspMessageBuildingException("Problem encountered sending message '" + message.toString() + "' : " + replyMessage.getHeaders().get(WaspTask.EXCEPTION));
		if (WaspStatus.class.isInstance(replyMessage.getPayload()) && replyMessage.getPayload().equals(WaspStatus.FAILED))
			throw new WaspMessageBuildingException("Problem encountered sending message'" + message.toString() + "': no exception returned but status was FAILED");
	}	

}
