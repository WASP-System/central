package edu.yu.einstein.wasp.tasklets.sample;

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
import edu.yu.einstein.wasp.tasklets.WaspTasklet;

public class WaitSampleReceivedTasklet extends WaspTasklet implements Tasklet, MessageHandler, StepExecutionListener {
	
	private final Logger logger = Logger.getLogger(WaitSampleReceivedTasklet.class);

	private Integer sampleId;
	
	private SubscribableChannel subscribeChannel;
	
	private Message<WaspStatus> message;
	
		
	public WaitSampleReceivedTasklet(SubscribableChannel inputSubscribableChannel, Integer sampleId) {
		logger.debug("Constructing new instance with sampleId='"+sampleId+"'"); 
		this.sampleId = sampleId;
		this.subscribeChannel = inputSubscribableChannel;
		this.message = null;
	}
	
	@PostConstruct
	protected void init(){
		// subscribe to injected message channel
		logger.debug("subscribing to injected message channel");
		subscribeChannel.subscribe(this);
	}
	
	@PreDestroy
	protected void destroy() throws Throwable{
		// unregister from message channel only if this object gets garbage collected
		logger.debug("Destroying instance with sampleId='"+sampleId+"'"); 
		if (subscribeChannel != null){
			this.subscribeChannel.unsubscribe(this); 
			subscribeChannel = null;
		}
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution){
		ExitStatus exitStatus = stepExecution.getExitStatus();
		if (exitStatus.equals(ExitStatus.COMPLETED) && message.getPayload().isUnsuccessful()){
			exitStatus =  ExitStatus.FAILED; // modify exit code if abandoned
		} else if (! message.getPayload().isSuccessful()){
			exitStatus = new ExitStatus(message.getPayload().toString()); // set exit status to equal the message payload value
		}
		this.message = null; // clean up in case of restart
		logger.debug("AfterStep() going to return ExitStatus of '"+exitStatus.toString()+"' (instance with sampleId='"+sampleId+"')");
		return exitStatus;
	}

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		logger.debug("execute() invoked (instance with sampleId='"+sampleId+"'");
		if (message == null)
			return delayedRepeatStatusContinuable(5000); // returns RepeatStatus.CONTINUABLE after 5s delay	
		return RepeatStatus.FINISHED;
	}
	
	@SuppressWarnings("unchecked") 
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		logger.debug("handleMessage() invoked (instance with sampleId='"+sampleId+"'). Received message: " + message.toString());
		if (SampleStatusMessageTemplate.actUponMessage(message, sampleId)){
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
