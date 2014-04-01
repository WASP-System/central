package edu.yu.einstein.wasp.service.impl;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHandlingException;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspTask;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.service.WaspMessageHandlingService;

@Service
@Transactional
public class WaspMessageHandlingServiceImpl extends WaspServiceImpl implements WaspMessageHandlingService {
	
	private int messageTimeoutInMillis;
	
	/**
	 * Set the timeout when waiting for reply (in millis).  Default 5000 (5s).
	 */
	@Value(value="${wasp.message.timeout:5000}")
	public void setMessageTimeoutInMillis(int messageTimeout) {
		this.messageTimeoutInMillis = messageTimeout;
	}
	
	
	private DirectChannel outboundRemotingChannel; // channel to send messages out of system
	
	@Autowired
	@Qualifier(MessageChannelRegistry.OUTBOUND_MESSAGE_CHANNEL)
	public void setOutboundRemotingChannel(DirectChannel outboundRemotingChannel) {
		this.outboundRemotingChannel = outboundRemotingChannel;
	}

	/**
	 * Send an outbound message via Spring Integration
	 * @param message
	 */
	@Override
	public void sendOutboundMessage(final Message<?> message, boolean isReplyExpected){
		logger.debug("Sending message via '" + MessageChannelRegistry.OUTBOUND_MESSAGE_CHANNEL + "': "+message.toString());
		MessagingTemplate messagingTemplate = new MessagingTemplate();
		messagingTemplate.setReceiveTimeout(messageTimeoutInMillis);
		Message<?> replyMessage  = null;
		try{
			if (isReplyExpected){
				replyMessage = messagingTemplate.sendAndReceive(outboundRemotingChannel, message);
				if(replyMessage == null)
					throw new MessageHandlingException(message, "Did not receive a reply on sending outbound message (timeout after " 
							+ messageTimeoutInMillis + "):"+ message.toString());
				logger.debug("Received reply  :"+ replyMessage.toString());
				if (replyMessage.getHeaders().containsKey(WaspTask.EXCEPTION))
					throw new MessageHandlingException(message, "Problem encountered sending message '" + message.toString() + "' : " + replyMessage.getHeaders().get(WaspTask.EXCEPTION));
				if (WaspStatus.class.isInstance(replyMessage.getPayload()) && replyMessage.getPayload().equals(WaspStatus.FAILED))
					throw new MessageHandlingException(message,"Problem encountered sending message'" + message.toString() + "': no exception returned but status was FAILED");
			} else {
				messagingTemplate.send(outboundRemotingChannel, message);
			}
		} catch(Exception e){
			e.printStackTrace();
			throw new MessageHandlingException(message, "Problem encountered sending message '" + message.toString() + ": " + e.getLocalizedMessage());
		}
	}
	
	@Override
	public UUID launchBatchJob(String flow, Map<String,String> jobParameters) throws WaspMessageBuildingException {
		BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate(new BatchJobLaunchContext(flow, jobParameters));
		Message<BatchJobLaunchContext> message = batchJobLaunchMessageTemplate.build();
		sendOutboundMessage(message, true);
		return message.getHeaders().getId();
	}

}
