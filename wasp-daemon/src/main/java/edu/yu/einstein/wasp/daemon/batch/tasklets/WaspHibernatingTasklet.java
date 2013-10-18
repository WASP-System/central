package edu.yu.einstein.wasp.daemon.batch.tasklets;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.MessageChannel;

import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;

public abstract class WaspHibernatingTasklet extends WaspTasklet implements StepExecutionListener{
	
	protected boolean wasHibernationSuccessfullyRequested = false;
	
	@Autowired
	@Qualifier("wasp.channel.priority.default")
	MessageChannel sendChannel;
	
	protected abstract void requestHibernation(ChunkContext context, Object trigger);
	
	protected boolean wasHibernating(ChunkContext context){
		ExecutionContext executionContext = context.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
		if (!executionContext.containsKey(BatchJobHibernationManager.HIBERNATING))
			return false;
		boolean isHibernating = (boolean) executionContext.get(BatchJobHibernationManager.HIBERNATING);
		logger.debug("StepExecutionId=" + context.getStepContext().getStepExecution().getId() + " isHibernating=" + isHibernating);
		return isHibernating;	
	}
	
	
	protected void setWasHibernatingFlag(ChunkContext context, boolean value){
		ExecutionContext executionContext = context.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
		executionContext.put(BatchJobHibernationManager.HIBERNATING, value);
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
