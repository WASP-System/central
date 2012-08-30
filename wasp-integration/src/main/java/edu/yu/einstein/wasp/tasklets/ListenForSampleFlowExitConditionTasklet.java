package edu.yu.einstein.wasp.tasklets;

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

import edu.yu.einstein.wasp.messages.JobStatusMessageTemplate;
import edu.yu.einstein.wasp.messages.SampleStatusMessageTemplate;
import edu.yu.einstein.wasp.messages.WaspStatus;

/**
 * Listens for job abandoment / failure or sample acceptance / abandonment or failure to signal that the sample flow
 * should be terminated.
 * @author andymac
 */
public class ListenForSampleFlowExitConditionTasklet extends WaspTasklet implements Tasklet, MessageHandler, StepExecutionListener {
	
	private final Logger logger = Logger.getLogger(ListenForSampleFlowExitConditionTasklet.class);

	private Integer sampleId;
	
	private Integer jobId;
	
	private Set<SubscribableChannel> subscribeChannels;
	
	private Message<WaspStatus> message;
	
		
	public ListenForSampleFlowExitConditionTasklet(Set<SubscribableChannel> subscribeChannels, Integer sampleId, Integer jobId) {
		this.sampleId = sampleId;
		this.jobId = jobId;
		this.subscribeChannels = subscribeChannels;
		this.message = null;
	}
	
	@PostConstruct
	protected void init() throws MessagingException{
		// subscribe to injected message channel
		logger.debug("subscribing to injected message channels");
		for (SubscribableChannel subscribeChannel: subscribeChannels)
			subscribeChannel.subscribe(this);
	}
	
	@PreDestroy
	protected void destroy() throws Throwable{
		// unregister from message channel only if this object gets garbage collected
		if (subscribeChannels != null){
			for (SubscribableChannel subscribeChannel: subscribeChannels){
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
		if ( ( JobStatusMessageTemplate.actUponMessage(message, jobId) && statusFromMessage.isUnsuccessful() ) ||
			( SampleStatusMessageTemplate.actUponMessage(message, sampleId) && 
					( statusFromMessage.equals(WaspStatus.ACCEPTED) || statusFromMessage.isUnsuccessful() ) ) ){
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
