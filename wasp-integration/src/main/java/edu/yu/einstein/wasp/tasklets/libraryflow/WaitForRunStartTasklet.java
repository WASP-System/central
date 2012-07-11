package edu.yu.einstein.wasp.tasklets.libraryflow;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.core.SubscribableChannel;

import edu.yu.einstein.wasp.exceptions.UnexpectedMessagePayloadValueException;
import edu.yu.einstein.wasp.messages.WaspRunStatus;
import edu.yu.einstein.wasp.messages.WaspRunStatusMessage;

public class WaitForRunStartTasklet implements Tasklet, MessageHandler, StepExecutionListener, ApplicationContextAware {
	
	private final Logger logger = Logger.getLogger(WaitForRunStartTasklet.class);
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	private WaspRunStatus runStatus;
	
	private Integer platformUnitId;
	
	private SubscribableChannel waspRunPublishSubscribeChannel;

	
	/**
	 * Constructor
	 * @param platformUnitId
	 */
	public WaitForRunStartTasklet(Integer platformUnitId) {
		logger.debug("Constructing new Instance"); // TODO: remove. This is for initial testing ONLY
		this.platformUnitId = platformUnitId;
		this.runStatus = null;
	}

	
	/**
	 * Implementation of {@link Tasklet} Interface method
	 */
	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
		if (waspRunPublishSubscribeChannel == null){
			// listen in on the waspRunPublishSubscribeChannel for messages 
			this.waspRunPublishSubscribeChannel = applicationContext.getBean("waspRunPublishSubscribeChannel", SubscribableChannel.class);
			waspRunPublishSubscribeChannel.subscribe(this); // register as a message handler on the waspRunPublishSubscribeChannel
		}
		logger.debug("Entering WaitForRunStartTasklet:execute()"); // TODO: remove. This is for initial testing ONLY
		if (this.runStatus == null){
			// no messages yet
			try {
				Thread.sleep(5000); // sleep 5 seconds
			} catch (InterruptedException e) {
				// do nothing here just proceed to the return
			}
			return RepeatStatus.CONTINUABLE; // we're not done with this step yet
		}
		
		// We have received a run status message. Woohoo! Better be sure it's one we're expecting
		if (this.runStatus != WaspRunStatus.STARTED && 
				this.runStatus != WaspRunStatus.ABANDONED){
			throw new UnexpectedMessagePayloadValueException("Got unexpected message WaspRunStatus."+runStatus.toString());
		}
		this.waspRunPublishSubscribeChannel.unsubscribe(this); //unregister as a message handler on the waspRunPublishSubscribeChannel
		// the status will be returned as the exit code from this step using the afterStep() method from the StepExecutionListener interface
		return RepeatStatus.FINISHED; // clean exit to complete step
	}

	/**
	 * Implementation of {@link MessageHandler} Interface method.
	 * Gets the message and sets the runStatus property to it's reference.
	 */
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		// we have picked up a message broadcast on the waspRunPublishSubscribeChannel.
		// Let's see if it is for us and if so we should get the run status from the payload
		logger.debug("Message recieved by WaitForRunStartTasklet:handleMessage(): "+message.toString());
		if (WaspRunStatusMessage.actUponMessage(message, null, this.platformUnitId)){
			this.runStatus = (WaspRunStatus) message.getPayload();
		}
	}
	
	/**
	 * Implementation of the {@link StepExecutionListener} Interface method
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		// nothing to do here
	}

	/**
	 * Implementation of the {@link StepExecutionListener} Interface method
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// Re-define ExitStatus
		logger.debug("Entering afterStep()"); // TODO: remove. This is for initial testing ONLY
		if (stepExecution.getExitStatus() == ExitStatus.FAILED)
			return ExitStatus.FAILED;
		if (this.runStatus == WaspRunStatus.STARTED)
			return ExitStatus.COMPLETED;
		return new ExitStatus("REPEAT STEP");
	}

	

}
