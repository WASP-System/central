package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
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

import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionFixed;
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
	
	private List<Message<?>> abandonMessageQueue = new ArrayList<>();
	
	private int preHibernationRepeatCounter = 0;
	
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
	
	public ListenForStatusTasklet(Collection<WaspStatusMessageTemplate> messageTemplates) {
		setMessagesToListenFor(messageTemplates);
	}
	
	public void setMessageToListenFor(WaspStatusMessageTemplate messageTemplate) {
		Set<WaspStatusMessageTemplate> templates = new HashSet<>();
		templates.add(messageTemplate);
		setMessagesToListenFor(templates);
	}
	
	public void setMessagesToListenFor(Collection<WaspStatusMessageTemplate> messageTemplates) {
		this.messageTemplates.clear();
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
	@RetryOnExceptionFixed
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		StepExecution stepExecution =  context.getStepContext().getStepExecution();
		Long stepExecutionId =stepExecution.getId();
		Long jobExecutionId = context.getStepContext().getStepExecution().getJobExecutionId();
		logger.trace(name + "execute() invoked");
		/*if ((!messageQueue.isEmpty() || !abandonMessageQueue.isEmpty()) && 
				!isHibernationRequestedForJob(stepExecution.getJobExecution())){
			if (wasHibernationRequested){
				setHibernationRequestedForStep(stepExecution, false);
				hibernationManager.removeStepExecutionFromWakeMessageMap(stepExecution);
				hibernationManager.removeStepExecutionFromAbandonMessageMap(stepExecution);
			}
			logger.info("StepExecution (id=" + stepExecutionId + ", JobExecution id=" + jobExecutionId + ") received an expected message so finishing step.");
			setStepStatusInJobExecutionContext(stepExecution, BatchStatus.COMPLETED);
			return RepeatStatus.FINISHED;
		}*/
		if (wasWokenOnMessage(context)){
			logger.info("StepExecution (id=" + stepExecutionId + ", JobExecution id=" + jobExecutionId + 
					") was woken up from hibernation for a message. Skipping to next step...");
			setStepStatusInJobExecutionContext(stepExecution, BatchStatus.COMPLETED);
			return RepeatStatus.FINISHED;
		}
		if (isHibernationRequestedForJob(context.getStepContext().getStepExecution().getJobExecution())){
			logger.trace("This JobExecution (id=" + jobExecutionId + ") is already undergoing hibernation. Awaiting hibernation...");
		} else if (!wasHibernationRequested){
			// let cycle a few times before attempting hibernation so that all steps and the job are fully awake and recorded in batch. Will not hibernate
			// all steps if this isn't done.
			if (preHibernationRepeatCounter++ < 10)
				return RepeatStatus.CONTINUABLE;
			logger.info("Going to request hibernation from StepExecution (id=" + stepExecutionId + ", JobExecution id=" + jobExecutionId + 
					") as not previously requested");
			addStatusMessagesToWakeStepToContext(context, messageTemplates);
			addStatusMessagesToAbandonStepToContext(context, abandonTemplates);
			requestHibernation(context);
		} else if (!wasHibernationRequestGranted){
				logger.debug("Previous hibernation request made by this StepExecution (id=" + stepExecutionId + ", JobExecution id=" + jobExecutionId + 
						") but we were still waiting for all steps to be ready. Going to retry request.");
			requestHibernation(context);
			logger.debug("Hibernate request made by this StepExecution (id=" + stepExecutionId + ", JobExecution id=" + jobExecutionId + 
					") but JobExecution is not yet ready to hibernate");
		} else {
			logger.debug("Hibernate request was granted to this StepExecution (id=" + stepExecutionId + ", JobExecution id=" + jobExecutionId + 
					"). Awaiting hibernation...");
		}
		return RepeatStatus.CONTINUABLE;	
	}
	
	private ExitStatus getExitStatus(ExitStatus currentExitStatus, WaspStatus waspStatus){
		if (waspStatus.equals(WaspStatus.FAILED))
			return ExitStatus.FAILED;
		if (waspStatus.equals(WaspStatus.ABANDONED))
			return ExitStatus.TERMINATED;
		return currentExitStatus;
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		ExitStatus exitStatus = stepExecution.getExitStatus();
		exitStatus = exitStatus.and(getExitStatus(exitStatus, getWokenOnMessageStatus(stepExecution)));
		// set exit status to equal the most severe outcome of all received messages
		if (!messageQueue.isEmpty()){
			for (Message<?> message: messageQueue)
				exitStatus = exitStatus.and(getExitStatus(exitStatus, (WaspStatus) message.getPayload()));
		} else if (!abandonMessageQueue.isEmpty()){
			for (Message<?> message: abandonMessageQueue)
				exitStatus = exitStatus.and(getExitStatus(exitStatus, (WaspStatus) message.getPayload()));
		}
		// make sure all messages get replies
		sendSuccessReplyToAllMessagesInQueue(messageQueue);
		sendSuccessReplyToAllMessagesInQueue(abandonMessageQueue);
		this.messageQueue.clear(); // clean up in case of restart
		logger.debug("Going to exit step with ExitStatus=" + exitStatus);
		return exitStatus;
	}
	
	protected void sendSuccessReplyToAllMessagesInQueue(List<Message<?>> queue){
		MessagingTemplate messagingTemplate = new MessagingTemplate();
		logger.debug("Going to send " + messageQueue.size()  + " reply message(s)...");
		for (Message<?> message: queue){
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
		for (StatusMessageTemplate messageTemplate: abandonTemplates){
			if (messageTemplate.actUponMessage(message)){
				this.abandonMessageQueue.add(message);
				logger.debug(name + "handleMessage() found ABANDONED message for abort-monitored template " + 
						messageTemplate.getClass().getName() + ". Going to fail step.");
			}
		}
		
		// then check the messages and status against the status we are interested in for a reportable match
		for (StatusMessageTemplate messageTemplate: messageTemplates){
			if (messageTemplate.actUponMessage(message) && statusFromMessage.equals(messageTemplate.getStatus()) ){
				this.messageQueue.add(message);
				logger.debug(name + "handleMessage() adding found message to be compatible so adding to queue: " + message.toString());
			}
		}
	}

}
