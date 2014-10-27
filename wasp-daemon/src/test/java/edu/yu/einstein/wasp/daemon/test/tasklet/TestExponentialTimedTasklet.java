package edu.yu.einstein.wasp.daemon.test.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;
import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager.LockType;

public class TestExponentialTimedTasklet extends WaspRemotingTasklet {

	public TestExponentialTimedTasklet() {}
	
	private static final int MAX_REPEATS = 3;
	
	@Override
	public void doExecute(ChunkContext context) throws Exception {
		
	}
	
	@Override
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception{
		if (wasWokenOnTimeout(context)){
			logger.debug("Woken on timeout");
			wasHibernationRequested = false;
			BatchJobHibernationManager.unlockJobExecution(context.getStepContext().getStepExecution().getJobExecution(), LockType.WAKE);
		}
		ExecutionContext ec = context.getStepContext().getStepExecution().getExecutionContext();
		int count = 0;
		if (!ec.containsKey("COUNT")){
			count = 1;
			ec.put("COUNT", count);
			logger.debug("Repeat count = "  + count + " / " + MAX_REPEATS + " so going to repeat with ");
		} else if (ec.getInt("COUNT") < MAX_REPEATS){
			count = ec.getInt("COUNT") + 1;
			ec.put("COUNT", count);
			logger.debug("Repeat count = "  + count + " / " + MAX_REPEATS + " so going to repeat");
		} else {
			count = ec.getInt("COUNT");
			logger.debug("Repeat count = "  + count + " / " + MAX_REPEATS + "  so step finished");
			return RepeatStatus.FINISHED;
		}
		
		Long timeoutInterval = exponentiallyIncreaseTimeoutIntervalInContext(context);
		logger.debug("Going to request hibernation for " + timeoutInterval + " ms");
		addStatusMessagesToAbandonStepToContext(context, abandonTemplates);
		requestHibernation(context);
		return RepeatStatus.CONTINUABLE;
	}

	@Override
	public void doCleanupBeforeRestart(ChunkContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
