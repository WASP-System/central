package edu.yu.einstein.wasp.daemon.batch.tasklets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;


public abstract class WaspTasklet implements Tasklet {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public abstract RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception;

	/**
	 * Returns a status of RepeatStatus.CONTINUABLE after specified timeout
	 * @param ms
	 * @return
	 */
	protected RepeatStatus delayedRepeatStatusContinuable(Integer ms){
		try {
			Thread.sleep(ms); 
		} catch (InterruptedException e) {
			logger.debug("caught sleeping");
			// do nothing here just proceed to the return
		}
		return RepeatStatus.CONTINUABLE; // we're not done with this step yet
	}
	
}
