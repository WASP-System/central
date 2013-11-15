/**
 * 
 */
package edu.yu.einstein.wasp.plugin.illumina.batch.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionFixed;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.plugin.illumina.software.IlluminaHiseqSequenceRunProcessor;
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
	private IlluminaHiseqSequenceRunProcessor casava;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 
	 */
	public SampleSheetTasklet(Integer runId) {
		this.runId = runId;
	}
	
	public SampleSheetTasklet() {
		// proxy
	}

	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	@Override
	@RetryOnExceptionFixed
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws GridException {
		run = runService.getRunById(runId);
		logger.debug("preparing sample sheet for " + run.getName() + ":" + run.getPlatformUnit().getName());
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
