/**
 * 
 */
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
import edu.yu.einstein.wasp.plugin.illumina.software.SavR;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.software.SoftwarePackage;
import edu.yu.einstein.wasp.util.PropertyHelper;

/**
 * Determine if the Illumina pipeline is already running.  If not, create a new Work unit and monitor.  Requires 
 * Backoff and Retry annotation.
 * 
 * @author calder
 *
 */
@Component
public class ProcessSAVTasklet extends WaspRemotingTasklet {
	
	private RunService runService;

	private int runId;
	private Run run;
	
	@Autowired
	private GridHostResolver hostResolver;
	
	@Autowired
	private IlluminaHiseqSequenceRunProcessor casava;
	
	@Autowired
	private SavR savR;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public ProcessSAVTasklet() {
		// required for AOP/CGLIB/Batch/Annotations/BeanIdentity
	}

	/**
	 * 
	 */
	public ProcessSAVTasklet(Integer runId) {
		this.runId = runId;
	}

	@Override
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		
		run = runService.getRunById(runId);
		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(casava);
		sd.add(savR);
		
		
		// creating a work unit this way sets the runID from the jobparameters
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
		c.setProcessMode(ProcessMode.FIXED);
		c.setSoftwareDependencies(sd);
		GridWorkService gws = hostResolver.getGridWorkService(c);
		Integer procs = 1;
		c.setProcessorRequirements(procs);
		String dataDir = gws.getTransportConnection().getConfiguredSetting("illumina.data.dir");
		if (!PropertyHelper.isSet(dataDir))
			throw new GridException("illumina.data.dir is not defined!");
		
		c.setWorkingDirectory(dataDir + "/" + run.getName() );
		
		c.setResultsDirectory(dataDir + "/" + run.getName() );
		
		WorkUnit w = new WorkUnit(c);
		w.setCommand(savR.getSavR());

		GridResult result = gws.execute(w);
		
		logger.debug("started savR processing: " + result.getUuid());
		
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
	 * @param runService the runService to set
	 */
	@Autowired
	public void setRunService(RunService runService) {
		this.runService = runService;
	}

}
