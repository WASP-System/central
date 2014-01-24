package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.SubscribableChannel;

import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.templates.StatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;

/**
 * Listens on the provided subscribable channel(s) for relevant completion messages. Also monitors the abort monitoring channel
 * and stops the entire job if a relevant notifying abort message is received at any time. 
 * @author asmclellan
 */
public class ListenForExitConditionTasklet extends WaspTasklet {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private Set<WaspStatusMessageTemplate> messageTemplates;
	
	private SubscribableChannel abortMonitoringChannel;
	
	private Set<SubscribableChannel> subscribeChannels;
	
	private Message<WaspStatus> message = null;
	
	private boolean stopJobNotificationReceived = false;
	
	private boolean isHibernationRequested = false;
	
	public ListenForExitConditionTasklet() {
		// proxy
	}
	
	public ListenForExitConditionTasklet(SubscribableChannel inputSubscribableChannel, SubscribableChannel abortMonitoringChannel, WaspStatusMessageTemplate messageTemplate) {
		this.messageTemplates = new HashSet<>();
		this.messageTemplates.add(messageTemplate);
		this.abortMonitoringChannel = abortMonitoringChannel;
		this.subscribeChannels = new HashSet<>();
		this.subscribeChannels.add(inputSubscribableChannel);
	}
	
	public ListenForExitConditionTasklet(Set<SubscribableChannel> inputSubscribableChannels, SubscribableChannel abortMonitoringChannel, Set<WaspStatusMessageTemplate> messageTemplates) {
		this.messageTemplates = messageTemplates;
		this.abortMonitoringChannel = abortMonitoringChannel;
		this.subscribeChannels = inputSubscribableChannels;
	}
	
	public ListenForExitConditionTasklet(SubscribableChannel inputSubscribableChannel, SubscribableChannel abortMonitoringChannel, Set<WaspStatusMessageTemplate> messageTemplates) {
		this.messageTemplates = messageTemplates;
		this.abortMonitoringChannel = abortMonitoringChannel;
		this.subscribeChannels = new HashSet<SubscribableChannel>();
		this.subscribeChannels.add(inputSubscribableChannel);
	}
	
	public ListenForExitConditionTasklet(Set<SubscribableChannel> inputSubscribableChannels, SubscribableChannel abortMonitoringChannel, WaspStatusMessageTemplate messageTemplate) {
		this.messageTemplates = new HashSet<WaspStatusMessageTemplate>();
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
		logger.debug(name + "subscribing to abort message channel");
		//abortMonitoringChannel.subscribe(this);
		logger.debug(name + "subscribing to subscribe channel(s)");
		//for (SubscribableChannel subscribeChannel: subscribeChannels)
		//	subscribeChannel.subscribe(this);
	}


	@PreDestroy
	protected void destroy() throws Throwable{
		// unregister from message channel only if this object gets garbage collected
		if (subscribeChannels != null && !subscribeChannels.isEmpty()){
			//for (SubscribableChannel subscribeChannel: subscribeChannels){
			//	subscribeChannel.unsubscribe(this); 
			//	subscribeChannel = null;
			//}
		} 
		//if (abortMonitoringChannel != null){
		//	abortMonitoringChannel.unsubscribe(this); 
		//	abortMonitoringChannel = null;
		//} 
	}
	
/*	@Override
	public ExitStatus afterStep(StepExecution stepExecution){
		ExitStatus exitStatus = stepExecution.getExitStatus();
		if (message != null){
			WaspStatus statusFromMessage = (WaspStatus) message.getPayload();
			if (statusFromMessage.isUnsuccessful()){
				// this notice should trigger stopping the job
				logger.debug(name + "Stopping job due to receiving a message containing an ABANDONED / FAILED notice");
				// Signal the JobExecution to stop. JobExecution().stop() iterates through the associated StepExecutions, 
				// calling StepExecution.setTerminateOnly()
				try {
					// wait for cleaning up of steps before termination. A step may need to act on this and finalize before
					// stopping the job execution (which may leave step in the wrong state i.e. STOPPED instead of FAILED for example)
					Thread.sleep(6000); 
				} catch (InterruptedException e) {} 
				logger.debug("Executing: stepExecution.getJobExecution().stop();");
				stepExecution.getJobExecution().stop(); 
			}
			this.message = null; // clean up in case of restart
			stopJobNotificationReceived = false; // clean up in case of restart
		}
		logger.debug(name + "AfterStep() going return ExitStatus of '"+exitStatus.getExitCode().toString()+"'");
		return exitStatus;
	}*/

	@Override
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		logger.trace(name + "execute() invoked");
		return RepeatStatus.CONTINUABLE;
	}
	/*
	@SuppressWarnings("unchecked") 
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		logger.debug(name + "ListenForExitCondition()::handleMessage() invoked). Received message: " + message.toString());
		if (! WaspStatus.class.isInstance(message.getPayload()))
			return;
		WaspStatus statusFromMessage = (WaspStatus) message.getPayload();
		// only single messages with unsuccessful status if they are general notifications (i.e. not for specific tasks)
		boolean stopJobNotificationPreviouslyReceived = stopJobNotificationReceived;
		
		for (StatusMessageTemplate messageTemplate: messageTemplates){
			if (messageTemplate.actUponMessage(message)){
				logger.debug(name + "handleMessage() adding found message to be compatible: " + message.toString());
				if (statusFromMessage.isUnsuccessful() && message.getHeaders().get(WaspJobTask.HEADER_KEY).equals(WaspJobTask.NOTIFY_STATUS))
					stopJobNotificationReceived = true;
				if (stopJobNotificationReceived || statusFromMessage.equals(messageTemplate.getStatus()) ){
					if (this.message == null || statusFromMessage.isUnsuccessful()){
						this.message = (Message<WaspStatus>) message;
					} else if (!stopJobNotificationPreviouslyReceived){
						throw new MessagingException("Received an applicable message before previous message processed");
					} else{
						logger.warn(name + "Recieved a message with non-unsuccessful status whilst an existing received job-stopping message is pending processing");
					}
				}
			}
		}
	}
*/

}
