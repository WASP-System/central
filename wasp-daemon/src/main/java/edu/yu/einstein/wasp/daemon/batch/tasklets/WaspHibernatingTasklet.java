package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.Collection;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.core.MessagingTemplate;

import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;
import edu.yu.einstein.wasp.integration.messages.templates.HibernationMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.HibernationMessageTemplate.HibernationType;

public abstract class WaspHibernatingTasklet extends WaspTasklet implements StepExecutionListener{
	
	protected boolean wasHibernationSuccessfullyRequested = false;
	
	@Autowired
	@Qualifier("wasp.channel.priority.default")
	MessageChannel sendChannel;
	
	@Autowired
	private BatchJobHibernationManager hibernationManager;
	
	protected void requestHibernation(ChunkContext context, Object trigger){
		Collection<WaspStatusMessageTemplate> messageTemplates = (Collection<WaspStatusMessageTemplate>) trigger;
		StepContext stepContext = context.getStepContext();
		Long jobExecutionId = stepContext.getStepExecution().getJobExecutionId();
		logger.info("Going to hibernate job " + stepContext.getJobName() + 
				" (execution id=" + stepContext.getStepExecution().getJobExecutionId() + ") from step " + 
				stepContext.getStepName() + " (step id=" + stepContext.getStepExecution().getId() + ")");
		if (wasHibernationRequestedAlready(context)){
			logger.debug("Not going to request hibernation as hibernation request already exists. Registering messages with HibernationManager");
			hibernationManager.addMessageTemplatesForJobStep(stepContext.getStepExecution().getJobExecutionId(), stepContext.getStepExecution().getId());
			return;
		}
		setHibernateRequestedFlag(context);
		HibernationMessageTemplate messageTemplate = new HibernationMessageTemplate(stepContext.getStepExecution(), HibernationType.STOP_AND_AWAKE_ON_MESSAGE);
		Message<HibernationType> message = null;
		try {
			message = messageTemplate.build();
			logger.debug("sending message: " + message);
			MessagingTemplate messagingTemplate = new MessagingTemplate();
			messagingTemplate.send(sendChannel, message);
			wasHibernationSuccessfullyRequested = true;
		} catch (Exception e) {
			logger.warn("Unable to hibernate batch JobExecution id= " + jobExecutionId + ". Failure to send reply message (reason: " + 
					e.getLocalizedMessage() + ") to reply channel specified in source message : " + message.toString() + ". Original exception stack: ");
			e.printStackTrace();
		}
	}
	
	protected boolean wasHibernating(ChunkContext context){
		ExecutionContext executionContext = context.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
		if (!executionContext.containsKey(BatchJobHibernationManager.HIBERNATING))
			return false;
		boolean isHibernating = (boolean) executionContext.get(BatchJobHibernationManager.HIBERNATING);
		logger.debug("StepExecutionId=" + context.getStepContext().getStepExecution().getId() + " isHibernating=" + isHibernating);
		return isHibernating;	
	}
	
	protected boolean wasHibernationRequestedAlready(ChunkContext context){
		ExecutionContext executionContext = context.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
		if (!executionContext.containsKey(BatchJobHibernationManager.HIBERNATION_REQUESTED))
			return false;
		boolean isHibernatingRequested = (boolean) executionContext.get(BatchJobHibernationManager.HIBERNATION_REQUESTED);
		logger.debug("StepExecutionId=" + context.getStepContext().getStepExecution().getId() + " hibernation requested already=" + isHibernatingRequested);
		return isHibernatingRequested;	
	}
	
	
	protected void setWasHibernatingFlag(ChunkContext context, boolean value){
		ExecutionContext executionContext = context.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
		executionContext.put(BatchJobHibernationManager.HIBERNATING, value);
	}
	
	private void setHibernateRequestedFlag(ChunkContext context){
		ExecutionContext executionContext = context.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
		executionContext.put(BatchJobHibernationManager.HIBERNATION_REQUESTED, true);
	}
	
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		// Do Nothing here
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution){
		return stepExecution.getExitStatus();
	}
}
