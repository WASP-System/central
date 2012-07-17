package edu.yu.einstein.wasp.tasklets.runflow;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.integration.Message;
import org.springframework.integration.core.PollableChannel;

import edu.yu.einstein.wasp.messages.WaspRunStatus;
import edu.yu.einstein.wasp.messages.WaspRunStatusMessage;

/**
 * Tasklet to handle business logic associated with a run being started. A {@link WaspRunStatusMessage} is sent to inform other flows of the fact.
 * @author andymac
 *
 */
public class RunStartTasklet implements Tasklet, ApplicationContextAware{
	
	private final Logger logger = Logger.getLogger(RunStartTasklet .class);
	
	private Integer runId;
	private Integer platformUnitId;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	public RunStartTasklet(Integer runId, Integer platformUnitId){
		this.runId = runId;
		this.platformUnitId = platformUnitId;
	}

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		// send message to inform other flows that a run has started
		Message<WaspRunStatus> message =  WaspRunStatusMessage.build(runId, platformUnitId, WaspRunStatus.STARTED);
		logger.debug("Sending message via 'waspRunPriorityChannel': "+message.toString());
		PollableChannel waspRunPriorityChannel = applicationContext.getBean("waspRunPriorityChannel", PollableChannel.class);
		waspRunPriorityChannel.send(message);
		return RepeatStatus.FINISHED;
	}

}