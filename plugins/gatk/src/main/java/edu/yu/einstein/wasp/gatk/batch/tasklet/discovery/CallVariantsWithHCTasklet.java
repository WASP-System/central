package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Strategy;
import edu.yu.einstein.wasp.Strategy.StrategyType;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.StrategyService;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 *  @author asmclellan
 *  @author jcai
 */
public class CallVariantsWithHCTasklet extends AbstractGatkTasklet implements StepExecutionListener {

	private static Logger logger = LoggerFactory.getLogger(CallVariantsWithHCTasklet.class);
	
	private Long jobExecutionId;
	
	@Autowired
	private StrategyService strategyService;
	
	@Autowired
	private JobExplorer jobExplorer;
	
	Build build = null;
	
	public CallVariantsWithHCTasklet(String inputFilegroupIds, String outputFilegroupIds, Integer jobId, Long parentJobExecutionId) {
		super(inputFilegroupIds, outputFilegroupIds, jobId);
		this.jobExecutionId = parentJobExecutionId;
	}

	@Override
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		
		GridResult result = executeWorkUnit();
		
		//place the grid result in the step context
		saveGridResult(context, result);
	}

	@Override
	@Transactional("entityManager")
	public GenomeIndexStatus getGenomeIndexStatus() {
		try {
			return genomeMetadataService.getFastaStatus(getGridWorkService(), build);
		} catch (GridUnresolvableHostException | IOException e) {
			String mess = "Unable to determine build or build status " + e.getLocalizedMessage();
			logger.error(mess);
			throw new WaspRuntimeException(mess);
		}
	}

	@Override
	@Transactional("entityManager")
	public WorkUnit prepareWorkUnit() throws Exception {
		Job job = jobService.getJobByJobId(jobId);
		
		WorkUnit w = new WorkUnit();
		w.setMode(ExecutionMode.PROCESS);
		w.setProcessMode(ProcessMode.SINGLE);
		w.setMemoryRequirements(MEMORY_GB_16);
		w.setSecureResults(true);
		w.setWorkingDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		w.setResultsDirectory(fileService.generateJobSoftwareBaseFolderName(job, gatk));
		LinkedHashSet<FileHandle> outFiles = new LinkedHashSet<FileHandle>();
        for (Integer fgId : this.getOutputFilegroupIds()){
            FileGroup fg = fileService.getFileGroupById(fgId);
            logger.debug("FileGroup with id=" + fgId + " contains " + fg.getFileHandles().size() + " filehandles");
            // single file handle groups
            if (fg.getFileHandles().iterator().hasNext())
            	outFiles.add(fg.getFileHandles().iterator().next());
            else
            	throw new WaspRuntimeException("Cannot obtain a single filehandle from FileGroup id=" + fgId);
        }
        w.setResultFiles(outFiles);
		List<FileHandle> fhlist = new ArrayList<FileHandle>();
		for (Integer fgId : this.getInputFilegroupIds()){
			FileGroup fg = fileService.getFileGroupById(fgId);
			if (fhlist.isEmpty()) // first entry not yet entered
				build = gatkService.getBuildForFg(fg);
			fhlist.addAll(fg.getFileHandles());
		}
		if (build == null)
			throw new WaspRuntimeException("No build could be sourced for job id=" + jobId);
		w.setRequiredFiles(fhlist);
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(gatk);
		w.setSoftwareDependencies(sd);
		
		Map<String,Object> jobParameters = new HashMap<>();
		Map<String, JobParameter> paramMap = jobExplorer.getJobExecution(jobExecutionId).getJobParameters().getParameters();
		for (String key : paramMap.keySet()) {
			// this is legacy ?
			Object value =  paramMap.get(key).getValue();
			logger.trace("Key: " + key + " Value: " + value.toString());
			jobParameters.put(key, value);
		}
		Strategy strategy = strategyService.getThisJobsStrategy(StrategyType.LIBRARY_STRATEGY, job);
		String wxsIntervalFile = null; 
		if (strategy.getStrategy().equals("WXS"))
			wxsIntervalFile = gatkService.getWxsIntervalFile(job, build);
		String gatkOpts = gatk.getCallVariantOpts(paramMap);
		String outputGvcfFileName = "${" + WorkUnit.OUTPUT_FILE + "[0]}";
		String referenceGenomeFile = genomeMetadataService.getRemoteGenomeFastaPath(getGridWorkService(), build);
		LinkedHashSet<String> inputBamFilenames = new LinkedHashSet<>();
		for (int i=0; i < fhlist.size(); i++)
			inputBamFilenames.add("${" + WorkUnit.INPUT_FILE + "[" + i + "]}");
		w.setCommand(gatk.getCallVariantsByHaplotypeCaller(inputBamFilenames, outputGvcfFileName, referenceGenomeFile, wxsIntervalFile, gatkOpts, MEMORY_GB_16));
		return w;
	}
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		super.beforeStep(stepExecution);
	}

}
