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
import edu.yu.einstein.wasp.grid.work.GridJobStatus;
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
		boolean isJobInRunningToEndingTransition = false;
		if (isGridWorkUnitStarted(context)){
			GridResult result = getStartedResult(context);
			GridJobStatus currentStatus = new GridJobStatus(result.getJobStatus().toString());
			Map<String, GridResult> currentChildJobResults = new HashMap<String, GridResult>(result.getChildResults());
			GridWorkService gws = hostResolver.getGridWorkService(result);
			try {
				if (gws.isFinished(result)){
					doPreFinish(context);
					logger.debug("Workunit is finished. Step complete.");
					return RepeatStatus.FINISHED;
				}
				boolean jobHasTransitioned = currentStatus.isRunning() && result.getJobStatus().isEnded();
				boolean jobHasUpdatedChild = !currentChildJobResults.equals(result.getChildResults());
				if (jobHasTransitioned || jobHasUpdatedChild){
					logger.debug("Job result has transitioned state (" + jobHasTransitioned + 
							") or has updated a child job (" + jobHasUpdatedChild + "). Going to reset timeoutInterval to minimum");
					isJobInRunningToEndingTransition = true;
				}
				storeStartedResult(context, result); // result may have been modified whilst checking in isFinished
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
		if (!isGridWorkUnitStarted(context)) {
			logger.debug("no work unit configured, exiting without execution.");
			return RepeatStatus.FINISHED;
		}
		if (!wasHibernationRequested){
			Long timeoutInterval;
			if (isJobInRunningToEndingTransition){
				timeoutInterval = initialExponentialInterval;
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
	

	
	/**
	 * Check to see if a grid result has been stored by a previous execution of the current step.
	 * @param context
	 * @return
	 */
	public static boolean isGridWorkUnitStarted(ChunkContext context) {
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		boolean isStarted = false;
		if (stepExecution.getExecutionContext().containsKey(GridResult.GRID_RESULT_KEY))
			isStarted = true;
		logger.debug("Grid work unit for StepExecutionId=" + stepExecution.getId() + " is started=" + isStarted);
		return isStarted;
	}
	protected static void storeStartedResult(ChunkContext context, GridResult result) {
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		logger.debug(result.toString());
		stepExecution.getExecutionContext().put(GridResult.GRID_RESULT_KEY, result);
	}
	
	private void removeStartedResult(ChunkContext context) {
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		logger.debug("removing result from step context due to GridException");
		stepExecution.getExecutionContext().remove(GridResult.GRID_RESULT_KEY);
	}
	
	public static GridResult getStartedResult(ChunkContext context) {
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		return (GridResult) stepExecution.getExecutionContext().get(GridResult.GRID_RESULT_KEY);
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution){
		return super.afterStep(stepExecution);
	}

	
}
