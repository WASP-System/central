package edu.yu.einstein.wasp.tasklets.job;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.core.SubscribableChannel;

import edu.yu.einstein.wasp.exceptions.UnexpectedMessagePayloadValueException;
import edu.yu.einstein.wasp.messages.WaspJobStatusMessageTemplate;
import edu.yu.einstein.wasp.messages.WaspJobTask;
import edu.yu.einstein.wasp.messages.WaspStatus;
import edu.yu.einstein.wasp.tasklets.WaspTasklet;

public class WaspJobApprovalTasklet extends WaspTasklet implements Tasklet, MessageHandler, StepExecutionListener {
	
	private final Logger logger = Logger.getLogger(WaspJobApprovalTasklet.class);

	private Integer jobId;
	
	private SubscribableChannel subscribeChannel;
	
	private WaspStatus finalWaspStatus;
	
	private String task;
	
	
	public WaspJobApprovalTasklet(SubscribableChannel inputSubscribableChannel, Integer jobId, String task) {
		super();
		logger.debug("Constructing new instance with jobId='"+jobId+"' and task='"+task+"'"); 
		this.jobId = jobId;
		this.subscribeChannel = inputSubscribableChannel;
		this.task = task;
		this.finalWaspStatus = null;
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
		logger.debug("Destroying instance with jobId='"+jobId+"' and task='"+task+"'"); 
		if (subscribeChannel != null){
			this.subscribeChannel.unsubscribe(this); 
			subscribeChannel = null;
		}
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution){
		ExitStatus exitStatus = stepExecution.getExitStatus();
		if (exitStatus.equals(ExitStatus.COMPLETED) && finalWaspStatus.equals(WaspStatus.ABANDONED))
			exitStatus =  ExitStatus.FAILED; // modify exit code if abandoned
		logger.debug("AfterStep() going to return ExitStatus of '"+exitStatus.toString()+"' (instance with jobId='"+jobId+"' and task='"+task+"')");
		return exitStatus;
	}

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		logger.debug("execute() invoked (instance with jobId='"+jobId+"' and task='"+task+"')");
		if (statusMessageStack.isEmpty())
			return delayedRepeatStatusContinuable(5000); // returns RepeatStatus.CONTINUABLE after 5s delay
		
		// We have received a status message. Woohoo! Better be sure it's one we're expecting
		Iterator<Message<WaspStatus>> statusItr = statusMessageStack.iterator();
		while(statusItr.hasNext()){
			WaspStatus jobStatus = statusItr.next().getPayload();
			statusItr.remove();
			if ( jobStatus == WaspStatus.ABANDONED || jobStatus == WaspStatus.COMPLETED){
				finalWaspStatus = jobStatus;
				while(statusItr.hasNext())
					statusItr.remove(); // remove remainder of messages from list
				break;
			}
		}
		if (finalWaspStatus == null){
			String failureMessage = "Got unexpected message (instance with jobId='"+jobId+"' and task='"+task+"')";
			logger.error(failureMessage); 
			throw new UnexpectedMessagePayloadValueException(failureMessage);
		}
		
		return RepeatStatus.FINISHED;
	}
	
	@SuppressWarnings("unchecked") 
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		logger.debug("handleMessage() invoked (instance with jobId='"+jobId+"' and task='"+task+"'). Received message: " + message.toString());
		if (WaspJobStatusMessageTemplate.actUponMessage(message, jobId, task))
			statusMessageStack.add((Message<WaspStatus>) message);
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// Do Nothing here
	}

}
