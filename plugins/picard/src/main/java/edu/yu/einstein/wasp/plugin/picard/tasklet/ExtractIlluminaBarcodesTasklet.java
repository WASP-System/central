/**
 * Tasklet to run the Picard tool "ExtractIlluminaBarcodes" 
 */
package edu.yu.einstein.wasp.plugin.picard.tasklet;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ProcessMode;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.plugin.illumina.service.WaspIlluminaService;
import edu.yu.einstein.wasp.plugin.illumina.software.IlluminaHiseqSequenceRunProcessor;
import edu.yu.einstein.wasp.plugin.picard.software.Picard;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;
import edu.yu.einstein.wasp.util.PropertyHelper;

/**
 * @author calder
 *
 */
public class ExtractIlluminaBarcodesTasklet extends WaspRemotingTasklet {
	
	@Autowired
	private RunService runService;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private GridHostResolver hostResolver;
	
	@Autowired
	private IlluminaHiseqSequenceRunProcessor casava;
	
	@Autowired
	private WaspIlluminaService illuminaService;
	
	@Autowired
	@Qualifier("picard")
	private Picard picard;
	
	private Integer runId;
	private Run run;

	/**
	 * @throws WaspException 
	 * 
	 */
	public ExtractIlluminaBarcodesTasklet(Integer runId) throws WaspException {
		logger.debug("ExtractIlluminaBarcodesTasklet initiated with runId " + runId);
		this.runId = runId;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public GridResult doExecute(ChunkContext context) throws Exception {
		
		// Document runInfo = illuminaService.getIlluminaRunXml(run);
		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(casava);
		sd.add(picard);
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
		
		c.setSoftwareDependencies(sd);
		c.setProcessMode(ProcessMode.MAX);
		c.setMode(ExecutionMode.PROCESS);
		
		GridWorkService gws = hostResolver.getGridWorkService(c);
		
		String dataDir = gws.getTransportConnection().getConfiguredSetting("illumina.data.dir");
		if (!PropertyHelper.isSet(dataDir)) {
			String mess = "Unable to determine illumina.data.stage for host: " + gws.getTransportConnection().getHostName();
			logger.error(mess);
			throw new WaspException(mess);
		}
		
		String runFolder = dataDir + "/" + run.getName(); 
		
		c.setWorkingDirectory(runFolder);
		c.setResultsDirectory(runFolder);
		
		WorkUnit w = new WorkUnit(c);
		w.setSecureResults(true); // 
		w.setCommand(picard.getExtractIlluminaBarcodesCmd(run));
		
		
		return hostResolver.execute(w);
		

	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	@Transactional("entityManager")
	public void doPreFinish(ChunkContext context) throws Exception {
		logger.trace("doPreFinish");
		GridResult result = (GridResult) context.getStepContext().getStepExecution().getExecutionContext().get(GridResult.GRID_RESULT_KEY);
		try {
			picard.registerBarcodeMetadata(run, result);
		} catch (WaspException e) {
			String message = "Problem registering barcode scanning results: " + e.getLocalizedMessage();
			logger.error(message);
			throw new WaspRuntimeException(message);
		}
		super.doPreFinish(context);
	}
	
	/** 
	 * {@inheritDoc}
	 * @throws WaspException 
	 * @throws MetadataException 
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		logger.trace("afterStep");
		return super.afterStep(stepExecution);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		logger.trace("beforeStep");
		this.run = runService.getRunById(this.runId);
		logger.trace("going to operate on run " + run.getName());
		super.beforeStep(stepExecution);
	}

	@Override
	public void doCleanupBeforeRestart(StepExecution stepExecution) throws Exception {
		// TODO Auto-generated method stub
		
	}
	

}
