package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
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
 * @author jcai
 * @author asmclellan
 */
public class CallVariantsWithUGTasklet extends AbstractGatkTasklet implements StepExecutionListener {
	
	private static Logger logger = LoggerFactory.getLogger(CallVariantsWithUGTasklet.class);

	@Autowired
	private StrategyService strategyService;

	Map<String,JobParameter> jobParameters;

	public CallVariantsWithUGTasklet(String inputFilegroupIds, String outputFilegroupIds, Integer jobId) {
		super(inputFilegroupIds, outputFilegroupIds, jobId);
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
	public WorkUnitGridConfiguration configureWorkUnit(StepExecution stepExecution) throws Exception {
		Job job = jobService.getJobByJobId(jobId);
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
		c.setMode(ExecutionMode.PROCESS);
		c.setProcessMode(ProcessMode.MAX);
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
	public WorkUnit buildWorkUnit(StepExecution stepExecution) throws Exception {
		Job job = jobService.getJobByJobId(jobId);
		
		WorkUnit w = new WorkUnit(configureWorkUnit(stepExecution));
		
		w.setSecureResults(true);
		
		LinkedHashSet<FileHandle> outFiles = new LinkedHashSet<FileHandle>();
        for (Integer fgId : this.getOutputFilegroupIds()){
            FileGroup fg = fileService.getFileGroupById(fgId);
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
		w.setRequiredFiles(fhlist);
		
		for (String key : jobParameters.keySet()) {
			logger.trace("Key: " + key + " Value: " + jobParameters.get(key).toString());
		}
		Strategy strategy = strategyService.getThisJobsStrategy(StrategyType.LIBRARY_STRATEGY, job);
		String gatkOpts = gatk.getCallVariantOpts(jobParameters);
		
		LinkedHashSet<String> inputBamFilenames = new LinkedHashSet<>();
		for (int i=0; i < fhlist.size(); i++)
			inputBamFilenames.add("${" + WorkUnit.INPUT_FILE + "[" + i + "]}");
		String outputFileName = "${" + WorkUnit.OUTPUT_FILE + "[0]}";
		String referenceGenomeFile = genomeMetadataService.getPrefixedGenomeFastaPath(getGridWorkService(), build);
		String wxsIntervalFile = null;
		if (strategy.getStrategy().equals("WXS"))
			wxsIntervalFile = gatkService.getWxsIntervalFile(job, build);

		w.setCommand(gatk.getCallVariantsByUnifiedGenotyper(inputBamFilenames, outputFileName, referenceGenomeFile, wxsIntervalFile, gatkOpts, MEMORY_GB_16));
		return w;
	}

	@Override
	@Transactional("entityManager")
	public void beforeStep(StepExecution stepExecution){
		super.beforeStep(stepExecution);
	}

	

}
