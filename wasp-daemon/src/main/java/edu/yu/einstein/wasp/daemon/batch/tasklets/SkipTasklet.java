package edu.yu.einstein.wasp.daemon.batch.tasklets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionFixed;

/**
 * Placeholder tasklet. Simply executes once and completes immediately
 * @author asmclellan
 *
 */
public class SkipTasklet implements Tasklet {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public SkipTasklet() {
		// proxy
	}

	@Override
	@RetryOnExceptionFixed
	public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws InterruptedException {
		// do nothing
		StepContext sc = context.getStepContext();
		logger.debug("SkipTasklet: " + sc.getJobName() + ":" + sc.getStepName());
		
		return RepeatStatus.FINISHED;
	}
}
