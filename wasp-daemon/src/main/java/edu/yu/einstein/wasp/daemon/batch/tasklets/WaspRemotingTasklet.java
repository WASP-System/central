package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;
import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager.LockType;


public abstract class WaspRemotingTasklet extends WaspHibernatingTasklet {
	
	@Autowired
	private GridHostResolver hostResolver;
	
	/**
	 * protected constructor to prevent instantiation of this class directly
	 */
	protected WaspRemotingTasklet() {}
	
	
	/**
	 * Remote work defined by implementing tasklets
	 * @param context
	 */
	public abstract void doExecute(ChunkContext context) throws Exception;
	
	/**
	 * Default implementation checks to see if a stored result is running 
	 */
	@Override
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		Long stepExecutionId = context.getStepContext().getStepExecution().getId();
		if (wasWokenOnTimeout(context)){
			logger.debug("StepExecution id=" + stepExecutionId + " was woken up from hibernation after a timeout.");
			BatchJobHibernationManager.unlockJobExecution(context.getStepContext().getStepExecution().getJobExecution(), LockType.WAKE);
			wasHibernationRequested = false;
		} else if (wasWokenOnMessage(context)){
			logger.debug("StepExecution id=" + stepExecutionId + " was woken up from hibernation for a message.");
			BatchJobHibernationManager.unlockJobExecution(context.getStepContext().getStepExecution().getJobExecution(), LockType.WAKE);
			wasHibernationRequested = false;
		}
		if (isGridWorkUnitStarted(context)){
			GridResult result = getStartedResult(context);
			GridWorkService gws = hostResolver.getGridWorkService(result);
			try {
				if (gws.isFinished(result)){
					logger.debug("Workunit is finished. Step complete.");
					return RepeatStatus.FINISHED;
				}
			} catch (GridException e) {
				logger.debug(result.toString() + " threw exception: " + e.getLocalizedMessage() + " removing and rethrowing");
				removeStartedResult(context);
				throw e;
			}
			logger.debug("StepExecution id=" + stepExecutionId + " is going to request hibernation as " + result.getUuid() + " started but not complete");
		} else if (!wasHibernationRequested){
			logger.debug("Tasklet not yet configured with a result (StepExecution id=" + stepExecutionId + ")");
			doExecute(context);
		}
		if (!wasHibernationRequested){
			Long timeoutInterval = exponentiallyIncreaseTimeoutIntervalInContext(context);
			logger.debug("Going to request hibernation for " + timeoutInterval + " ms");
			addStatusMessagesToAbandonStepToContext(context, abandonTemplates);
		} else {
			logger.debug("Previous hibernation request made by this StepExecution (id=" + stepExecutionId + 
					") but we were still waiting for all steps to be ready. Going to retry request.");
		}
		requestHibernation(context);
		return RepeatStatus.CONTINUABLE;
	}

	protected final static Logger logger = LoggerFactory.getLogger(WaspRemotingTasklet.class);
	

	
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
	protected static void storeStartedResult(ChunkContext context, GridResult result) {
		ExecutionContext executionContext = context.getStepContext().getStepExecution().getExecutionContext();
		logger.debug(result.toString());
		executionContext.put(GridResult.GRID_RESULT_KEY, result);
	}
	
	private void removeStartedResult(ChunkContext context) {
		ExecutionContext executionContext = context.getStepContext().getStepExecution().getExecutionContext();
		logger.debug("removing result from step context due to GridException");
		executionContext.remove(GridResult.GRID_RESULT_KEY);
	}
	
	public static GridResult getStartedResult(ChunkContext context) {
		return (GridResult) context.getStepContext().getStepExecution().getExecutionContext().get(GridResult.GRID_RESULT_KEY);
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution){
		return super.afterStep(stepExecution);
	}

	
}
