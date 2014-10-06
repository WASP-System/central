/**
 * 
 */
package edu.yu.einstein.wasp.plugin.genomemetadata.batch.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;
import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager.LockType;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;

/**
 * @author calder
 * 
 */
public abstract class TestForGenomeIndexTasklet extends WaspRemotingTasklet {
	
	public TestForGenomeIndexTasklet() {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void doExecute(ChunkContext context) throws Exception;
	
	@Override
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		if (wasWokenOnTimeout(context)){
			logger.trace("Woken on timeout");
			wasHibernationRequested = false;
			BatchJobHibernationManager.unlockJobExecution(context.getStepContext().getStepExecution().getJobExecution(), LockType.WAKE);
		}
		
		GenomeIndexStatus status = getGenomeIndexStatus();
		
		if (status.isAvailable()) {
			if (status.isCurrentlyAvailable()) {
				logger.debug("genome index is available, continue with alignment");
				super.execute(contrib, context);
			}
		} else {
			String mess = "genome not available: " + status.toString() + " : " + status.getMessage();
			logger.error(mess);
			throw new WaspException(mess);
		}
		logger.debug("genome index is currently being built, going to request hibernation before alignment begins");
		Long timeoutInterval = exponentiallyIncreaseTimeoutIntervalInContext(context);
		logger.trace("Going to request hibernation for " + timeoutInterval + " ms");
		addStatusMessagesToAbandonStepToContext(context, abandonTemplates);
		requestHibernation(context);
		return RepeatStatus.CONTINUABLE;
	}

	/**
	 * Needs to be set by the aligner tasklet, including working out the destination host where the job will go.
	 * @return
	 */
	public abstract GenomeIndexStatus getGenomeIndexStatus();

}
