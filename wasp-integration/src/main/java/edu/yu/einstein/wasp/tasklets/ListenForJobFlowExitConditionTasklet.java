package edu.yu.einstein.wasp.tasklets;

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
 * Listens on the provided subscribable channel for a completion message. Also monitors the abort monitoring channel
 * and stops the entire job if a job abort message is received at any time. 
 * @author andymac
 */
public class ListenForJobFlowExitConditionTasklet extends WaspTasklet implements Tasklet, MessageHandler, StepExecutionListener {
	
	private final Logger logger = Logger.getLogger(ListenForJobFlowExitConditionTasklet.class);

	private StatusMessageTemplate messageTemplate;
	
	private SubscribableChannel abortMonitoringChannel;
	
	private SubscribableChannel subscribeChannel;
	
	private Message<WaspStatus> message;
	
	public ListenForJobFlowExitConditionTasklet(SubscribableChannel inputSubscribableChannel, SubscribableChannel abortMonitoringChannel, StatusMessageTemplate messageTemplate) {
		this.messageTemplate = messageTemplate;
		this.abortMonitoringChannel = abortMonitoringChannel;
		this.subscribeChannel = inputSubscribableChannel;
		this.message = null;
	}
	
	@PostConstruct
	protected void init() throws MessagingException{
		// subscribe to injected message channel
		logger.debug("subscribing to abort message channel");
		abortMonitoringChannel.subscribe(this);
		logger.debug("subscribing to subscribe channel");
		subscribeChannel.subscribe(this);
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
			if (message.getPayload().isUnsuccessful()){
				exitStatus =  ExitStatus.FAILED; // modify exit code if abandoned
				logger.debug("Stopping job due to receiving abandon / fail notice");
				stepExecution.getJobExecution().stop(); // stop this job!!
			}
		}
		this.message = null; // clean up in case of restart
		logger.debug("AfterStep() going to stop the job and return ExitStatus of '"+exitStatus.toString()+"'");
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
		if (messageTemplate.actUponMessage(message) && (statusFromMessage.isSuccessful() ||  statusFromMessage.isUnsuccessful()) ){
			if (this.message == null){
				this.message = (Message<WaspStatus>) message;
			} else {
				throw new MessagingException("Received an applicable message before previous message processed");
			}
		}
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// Do Nothing here
	}

}
