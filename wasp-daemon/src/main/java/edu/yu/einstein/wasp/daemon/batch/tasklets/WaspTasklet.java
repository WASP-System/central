package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;

import edu.yu.einstein.wasp.grid.work.GridResult;


public abstract class WaspTasklet implements Tasklet {
	
	protected final static Logger logger = LoggerFactory.getLogger(WaspTasklet.class);
	
	@Value("${wasp.task.delay:50}")
	protected Long executeRepeatDelay;
	
	@Override
	public abstract RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception;

	
	/**
	 * Check to see if a grid result has been stored by a previous execution of the current step.
	 * @param context
	 * @return
	 */
	public static boolean isGridWorkUnitStarted(ChunkContext context) {
		 Map<String, Object> stepContext = context.getStepContext().getStepExecutionContext();
		 if (stepContext.containsKey(GridResult.GRID_RESULT_KEY))
			 return true;
		 return false;
		 
	}
	
	public static void storeStartedResult(ChunkContext context, GridResult result) {
		ExecutionContext executionContext = context.getStepContext().getStepExecution().getExecutionContext();
		logger.debug(result.toString());
		executionContext.put(GridResult.GRID_RESULT_KEY, result);
	}
	
	public static GridResult getStartedResult(ChunkContext context) {
		return (GridResult) context.getStepContext().getStepExecution().getExecutionContext().get(GridResult.GRID_RESULT_KEY);
	}
}
