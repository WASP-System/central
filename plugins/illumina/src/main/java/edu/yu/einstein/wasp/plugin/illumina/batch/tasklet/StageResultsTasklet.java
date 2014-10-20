package edu.yu.einstein.wasp.plugin.illumina.batch.tasklet;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ProcessMode;
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
public class StageResultsTasklet extends WaspRemotingTasklet {

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
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
	    
	    // TODO: this step is sensitive to an existing results folder

		run = runService.getRunById(runId);

		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(casava);
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
		
		c.setProcessMode(ProcessMode.SINGLE);
		c.setSoftwareDependencies(sd);
		GridWorkService gws = hostResolver.getGridWorkService(c);
		
		String dataDir = gws.getTransportConnection().getConfiguredSetting("illumina.data.dir");
		if (!PropertyHelper.isSet(dataDir))
			throw new GridException("illumina.data.dir is not defined!");
		String stageDir = gws.getTransportConnection().getConfiguredSetting("illumina.data.stage");
		if (!PropertyHelper.isSet(stageDir))
			throw new GridException("illumina.data.stage is not defined!");
		
		c.setWorkingDirectory(dataDir + "/" + run.getName() + "/");
		
		c.setResultsDirectory(stageDir + "/" + run.getName());
		
		WorkUnit w = new WorkUnit(c);
		w.setCommand("rm -rf ${WASP_RESULT_DIR}/Project_WASP");
		w.setCommand("mkdir -vp ${WASP_RESULT_DIR}/Project_WASP");
		
		// copy files from single barcode truseq run
		w.addCommand("if [ -e " + IlluminaHiseqSequenceRunProcessor.SINGLE_INDEX_OUTPUT_FOLDER_NAME + "/DemultiplexConfig.xml ]; then");
		w.addCommand("mkdir -vp ${WASP_RESULT_DIR}/" + IlluminaHiseqSequenceRunProcessor.SINGLE_INDEX_OUTPUT_FOLDER_NAME);
		w.addCommand("cp -vf " + IlluminaHiseqSequenceRunProcessor.SINGLE_INDEX_OUTPUT_FOLDER_NAME + "/*.xml ${WASP_RESULT_DIR}/" + 
		        IlluminaHiseqSequenceRunProcessor.SINGLE_INDEX_OUTPUT_FOLDER_NAME + "/");
		w.addCommand("cp -vf " + IlluminaHiseqSequenceRunProcessor.SINGLE_INDEX_OUTPUT_FOLDER_NAME + "/*.txt ${WASP_RESULT_DIR}/" + 
		        IlluminaHiseqSequenceRunProcessor.SINGLE_INDEX_OUTPUT_FOLDER_NAME + "/");
		w.addCommand("cp -vfR " + IlluminaHiseqSequenceRunProcessor.SINGLE_INDEX_OUTPUT_FOLDER_NAME + "/Project_WASP/* ${WASP_RESULT_DIR}/Project_WASP/");
		w.addCommand("if [ -e " + IlluminaHiseqSequenceRunProcessor.SINGLE_INDEX_OUTPUT_FOLDER_NAME + "/Undetermined_indices ]; then\n" +
		        "  cp -vfR " + IlluminaHiseqSequenceRunProcessor.SINGLE_INDEX_OUTPUT_FOLDER_NAME + "/Undetermined_indices ${WASP_RESULT_DIR}/" + 
		            IlluminaHiseqSequenceRunProcessor.SINGLE_INDEX_OUTPUT_FOLDER_NAME + "/\n" +
		        "fi");
		w.addCommand("fi");
		
		// copy files from dual barcode truseq run
		w.addCommand("if [ -e " + IlluminaHiseqSequenceRunProcessor.DUAL_INDEX_OUTPUT_FOLDER_NAME + "/DemultiplexConfig.xml ]; then");
		w.addCommand("mkdir -vp ${WASP_RESULT_DIR}/" + IlluminaHiseqSequenceRunProcessor.DUAL_INDEX_OUTPUT_FOLDER_NAME);
		w.addCommand("cp -vf " + IlluminaHiseqSequenceRunProcessor.DUAL_INDEX_OUTPUT_FOLDER_NAME + "/*.xml ${WASP_RESULT_DIR}/" + 
		        IlluminaHiseqSequenceRunProcessor.DUAL_INDEX_OUTPUT_FOLDER_NAME + "/");
		w.addCommand("cp -vf " + IlluminaHiseqSequenceRunProcessor.DUAL_INDEX_OUTPUT_FOLDER_NAME + "/*.txt ${WASP_RESULT_DIR}/" + 
		        IlluminaHiseqSequenceRunProcessor.DUAL_INDEX_OUTPUT_FOLDER_NAME + "/");
		w.addCommand("cp -vfR " + IlluminaHiseqSequenceRunProcessor.DUAL_INDEX_OUTPUT_FOLDER_NAME + "/Project_WASP/* ${WASP_RESULT_DIR}/Project_WASP/");
		w.addCommand("if [ -e " + IlluminaHiseqSequenceRunProcessor.DUAL_INDEX_OUTPUT_FOLDER_NAME + "/Undetermined_indices ]; then\n" +
		        "  cp -vfR " + IlluminaHiseqSequenceRunProcessor.DUAL_INDEX_OUTPUT_FOLDER_NAME + "/Undetermined_indices ${WASP_RESULT_DIR}/" + 
		            IlluminaHiseqSequenceRunProcessor.DUAL_INDEX_OUTPUT_FOLDER_NAME + "/\n" +
		        "fi");
		w.addCommand("fi");
		
		// copy run-specific files
		w.addCommand("cp -vf RunInfo.xml ${WASP_RESULT_DIR}");
		w.addCommand("cp -vf Data/Intensities/BaseCalls/*SampleSheet.csv ${WASP_RESULT_DIR}");
		w.addCommand("mkdir -vp ${WASP_RESULT_DIR}/reports/FWHM");
		w.addCommand("mkdir -vp ${WASP_RESULT_DIR}/reports/Intensity");
		w.addCommand("mkdir -vp ${WASP_RESULT_DIR}/reports/NumGT30");
		w.addCommand("mkdir -vp ${WASP_RESULT_DIR}/reports/ByCycle");
		w.addCommand("cp -vf ./Data/wasp-reports/*[^@].png ${WASP_RESULT_DIR}/reports");
		w.addCommand("cp -vf ./Data/wasp-reports/FWHM/*[^@].png ${WASP_RESULT_DIR}/reports/FWHM");
		w.addCommand("cp -vf ./Data/wasp-reports/Intensity/*[^@].png ${WASP_RESULT_DIR}/reports/Intensity");
		w.addCommand("cp -vf ./Data/wasp-reports/NumGT30/*[^@].png ${WASP_RESULT_DIR}/reports/NumGT30");
		w.addCommand("cp -vf ./Data/wasp-reports/ByCycle/*.png ${WASP_RESULT_DIR}/reports/ByCycle");

		GridResult result = gws.execute(w);
		
		logger.debug("started staging of illumina output: " + result.getUuid());
		
		//place the grid result in the step context
		saveGridResult(context, result);

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
