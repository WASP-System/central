package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;

public abstract class WaspHibernatingTasklet implements Tasklet{
	
	private static final Logger logger = LoggerFactory.getLogger(WaspHibernatingTasklet.class);
	
	protected boolean wasHibernationRequested = false;
	
	@Autowired
	@Value("${wasp.hibernation.retry.exponential.initialInterval:5000}")
	private Long initialExponentialInterval;
	
	@Autowired
	@Value("${wasp.hibernation.retry.exponential.maxInterval:60000}")
	private Long maxExponentialInterval;
	
	@Autowired
	private BatchJobHibernationManager hibernationManager;
	
	/**
	 * Request hibernation of this jobExecution, to be woken again after specified time interval.
	 * @param context
	 * @param timeInterval
	 */
	protected void requestHibernation(ChunkContext context, Long timeInterval){
		// NOTE: due to transactional behavior, execution contexts are not visible in the database outside of this job thread until job stopped / completed
		// so care is required knowing what is visible where. Any additions to job execution context here will be visible immediately job-wide.
		ExecutionContext executionContext = context.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
		logContexts(context);
		StepContext stepContext = context.getStepContext();
		logger.info("Going to hibernate job " + stepContext.getJobName() + 
				" (execution id=" + stepContext.getStepExecution().getJobExecutionId() + ") from step " + 
				stepContext.getStepName() + " (step id=" + stepContext.getStepExecution().getId() + ")");
		if (executionContext.containsKey(BatchJobHibernationManager.HIBERNATION_REQUESTED) && 
				(boolean) executionContext.get(BatchJobHibernationManager.HIBERNATION_REQUESTED)){
			wasHibernationRequested = true;
			logger.debug("Execution context already contains HIBERNATION_REQUESTED=true. Setting wasHibernationRequested=true");
		} else
			executionContext.put(BatchJobHibernationManager.HIBERNATION_REQUESTED, true);
		if (wasHibernationRequested){
			logger.debug("Hibernation request aborted as hibernation request already made by another step. Registering messages with HibernationManager");
			hibernationManager.addTimeIntervalForJobStep(context.getStepContext().getStepExecution().getJobExecutionId(), 
					context.getStepContext().getStepExecution().getId(), timeInterval);
			return;
		} else {
			wasHibernationRequested = true;
			hibernationManager.processHibernateRequest(stepContext.getStepExecution().getJobExecutionId(), stepContext.getStepExecution().getId(), timeInterval);
		}
	}
	
	/**
	 * Request hibernation of this jobExecution, to be woken again after any of the provided messages are received.
	 * @param context
	 * @param wakeMessageTemplates
	 */
	protected void requestHibernation(ChunkContext context, Set<WaspStatusMessageTemplate> wakeMessageTemplates){
		// NOTE: due to transactional behavior, execution contexts are not visible in the database outside of this job thread until job stopped / completed
		// so care is required knowing what is visible where. Any additions to job execution context here will be visible immediately job-wide.
		ExecutionContext executionContext = context.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
		logContexts(context);
		StepContext stepContext = context.getStepContext();
		logger.info("Going to hibernate job " + stepContext.getJobName() + 
				" (execution id=" + stepContext.getStepExecution().getJobExecutionId() + ") from step " + 
				stepContext.getStepName() + " (step id=" + stepContext.getStepExecution().getId() + ")");
		if (executionContext.containsKey(BatchJobHibernationManager.HIBERNATION_REQUESTED) && 
				(boolean) executionContext.get(BatchJobHibernationManager.HIBERNATION_REQUESTED)){
			wasHibernationRequested = true;
			logger.debug("Execution context already contains HIBERNATION_REQUESTED=true. Setting wasHibernationRequested=true");
		} else
			executionContext.put(BatchJobHibernationManager.HIBERNATION_REQUESTED, true);
		if (wasHibernationRequested){
			logger.debug("Hibernation request aborted as hibernation request already made by another step. Registering messages with HibernationManager");
			hibernationManager.addMessageTemplatesForWakingJobStep(context.getStepContext().getStepExecution().getJobExecutionId(), 
					context.getStepContext().getStepExecution().getId(), wakeMessageTemplates);
			return;
		} else {
			wasHibernationRequested = true;
			hibernationManager.processHibernateRequest(stepContext.getStepExecution().getJobExecutionId(), stepContext.getStepExecution().getId(), wakeMessageTemplates);
		}
	}
	
	/**
	 * Display contents of the JobExecutionContext and StepExecution context. Handy for debugging.
	 * @param context
	 */
	private void logContexts(ChunkContext context){
		ExecutionContext executionContext = context.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
		ExecutionContext stepExecutionContext = context.getStepContext().getStepExecution().getExecutionContext();
		for (Entry<String,Object> entry: executionContext.entrySet())
			logger.debug("ExecutionContext : " + entry.getKey() + "=" + entry.getValue().toString());
		for (Entry<String,Object> entry: stepExecutionContext.entrySet())
			logger.debug("StepExecutionContext : " + entry.getKey() + "=" + entry.getValue().toString());
	}
	
	/**
	 * Sets the timeout interval in the step context. If not previously set, the configured initial value is set. If a value is already present
	 * in the context, it is multiplied by two and re-set in the context. If the new value is greater than the maximum specified in configuration then 
	 * the maximum value is set
	 * @param context
	 * @return the current value of the timeoutInterval
	 */
	protected Long exponentiallyIncreaseTimeoutIntervalInContext(ChunkContext context){
		final Integer MULTIPLICATION_FACTOR = 2;
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		Long previousTimeInterval = BatchJobHibernationManager.getWakeTimeInterval(stepExecution);
		Long newTimeInterval;
		if (previousTimeInterval == null){
			newTimeInterval = initialExponentialInterval;
			previousTimeInterval = 0L;
		} else if (previousTimeInterval * MULTIPLICATION_FACTOR > maxExponentialInterval)
			newTimeInterval = maxExponentialInterval;
		else
			newTimeInterval = previousTimeInterval * MULTIPLICATION_FACTOR;
		logger.debug(String.format("Previously set time interval (in StepExecutionContect) was %d ms, setting new time interval to be %d ms", 
				previousTimeInterval, newTimeInterval));
		if (!newTimeInterval.equals(previousTimeInterval))
			BatchJobHibernationManager.setWakeTimeInterval(stepExecution, newTimeInterval);
		return newTimeInterval;
	}
	
	protected void addStatusMessagesToWakeStepToContext(ChunkContext context, Set<WaspStatusMessageTemplate> templates) throws JSONException{
		BatchJobHibernationManager.setWakeMessages(context.getStepContext().getStepExecution(), templates);
	}
	
	protected void addStatusMessagesToAbandonStepToContext(ChunkContext context, Set<WaspStatusMessageTemplate> templates) throws JSONException{
		BatchJobHibernationManager.setAbandonMessages(context.getStepContext().getStepExecution(), templates);
	}
	
	protected boolean wasWokenOnMessage(ChunkContext context){
		ExecutionContext executionContext = context.getStepContext().getStepExecution().getExecutionContext();
		boolean woken = false;
		if (executionContext.containsKey(BatchJobHibernationManager.WOKEN_ON_MESSAGE))
			woken = (boolean) executionContext.get(BatchJobHibernationManager.WOKEN_ON_MESSAGE);
		logger.debug("StepExecutionId=" + context.getStepContext().getStepExecution().getId() + " wasWokenByMessage=" + woken);
		return woken;	
	}
	
	protected boolean wasWokenOnTimeout(ChunkContext context){
		ExecutionContext executionContext = context.getStepContext().getStepExecution().getExecutionContext();
		boolean woken = false;
		if (executionContext.containsKey(BatchJobHibernationManager.WOKEN_ON_TIMEOUT))
			woken = (boolean) executionContext.get(BatchJobHibernationManager.WOKEN_ON_TIMEOUT);
		logger.debug("StepExecutionId=" + context.getStepContext().getStepExecution().getId() + " wasWokenByTimeout=" + woken);
		return woken;	
	}
	
	
}
