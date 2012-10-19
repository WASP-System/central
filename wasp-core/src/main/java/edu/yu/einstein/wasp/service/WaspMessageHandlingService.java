package edu.yu.einstein.wasp.service;

import org.springframework.integration.Message;
import org.springframework.integration.core.MessageHandler;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;

public interface WaspMessageHandlingService extends WaspService, MessageHandler {
	
	public void sendOutboundMessage(final Message<?> message) throws WaspMessageBuildingException;

}
