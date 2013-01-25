package edu.yu.einstein.wasp.daemon.batch.tasklets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;

import edu.yu.einstein.wasp.integration.messages.templates.StatusMessageTemplate;

public class NotifyStatusTasklet extends WaspTasklet {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private StatusMessageTemplate messageTemplate;
	
	private MessageChannel messageChannel;
	
	
	public NotifyStatusTasklet(MessageChannel outputMessageChannel, StatusMessageTemplate messageTemplate) {
		logger.debug("Constructing new instance"); 
		this.messageTemplate = messageTemplate;
		this.messageChannel = outputMessageChannel;
	}
	
	
	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
		logger.debug("execute() invoked");
		Message<?> message = messageTemplate.build();
		logger.debug("sending message: "+message);
		messageChannel.send(message);
		return RepeatStatus.FINISHED;
	}

}
