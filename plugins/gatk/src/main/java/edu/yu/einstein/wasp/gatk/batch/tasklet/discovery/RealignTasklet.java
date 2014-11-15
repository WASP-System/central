package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.MetadataException;
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
import edu.yu.einstein.wasp.plugin.genomemetadata.plugin.GenomeMetadataPlugin.VCF_TYPE;
import edu.yu.einstein.wasp.plugin.picard.software.Picard;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * 
 * @author asmclellan
 *
 */
public class RealignTasklet extends AbstractGatkTasklet {
	
	private static Logger logger = LoggerFactory.getLogger(JointGenotypingTasklet.class);

	public RealignTasklet(String inputFilegroupIds, String outputFilegroupIds, Integer jobId) {
		super(inputFilegroupIds, outputFilegroupIds, jobId);
	}
	
	@Override
	@Transactional("entityManager")
	public void beforeStep(StepExecution stepExecution){
		super.beforeStep(stepExecution);
	}

	@Override
	@Transactional("entityManager")
	public GenomeIndexStatus getGenomeIndexStatus(StepExecution stepExecution) {
		ExecutionContext stepExecutionContext = getStepExecutionContext(stepExecution);
		try {
			if (!this.getInputFilegroupIds().iterator().hasNext())
				throw new GenomeMetadataException("unable to retrieve build as no files from which to determine build");
			FileGroup fg = fileService.getFileGroupById(this.getInputFilegroupIds().iterator().next());
			Build build = gatkService.getBuildForFg(fg);
			GenomeIndexStatus fq = genomeMetadataService.getFastaStatus(getGridWorkService(stepExecutionContext), build);
			GenomeIndexStatus s1 = genomeMetadataService.getVcfStatus(getGridWorkService(stepExecutionContext), build, genomeMetadataService.getDefaultVcf(build, VCF_TYPE.INDEL));
			GenomeIndexStatus s2 = genomeMetadataService.getVcfStatus(getGridWorkService(stepExecutionContext), build, genomeMetadataService.getDefaultVcf(build, VCF_TYPE.SNP));
			if (! (fq.isAvailable() && s1.isAvailable() && s2.isAvailable()) ) {
				return GenomeIndexStatus.UNBUILDABLE;
			} 
			if (fq.isCurrentlyAvailable() && s1.isCurrentlyAvailable() && s2.isCurrentlyAvailable()) {
				return GenomeIndexStatus.BUILT;
			} else {
				return GenomeIndexStatus.BUILDING;
			}
		} catch (IOException | GridUnresolvableHostException | MetadataException | GenomeMetadataException e) {
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
		List<SoftwarePackage> dependencies = new ArrayList<>();
		Picard picard = (Picard) gatk.getSoftwareDependencyByIname("picard");
		dependencies.add(gatk);
		dependencies.add(picard);
		c.setSoftwareDependencies(dependencies);
		return c;
	}

	@Override
	@Transactional("entityManager")
	public WorkUnit buildWorkUnit(StepExecution stepExecution) throws Exception {
		WorkUnit w = new WorkUnit(configureWorkUnit(stepExecution));
	
		w.setSecureResults(true);
		Build build = null;
		List<FileHandle> inFiles = new ArrayList<FileHandle>();
		for (Integer fgId : this.getInputFilegroupIds()){
			FileGroup fg = fileService.getFileGroupById(fgId);
			if (inFiles.isEmpty()) // first entry not yet entered
				build = gatkService.getBuildForFg(fg);
			inFiles.addAll(fg.getFileHandles());
		}
		w.setRequiredFiles(inFiles);
		
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
		
		LinkedHashSet<String> inputBamFilenames = new LinkedHashSet<>();
		for (int i=0; i < inFiles.size(); i++)
			inputBamFilenames.add("${" + WorkUnit.INPUT_FILE + "[" + i + "]}");
		LinkedHashSet<String> outputFilenames = new LinkedHashSet<>();
		for (int i=0; i < outFiles.size(); i++)
			outputFilenames.add("${" + WorkUnit.OUTPUT_FILE + "[" + i + "]}");
		
		String intervalFileName = "gatk.${" + WorkUnit.OUTPUT_FILE + "}.realign.intervals";
		w.addCommand(gatk.getCreateTargetCmd(getGridWorkService(getStepExecutionContext(stepExecution)), build, inputBamFilenames, intervalFileName, MEMORY_GB_16));
		w.addCommand(gatk.getLocalAlignCmd(getGridWorkService(getStepExecutionContext(stepExecution)), build, inputBamFilenames, intervalFileName, outputFilenames, MEMORY_GB_16));
		return w;
	}

	@Override
	public void doCleanupBeforeRestart(StepExecution stepExecution) throws Exception {
		super.doCleanupBeforeRestart(stepExecution);
	}

}
