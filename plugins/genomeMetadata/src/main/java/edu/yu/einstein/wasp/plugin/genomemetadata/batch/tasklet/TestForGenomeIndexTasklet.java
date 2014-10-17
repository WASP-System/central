/**
 * 
 */
package edu.yu.einstein.wasp.plugin.genomemetadata.batch.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
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
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;
import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager.LockType;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;
import edu.yu.einstein.wasp.plugin.genomemetadata.service.GenomeMetadataService;

/**
 * @author calder
 * 
 */
public abstract class TestForGenomeIndexTasklet extends WaspRemotingTasklet {
	
	protected final static Logger logger = LoggerFactory.getLogger(TestForGenomeIndexTasklet.class);
	
	@Autowired
	private GridHostResolver gridHostResolver;
	
	@Autowired
	protected GenomeMetadataService genomeMetadataService;
	
	public TestForGenomeIndexTasklet() {
		
	}
	
	private static final String REMOTE_HOST_KEY = "remotehost";

		
	@Override
	@Transactional("entityManager")
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		Long stepExecutionId = context.getStepContext().getStepExecution().getId();
		if (wasWokenOnTimeout(context)){
			logger.debug("StepExecution id=" + stepExecutionId + " was woken up from hibernation after a timeout.");
			wasHibernationRequested = false;
			BatchJobHibernationManager.unlockJobExecution(context.getStepContext().getStepExecution().getJobExecution(), LockType.WAKE);
		}
		if (!wasHibernationRequested){
			GenomeIndexStatus status = getGenomeIndexStatus(context.getStepContext().getStepExecution());
			
			if (status.isAvailable()) {
				if (status.isCurrentlyAvailable()) {
					logger.debug("genome index is available, continue with alignment");
					RepeatStatus stepRepeatStatus = super.execute(contrib, context);
					if (stepRepeatStatus.equals(RepeatStatus.FINISHED))
						return RepeatStatus.FINISHED;
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
		} else 
			logger.debug("Doing nothing as StepExecution id=" + stepExecutionId + " has already requested hibernation");
		return RepeatStatus.CONTINUABLE;
	}
	
	@Override
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		
		GridResult result = executeWorkUnit(context);
		
		//place the grid result in the step context
		saveGridResult(context, result);
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
		try{
			getRemoteHost(stepExecution.getExecutionContext()); // if not set throws a GridUnresolvableHostException
		} catch (GridUnresolvableHostException e){
			// not yet set so set now
			try {
				logger.trace("test for genome index beforeStep");
				setRemoteHost(stepExecution.getExecutionContext(), configureWorkUnit(stepExecution));
			} catch (Exception e1) {
				e1.printStackTrace();
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
	public abstract GenomeIndexStatus getGenomeIndexStatus(StepExecution stepExecution);
	
	/**
	 * Work unit needs to be configured before execution in order to resolve the host prior to step execution.
	 */
	public abstract WorkUnitGridConfiguration configureWorkUnit(StepExecution stepExecution) throws Exception;
	
	/**
	 * Work unit needs to be set up before execution in order to resolve the host prior to step execution.
	 */
	public abstract WorkUnit buildWorkUnit(StepExecution stepExecution) throws Exception;
	
	/**
	 * Execute a new work unit on the predetermined host
	 * 
	 * @param w
	 * @return
	 * @throws GridUnresolvableHostException
	 * @throws GridException
	 */
	public GridResult executeWorkUnit(ChunkContext context, WorkUnit w) throws GridUnresolvableHostException, GridException {
		ExecutionContext stepExecutionContext = context.getStepContext().getStepExecution().getExecutionContext();
		return gridHostResolver.getGridWorkService(getRemoteHost(stepExecutionContext)).execute(w);
	}
	
	/**
	 * Execute the stored work unit on the predetermined host
	 * @return
	 * @throws Exception 
	 */
	public GridResult executeWorkUnit(ChunkContext context) throws Exception {
		return executeWorkUnit(context, 
				buildWorkUnit( context.getStepContext().getStepExecution() ) 
				);
	}
	
	/**
	 * Get the GridWorkService for the predetermined remote host.
	 * @return
	 * @throws GridUnresolvableHostException
	 */
	public GridWorkService getGridWorkService(ExecutionContext stepExecutionContext) throws GridUnresolvableHostException {
		return gridHostResolver.getGridWorkService(getRemoteHost(stepExecutionContext));
	}
	
	private void setRemoteHost(ExecutionContext stepExecutionContext, WorkUnitGridConfiguration c) throws GridUnresolvableHostException{
		stepExecutionContext.put(REMOTE_HOST_KEY, gridHostResolver.getGridWorkService(c).getTransportConnection().getHostName());
	}
	
	public String getRemoteHost(ExecutionContext stepExecutionContext) throws GridUnresolvableHostException{
		if (stepExecutionContext.containsKey(REMOTE_HOST_KEY))
			return stepExecutionContext.getString(REMOTE_HOST_KEY);
		throw new GridUnresolvableHostException("No remote host name stored in StepExecutionContext");
	}

}
