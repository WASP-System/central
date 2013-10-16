package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.integration.core.SubscribableChannel;

import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;

/**
 * Listens on the provided subscribable channel for a message with a task and status specified in the
 * provided message template.
 * @author asmclellan
 */
public class ListenForStatusTasklet extends WaspMessageHandlingTasklet  {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	

	
	// TODO:: merge the following two attributes  
	private WaspStatusMessageTemplate messageTemplate;
	
	private Set<WaspStatusMessageTemplate> abortMonitoredTemplates;
	
	
	public ListenForStatusTasklet() {
		// proxy
	}
	
	// TODO:: remove this
	public void setAdditionalAbortMonitoredTemplates(Set<WaspStatusMessageTemplate> additionalAbortMessageMonitoredTemplates){
		this.abortMonitoredTemplates.addAll(additionalAbortMessageMonitoredTemplates);
	}
	
	// TODO:: remove SubscribableChannel inputSubscribableChannel, SubscribableChannel abortMonitoringChannel
	public ListenForStatusTasklet(SubscribableChannel inputSubscribableChannel, SubscribableChannel abortMonitoringChannel, WaspStatusMessageTemplate messageTemplate) {
		this.messageTemplate = messageTemplate;
		this.messageQueue = new HashSet<>();
		this.abortMonitoredTemplates = new HashSet<WaspStatusMessageTemplate>();
		this.abortMonitoredTemplates.add(messageTemplate);
	}
	
	@Override
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		logger.trace(name + "execute() invoked");
		if (wasHibernating(context)){
			if (wasWokenOnMessage(context)){
				logger.debug("StepExecution id=" + context.getStepContext().getStepExecution().getId() + " was woken up from hibernation for a message. Skipping to next step...");
				setWasHibernatingFlag(context, false);
				return RepeatStatus.FINISHED;
			}
			// If we get here, this step is one part of a split step that was not woken
			return RepeatStatus.CONTINUABLE; 
		}
		if (!wasHibernationSuccessfullyRequested){
			Set<WaspStatusMessageTemplate> messages = new HashSet<>();
			messages.addAll(abortMonitoredTemplates);
			messages.add(messageTemplate);
			requestHibernation(context, messages);
		}
		return RepeatStatus.CONTINUABLE;
	}
	
/*
	@SuppressWarnings("unchecked") 
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		logger.debug(name + "handleMessage() invoked. Received message: " + message.toString());
		if (! WaspStatus.class.isInstance(message.getPayload()))
			return;
		WaspStatus statusFromMessage = (WaspStatus) message.getPayload();
		
		// first check if any abort / failure messages have been delivered from a monitored message template
		if (statusFromMessage.isUnsuccessful()){
			for (StatusMessageTemplate messageTemplate: abortMonitoredTemplates){
				if (messageTemplate.actUponMessage(message)){
					this.messageQueue.add(message);
					logger.debug(name + "handleMessage() found ABANDONED or FAILED message for abort-monitored template " + 
							messageTemplate.getClass().getName() + ". Going to fail step.");
					abandonStep = true;
					return; // we have found a valid abort message so return
				}
			}
		}
		
		// then check the message and it's status against the status we are interested in for a reportable match
		if (messageTemplate.actUponMessage(message) && statusFromMessage.equals(messageTemplate.getStatus()) ){
			this.messageQueue.add(message);
			logger.debug(name + "handleMessage() adding found message to be compatible so adding to queue: " + message.toString());
			if (statusFromMessage.isUnsuccessful()){
				logger.debug(name + "handleMessage() found ABANDONED or FAILED message to act upon for expected task. Going to fail step.");
				abandonStep = true;
			}
		}
	}
	
	*/

}
