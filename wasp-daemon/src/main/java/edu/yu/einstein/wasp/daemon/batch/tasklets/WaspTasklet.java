package edu.yu.einstein.wasp.daemon.batch.tasklets;

import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;


public abstract class WaspTasklet implements Tasklet{
	
	
	/**
	 * Returns a status of RepeatStatus.CONTINUABLE after specified timeout
	 * @param ms
	 * @return
	 */
	protected RepeatStatus delayedRepeatStatusContinuable(Integer ms){
		try {
			Thread.sleep(ms); 
		} catch (InterruptedException e) {
			// do nothing here just proceed to the return
		}
		return RepeatStatus.CONTINUABLE; // we're not done with this step yet
	}
	
}
