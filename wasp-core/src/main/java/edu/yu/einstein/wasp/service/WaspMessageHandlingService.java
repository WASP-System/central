package edu.yu.einstein.wasp.service;

import org.springframework.integration.Message;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;

public interface WaspMessageHandlingService extends WaspService {
	
	public void sendOutboundMessage(final Message<?> message) throws WaspMessageBuildingException;

}
