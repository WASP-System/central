/**
 * 
 */
package edu.yu.einstein.wasp.daemon.batch.tasklets.illumina;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet;
import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.mps.illumina.IlluminaSequenceRunProcessor;
import edu.yu.einstein.wasp.service.RunService;

/**
 * @author calder
 *
 */
@Component
public class SampleSheetTasklet extends WaspTasklet {
	
	private RunService runService;
	
	private int runId;
	private Run run;
	
	@Autowired
	private IlluminaSequenceRunProcessor casava;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 
	 */
	public SampleSheetTasklet(int runId) {
		this.runId = runId;
	}

	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws GridUnresolvableHostException, GridAccessException, GridExecutionException {
		run = runService.getRunById(runId);
		logger.debug("preparing sample sheet for " + run.getName());
		casava.doSampleSheet(run);
		return RepeatStatus.FINISHED;
	}

	/**
	 * @return the runService
	 */
	public RunService getRunService() {
		return runService;
	}

	/**
	 * @param runService the runService to set
	 */
	@Autowired
	public void setRunService(RunService runService) {
		this.runService = runService;
	}

}
