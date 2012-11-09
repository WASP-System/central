package edu.yu.einstein.wasp.daemon.batch.tasklets;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * Placeholder tasklet. Simply executes once and completes immediately
 * @author andymac
 *
 */
public class SkipTasklet implements Tasklet {

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		// do nothing
		return RepeatStatus.FINISHED;
	}

}
