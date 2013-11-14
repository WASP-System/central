package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionFixed;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;

/**
 * Listens on the provided subscribable channel for a message with a task and status specified in the
 * provided message template.
 * @author asmclellan
 */
public class ListenForStatusTasklet extends WaspTasklet  {
	
	private static final Logger logger = LoggerFactory.getLogger(ListenForStatusTasklet.class);
	
	private Set<WaspStatusMessageTemplate> messageTemplates = new HashSet<>();
	
	public ListenForStatusTasklet() {
		// proxy
	}
	
	public ListenForStatusTasklet(WaspStatusMessageTemplate messageTemplate) {
		setMessageToListenFor(messageTemplate);
	}
	
	public ListenForStatusTasklet(Set<WaspStatusMessageTemplate> messageTemplates) {
		setMessagesToListenFor(messageTemplates);
	}
	
	public void setMessageToListenFor(WaspStatusMessageTemplate messageTemplate) {
		Set<WaspStatusMessageTemplate> templates = new HashSet<>();
		templates.add(messageTemplate);
		setMessagesToListenFor(templates);
	}
	
	public void setMessagesToListenFor(Set<WaspStatusMessageTemplate> messageTemplates) {
		this.messageTemplates.clear();
		addAbandonedAndFailureMessageTemplates(messageTemplates);
		this.messageTemplates.addAll(messageTemplates);
	}
	
	@Override
	@Transactional
	@RetryOnExceptionFixed
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		logger.trace(name + "execute() invoked");
		if (wasWokenOnMessage(context)){
			logger.debug("StepExecution id=" + context.getStepContext().getStepExecution().getId() + 
					" was woken up from hibernation for a message. Skipping to next step...");
			return RepeatStatus.FINISHED;
		}
		
		if (!wasHibernationRequested){
			logger.debug("Going to request hibernation from StepExecution (id=" + context.getStepContext().getStepExecution().getId() + 
					") as not previously requested");
			addStatusMessagesToWakeStepToContext(context, messageTemplates);
			addStatusMessagesToAbandonStepToContext(context, abandonTemplates);
		} else
			logger.debug("Previous hibernation request made by this StepExecution (id=" + context.getStepContext().getStepExecution().getId() + 
					") but we were still waiting for all steps to be ready. Going to retry request.");
		requestHibernation(context);
		logger.debug("Hibernate request made by this StepExecution (id=" + context.getStepContext().getStepExecution().getId() + 
					") but JobExecution is not yet ready to hibernate");
		return RepeatStatus.CONTINUABLE;	
	}
	
	/**
	 * If waiting for a message with a CREATED / ACCEPTED status etc, we may also wish to wake in case of receiving a status of ABANDONED or FAILED
	 * @param messageTemplates
	 */
	private void addAbandonedAndFailureMessageTemplates(Set<WaspStatusMessageTemplate> messageTemplates){
		Set<WaspStatusMessageTemplate> newTemplates = new HashSet<>();
		for (WaspStatusMessageTemplate t : messageTemplates){
			if (!t.getStatus().equals(WaspStatus.ABANDONED)){
				WaspStatusMessageTemplate newTemplate = t.getNewInstance(t);
				newTemplate.setStatus(WaspStatus.ABANDONED);
				newTemplates.add(newTemplate);
			}
			if (!t.getStatus().equals(WaspStatus.FAILED)){
				WaspStatusMessageTemplate newTemplate = t.getNewInstance(t);
				newTemplate.setStatus(WaspStatus.FAILED);
				newTemplates.add(newTemplate);
			}
		}
		messageTemplates.addAll(newTemplates);
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		ExitStatus exitStatus = stepExecution.getExitStatus();
		if (getWokenOnMessageStatus(stepExecution).isUnsuccessful())
			exitStatus = ExitStatus.FAILED;
		logger.debug("Going to exit step with ExitStatus=" + exitStatus);
		return exitStatus;
	}


}
