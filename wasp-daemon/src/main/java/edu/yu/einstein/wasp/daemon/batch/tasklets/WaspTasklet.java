package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryException;

import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionFixed;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;


public abstract class WaspTasklet extends WaspHibernatingTasklet implements StepExecutionListener {
	
	@Autowired
	private GridHostResolver hostResolver;
	
	protected Set<WaspStatusMessageTemplate> abandonTemplates = new HashSet<>();
	
	public WaspTasklet() {
		// proxy
	}
	
	
	public void setAbandonMessages(final Set<WaspStatusMessageTemplate> abandonTemplates){
		this.abandonTemplates.clear();
		this.abandonTemplates.addAll(abandonTemplates);
	}
	
	public void setAbandonMessage(final WaspStatusMessageTemplate abandonTemplate){
		Set<WaspStatusMessageTemplate> templates = new HashSet<>();
		templates.add(abandonTemplate);
		setAbandonMessages(templates);
	}
	
	/**
	 * Default implementation checks to see if a stored result is running 
	 */
	@Override
	@RetryOnExceptionFixed
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		if (isTheHibernationControllingStep){
			requestHibernation(context);
			logger.debug("Hibernate controlling step detected that JobExecution is not yet ready to hibernate");
			throw new RetryException("Not yet ready to hibernate");
		}
		if (wasHibernationRequested){
			logger.debug("Not going to request hibernation or check WorkUnit status as hibernation has been previously requested. Awaiting Hibernation");
			throw new RetryException("Awaiting hibernation");
		} 
		if (isGridWorkUnitStarted(context)){
			GridResult result = getStartedResult(context);
			GridWorkService gws = hostResolver.getGridWorkService(result);
			try {
				if (gws.isFinished(result))
					return RepeatStatus.FINISHED;
			} catch (GridException e) {
				logger.debug(result.toString() + " threw exception: " + e.getLocalizedMessage() + " removing and rethrowing");
				removeStartedResult(context);
				throw e;
			}
			logger.debug("Going to request hibernation as " + result.getUuid() + " not complete");
		}
		logger.debug("Tasklet not yet configured with a result");
		Long timeoutInterval = exponentiallyIncreaseTimeoutIntervalInContext(context);
		logger.debug("Going to request hibernation for " + timeoutInterval + " ms");
		addStatusMessagesToAbandonStepToContext(context, abandonTemplates);
		requestHibernation(context);
		return RepeatStatus.CONTINUABLE;
	}

	protected final static Logger logger = LoggerFactory.getLogger(WaspTasklet.class);
	
	protected String name = "";
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name + "#";
	}

	
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
	public void beforeStep(StepExecution stepExecution) {
		// Do Nothing here
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution){
		return stepExecution.getExitStatus();
	}
	
}
