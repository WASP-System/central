/**
 * 
 */
package edu.yu.einstein.wasp.plugin.illumina.batch.tasklet;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionFixed;
import edu.yu.einstein.wasp.daemon.batch.tasklets.AbandonMessageHandlingTasklet;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.plugin.illumina.software.IlluminaPlatformSequenceRunProcessor;
import edu.yu.einstein.wasp.service.RunService;

/**
 * @author calder
 *
 */
@Component
public class UpdateRunCompletedTasklet extends AbandonMessageHandlingTasklet {
	
	@Autowired
	private RunService runService;
	
	private int runId;
	private Run run;
	
	@Autowired
	private IlluminaPlatformSequenceRunProcessor casava;

	public UpdateRunCompletedTasklet() {
		
	}
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 
	 */
	public UpdateRunCompletedTasklet(Integer runId) {
		this.runId = runId;
	}

	@Override
	@Transactional("entityManager")
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws GridException {
		run = runService.getRunById(runId);
		logger.debug("Marking run " + run.getName() + ":" + run.getPlatformUnit().getName() + " as completed.");
		run.setFinished(new Date());
		runService.getRunDao().save(run);
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
