package edu.yu.einstein.wasp.tasklets;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.core.SubscribableChannel;

import edu.yu.einstein.wasp.messages.StatusMessageTemplate;
import edu.yu.einstein.wasp.messages.WaspStatus;

/**
 * Listens on the provided subscribable channel(s) for a message for a task 
 * specified in any of the provided message template(s) with an unsuccessful status (ABANDONED or FAILED) .
 * Returns ExitStatus of FAILED on receiving an applicable ABANDONED and FAILED wasp status.
 * @author andymac
 *
 */
public class ListenForFinishedStatusTasklet extends WaspTasklet implements Tasklet, MessageHandler, StepExecutionListener {
	
	private final Logger logger = Logger.getLogger(ListenForFinishedStatusTasklet.class);

	private Set<StatusMessageTemplate> messageTemplateSet;
	
	private Set<SubscribableChannel> subscribeChannelSet;
	
	private Message<WaspStatus> message;
	
	public ListenForFinishedStatusTasklet(Set<SubscribableChannel> inputSubscribableChannelSet, Set<StatusMessageTemplate> messageTemplateSet) {
		logger.debug("Constructing new instance"); 
		this.messageTemplateSet = messageTemplateSet;
		this.subscribeChannelSet = inputSubscribableChannelSet;
		this.message = null;
	}
		
	public ListenForFinishedStatusTasklet(SubscribableChannel inputSubscribableChannel, StatusMessageTemplate messageTemplate) {
		logger.debug("Constructing new instance"); 
		this.messageTemplateSet = new HashSet<StatusMessageTemplate>();
		this.messageTemplateSet.add(messageTemplate);
		this.subscribeChannelSet = new HashSet<SubscribableChannel>();
		this.subscribeChannelSet.add(inputSubscribableChannel);
		this.message = null;
	}
	
	public ListenForFinishedStatusTasklet(SubscribableChannel inputSubscribableChannel, Set<StatusMessageTemplate> messageTemplateSet) {
		logger.debug("Constructing new instance"); 
		this.messageTemplateSet = messageTemplateSet;
		this.subscribeChannelSet = new HashSet<SubscribableChannel>();
		this.subscribeChannelSet.add(inputSubscribableChannel);
		this.message = null;
	}
	
	public ListenForFinishedStatusTasklet(Set<SubscribableChannel> inputSubscribableChannelSet, StatusMessageTemplate messageTemplate) {
		logger.debug("Constructing new instance"); 
		this.messageTemplateSet = new HashSet<StatusMessageTemplate>();
		this.messageTemplateSet.add(messageTemplate);
		this.subscribeChannelSet = inputSubscribableChannelSet;
		this.message = null;
	}
	
	@PostConstruct
	protected void init(){
		// subscribe to injected message channel
		logger.debug("subscribing to injected message channel");
		for (SubscribableChannel subscribeChannel: subscribeChannelSet)
			subscribeChannel.subscribe(this);
	}
	
	@PreDestroy
	protected void destroy() throws Throwable{
		// unregister from message channel only if this object gets garbage collected
		if (subscribeChannelSet != null){
			for (SubscribableChannel subscribeChannel: subscribeChannelSet){
				subscribeChannel.unsubscribe(this); 
				subscribeChannel = null;
			}
		}
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution){
		ExitStatus exitStatus = stepExecution.getExitStatus();
		if (exitStatus.equals(ExitStatus.COMPLETED) && message.getPayload().isUnsuccessful()){
			exitStatus =  ExitStatus.FAILED; // modify exit code if abandoned
		} 
		this.message = null; // clean up in case of restart
		logger.debug("AfterStep() going to return ExitStatus of '"+exitStatus.toString()+"'");
		return exitStatus;
	}

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		logger.debug("execute() invoked");
		if (message == null)
			return delayedRepeatStatusContinuable(5000); // returns RepeatStatus.CONTINUABLE after 5s delay	
		return RepeatStatus.FINISHED;
	}
	
	@SuppressWarnings("unchecked") 
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		logger.debug("handleMessage() invoked). Received message: " + message.toString());
		if (! WaspStatus.class.isInstance(message.getPayload()))
			return;
		WaspStatus statusFromMessage = (WaspStatus) message.getPayload();
		if (! statusFromMessage.isFinished() )
			return;
		for (StatusMessageTemplate messageTemplate: messageTemplateSet){
			// we need to process the message if any registered messageTemplates can act on a finished status
			if (messageTemplate.actUponMessage(message)){
				if (this.message == null){
					this.message = (Message<WaspStatus>) message;
				} else {
					throw new MessagingException("Received an applicable message before previous message processed");
				}
				return;
			}
		}
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// Do Nothing here
	}

}
