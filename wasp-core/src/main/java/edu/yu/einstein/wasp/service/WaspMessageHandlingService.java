package edu.yu.einstein.wasp.service;

import java.util.Map;

import org.springframework.integration.Message;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;

public interface WaspMessageHandlingService extends WaspService {
	
	public void sendOutboundMessage(final Message<?> message, boolean isReplyExpected);
	
	public void launchBatchJob(String flow, Map<String, String> jobParameters) throws WaspMessageBuildingException;

}
