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
 * Listens on the provided subscribable channel for a message with a task and status specified in the
 * provided message template.
 * @author andymac
 */
public class ListenForAbortStatusTasklet extends WaspTasklet implements Tasklet, MessageHandler, StepExecutionListener {
	
	private final Logger logger = Logger.getLogger(ListenForAbortStatusTasklet.class);

	private StatusMessageTemplate messageTemplate;
	
	private SubscribableChannel abortMonitoringChannel;
	
	private Message<WaspStatus> message;
	
	private Set<StatusMessageTemplate> abortMonitoredTemplates;
	
	public void setAdditionalAbortMonitoredTemplates(Set<StatusMessageTemplate> additionalAbortMessageMonitoredTemplates){
		this.abortMonitoredTemplates.addAll(additionalAbortMessageMonitoredTemplates);
	}
	
	public ListenForAbortStatusTasklet(SubscribableChannel abortMonitoringChannel, StatusMessageTemplate messageTemplate) {
		this.messageTemplate = messageTemplate;
		this.abortMonitoringChannel = abortMonitoringChannel;
		this.message = null;
		this.abortMonitoredTemplates = new HashSet<StatusMessageTemplate>();
		this.abortMonitoredTemplates.add(messageTemplate);
	}
	
	@PostConstruct
	protected void init() throws MessagingException{
		if (messageTemplate.getStatus() == null)
			throw new MessagingException("The message template defines no status to check against");
		// subscribe to injected message channel
		logger.debug("subscribing to abort message channel");
		abortMonitoringChannel.subscribe(this);
	}
	
	@PreDestroy
	protected void destroy() throws Throwable{
		// unregister from message channel only if this object gets garbage collected
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
			if (message.getPayload().isUnsuccessful())
				exitStatus =  ExitStatus.FAILED; // modify exit code if abandoned
		}
		this.message = null; // clean up in case of restart
		logger.debug("AfterStep() going to stop the job and return ExitStatus of '"+exitStatus.toString()+"'");
		stepExecution.getJobExecution().stop(); // stop this job
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
		
		// first check if any abort / failure messages have been delivered from a monitored message template
		if (statusFromMessage.isUnsuccessful() && this.message == null){
			for (StatusMessageTemplate messageTemplate: abortMonitoredTemplates){
				if (messageTemplate.actUponMessage(message)){
					this.message = (Message<WaspStatus>) message;
					return; // we have found a valid abort message so return
				}
			}
		}
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// Do Nothing here
	}

}
