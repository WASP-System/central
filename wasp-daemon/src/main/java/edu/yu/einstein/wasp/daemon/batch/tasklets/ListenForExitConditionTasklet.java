package edu.yu.einstein.wasp.daemon.batch.tasklets;

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

import edu.yu.einstein.wasp.integration.messages.StatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.WaspJobTask;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;

/**
 * Listens on the provided subscribable channel(s) for relevant completion messages. Also monitors the abort monitoring channel
 * and stops the entire job if a relevant notifying abort message is received at any time. 
 * @author andymac
 */
public class ListenForExitConditionTasklet extends WaspTasklet implements Tasklet, MessageHandler, StepExecutionListener {
	
	private final Logger logger = Logger.getLogger(ListenForExitConditionTasklet.class);

	private Set<StatusMessageTemplate> messageTemplates;
	
	private SubscribableChannel abortMonitoringChannel;
	
	private Set<SubscribableChannel> subscribeChannels;
	
	private Message<WaspStatus> message = null;
	
	private boolean stopJobNotificationReceived = false;
	
	public ListenForExitConditionTasklet(SubscribableChannel inputSubscribableChannel, SubscribableChannel abortMonitoringChannel, StatusMessageTemplate messageTemplate) {
		this.messageTemplates = new HashSet<StatusMessageTemplate>();
		this.messageTemplates.add(messageTemplate);
		this.abortMonitoringChannel = abortMonitoringChannel;
		this.subscribeChannels = new HashSet<SubscribableChannel>();
		this.subscribeChannels.add(inputSubscribableChannel);
	}
	
	public ListenForExitConditionTasklet(Set<SubscribableChannel> inputSubscribableChannels, SubscribableChannel abortMonitoringChannel, Set<StatusMessageTemplate> messageTemplates) {
		this.messageTemplates = messageTemplates;
		this.abortMonitoringChannel = abortMonitoringChannel;
		this.subscribeChannels = inputSubscribableChannels;
	}
	
	public ListenForExitConditionTasklet(SubscribableChannel inputSubscribableChannel, SubscribableChannel abortMonitoringChannel, Set<StatusMessageTemplate> messageTemplates) {
		this.messageTemplates = messageTemplates;
		this.abortMonitoringChannel = abortMonitoringChannel;
		this.subscribeChannels = new HashSet<SubscribableChannel>();
		this.subscribeChannels.add(inputSubscribableChannel);
	}
	
	public ListenForExitConditionTasklet(Set<SubscribableChannel> inputSubscribableChannels, SubscribableChannel abortMonitoringChannel, StatusMessageTemplate messageTemplate) {
		this.messageTemplates = new HashSet<StatusMessageTemplate>();
		this.messageTemplates.add(messageTemplate);
		this.abortMonitoringChannel = abortMonitoringChannel;
		this.subscribeChannels = inputSubscribableChannels;
	}
	
	@PostConstruct
	protected void init() throws MessagingException{
		// All provided message templates must specify a target status to check for
		for (StatusMessageTemplate messageTemplate: messageTemplates){
			if (messageTemplate.getStatus() == null)
				throw new MessagingException("a StatusMessageTemplate provided does not specify a target status to monitor");
		}
		
		// subscribe to injected message channels
		logger.debug("subscribing to abort message channel");
		abortMonitoringChannel.subscribe(this);
		logger.debug("subscribing to subscribe channel(s)");
		for (SubscribableChannel subscribeChannel: subscribeChannels)
			subscribeChannel.subscribe(this);
	}
	
	@PreDestroy
	protected void destroy() throws Throwable{
		// unregister from message channel only if this object gets garbage collected
		if (subscribeChannels != null && !subscribeChannels.isEmpty()){
			for (SubscribableChannel subscribeChannel: subscribeChannels){
				subscribeChannel.unsubscribe(this); 
				subscribeChannel = null;
			}
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
		if (stopJobNotificationReceived){
			// this notice should trigger stopping the job
			logger.debug("Stopping job due to receiving a message containing an ABANDONED / FAILED notice");
			// Signal the JobExecution to stop. JobExecution().stop() iterates through the associated StepExecutions, 
			// calling StepExecution.setTerminateOnly()
			stepExecution.getJobExecution().stop(); 
		}
		this.message = null; // clean up in case of restart
		stopJobNotificationReceived = false; // clean up in case of restart
		logger.debug("AfterStep() going return ExitStatus of '"+exitStatus.toString()+"'");
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
		// only single messages with unsuccessful status if they are general notifications (i.e. not for specific tasks)
		boolean stopJobNotificationPreviouslyReceived = stopJobNotificationReceived;
		if (statusFromMessage.isUnsuccessful() && message.getHeaders().get(WaspJobTask.HEADER_KEY).equals(WaspJobTask.NOTIFY_STATUS))
			stopJobNotificationReceived = true;
		
		for (StatusMessageTemplate messageTemplate: messageTemplates){
			if (messageTemplate.actUponMessage(message) && (stopJobNotificationReceived || statusFromMessage.equals(messageTemplate.getStatus()) ) ){
				if (this.message == null || stopJobNotificationReceived){
					this.message = (Message<WaspStatus>) message;
				} else if (!stopJobNotificationPreviouslyReceived){
					throw new MessagingException("Received an applicable message before previous message processed");
				} else{
					logger.warn("Recieved a message with non-unsuccessful status whilst an existing received job-stopping message is pending processing");
				}
			}
		}
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// Do Nothing here
	}

}
