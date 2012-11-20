package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import edu.yu.einstein.wasp.grid.work.GridResult;


public abstract class WaspTasklet implements Tasklet {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
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
		Map<String, Object> stepContext = context.getStepContext().getStepExecutionContext();
		stepContext.put(GridResult.GRID_RESULT_KEY, result);
	}
	
	public static GridResult getStartedResult(ChunkContext context) {
		Map<String, Object> stepContext = context.getStepContext().getStepExecutionContext();
		return (GridResult) stepContext.get(GridResult.GRID_RESULT_KEY);
	}
}
