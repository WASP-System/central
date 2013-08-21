/**
 * 
 */
package edu.yu.einstein.wasp.daemon.batch.tasklets.illumina;

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
import edu.yu.einstein.wasp.mps.illumina.IlluminaHiseqSequenceRunProcessor;
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
public class PipelineTasklet extends WaspTasklet {
	
	private RunService runService;

	private int runId;
	private Run run;
	
	@Autowired
	private GridHostResolver hostResolver;
	
	@Autowired
	private IlluminaHiseqSequenceRunProcessor casava;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public PipelineTasklet() {
		// required for AOP/CGLIB/Batch/Annotations/BeanIdentity
	}

	/**
	 * 
	 */
	public PipelineTasklet(Integer runId) {
		this.runId = runId;
	}

	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	@Override
	@RetryOnExceptionExponential
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		
		// if the work has already been started, then check to see if it is finished
		// if not, throw an exception that is caught by the repeat policy.
		RepeatStatus repeatStatus = super.execute(contrib, context);
		if (repeatStatus.equals(RepeatStatus.FINISHED))
			return RepeatStatus.FINISHED;
		
		// this is our first try
		// TODO: check to see if the Makefile exists already (already configured and re-run because of grid exception).
		
		run = runService.getRunById(runId);
		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(casava);
		
		
		// creating a work unit this way sets the runID from the jobparameters
		WorkUnit w = new WorkUnit();
		w.setProcessMode(ProcessMode.FIXED);
		w.setSoftwareDependencies(sd);
		GridWorkService gws = hostResolver.getGridWorkService(w);
		SoftwareManager sm = gws.getTransportConnection().getSoftwareManager();
		String p = sm.getConfiguredSetting("casava.env.processors");
		Integer procs = 1;
		if (PropertyHelper.isSet(p)) {
			procs = new Integer(p);
		}
		w.setProcessorRequirements(procs);
		String dataDir = gws.getTransportConnection().getConfiguredSetting("illumina.data.dir");
		if (!PropertyHelper.isSet(dataDir))
			throw new GridException("illumina.data.dir is not defined!");
		
		w.setWorkingDirectory(dataDir + "/" + run.getName() 
				+ "/Data/Intensities/BaseCalls/" );
		
		w.setResultsDirectory(dataDir + "/" + run.getName() + "/Unaligned");
		
		w.setCommand(getConfigureBclToFastqString(sm, procs));

		GridResult result = gws.execute(w);
		
		logger.debug("started illumina pipeline: " + result.getUuid());
		
		//place the grid result in the step context
		storeStartedResult(context, result);
		
		return RepeatStatus.CONTINUABLE;
	}
	
	private String getConfigureBclToFastqString(SoftwareManager sm, int proc) {
		String failed = sm.getConfiguredSetting("casava.with-failed-reads");
		String mismatches = sm.getConfiguredSetting("casava.mismatches");
		String missingStats = sm.getConfiguredSetting("casava.ignore-missing-stats");
		String missingBcl = sm.getConfiguredSetting("casava.ignore-missing-bcl");
		String missingControl = sm.getConfiguredSetting("casava.ignore-missing-control");
		String fastqNclusters = sm.getConfiguredSetting("casava.fastq-cluster-count");
		
		String retval = "if [ ! -e wasp_begin.txt ]; then\n";
		
		retval+=" loc=\"_pos.txt\"\n" + 
				" if [ -e ../L001/s_1_1101.clocs ]; then\n" +
				"  loc=.clocs\n" +
				" elif [ -e ../L001/s_1_1101.locs ]; then\n" +
				"  loc=.locs\n" +
				" fi\n\n";
		
		retval += " configureBclToFastq.pl --force --positions-format ${loc} ";
		
		if (PropertyHelper.isSet(failed) && failed == "true")
			retval += "--with-failed-reads ";
		if (PropertyHelper.isSet(mismatches)) {
			int mm = new Integer(mismatches).intValue();
			if (mm == 0 || mm == 1) 
				retval += "--mismatches " + mm + " ";
		}
		if (PropertyHelper.isSet(missingStats) && missingStats == "true")
			retval += "--ignore-missing-stats ";
		if (PropertyHelper.isSet(missingBcl) && missingBcl == "true")
			retval += "--ignore-missing-bcl ";
		if (PropertyHelper.isSet(missingControl) && missingControl == "true")
			retval += "--ignore-missing-control ";
		if (PropertyHelper.isSet(fastqNclusters)) {
			int fqc = new Integer(fastqNclusters).intValue();
			retval += "--fastq-cluster-count " + fqc;
		}
		
		retval += "\n\n touch wasp_begin.txt\n\n";
		
		retval += " cd ../../../Unaligned && make -j ${threads} \n\nfi\n\n";

		return retval;
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
