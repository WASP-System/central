/**
 * 
 */
package edu.yu.einstein.wasp.plugin.illumina.batch.tasklet;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.SoftwareManager;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ProcessMode;
import edu.yu.einstein.wasp.interfacing.IndexingStrategy;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.plugin.illumina.IlluminaIndexingStrategy;
import edu.yu.einstein.wasp.plugin.illumina.service.WaspIlluminaService;
import edu.yu.einstein.wasp.plugin.illumina.software.IlluminaHiseqSequenceRunProcessor;
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
public class PipelineTasklet extends WaspRemotingTasklet {
	
	private RunService runService;

	private int runId;
	private Run run;
	
	private IndexingStrategy method;
	
	@Autowired
	private GridHostResolver hostResolver;
	
	@Autowired
	private IlluminaHiseqSequenceRunProcessor casava;
	
	@Autowired
	private WaspIlluminaService waspIlluminaService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public PipelineTasklet() {
		// required for AOP/CGLIB/Batch/Annotations/BeanIdentity
	}

	@Value("${wasp.developermode:false}")
	private String devmode;
	
	/**
	 * 
	 */
	public PipelineTasklet(Integer runId, IndexingStrategy method) {
		this.runId = runId;
		if (! method.equals(IlluminaIndexingStrategy.TRUSEQ) && ! method.equals(IlluminaIndexingStrategy.TRUSEQ_DUAL)) {
		    logger.error("unable to run illumina pipeline in mode " + method);
		    throw new WaspRuntimeException("unknown illumina pipeline mode: " + method);
		}
		this.method = method;
		logger.debug("PipelineTasklet with method type " + method);
	}

	
	@Override
	@Transactional("entityManager")
	public GridResult doExecute(ChunkContext context) throws Exception {
		
		// TODO: check to see if the Makefile exists already (already configured and re-run because of grid exception).
		
		run = runService.getRunById(runId);
		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(casava);
		
		String outputFolder;
		String sampleSheetName;
		
		if (method == IlluminaIndexingStrategy.TRUSEQ) {
		    outputFolder = IlluminaHiseqSequenceRunProcessor.SINGLE_INDEX_OUTPUT_FOLDER_NAME;
		    sampleSheetName = IlluminaHiseqSequenceRunProcessor.SINGLE_INDEX_SAMPLE_SHEET_NAME;
		} else {
		    outputFolder = IlluminaHiseqSequenceRunProcessor.DUAL_INDEX_OUTPUT_FOLDER_NAME;
		    sampleSheetName = IlluminaHiseqSequenceRunProcessor.DUAL_INDEX_SAMPLE_SHEET_NAME;
		}
		
		// TODO: handle single and dual situations.
		
		// creating a work unit this way sets the runID from the jobparameters
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
		
		c.setProcessMode(ProcessMode.FIXED);
		c.setSoftwareDependencies(sd);
		GridWorkService gws = hostResolver.getGridWorkService(c);
		SoftwareManager sm = gws.getTransportConnection().getSoftwareManager();
		String p = sm.getConfiguredSetting("casava.env.processors");
		Integer procs = 1;
		if (PropertyHelper.isSet(p)) {
			procs = new Integer(p);
		}
		c.setProcessorRequirements(procs);
		String dataDir = gws.getTransportConnection().getConfiguredSetting("illumina.data.dir");
		if (!PropertyHelper.isSet(dataDir))
			throw new GridException("illumina.data.dir is not defined!");
		
		c.setWorkingDirectory(dataDir + "/" + run.getName() 
				+ "/Data/Intensities/BaseCalls/" );
		
		c.setResultsDirectory(dataDir + "/" + run.getName() + "/" + outputFolder);
		WorkUnit w = new WorkUnit(c);
		w.setCommand(getConfigureBclToFastqString(sm, run, procs, sampleSheetName, outputFolder));

		GridResult result = gws.execute(w);
		
		logger.debug("started illumina pipeline: " + result.getUuid());
		
		return result;
	}
	
	private String getConfigureBclToFastqString(SoftwareManager sm, Run run, int proc, String sampleSheetName, String outputFolder) throws MetadataException {
		String failed = sm.getConfiguredSetting("casava.with-failed-reads");
		String mismatches = sm.getConfiguredSetting("casava.mismatches");
		String missingStats = sm.getConfiguredSetting("casava.ignore-missing-stats");
		String missingBcl = sm.getConfiguredSetting("casava.ignore-missing-bcl");
		String missingControl = sm.getConfiguredSetting("casava.ignore-missing-control");
		String fastqNclusters = sm.getConfiguredSetting("casava.fastq-cluster-count");
		
		String semaphore = sampleSheetName.equals(IlluminaHiseqSequenceRunProcessor.SINGLE_INDEX_SAMPLE_SHEET_NAME) ? 
		        IlluminaHiseqSequenceRunProcessor.SINGLE_INDEX_SEMAPHORE : IlluminaHiseqSequenceRunProcessor.DUAL_INDEX_SEMAPHORE;
		
		String retval = "if [ ! -e " + semaphore + " ]; then\n\n touch " + semaphore + "\n\n";
		
		// if the sample sheet is empty or only contains controls, don't prepare
		retval += " nl=`awk '{if ( $0 !~ /^FCID,/ && $0 !~ /,Y,/ ) { print $0 } } ' " + sampleSheetName + " | wc -l`\n";
		retval += " if [ $nl -ne 0 ]; then\n";
		
		retval+="  loc=\"_pos.txt\"\n" + 
				"  if [ -e ../L001/s_1_1101.clocs ]; then\n" +
				"   loc=.clocs\n" +
				"  elif [ -e ../L001/s_1_1101.locs ]; then\n" +
				"   loc=.locs\n" +
				"  fi\n\n";
		
		String dev = "";
		if (Boolean.parseBoolean(devmode)) {
		    logger.debug("found developer mode annotation, will only process 1 tile in each lane for illumina pipeline");
		    dev = " --tiles 1101";
		}
		
		retval += "  configureBclToFastq.pl " + dev + " --force --positions-format ${loc} --sample-sheet " + sampleSheetName + " --output-dir ../../../" + outputFolder + " ";
		
		if (PropertyHelper.isSet(failed) && failed == "true")
			retval += "--with-failed-reads ";
		if (PropertyHelper.isSet(mismatches)) {
			int mm = new Integer(mismatches).intValue();
			if (mm == 0 || mm == 1) { 
				retval += "--mismatches " + mm + " ";
			} else {
			    logger.warn("unknown bcl2fastq mismatch option: " + mm + ", using default");
			}
		}
		if (PropertyHelper.isSet(missingStats) && missingStats == "true")
			retval += " --ignore-missing-stats ";
		if (PropertyHelper.isSet(missingBcl) && missingBcl == "true")
			retval += " --ignore-missing-bcl ";
		if (PropertyHelper.isSet(missingControl) && missingControl == "true")
			retval += " --ignore-missing-control ";
		if (PropertyHelper.isSet(fastqNclusters)) {
			int fqc = new Integer(fastqNclusters).intValue();
			retval += " --fastq-cluster-count " + fqc;
		}
		int readSegments = waspIlluminaService.getNumberOfReadSegments(run);
		int indexedReads = waspIlluminaService.getNumberOfIndexedReads(run);
		
		String basesMask = "";
		if (readSegments == 1 || readSegments == 2) {
			basesMask = "Y*";
			if (indexedReads == 1) {
				basesMask += ",I6n*";
				logger.debug("TruSeq single barcode, single sequencing");
			} else if (indexedReads == 2) {
				if (method.equals(IlluminaIndexingStrategy.TRUSEQ)) {
					basesMask += ",I6n*,n*";
					logger.debug("TruSeq single barcode, dual sequencing");
				} else if (method.equals(IlluminaIndexingStrategy.TRUSEQ_DUAL)) {
					basesMask += ",I8,I8";
					logger.debug("TruSeq dual barcode, dual sequencing");
				} else {
					String mess = "Unknown indexing strategy " + method.toString();
					logger.error(mess);
					throw new WaspRuntimeException(mess);
				}
			} else {
				logger.warn("Number of number of indexed read segments was " + indexedReads );
			}
		} else {
			String mess = "Unknown number of read segments " + readSegments;
			logger.error(mess);
			throw new WaspRuntimeException(mess);
		}
		if (readSegments == 2) {
			basesMask += ",Y*";
			logger.debug("paired end run");
		}
		logger.info("run " + run.getName() + " bcl2fastq processing for " + method.toString() + " bases mask=" + basesMask);
		
		retval += " --use-bases-mask " + basesMask;
		
		retval += "\n  cd ../../../" + outputFolder + " && make -j ${NTHREADS} \n\n else\n  echo no cell libraries >&2\n fi\nelse\n echo semaphore exists >&2\nfi\n\n";

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


	@Override
	public void doCleanupBeforeRestart(StepExecution stepExecution) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
