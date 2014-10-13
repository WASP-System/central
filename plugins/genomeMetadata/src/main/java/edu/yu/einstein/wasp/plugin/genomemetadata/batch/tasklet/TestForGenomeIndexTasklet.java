/**
 * 
 */
package edu.yu.einstein.wasp.plugin.genomemetadata.batch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;
import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager.LockType;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;
import edu.yu.einstein.wasp.plugin.genomemetadata.service.GenomeMetadataService;

/**
 * @author calder
 * 
 */
public abstract class TestForGenomeIndexTasklet extends WaspRemotingTasklet {
	
	@Autowired
	private GridHostResolver gridHostResolver;
	
	@Autowired
	protected GenomeMetadataService genomeMetadataService;
	
	public TestForGenomeIndexTasklet() {
		//
	}
	
	private WorkUnit w;
	private String remoteHost;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void doExecute(ChunkContext context) throws Exception;
	
	@Override
	@Transactional("entityManager")
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
	 * {@inheritDoc}
	 * 
	 * TestForGenomeIndexTasklet requires that the host be resolved before the step executes.
	 * Classes that override this method must call to super.beforeStep(stepExecution).
	 */
	@Override
	@Transactional("entityManager")
	public void beforeStep(StepExecution stepExecution){
		if (w == null) {
			logger.trace("test for genome index beforeStep");
			
			try {
				w = this.prepareWorkUnit();
				remoteHost = gridHostResolver.getGridWorkService(w).getTransportConnection().getHostName();
			} catch (Exception e) {
				e.printStackTrace();
				String message = "Unable to determine appropriate host for BWA alignment: " + e.getLocalizedMessage();
				logger.error(message);
				throw new WaspRuntimeException(message);
			}
		}
		super.beforeStep(stepExecution);
	}

	/**
	 * Needs to be set by the aligner tasklet, including working out the destination host where the job will go.
	 * @return
	 */
	public abstract GenomeIndexStatus getGenomeIndexStatus();
	
	/**
	 * Work unit needs to be prepared before execution in order to resolve the host prior to step execution.
	 */
	public abstract WorkUnit prepareWorkUnit() throws Exception;
	
	/**
	 * Execute a new work unit on the predetermined host
	 * 
	 * @param w
	 * @return
	 * @throws GridUnresolvableHostException
	 * @throws GridException
	 */
	public GridResult executeWorkUnit(WorkUnit w) throws GridUnresolvableHostException, GridException {
		return gridHostResolver.getGridWorkService(remoteHost).execute(w);
	}
	
	/**
	 * Execute the stored work unit on the predetermined host
	 * @return
	 * @throws GridUnresolvableHostException
	 * @throws GridException
	 */
	public GridResult executeWorkUnit() throws GridUnresolvableHostException, GridException {
		return gridHostResolver.getGridWorkService(remoteHost).execute(this.w);
	}
	
	/**
	 * Get the GridWorkService for the predetermined remote host.
	 * @return
	 * @throws GridUnresolvableHostException
	 */
	public GridWorkService getGridWorkService() throws GridUnresolvableHostException {
		return gridHostResolver.getGridWorkService(remoteHost);
	}

}
