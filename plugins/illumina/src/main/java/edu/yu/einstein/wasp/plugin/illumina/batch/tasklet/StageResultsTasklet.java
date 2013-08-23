package edu.yu.einstein.wasp.plugin.illumina.batch.tasklet;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionExponential;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.TaskletRetryException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.SoftwareManager;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.plugin.illumina.software.IlluminaHiseqSequenceRunProcessor;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.software.SoftwarePackage;
import edu.yu.einstein.wasp.util.PropertyHelper;

/**
 * 
 * 
 * @author calder
 * 
 */
@Component
public class StageResultsTasklet extends WaspTasklet {

	private RunService runService;

	private int runId;
	private Run run;

	@Autowired
	private GridHostResolver hostResolver;

	@Autowired
	private IlluminaHiseqSequenceRunProcessor casava;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public StageResultsTasklet() {
		// required by cglib
	}
	
	public StageResultsTasklet(Integer runId) {
		this.runId = runId;
	}

	@Override
	@RetryOnExceptionExponential
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {

		// if the work has already been started, then check to see if it is finished
		// if not, throw an exception that is caught by the repeat policy.
		RepeatStatus repeatStatus = super.execute(contrib, context);
		if (repeatStatus.equals(RepeatStatus.FINISHED))
			return RepeatStatus.FINISHED;
		
		run = runService.getRunById(runId);

		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(casava);
		
		WorkUnit w = new WorkUnit();
		w.setProcessMode(ProcessMode.SINGLE);
		w.setSoftwareDependencies(sd);
		GridWorkService gws = hostResolver.getGridWorkService(w);
		
		String dataDir = gws.getTransportConnection().getConfiguredSetting("illumina.data.dir");
		if (!PropertyHelper.isSet(dataDir))
			throw new GridException("illumina.data.dir is not defined!");
		String stageDir = gws.getTransportConnection().getConfiguredSetting("illumina.data.stage");
		if (!PropertyHelper.isSet(stageDir))
			throw new GridException("illumina.data.stage is not defined!");
		
		w.setWorkingDirectory(dataDir + "/" + run.getName() + "/Unaligned/");
		
		w.setResultsDirectory(stageDir + "/" + run.getName());
		
		w.setCommand("mkdir -p ${WASP_RESULT_DIR}");
		w.addCommand("cp -f *.xml ${WASP_RESULT_DIR}");
		w.addCommand("cp -f ../RunInfo.xml ${WASP_RESULT_DIR}");
		w.addCommand("cp -f ../Data/Intensities/BaseCalls/SampleSheet.csv ${WASP_RESULT_DIR}");
		w.addCommand("cp -f *.txt ${WASP_RESULT_DIR}");
		w.addCommand("cp -fR Project_* ${WASP_RESULT_DIR}");
		w.addCommand("cp -fR Undetermined_indices ${WASP_RESULT_DIR}");
		w.addCommand("mkdir -p ${WASP_RESULT_DIR}/reports/FWHM");
		w.addCommand("mkdir -p ${WASP_RESULT_DIR}/reports/Intensity");
		w.addCommand("mkdir -p ${WASP_RESULT_DIR}/reports/NumGT30");
		w.addCommand("mkdir -p ${WASP_RESULT_DIR}/reports/ByCycle");
		w.addCommand("cp -f ../Data/reports/{*.xml,*[^@].png} ${WASP_RESULT_DIR}/reports");
		w.addCommand("cp -f ../Data/reports/FWHM/{*.xml,*[^@].png} ${WASP_RESULT_DIR}/reports/FWHM");
		w.addCommand("cp -f ../Data/reports/Intensity/{*.xml,*[^@].png} ${WASP_RESULT_DIR}/reports/Intensity");
		w.addCommand("cp -f ../Data/reports/NumGT30/{*.xml,*[^@].png} ${WASP_RESULT_DIR}/reports/NumGT30");
		w.addCommand("cp -f ../Data/reports/ByCycle/*.png ${WASP_RESULT_DIR}/reports/ByCycle");

		GridResult result = gws.execute(w);
		
		logger.debug("started staging of illumina output: " + result.getUuid());
		
		//place the grid result in the step context
		WaspTasklet.storeStartedResult(context, result);
		
		return RepeatStatus.CONTINUABLE;

	}

	/**
	 * @return the runService
	 */
	public RunService getRunService() {
		return runService;
	}

	/**
	 * @param runService
	 *            the runService to set
	 */
	@Autowired
	public void setRunService(RunService runService) {
		this.runService = runService;
	}

}
