package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionFixed;
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
	@RetryOnExceptionFixed
	@Transactional("entityManager")
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
		
		// Three cases at this point
		// isStarted=F and isHibernationRequested=F == first run
		// isStarted=T and isHibernationResuested=T == job started on grid service
		// isStarted=F and isHibernationRequested=T == not going to request grid work
		boolean jobHasUpdatedChild = false;
		GridResult result = getGridResult(context);
		if (result != null && !isFlaggedForRestart(context)){
			Map<String, GridResult> currentChildJobResults = new HashMap<String, GridResult>(result.getChildResults());
			GridWorkService gws = hostResolver.getGridWorkService(result);
			try {
				if (gws.isFinished(result)){
					doPreFinish(context);
					logger.debug("Workunit is finished. Step complete.");
					return RepeatStatus.FINISHED;
				}
				jobHasUpdatedChild = !currentChildJobResults.equals(result.getChildResults());
				if (jobHasUpdatedChild)
					logger.debug("Job result has updated a child job so going to reset timeoutInterval to minimum");
			} catch (GridException e) {
				logger.debug(result.toString() + " threw exception: " + e.getLocalizedMessage() + " removing and rethrowing");
				setIsFlaggedForRestart(context, result, true);
				throw e;
			} finally {
				saveGridResult(context, result); // result may have been modified whilst checking in isFinished
			}
			logger.debug("StepExecution id=" + stepExecutionId + " is going to request hibernation as " + result.getUuid() + " started but not complete");
		} else if (!wasHibernationRequested){
			logger.debug("Tasklet not yet configured with a result (StepExecution id=" + stepExecutionId + ")");
			doExecute(context);
			result = getGridResult(context);
			if (result == null || !result.getJobStatus().isSubmitted()) {
				logger.debug("no work unit configured, or workunit not properly configured. Exiting without execution.");
				return RepeatStatus.FINISHED;
			} else 
				setIsFlaggedForRestart(context, getGridResult(context), false);
		}
		if (!wasHibernationRequested){
			Long timeoutInterval;
			if (jobHasUpdatedChild){
				timeoutInterval = getRandomInitialExponentialInterval();
				setTimeoutIntervalInContext(context, timeoutInterval);
			} else 
				timeoutInterval = exponentiallyIncreaseTimeoutIntervalInContext(context);
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
	
	public boolean isFlaggedForRestart(ChunkContext context) {
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		boolean isFlaggedForRestart = false;
		if (stepExecution.getExecutionContext().containsKey(GridResult.FLAGGED_FOR_RESTART))
			isFlaggedForRestart =  true;
		logger.debug("Grid work unit for StepExecutionId=" + stepExecution.getId() + " is flagged for restart=" + isFlaggedForRestart);
		return isFlaggedForRestart;
	}
	
	protected static void setIsFlaggedForRestart(ChunkContext context, GridResult result, Boolean isFlaggedForRestart) {
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		logger.debug(result.toString());
		stepExecution.getExecutionContext().put(GridResult.FLAGGED_FOR_RESTART, isFlaggedForRestart.toString());
	}
	
	protected static void saveGridResult(ChunkContext context, GridResult result) {
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		logger.debug(result.toString());
		stepExecution.getExecutionContext().put(GridResult.GRID_RESULT_KEY, result);
	}
	
	public static GridResult getGridResult(ChunkContext context) {
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		if (!stepExecution.getExecutionContext().containsKey(GridResult.GRID_RESULT_KEY))
			return null;
		return (GridResult) stepExecution.getExecutionContext().get(GridResult.GRID_RESULT_KEY);
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution){
		return super.afterStep(stepExecution);
	}

	
}
