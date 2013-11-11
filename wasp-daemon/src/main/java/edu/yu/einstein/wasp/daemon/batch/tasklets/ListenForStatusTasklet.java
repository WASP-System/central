package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.transaction.annotation.Transactional;

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
		this.messageTemplates.add(messageTemplate);
	}
	
	public ListenForStatusTasklet(Set<WaspStatusMessageTemplate> messageTemplates) {
		this.messageTemplates.addAll(messageTemplates);
	}
	
	@Override
	@Transactional
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		logger.trace(name + "execute() invoked");
		if (wasWokenOnMessage(context)){
			logger.debug("StepExecution id=" + context.getStepContext().getStepExecution().getId() + 
					" was woken up from hibernation for a message. Skipping to next step...");
			return RepeatStatus.FINISHED;
		}
			
		if (wasHibernationRequested){
			logger.debug("Not going to request hibernation already done: wasHibernationRequested=" + wasHibernationRequested);
		} else {
			logger.debug("Going to request hibernation as not already done: wasHibernationRequested=" + wasHibernationRequested);
			addStatusMessagesToContext(context, messageTemplates);
			requestHibernation(context, messageTemplates);
		}
		return RepeatStatus.CONTINUABLE;
	}
	
	

}
