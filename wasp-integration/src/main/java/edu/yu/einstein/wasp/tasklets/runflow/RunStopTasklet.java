package edu.yu.einstein.wasp.tasklets.runflow;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.core.PollableChannel;

import edu.yu.einstein.wasp.messages.WaspRunStatus;
import edu.yu.einstein.wasp.messages.WaspRunStatusMessage;

/**
 * Tasklet to handle business logic associated with a run being stopped either normally or abnormally. A {@link WaspRunStatusMessage} is sent to inform other flows of the fact.
 * @author andymac
 *
 */
public class RunStopTasklet implements Tasklet{
	
	private Integer runId;
	private Integer platformUnitId;
	
	@Autowired
	private ApplicationContext context;
	
	public void setContext(ApplicationContext context) {
		this.context = context;
	}
	
	public RunStopTasklet(Integer runId, Integer platformUnitId){
		this.runId = runId;
		this.platformUnitId = platformUnitId;
	}

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		// send message to inform other flows that a run has started
		PollableChannel waspRunPriorityChannel = context.getBean("waspRunPriorityChannel", PollableChannel.class);
		waspRunPriorityChannel.send(WaspRunStatusMessage.build(runId, platformUnitId, WaspRunStatus.STOPPED));
		try{
			Thread.sleep(6000); // gives time for message to be processed (TESTING ONLY)
		} catch (InterruptedException e) {
			// do nothing here just proceed to the return
		}
		return RepeatStatus.FINISHED;
	}

}
