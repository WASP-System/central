package edu.yu.einstein.wasp.tasklets.wasprunlibraryflow;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class AbandonRunTasklet implements Tasklet {
	
	private Integer platformUnitId;

	public AbandonRunTasklet(Integer platformUnitId) {
		this.platformUnitId = platformUnitId;
	}

	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
		// TODO: code here
		return null;
	}

}
