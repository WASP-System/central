package edu.yu.einstein.wasp.service;

import org.springframework.integration.Message;

public interface WaspMessageHandlingService extends WaspService {
	
	public void sendOutboundMessage(final Message<?> message, boolean isReplyExpected);

}
