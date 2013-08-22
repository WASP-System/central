package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.integration.messages.WaspStatus;

public abstract class WaspMessageHandlingTasklet extends WaspTasklet implements MessageHandler, StepExecutionListener{
	
	protected List<Message<?>> messageQueue;
	
	protected void sendSuccessReplyToAllMessagesInQueue(){
		Message<WaspStatus> replyMessage = MessageBuilder.withPayload(WaspStatus.COMPLETED).build();
		MessagingTemplate messagingTemplate = new MessagingTemplate();
		for (Message<?> message: messageQueue){
			try{
				if ( message.getHeaders().containsKey(MessageHeaders.REPLY_CHANNEL)){
					Object replyChannelObj = message.getHeaders().get(MessageHeaders.REPLY_CHANNEL);
					if (MessageChannel.class.isInstance(replyChannelObj))
						messagingTemplate.send((MessageChannel) replyChannelObj, replyMessage);
					else if (String.class.isInstance(replyChannelObj))
						messagingTemplate.send((String) replyChannelObj, replyMessage);
					else {
						logger.warn("Unable to send reply message to reply message channel of type " + replyChannelObj.getClass().getName() +
								" specified in source message: " + message.toString());
					}
					
				} else {
					logger.warn("Failure to send reply message because no reply channel header specified in source message: " + message.toString());
				}
			} catch (Exception e){
				logger.warn("Failure to send reply message (reason: " + e.getLocalizedMessage() + ") to reply channel specified in source message : " +
						message.toString() + ". Original exception stack: ");
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		// Do Nothing here
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution){
		sendSuccessReplyToAllMessagesInQueue();
		this.messageQueue.clear(); // clean up in case of restart
		logger.debug(name + "AfterStep() going to return ExitStatus of '" + stepExecution.getExitStatus().getExitCode().toString() + "'");
		return stepExecution.getExitStatus();
	}
}
