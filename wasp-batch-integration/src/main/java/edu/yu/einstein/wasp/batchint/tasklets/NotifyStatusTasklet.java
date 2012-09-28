package edu.yu.einstein.wasp.batchint.tasklets;

import org.apache.log4j.Logger;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;

import edu.yu.einstein.wasp.integration.messages.StatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;

public class NotifyStatusTasklet extends WaspTasklet implements Tasklet {
	
	private final Logger logger = Logger.getLogger(NotifyStatusTasklet.class);

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
		Message<WaspStatus> message = messageTemplate.build();
		logger.debug("sending message: "+message);
		messageChannel.send(message);
		return RepeatStatus.FINISHED;
	}

}
