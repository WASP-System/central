package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.HashMap;
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
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.TaskletRetryException;
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
	public abstract GridResult doExecute(ChunkContext context) throws Exception;
	
	/**
	 * cleanup work to do before a restart
	 * @param context
	 */
	public abstract void doCleanupBeforeRestart(StepExecution stepExecution) throws Exception;
	
	/**
	 * tasks to perform before finishing step execution
	 * @param context
	 * @throws Exception
	 */
	public void doPreFinish(ChunkContext context) throws Exception {
		// override as necessary in child classes
	}
	
	/**
	 * Default implementation checks to see if a stored result is running 
	 */
	@Override
	@Transactional("entityManager")
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		Long stepExecutionId = stepExecution.getId();
		if (wasWokenOnTimeout(context)){
			logger.debug("StepExecution id=" + stepExecutionId + " was woken up from hibernation after a timeout.");
			BatchJobHibernationManager.unlockJobExecution(context.getStepContext().getStepExecution().getJobExecution(), LockType.WAKE);
			wasHibernationRequested = false;
			removeWokenOnTimeoutStatus(stepExecution);
		} else if (wasWokenOnMessage(stepExecution)){
			logger.debug("StepExecution id=" + stepExecutionId + " was woken up from hibernation for a message.");
			BatchJobHibernationManager.unlockJobExecution(context.getStepContext().getStepExecution().getJobExecution(), LockType.WAKE);
			wasHibernationRequested = false;
			removeWokenOnMessageStatus(stepExecution);
		}
		
		// Three cases at this point
		// isStarted=F and isHibernationRequested=F == first run
		// isStarted=T and isHibernationResuested=T == job started on grid service
		// isStarted=F and isHibernationRequested=T == not going to request grid work
		boolean jobHasUpdatedChild = false;
		GridResult result = getGridResult(context);
		try{
			if (result != null && !isFlaggedForRestart(stepExecution)){
				Map<String, GridResult> currentChildJobResults = new HashMap<String, GridResult>(result.getChildResults());
				GridWorkService gws = hostResolver.getGridWorkService(result);
				if (gws.isFinished(result)){
					doPreFinish(context);
					logger.debug("Workunit is finished. Step complete.");
					return RepeatStatus.FINISHED;
				}
				jobHasUpdatedChild = !currentChildJobResults.equals(result.getChildResults());
				logger.debug("StepExecution id=" + stepExecutionId + " is going to request hibernation as " + result.getUuid() + " started but not complete");
			} else if (!wasHibernationRequested){
				logger.debug("Tasklet not yet configured with a result (StepExecution id=" + stepExecutionId + ")");
				result = doExecute(context);
				if (result == null) {
					logger.debug("no work unit configured. Exiting without execution.");
					return RepeatStatus.FINISHED;
				}
				setIsInErrorConditionAndFlaggedForRestart(stepExecution, false);		
			}
		} catch (GridException e) {
			logger.warn("GridException caught : " + e.getLocalizedMessage() + ". Going to run cleanup code and throw TaskletRetryException");
			setIsInErrorConditionAndFlaggedForRestart(stepExecution, true);
			doCleanupBeforeRestart(stepExecution);
			throw new TaskletRetryException(e.getMessage());
		} catch (Exception e) {
			logger.warn("Exception of type " + e.getClass().getName() + " caught: " + e.getLocalizedMessage() + ". Flagging in step execution and rethrowing");
			throw e;
		} finally {
			logger.trace("saving GridResult from finally block");
			saveGridResult(context, result); // do this whatever else happens in the try block
		}
		if (!wasHibernationRequested){
			Long timeoutInterval;
			if (jobHasUpdatedChild){
				logger.debug("Job result has updated a child job so going to reset timeoutInterval to minimum");
				timeoutInterval = getRandomInitialExponentialInterval();
				setTimeoutIntervalInContext(stepExecution, timeoutInterval);
			} else 
				timeoutInterval = exponentiallyIncreaseTimeoutIntervalInContext(context);
			logger.debug("Going to request hibernation for " + timeoutInterval + " ms");
			addStatusMessagesToAbandonStepToContext(stepExecution, abandonTemplates);
		} else {
			logger.debug("Previous hibernation request made by this StepExecution (id=" + stepExecutionId + 
					") but we were still waiting for all steps to be ready. Going to retry request.");
		}
		requestHibernation(context);
		return RepeatStatus.CONTINUABLE;
	}

	protected final static Logger logger = LoggerFactory.getLogger(WaspRemotingTasklet.class);
	
	private static void saveGridResult(ChunkContext context, GridResult result) {
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		logger.debug(result.toString());
		stepExecution.getExecutionContext().put(GridResult.GRID_RESULT_KEY, result);
	}
	
	protected static GridResult getGridResult(ChunkContext context) {
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		if (!stepExecution.getExecutionContext().containsKey(GridResult.GRID_RESULT_KEY))
			return null;
		return (GridResult) stepExecution.getExecutionContext().get(GridResult.GRID_RESULT_KEY);
	}
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		super.beforeStep(stepExecution);
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution){
		return super.afterStep(stepExecution);
	}
	
	protected ExecutionContext getStepExecutionContext(StepExecution se){
		return se.getExecutionContext();
	}
	
	protected ExecutionContext getJobExecutionContext(StepExecution se){
		return se.getJobExecution().getExecutionContext();
	}
	
	protected ExecutionContext getStepExecutionContext(ChunkContext context){
		return getStepExecutionContext(context.getStepContext().getStepExecution());
	}
	
	protected ExecutionContext getJobExecutionContext(ChunkContext context){
		return getJobExecutionContext(context.getStepContext().getStepExecution());
	}

	
}
