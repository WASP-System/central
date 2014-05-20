package edu.yu.einstein.wasp.service;

import java.util.Map;
import java.util.UUID;

import org.springframework.messaging.Message;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;

public interface WaspMessageHandlingService extends WaspService {
	
	public void sendOutboundMessage(final Message<?> message, boolean isReplyExpected);
	
	public UUID launchBatchJob(String flow, Map<String, String> jobParameters) throws WaspMessageBuildingException;

}
