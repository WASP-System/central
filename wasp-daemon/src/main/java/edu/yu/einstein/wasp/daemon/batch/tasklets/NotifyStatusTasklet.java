package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionFixed;
import edu.yu.einstein.wasp.integration.messages.templates.StatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.WaspMessageTemplate;

public class NotifyStatusTasklet extends AbandonMessageHandlingTasklet {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private StatusMessageTemplate messageTemplate;
	
	private MessageChannel messageChannel;
	
	public NotifyStatusTasklet() {
		// proxy
	}
	
	
	public NotifyStatusTasklet(MessageChannel outputMessageChannel, StatusMessageTemplate messageTemplate) {
		logger.debug("Constructing new instance"); 
		this.messageTemplate = messageTemplate;
		this.messageChannel = outputMessageChannel;
	}
	
	
	@Override
	@RetryOnExceptionFixed
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
		logger.debug("execute() invoked");
		
		// if this tasklet is the messaging step of one of many flows, pass the relevant information
		// in the message headers.
		Map<String, Object> jp = chunkContext.getStepContext().getJobParameters(); 
		if (jp.containsKey(WaspMessageTemplate.PARENT_ID)) {
		    logger.debug("decorating message with information about parent launching tasklet: " + jp.get(WaspMessageTemplate.PARENT_ID));
		    messageTemplate.getHeaders().put(WaspMessageTemplate.PARENT_ID, jp.get(WaspMessageTemplate.PARENT_ID));
		    messageTemplate.getHeaders().put(WaspMessageTemplate.CHILD_MESSAGE_ID, jp.get(WaspMessageTemplate.CHILD_MESSAGE_ID));
		}
		
		Message<?> message = messageTemplate.build();
		logger.debug("sending message: "+message);
		messageChannel.send(message);
		return RepeatStatus.FINISHED;
	}

}
