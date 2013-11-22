package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.ResourceLockException;
import edu.yu.einstein.wasp.exception.WaspBatchJobExecutionReadinessException;
import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;

@Transactional
public abstract class WaspHibernatingTasklet implements Tasklet{
	
	// In the split-step scenario: 
	// The last WaspHibernatingTasklet in the split-step of this job to call requestHibernation() gets control by the method setting 
	// HIBERNATION_REQUESTED = true in the JobExecutionContext. All other steps in a split may also call requestHibernation() which will set 
	// HIBERNATION_REQUESTED = true in their StepExecutionContext but will not proceed to request hibernation. 
	// Only if all active steps have HIBERNATION_REQUESTED set to true then the controlling step will ask the hibernationManager to hibernate the job.
	
	// NOTE: due to transactional behavior, execution contexts are not visible in the database outside of this job thread until job stopped / completed
	// so care is required knowing what is visible where. Any additions to job/step execution contexts here will be visible immediately job-wide from within 
	// tasklet code.	
	
	private static final Logger logger = LoggerFactory.getLogger(WaspHibernatingTasklet.class);
	
	protected boolean wasHibernationRequested = false;
	
	protected boolean wasHibernationRequestGranted = false;
	
	@Autowired
	@Value("${wasp.hibernation.retry.exponential.initialInterval:5000}")
	private Long initialExponentialInterval;
	
	@Autowired
	@Value("${wasp.hibernation.retry.exponential.maxInterval:3600000}")
	private Long maxExponentialInterval;
	
	@Autowired
	private BatchJobHibernationManager hibernationManager;
	
	/**
	 * Request hibernation of this jobExecution
	 * @param context
	 */
	protected void requestHibernation(ChunkContext context){
		StepContext stepContext = context.getStepContext();
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		JobExecution jobExecution = stepExecution.getJobExecution();
		Long jobExecutionId = jobExecution.getId();
		Long stepExecutionId = stepExecution.getId();
		BatchStatus status = jobExecution.getStatus();
		if (!status.equals(BatchStatus.STARTED)){
			logger.debug("Request made by StepExecution id=" + stepExecution.getId() + " to hibernate but not going to because JobExecution id=" + 
					jobExecution.getId() + " is not STARTED: status=" + status);
			return;
		} 
		wasHibernationRequested = true;
		logContexts(context);
		if (!isHibernationRequestedForStep(stepExecution))
			setHibernationRequestedForStep(stepExecution, true);
		if (isHibernationRequestedForJob(jobExecution)){
			// another step execution has already declared itself as handling hibernation so let it do the rest
			logger.debug("StepExecution id=" + stepExecutionId + 
					" will not proceed to request hibernation of JobExecution id=" + jobExecutionId + 
					" because another step has already initiated hibernation");
		} else if (isAllActiveJobStepsRequestingHibernation(stepExecution)){
			try{
				BatchJobHibernationManager.addJobExecutionIdLockedForHibernating(jobExecutionId);
				setHibernationRequestedForJob(jobExecution, true);
				// we are ready to hibernate so request it now
				logger.info("Going to hibernate job " + stepContext.getJobName() + 
						" (JobExecution id=" + jobExecutionId + ") from step " + 
						stepContext.getStepName() + " (step id=" + stepExecutionId + ")");
				doHibernate(stepExecution);
				wasHibernationRequestGranted = true;
			} catch (ResourceLockException e){
				logger.info("Not going to hibernate job " + stepContext.getJobName() + 
						" (JobExecution id=" + jobExecutionId + ") from step " + 
						stepContext.getStepName() + " (step id=" + stepExecutionId + ") as already locked by another StepExecution");
			} catch (WaspBatchJobExecutionReadinessException e) {
				logger.info("Not going to hibernate job " + stepContext.getJobName() + 
						" (JobExecution id=" + jobExecutionId + ") from step " + 
						stepContext.getStepName() + " (step id=" + stepExecutionId + "): " + e.getLocalizedMessage());
			}
		} else { 
			logger.debug("StepExecution id=" + stepExecutionId + 
					" has requested that it wishes to hibernate but other running steps are preventing hibernation of the JobExecution id=" + 
					jobExecutionId + " at this time");
		}
	}
	
	private boolean isAllActiveJobStepsRequestingHibernation(StepExecution stepExecution){
		JobExecution jobExecution = stepExecution.getJobExecution();
		for (StepExecution se : jobExecution.getStepExecutions()){
			if (se.getStatus().equals(BatchStatus.STARTED) && !isHibernationRequestedForStep(se)){
				logger.debug("JobExecution id=" + jobExecution.getId() + ", StepExecution id=" + se.getId() + 
						" contains active steps which have not requested hibernation.");
				return false;
			}
		}
		logger.debug("JobExecution id=" + jobExecution.getId() + 
				" contains no active steps that have not requested hibernation");
		return true;
	}
	
	private void doHibernate(StepExecution stepExecution) throws WaspBatchJobExecutionReadinessException{
		Long requestingStepExecutionId = stepExecution.getId();
		JobExecution jobExecution = stepExecution.getJobExecution();
		Long jobExecutionId = jobExecution.getId();
		logger.info("Hibernation of JobExecution=" + jobExecutionId + " triggered by StepExecution id=" + stepExecution.getId());
		// register all wake triggers with hibernation manger
		for (StepExecution se : jobExecution.getStepExecutions()){
			if (!se.getStatus().equals(BatchStatus.STARTED))
				continue; // not a currently active StepExecution so ignore
			Long stepExecutionId = se.getId();
			try{
				Set<WaspStatusMessageTemplate> wakeMessages = BatchJobHibernationManager.getWakeMessagesFromStepExecutionContext(se);
				if (!wakeMessages.isEmpty())
					hibernationManager.addMessageTemplatesForWakingJobStep(jobExecutionId, stepExecutionId, wakeMessages);
			} catch (JSONException e) {
				logger.warn("Unable to get Wake Messages for JobExecution id=" + jobExecutionId + ", from StepExecution id=" + stepExecutionId + ": " + 
						e.getLocalizedMessage());
			}
			try{
				Set<WaspStatusMessageTemplate> abandonMessages = BatchJobHibernationManager.getAbandonMessagesForStep(se);
				if (!abandonMessages.isEmpty())
					hibernationManager.addMessageTemplatesForAbandoningJobStep(jobExecutionId, stepExecutionId, abandonMessages);
			} catch (JSONException e) {
				logger.warn("Unable to get Abandon Messages for JobExecution id=" + jobExecutionId + ", from StepExecution id=" + stepExecutionId + ": " + 
						e.getLocalizedMessage());
			}
			Long timeInterval = BatchJobHibernationManager.getWakeTimeInterval(se);
			if (timeInterval != null)
				hibernationManager.addTimeIntervalForJobStep(jobExecutionId, stepExecutionId, timeInterval);
			
		}
		
		// request hibernation
		hibernationManager.processHibernateRequest(jobExecution.getId(),requestingStepExecutionId);
	}
	
	protected void setHibernationRequestedForJob(JobExecution jobExecution, boolean isRequested){
		ExecutionContext executionContext = jobExecution.getExecutionContext();
		executionContext.put(BatchJobHibernationManager.HIBERNATION_REQUESTED, isRequested);
	}
	
	protected void setHibernationRequestedForStep(StepExecution stepExecution, boolean isRequested){
		ExecutionContext executionContext = stepExecution.getExecutionContext();
		executionContext.put(BatchJobHibernationManager.HIBERNATION_REQUESTED, isRequested);
	}
	
	protected boolean isHibernationRequestedForJob(JobExecution jobExecution){
		return BatchJobHibernationManager.isJobExecutionIdLockedForHibernating(jobExecution.getId());
	}
	
	protected boolean isHibernationRequestedForStep(StepExecution stepExecution){
		ExecutionContext executionContext = stepExecution.getExecutionContext();
		return executionContext.containsKey(BatchJobHibernationManager.HIBERNATION_REQUESTED) && 
				(boolean) executionContext.get(BatchJobHibernationManager.HIBERNATION_REQUESTED);
	}
	
	/**
	 * Display contents of the JobExecutionContext and StepExecution context. Handy for debugging.
	 * @param context
	 */
	
	private void logContexts(ChunkContext context){
		ExecutionContext executionContext = context.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
		ExecutionContext stepExecutionContext = context.getStepContext().getStepExecution().getExecutionContext();
		for (Entry<String,Object> entry: executionContext.entrySet())
			logger.trace("ExecutionContext : " + entry.getKey() + "=" + entry.getValue().toString());
		for (Entry<String,Object> entry: stepExecutionContext.entrySet())
			logger.trace("StepExecutionContext : " + entry.getKey() + "=" + entry.getValue().toString());
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
		BatchJobHibernationManager.setWakeMessagesInStepExecutionContext(context.getStepContext().getStepExecution(), templates);
	}
	
	protected void addStatusMessagesToAbandonStepToContext(ChunkContext context, Set<WaspStatusMessageTemplate> templates) throws JSONException{
		BatchJobHibernationManager.setAbandonMessagesForStep(context.getStepContext().getStepExecution(), templates);
	}
	
	protected Set<WaspStatusMessageTemplate> getStatusMessagesToWakeStepFromContext(ChunkContext context) throws JSONException{
		return BatchJobHibernationManager.getWakeMessagesFromStepExecutionContext(context.getStepContext().getStepExecution());
	}
	
	protected Set<WaspStatusMessageTemplate> getStatusMessagesToAbandonStepFromContext(ChunkContext context) throws JSONException{
		return BatchJobHibernationManager.getAbandonMessagesForStep(context.getStepContext().getStepExecution());
	}
	
	protected boolean wasWokenOnMessage(ChunkContext context){
		ExecutionContext executionContext = context.getStepContext().getStepExecution().getExecutionContext();
		return (executionContext.containsKey(BatchJobHibernationManager.WOKEN_ON_MESSAGE_STATUS));
	}
	
	protected WaspStatus getWokenOnMessageStatus(StepExecution stepExecution){
		ExecutionContext executionContext = stepExecution.getExecutionContext();
		WaspStatus status = WaspStatus.UNKNOWN;
		if (executionContext.containsKey(BatchJobHibernationManager.WOKEN_ON_MESSAGE_STATUS))
			status = (WaspStatus) executionContext.get(BatchJobHibernationManager.WOKEN_ON_MESSAGE_STATUS);
		logger.debug("StepExecutionId=" + stepExecution.getId() + " was woken with WaspStatus=" + status);
		return status;	
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
