package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.MessagingException;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.templates.StatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;

/**
 * Listens on the provided subscribable channel for a message with a task and status specified in the
 * provided message template. Will try and hibernate, but will keep responding to messages whilst waiting.
 * @author asmclellan
 */
@Transactional
public class ListenForStatusTasklet extends WaspTasklet implements MessageHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(ListenForStatusTasklet.class);
	
	private Set<WaspStatusMessageTemplate> messageTemplates = new HashSet<>();
	
	private List<Message<?>> messageQueue = new ArrayList<>();
	
	private boolean abandonStep = false;
	
	@Autowired
	@Qualifier("wasp.channel.reply")
	PublishSubscribeChannel replyChannel;
	
	@Autowired
	@Qualifier("wasp.channel.notification.batch")
	PublishSubscribeChannel subscribeChannel;
	
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
	
	@PostConstruct
	protected void init() throws MessagingException{
		if (messageTemplates == null)
			throw new MessagingException("No message templates defined to check against");
		// subscribe to injected message channel
		logger.debug("subscribing to injected message channel");
		subscribeChannel.subscribe(this);
	}
	
	@PreDestroy
	protected void destroy() throws Throwable{
		// unregister from message channel only if this object gets garbage collected
		if (subscribeChannel != null){
			subscribeChannel.unsubscribe(this); 
			subscribeChannel = null;
		}
	}
	
	@Override
	//@RetryOnExceptionFixed
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		Long stepExecutionId = context.getStepContext().getStepExecution().getId();
		logger.trace(name + "execute() invoked");
		if (!messageQueue.isEmpty()){
			logger.warn("StepExecution (id=" + stepExecutionId + ") received an expected message so finishing step.");
			//return RepeatStatus.FINISHED;
		}
		if (wasWokenOnMessage(context)){
			logger.debug("StepExecution (id=" + stepExecutionId + ") was woken up from hibernation for a message. Skipping to next step...");
			return RepeatStatus.FINISHED;
		}
		if (isHibernationRequestedForJob(context.getStepContext().getStepExecution().getJobExecution())){
			logger.debug("This job is already undergoing hibernation. Awaiting hibernation...");
		} else if (!wasHibernationRequested){
			logger.debug("Going to request hibernation from StepExecution (id=" + stepExecutionId + ") as not previously requested");
			addStatusMessagesToWakeStepToContext(context, messageTemplates);
			addStatusMessagesToAbandonStepToContext(context, abandonTemplates);
			requestHibernation(context);
		} else if (!wasHibernationRequestGranted){
				logger.debug("Previous hibernation request made by this StepExecution (id=" + stepExecutionId + 
						") but we were still waiting for all steps to be ready. Going to retry request.");
			requestHibernation(context);
			logger.debug("Hibernate request made by this StepExecution (id=" + stepExecutionId + ") but JobExecution is not yet ready to hibernate");
		} else {
			logger.debug("Hibernate request was granted to this StepExecution (id=" + stepExecutionId + "). Awaiting hibernation...");
		}
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
		if (exitStatus.getExitCode().equals(ExitStatus.COMPLETED.getExitCode())){
			for (Message<?> message: messageQueue)
				exitStatus.addExitDescription((String) message.getHeaders().get(WaspStatusMessageTemplate.EXIT_DESCRIPTION_HEADER));
			if (abandonStep || getWokenOnMessageStatus(stepExecution).isUnsuccessful())
				exitStatus =  ExitStatus.FAILED; // modify exit code if abandoned
		}
		sendSuccessReplyToAllMessagesInQueue();
		this.messageQueue.clear(); // clean up in case of restart
		logger.debug("Going to exit step with ExitStatus=" + exitStatus);
		return exitStatus;
	}
	
	protected void sendSuccessReplyToAllMessagesInQueue(){
		MessagingTemplate messagingTemplate = new MessagingTemplate();
		logger.debug("Going to send " + messageQueue.size()  + " reply message(s)...");
		for (Message<?> message: messageQueue){
			try{
				logger.debug("sending reply to message: " + message.toString());
				// this is a little complex. What we do here is attach the temporary point-to-point reply channel generated by the gateway
				// and attached to the source message to the reply message and send it on the 'wasp.channel.reply' channel. 
				// The Gateway will create a bridge from it to the temporary, anonymous reply channel that is stored in the header.
				// Of course if there is no reply channel specified then no reply will be sent.
				if ( message.getHeaders().containsKey(MessageHeaders.REPLY_CHANNEL)){
					Message<WaspStatus> replyMessage = MessageBuilder
							.withPayload(WaspStatus.COMPLETED)
							.setReplyChannel((MessageChannel) message.getHeaders().get(MessageHeaders.REPLY_CHANNEL))
							.build();
					logger.debug("sending reply message: " + replyMessage.toString());
					messagingTemplate.send(replyChannel, replyMessage);
				} else
					logger.debug("No reply message sent because no reply channel was specified in the original message");
			} catch (Exception e){
				logger.warn("Failure to send reply message (reason: " + e.getLocalizedMessage() + ") to reply channel specified in source message : " +
						message.toString() + ". Original exception stack: ");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		logger.debug(name + "handleMessage() invoked. Received message: " + message.toString());
		if (! WaspStatus.class.isInstance(message.getPayload()))
			return;
		WaspStatus statusFromMessage = (WaspStatus) message.getPayload();
		
		// first check if any abort / failure messages have been delivered from a monitored message template
		if (statusFromMessage.isUnsuccessful()){
			for (StatusMessageTemplate messageTemplate: abandonTemplates){
				if (messageTemplate.actUponMessage(message)){
					this.messageQueue.add(message);
					logger.debug(name + "handleMessage() found ABANDONED or FAILED message for abort-monitored template " + 
							messageTemplate.getClass().getName() + ". Going to fail step.");
					abandonStep = true;
					return; // we have found a valid abort message so return
				}
			}
		}
		
		// then check the messages and status against the status we are interested in for a reportable match
		for (StatusMessageTemplate messageTemplate: messageTemplates){
			if (messageTemplate.actUponMessage(message) && statusFromMessage.equals(messageTemplate.getStatus()) ){
				this.messageQueue.add(message);
				logger.debug(name + "handleMessage() adding found message to be compatible so adding to queue: " + message.toString());
				if (statusFromMessage.isUnsuccessful()){
					logger.debug(name + "handleMessage() found ABANDONED or FAILED message to act upon for expected task. Going to fail step.");
					abandonStep = true;
				}
			}
		}
	}

}
