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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Strategy;
import edu.yu.einstein.wasp.Strategy.StrategyType;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;
import edu.yu.einstein.wasp.plugin.genomemetadata.exception.GenomeMetadataException;
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
	
	public CallVariantsWithHCTasklet(String inputFilegroupIds, String outputFilegroupIds, Integer jobId, Long parentJobExecutionId) {
		super(inputFilegroupIds, outputFilegroupIds, jobId);
		this.jobExecutionId = parentJobExecutionId;
	}

	
	@Override
	@Transactional("entityManager")
	public GenomeIndexStatus getGenomeIndexStatus(StepExecution stepExecution) {
		try {
			if (!this.getInputFilegroupIds().iterator().hasNext())
				throw new GenomeMetadataException("unable to retrieve build as no files from which to determine build");
			FileGroup fg = fileService.getFileGroupById(this.getInputFilegroupIds().iterator().next());
			Build build = gatkService.getBuildForFg(fg);
			return genomeMetadataService.getFastaStatus(getGridWorkService(getStepExecutionContext(stepExecution)), build);
		} catch (GridUnresolvableHostException | IOException | GenomeMetadataException e) {
			String mess = "Unable to determine build or build status " + e.getLocalizedMessage();
			logger.error(mess);
			throw new WaspRuntimeException(mess);
		}
	}
	
	@Override
	@Transactional("entityManager")
	public WorkUnitGridConfiguration configureWorkUnit(StepExecution se){
		Job job = jobService.getJobByJobId(jobId);
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
		c.setMode(ExecutionMode.PROCESS);
		c.setProcessMode(ProcessMode.SINGLE);
		c.setMemoryRequirements(MEMORY_GB_16);
		
		c.setWorkingDirectory(WorkUnitGridConfiguration.SCRATCH_DIR_PLACEHOLDER);
		c.setResultsDirectory(fileService.generateJobSoftwareBaseFolderName(job, gatk));
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(gatk);
		c.setSoftwareDependencies(sd);
		return c;
	}

	@Override
	@Transactional("entityManager")
	public WorkUnit buildWorkUnit(StepExecution se) throws Exception {
		WorkUnit w = new WorkUnit(configureWorkUnit(se));
		Job job = jobService.getJobByJobId(jobId);
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
		Build build = null;
		for (Integer fgId : this.getInputFilegroupIds()){
			FileGroup fg = fileService.getFileGroupById(fgId);
			if (fhlist.isEmpty()) // first entry not yet entered
				build = gatkService.getBuildForFg(fg);
			fhlist.addAll(fg.getFileHandles());
		}
		if (build == null)
			throw new WaspRuntimeException("No build could be sourced for job id=" + jobId);
		w.setRequiredFiles(fhlist);
		
		
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
		String referenceGenomeFile = genomeMetadataService.getRemoteGenomeFastaPath(getGridWorkService(getStepExecutionContext(se)), build);
		LinkedHashSet<String> inputBamFilenames = new LinkedHashSet<>();
		for (int i=0; i < fhlist.size(); i++)
			inputBamFilenames.add("${" + WorkUnit.INPUT_FILE + "[" + i + "]}");
		w.setCommand(gatk.getCallVariantsByHaplotypeCaller(inputBamFilenames, outputGvcfFileName, referenceGenomeFile, wxsIntervalFile, gatkOpts, MEMORY_GB_16));
		w.setSecureResults(true);
		return w;
	}
	
	@Override
	@Transactional("entityManager")
	public void beforeStep(StepExecution stepExecution){
		super.beforeStep(stepExecution);
	}

}
