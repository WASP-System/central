package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;

public abstract class WaspHibernatingTasklet extends WaspTasklet implements StepExecutionListener{
	
	protected boolean wasHibernationRequested = false;
	
	@Autowired
	private BatchJobHibernationManager hibernationManager;
	
	protected void requestHibernation(ChunkContext context, Object trigger){
		// NOTE: due to transactional behavior, execution contexts are not visible in the database outside of this job thread until job stopped / completed
		// so care is required knowing what is visible where. Any additions to job execution context here will be visible immediately job-wide.
		if (!Collection.class.isInstance(trigger))
			throw new ClassCastException("Expect trigger to be of base type Collection<?> but instead it is of type: " + trigger.getClass().getName());
		// Best practice to fill messageTemplates this way to ensure all items in collection are of correct type
		// By doing it this way a ClassCastException exception will be thrown if an object in the collection is of the wrong type
		Set<WaspStatusMessageTemplate> messageTemplates = new HashSet<>();
		for (Object o: (Collection<?>) trigger)
			messageTemplates.add((WaspStatusMessageTemplate) o); 
		ExecutionContext executionContext = context.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
		ExecutionContext stepExecutionContext = context.getStepContext().getStepExecution().getExecutionContext();
		for (Entry<String,Object> entry: executionContext.entrySet())
			logger.debug("ExecutionContext : " + entry.getKey() + "=" + entry.getValue().toString());
		for (Entry<String,Object> entry: stepExecutionContext.entrySet())
			logger.debug("StepExecutionContext : " + entry.getKey() + "=" + entry.getValue().toString());
		
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
			hibernationManager.addMessageTemplatesForJobStep(context.getStepContext().getStepExecution().getJobExecutionId(), 
					context.getStepContext().getStepExecution().getId(), messageTemplates);
			return;
		} else {
			wasHibernationRequested = true;
			hibernationManager.processHibernateRequest(stepContext.getStepExecution().getJobExecutionId(), stepContext.getStepExecution().getId(), messageTemplates);
		}
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
