package edu.yu.einstein.wasp.daemon.batch.tasklets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import edu.yu.einstein.wasp.exception.WaspRuntimeException;

/**
 * Placeholder tasklet. Simply executes once and completes immediately unless instructed to sleep or throw an exception.
 * @author asmclellan
 *
 */
public class SkipTasklet implements Tasklet {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private int sleepInMillis = 0;
	
	private boolean dieHorribleDeath = false;
	
	public SkipTasklet() {
		// proxy
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws InterruptedException {
		// do nothing
		StepContext sc = context.getStepContext();
		logger.debug("SkipTasklet: " + sc.getJobName() + ":" + sc.getStepName());
		
		if (sleepInMillis > 0) {
		    logger.debug("Sleeping " + sleepInMillis + " milliseconds prior to skipping");
		    Thread.sleep(sleepInMillis);
		}
		
		if (dieHorribleDeath == true) {
		    String message = "SkipTasklet instructed to die horrible death.  This is useful primarily when running in a test.";
		    logger.warn(message);
		    throw new WaspRuntimeException(message);
		}
		
		return RepeatStatus.FINISHED;
	}

    public int getSleepInMillis() {
        return sleepInMillis;
    }

    public void setSleepInMillis(int sleepInMillis) {
        this.sleepInMillis = sleepInMillis;
    }

    public boolean isDieHorribleDeath() {
        return dieHorribleDeath;
    }

    public void setDieHorribleDeath(boolean dieHorribleDeath) {
        this.dieHorribleDeath = dieHorribleDeath;
    }
}
