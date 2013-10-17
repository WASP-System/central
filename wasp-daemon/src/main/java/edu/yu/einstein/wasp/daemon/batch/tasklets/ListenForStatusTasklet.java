package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.integration.Message;
import org.springframework.integration.core.MessagingTemplate;

import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;
import edu.yu.einstein.wasp.integration.messages.templates.HibernationMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.HibernationMessageTemplate.HibernationType;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;

/**
 * Listens on the provided subscribable channel for a message with a task and status specified in the
 * provided message template.
 * @author asmclellan
 */
public class ListenForStatusTasklet extends WaspHibernatingTasklet  {
	
	private static final Logger logger = LoggerFactory.getLogger(ListenForStatusTasklet.class);
	
	
	
	private Set<WaspStatusMessageTemplate> messageTemplates = new HashSet<>();
	
	
	public ListenForStatusTasklet() {
		// proxy
	}
	
	public ListenForStatusTasklet(WaspStatusMessageTemplate messageTemplate) {
		this.messageTemplates.add(messageTemplate);
	}
	
	public ListenForStatusTasklet(Set<WaspStatusMessageTemplate> messageTemplates) {
		this.messageTemplates.addAll(messageTemplates);
	}
	
	@Override
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		logger.trace(name + "execute() invoked");
		if (wasHibernating(context)){
			if (wasWokenOnMessage(context)){
				logger.debug("StepExecution id=" + context.getStepContext().getStepExecution().getId() + 
						" was woken up from hibernation for a message. Skipping to next step...");
				setWasHibernatingFlag(context, false);
				return RepeatStatus.FINISHED;
			}
			// If we get here, this step is one part of a split step that was not woken
			return RepeatStatus.CONTINUABLE; 
		}
		if (!wasHibernationSuccessfullyRequested){
			addStatusMessagesToContext(context, messageTemplates);
			requestHibernation(context, messageTemplates);
		}
		return RepeatStatus.CONTINUABLE;
	}
	
	@Override
	protected void requestHibernation(ChunkContext context, Object trigger){
		Collection<WaspStatusMessageTemplate> messageTemplates = (Collection<WaspStatusMessageTemplate>) trigger;
		StepContext stepContext = context.getStepContext();
		Long jobExecutionId = stepContext.getStepExecution().getJobExecutionId();
		logger.info("Going to hibernate job " + stepContext.getJobName() + 
				" (execution id=" + stepContext.getStepExecution().getJobExecutionId() + ") from step " + 
				stepContext.getStepName() + " (step id=" + stepContext.getStepExecution().getId() + ")");
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
	
	private boolean wasWokenOnMessage(ChunkContext context){
		ExecutionContext executionContext = context.getStepContext().getStepExecution().getExecutionContext();
		if (!executionContext.containsKey(BatchJobHibernationManager.WOKEN_ON_MESSAGE_KEY))
			return false;
		boolean waspWoken = (boolean) executionContext.get(BatchJobHibernationManager.WOKEN_ON_MESSAGE_KEY);
		logger.debug("StepExecutionId=" + context.getStepContext().getStepExecution().getId() + " wasWokenByMessage=" + waspWoken);
		return waspWoken;	
	}
	
	private void addStatusMessagesToContext(ChunkContext context, Set<WaspStatusMessageTemplate> templates) throws JSONException{
		BatchJobHibernationManager.setWakeMessages(context.getStepContext().getStepExecution(), templates);
	}

}
