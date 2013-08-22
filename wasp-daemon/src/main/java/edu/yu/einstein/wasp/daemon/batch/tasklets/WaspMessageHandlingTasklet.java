package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.integration.messages.WaspStatus;

public abstract class WaspMessageHandlingTasklet extends WaspTasklet implements MessageHandler, StepExecutionListener{
	
	protected List<Message<?>> messageQueue;
	
	@Autowired
	@Qualifier("wasp.channel.reply")
	PublishSubscribeChannel replyChannel;
	
	protected void sendSuccessReplyToAllMessagesInQueue(){
		Message<WaspStatus> replyMessage;
		MessagingTemplate messagingTemplate = new MessagingTemplate();
		logger.debug("Going to send " + messageQueue.size()  + " reply message(s)...");
		for (Message<?> message: messageQueue){
			try{
				logger.debug("sending reply to message: " + message.toString());
				if ( message.getHeaders().containsKey(MessageHeaders.REPLY_CHANNEL))
					replyMessage = MessageBuilder.withPayload(WaspStatus.COMPLETED).setReplyChannel((MessageChannel) message.getHeaders().get(MessageHeaders.REPLY_CHANNEL)).build();
				else
					replyMessage = MessageBuilder.withPayload(WaspStatus.COMPLETED).build();
				messagingTemplate.send(replyChannel, replyMessage);
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
