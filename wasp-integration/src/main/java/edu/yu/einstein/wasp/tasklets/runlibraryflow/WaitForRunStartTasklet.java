package edu.yu.einstein.wasp.tasklets.runlibraryflow;


import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
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

import edu.yu.einstein.annotations.Retryable;
import edu.yu.einstein.wasp.exceptions.UnexpectedMessagePayloadValueException;
import edu.yu.einstein.wasp.messages.WaspRunStatusMessage;
import edu.yu.einstein.wasp.messages.WaspStatus;
import edu.yu.einstein.wasp.messages.WaspStatusMessage;

public class WaitForRunStartTasklet implements Tasklet, MessageHandler, ApplicationContextAware {
	
	private final Logger logger = Logger.getLogger(WaitForRunStartTasklet.class);
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	private WaspStatus runStatus;
	
	private Integer platformUnitId;
	
	private SubscribableChannel waspRunPublishSubscribeChannel;

	
	/**
	 * Constructor
	 * @param platformUnitId
	 */
	public WaitForRunStartTasklet(Integer platformUnitId) {
		// using step.scope. Constructor called once per step execution.
		logger.debug("WaitForRunStartTasklet: Constructing new Instance"); 
		this.platformUnitId = platformUnitId;
		this.runStatus = null;
	}
	
	/**
	 * Returns a status of RepeatStatus.CONTINUABLE after specified timeout
	 * @param ms
	 * @return
	 */
	private RepeatStatus delayedRepeatStatusContinuable(Integer ms){
		try {
			Thread.sleep(ms); 
		} catch (InterruptedException e) {
			// do nothing here just proceed to the return
		}
		return RepeatStatus.CONTINUABLE; // we're not done with this step yet
	}

	
	/**
	 * Implementation of {@link Tasklet} Interface method
	 */
	@Retryable
	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
		if (waspRunPublishSubscribeChannel == null){
			// listen in on the waspRunPublishSubscribeChannel for messages 
			this.waspRunPublishSubscribeChannel = applicationContext.getBean("wasp.channel.notification.run", SubscribableChannel.class);
			waspRunPublishSubscribeChannel.subscribe(this); // register as a message handler on the waspRunPublishSubscribeChannel
		}
		logger.debug("Entering WaitForRunStartTasklet:execute()"); 
		if (this.runStatus == null){
			// no messages yet
			return delayedRepeatStatusContinuable(5000);
		}
		
		// We have received a run status message. Woohoo! Better be sure it's one we're expecting
		if (this.runStatus != WaspStatus.STARTED && 
				this.runStatus != WaspStatus.ABANDONED){
			logger.error("Got unexpected message waiting for 'WaspStatus.STARTED': 'WaspStatus."+runStatus.toString()+"'"); 
			String failureMessage = "Got unexpected message waiting for 'WaspStatus.COMPLETED': 'WaspStatus."+runStatus.toString()+"'";
			this.runStatus = null; // on fail this object is not deleted and may be re-used on restart so clean up to be safe
			logger.error(failureMessage); 
			throw new UnexpectedMessagePayloadValueException(failureMessage);
		}
		
		// message is as expected
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
			this.runStatus = (WaspStatus) message.getPayload();
		}
	}

	@PreDestroy
	protected void preDestroy() throws Throwable{
		// unregister from waspRunPublishSubscribeChannel only if this object gets garbage collected
		if (waspRunPublishSubscribeChannel != null){
			this.waspRunPublishSubscribeChannel.unsubscribe(this); 
			waspRunPublishSubscribeChannel = null;
		}
	}
	

}
