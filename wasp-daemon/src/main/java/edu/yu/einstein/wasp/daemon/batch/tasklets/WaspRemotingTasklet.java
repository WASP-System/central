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
import org.springframework.beans.factory.annotation.Value;
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
	
	@Value("${wasp.batch.retryOnException.fixed.maxattempts:3}")
	private Integer maxRetryAttempts;
	
	/**
	 * protected constructor to prevent instantiation of this class directly
	 */
	protected WaspRemotingTasklet() {}
	
	
	public Integer getMaxRetryAttempts() {
		return maxRetryAttempts;
	}


	public void setMaxRetryAttempts(Integer maxRetryAttempts) {
		this.maxRetryAttempts = maxRetryAttempts;
	}


	/**
	 * Remote work defined by implementing tasklets
	 * @param context
	 */
	public abstract GridResult doExecute(ChunkContext context) throws Exception;
	
	/**
	 * cleanup work to do before a restart and before entering error state
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
		if (isInErrorCondition(stepExecution)){
			logger.debug("StepExecution id=" + stepExecutionId + " is being woken up from hibernation from error state.");
			removeIsInErrorCondition(stepExecution);
			BatchJobHibernationManager.resetRetryCounter(stepExecution);
		}
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
			if (result != null && !isInErrorCondition(stepExecution)){
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
				setIsInErrorCondition(stepExecution, false);		
			}
		} catch (GridException e) {
			logger.warn("GridException caught : " + e.getLocalizedMessage() + ". Going to run cleanup code"); 
			doCleanupBeforeRestart(stepExecution);
			int retryCount = getRetryCount(stepExecution) + 1;
			if (retryCount <= maxRetryAttempts){
				incrementRetryCounter(stepExecution);
				logger.warn("Going to throw TaskletRetryException. This is retry attempt " + retryCount + " of " + maxRetryAttempts);
				throw new TaskletRetryException(e.getMessage());
			}
			else {
				setIsInErrorCondition(stepExecution, true);
				logger.warn("Maximum retries exceeded. Entering error hibernation state.");
			}
		} catch (Exception e1){
			// enter error state on catching any unexpected errors
			logger.warn("Exception caught of  type " + e1.getClass().getName() + ": " + e1.getLocalizedMessage() + ". Going to run cleanup code and enter error state"); 
			e1.printStackTrace();
			doCleanupBeforeRestart(stepExecution);
			setIsInErrorCondition(stepExecution, true);
			logger.info("Entering error hibernation state.");
		} finally {
			logger.trace("saving GridResult from finally block");
			if (result != null)
				saveGridResult(context, result); // do this whatever else happens in the try block
			else
				logger.warn("Not saving GridResult for StepExecution id=" + stepExecutionId + " as is null! (wasHibernationRequested=" + wasHibernationRequested + ")");
		}
		if (!wasHibernationRequested){
			if (isInErrorCondition(stepExecution)){
				logger.debug("Job is in error condition so removing wake time interval");
				removeWokenOnTimeoutStatus(stepExecution);
			} else {
				Long timeoutInterval;
				if (jobHasUpdatedChild){
					logger.debug("Job result has updated a child job so going to reset timeoutInterval to minimum");
					timeoutInterval = getRandomInitialExponentialInterval();
					setTimeoutIntervalInContext(stepExecution, timeoutInterval);
				} else 
					timeoutInterval = exponentiallyIncreaseTimeoutIntervalInContext(context);
				logger.debug("Going to request hibernation for " + timeoutInterval + " ms");
			}
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
		return getGridResult(stepExecution);
	}
	
	protected static GridResult getGridResult(StepExecution stepExecution) {
		if (!stepExecution.getExecutionContext().containsKey(GridResult.GRID_RESULT_KEY))
			return null;
		return (GridResult) stepExecution.getExecutionContext().get(GridResult.GRID_RESULT_KEY);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		super.beforeStep(stepExecution);
	}
	
	/**
	 * {@inheritDoc}
	 */
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
