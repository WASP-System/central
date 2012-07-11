package edu.yu.einstein.wasp.tasklets.libraryflow;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.core.SubscribableChannel;

import edu.yu.einstein.wasp.exceptions.UnexpectedMessagePayloadValueException;
import edu.yu.einstein.wasp.messages.WaspRunStatus;
import edu.yu.einstein.wasp.messages.WaspRunStatusMessage;

public class WaitForRunStopTasklet implements Tasklet, MessageHandler, StepExecutionListener {
	
	@Autowired
	private ApplicationContext context;
	
	private WaspRunStatus runStatus;
	
	private Integer platformUnitId;
	
	public void setContext(ApplicationContext context) {
		this.context = context;
	}
	

	public WaitForRunStopTasklet(Integer platformUnitId) {
		this.platformUnitId = platformUnitId;
		this.runStatus = null;
		
		// listen in on the waspRunPublishSubscribeChannel for messages
		SubscribableChannel waspRunPublishSubscribeChannel = context.getBean("waspRunPublishSubscribeChannel", SubscribableChannel.class);
		waspRunPublishSubscribeChannel.subscribe(this); // register as a message handler on the waspRunPublishSubscribeChannel
	}
	
	private RepeatStatus delayedContinueRepeatStatus(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// do nothing here just proceed to the return
		}
		return delayedContinueRepeatStatus();
	}

	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

		if (this.runStatus == null){
			// no messages yet
			return delayedContinueRepeatStatus(); // we're not done with this step yet
		}
		// We have received a run status message. Woohoo! Better be sure it's one we're expecting
		if (this.runStatus != WaspRunStatus.STOPPED && 
				this.runStatus != WaspRunStatus.COMPLETED && 
				this.runStatus != WaspRunStatus.ABANDONED &&
				this.runStatus != WaspRunStatus.FAILED){
			throw new UnexpectedMessagePayloadValueException("Got unexpected message WaspRunStatus."+runStatus.toString());
		}
		
		// the status will be returned as the exit code from this step using the afterStep() method from the StepExecutionListener interface
		return RepeatStatus.FINISHED; // clean exit to complete step
	}

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		// we have picked up a message broadcast on the waspRunPublishSubscribeChannel.
		// Let's see if it is for us and if so we should get the run status from the payload
		if (WaspRunStatusMessage.actUponMessage(message, null, this.platformUnitId)){
			this.runStatus = (WaspRunStatus) message.getPayload();
		}
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// Define exit status of this step to be the string value of runStatus
		return new ExitStatus("RUN "+runStatus.toString());
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// Nothing to do here
	}

}
