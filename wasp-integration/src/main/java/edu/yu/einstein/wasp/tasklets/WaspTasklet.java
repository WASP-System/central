package edu.yu.einstein.wasp.tasklets;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.integration.Message;

import edu.yu.einstein.wasp.messages.WaspStatus;

public abstract class WaspTasklet {

	protected List<Message<WaspStatus>> statusMessageStack;
	
	public WaspTasklet() {
		statusMessageStack = new ArrayList<Message<WaspStatus>>();
	}
	
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
