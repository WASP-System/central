package edu.yu.einstein.wasp.tasklets.wasprunlibraryflow;

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

import edu.yu.einstein.wasp.messages.WaspRunStatus;
import edu.yu.einstein.wasp.messages.WaspRunStatusMessage;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.service.SampleService;

public class WaitForRunStopTasklet implements Tasklet, MessageHandler, StepExecutionListener {
	
	@Autowired
	SampleService sampleService;
	
	@Autowired
	private ApplicationContext context;
	
	private WaspRunStatus runStatus;
	
	private Integer runId;
	
	public void setContext(ApplicationContext context) {
		this.context = context;
	}
	
	public void setSampleService(SampleService sampleService) {
		this.sampleService = sampleService;
	}

	private Integer platformUnitId;

	public WaitForRunStopTasklet(Integer platformUnitId) {
		this.platformUnitId = platformUnitId;
		this.runStatus = WaspRunStatus.UNKNOWN;
		Run run = sampleService.getCurrentRunForPlatformUnit(sampleService.getSampleDao().getSampleBySampleId(platformUnitId));
		this.runId = run.getSampleId();
		
		// listen in on the waspRunPublishSubscribeChannel for messages
		SubscribableChannel waspRunPublishSubscribeChannel = context.getBean("waspRunPublishSubscribeChannel", SubscribableChannel.class);
		waspRunPublishSubscribeChannel.subscribe(this); // register as a message handler on the waspRunPublishSubscribeChannel
	}

	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

		if (this.runStatus.equals(WaspRunStatus.UNKNOWN)){
			// no messages yet
			Thread.sleep(5000);
			return RepeatStatus.CONTINUABLE; // we're not done with this step yet
		}
		
		// We have received a run status message. Woohoo!
		// the status will be returned as the exit code from this step using the afterStep method from the StepExecutionListener interface
		return RepeatStatus.FINISHED; // clean exit to complete step
	}

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		// we have picked up a message broadcast on the waspRunPublishSubscribeChannel.
		// Let's see if it is for us and if so we should get the run status from the payload
		if (WaspRunStatusMessage.actUponMessage(message, this.runId)){
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
