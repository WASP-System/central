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

import edu.yu.einstein.wasp.integration.messages.StatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.payload.WaspStatus;

/**
 * Listens on the provided subscribable channel for a message with a task and status specified in the
 * provided message template.
 * @author andymac
 */
public class ListenForStatusTasklet extends WaspTasklet implements MessageHandler, StepExecutionListener {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private StatusMessageTemplate messageTemplate;
	
	private SubscribableChannel subscribeChannel;
	
	private SubscribableChannel abortMonitoringChannel;
	
	private List<Message<WaspStatus>> messageQueue;
	
	private Set<StatusMessageTemplate> abortMonitoredTemplates;
	
	private String name = "";
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name + "#";
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
		if (exitStatus.equals(ExitStatus.COMPLETED)){
			for (Message<WaspStatus> message: messageQueue){
				if (message.getPayload().isUnsuccessful())
					exitStatus =  ExitStatus.FAILED; // modify exit code if abandoned
			}
		}
		this.messageQueue.clear(); // clean up in case of restart
		logger.debug(name + "AfterStep() going to return ExitStatus of '"+exitStatus.toString()+"'");
		return exitStatus;
	}

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		logger.trace(name + "execute() invoked");
		if (messageQueue.isEmpty())
			return RepeatStatus.CONTINUABLE;	
		return RepeatStatus.FINISHED;
	}
	
	@SuppressWarnings("unchecked") 
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		logger.trace(name + "handleMessage() invoked. Received message: " + message.toString());
		if (! WaspStatus.class.isInstance(message.getPayload()))
			return;
		WaspStatus statusFromMessage = (WaspStatus) message.getPayload();
		
		// first check if any abort / failure messages have been delivered from a monitored message template
		if (statusFromMessage.isUnsuccessful()){
			for (StatusMessageTemplate messageTemplate: abortMonitoredTemplates){
				if (messageTemplate.actUponMessage(message)){
					this.messageQueue.add((Message<WaspStatus>) message);
					return; // we have found a valid abort message so return
				}
			}
		}
		
		// then check the message and it's status against the status we are interested in for a reportable match
		if (messageTemplate.actUponMessage(message) && statusFromMessage.equals(messageTemplate.getStatus()) ){
			this.messageQueue.add((Message<WaspStatus>) message);
			logger.debug(name + "handleMessage() adding found message to be compatible so adding to queue: " + message.toString());
		}
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// Do Nothing here
	}

}
