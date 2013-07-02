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
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.core.SubscribableChannel;

import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionFixed;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.templates.StatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;

/**
 * Listens on the provided subscribable channel for a message with a task and status specified in the
 * provided message template.
 * @author asmclellan
 */
public class ListenForStatusTasklet extends WaspTasklet implements MessageHandler, StepExecutionListener {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private StatusMessageTemplate messageTemplate;
	
	private SubscribableChannel subscribeChannel;
	
	private SubscribableChannel abortMonitoringChannel;
	
	private List<Message<WaspStatus>> messageQueue;
	
	private Set<StatusMessageTemplate> abortMonitoredTemplates;
	
	private boolean stopStep = false;
	
	private boolean abandonStep = false;
	
	public ListenForStatusTasklet() {
		// proxy
	}
	

	public void setAdditionalAbortMonitoredTemplates(Set<StatusMessageTemplate> additionalAbortMessageMonitoredTemplates){
		this.abortMonitoredTemplates.addAll(additionalAbortMessageMonitoredTemplates);
	}
	
	public ListenForStatusTasklet(SubscribableChannel inputSubscribableChannel, SubscribableChannel abortMonitoringChannel, StatusMessageTemplate messageTemplate) {
		this.messageTemplate = messageTemplate;
		this.subscribeChannel = inputSubscribableChannel;
		this.abortMonitoringChannel = abortMonitoringChannel;
		this.messageQueue = new ArrayList<Message<WaspStatus>>();
		this.abortMonitoredTemplates = new HashSet<StatusMessageTemplate>();
		this.abortMonitoredTemplates.add(messageTemplate);
	}
	
	@PostConstruct
	protected void init() throws MessagingException{
		if (messageTemplate.getStatus() == null)
			throw new MessagingException("The message template defines no status to check against");
		// subscribe to injected message channel
		logger.debug("subscribing to injected message channel");
		subscribeChannel.subscribe(this);
		logger.debug("subscribing to abort message channel");
		abortMonitoringChannel.subscribe(this);
	}
	
	@PreDestroy
	protected void destroy() throws Throwable{
		// unregister from message channel only if this object gets garbage collected
		if (subscribeChannel != null){
			subscribeChannel.unsubscribe(this); 
			subscribeChannel = null;
		}
		if (abortMonitoringChannel != null){
			abortMonitoringChannel.unsubscribe(this); 
			abortMonitoringChannel = null;
		} 
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution){
		ExitStatus exitStatus = stepExecution.getExitStatus();
		// if any messages in the queue are unsuccessful we wish to return an exit status of FAILED
		if (exitStatus.getExitCode().equals(ExitStatus.COMPLETED.getExitCode())){
			for (Message<WaspStatus> message: messageQueue)
				exitStatus.addExitDescription((String) message.getHeaders().get(WaspStatusMessageTemplate.EXIT_DESCRIPTION_HEADER));
			if (abandonStep)
				exitStatus =  ExitStatus.FAILED; // modify exit code if abandoned
			else if (stopStep)
				exitStatus =  ExitStatus.STOPPED; // modify exit code if stopped
		}
		this.messageQueue.clear(); // clean up in case of restart
		logger.debug(name + "AfterStep() going to return ExitStatus of '"+exitStatus.toString()+"'");
		
		return exitStatus;
	}

	@Override
	@RetryOnExceptionFixed
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		logger.trace(name + "execute() invoked");
		if (messageQueue.isEmpty() && !abandonStep && !stopStep){
			Thread.sleep(executeRepeatDelay);
			return RepeatStatus.CONTINUABLE;
		}	
		return RepeatStatus.FINISHED;
	}
	
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
					abandonStep = true;
					return; // we have found a valid abort message so return
				}
			}
		}
		
		// then check the message and it's status against the status we are interested in for a reportable match
		if (messageTemplate.actUponMessage(message) && statusFromMessage.equals(messageTemplate.getStatus()) ){
			this.messageQueue.add((Message<WaspStatus>) message);
			logger.debug(name + "handleMessage() adding found message to be compatible so adding to queue: " + message.toString());
			if (statusFromMessage.isUnsuccessful()){
				logger.debug(name + "handleMessage() found ABANDONED or FAILED message to act upon for expected task. Going to fail step.");
				abandonStep = true;
			}
		}
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// Do Nothing here
	}

}
