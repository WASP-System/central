package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;
import edu.yu.einstein.wasp.plugin.mps.grid.software.SnpEff;
import edu.yu.einstein.wasp.plugin.mps.grid.software.VcfTools;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * 
 * @author asmclellan
 *
 */
public class SplitAndAnnotateVcfTasklet extends AbstractGatkTasklet {
	
	private static Logger logger = LoggerFactory.getLogger(JointGenotypingTasklet.class);
	
	private String sampleIdentifierSet;
	
	public SplitAndAnnotateVcfTasklet(String inputFilegroupIds, String outputFilegroupIds, String sampleIdentifierSet, Integer jobId) {
		super(inputFilegroupIds, outputFilegroupIds, jobId);
		this.sampleIdentifierSet = sampleIdentifierSet;
	}
	
	@Override
	@Transactional("entityManager")
	public void beforeStep(StepExecution stepExecution){
		super.beforeStep(stepExecution);
	}

	@Override
	@Transactional("entityManager")
	public GenomeIndexStatus getGenomeIndexStatus(StepExecution stepExecution) {
		// no downloadable resources required.
		return GenomeIndexStatus.BUILT;
	}

	@Override
	public WorkUnitGridConfiguration configureWorkUnit(StepExecution stepExecution) throws Exception {
		Job job = jobService.getJobByJobId(jobId);
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
		c.setMode(ExecutionMode.PROCESS);
		c.setProcessMode(ProcessMode.SINGLE);
		c.setMemoryRequirements(MEMORY_GB_4);
		c.setWorkingDirectory(WorkUnitGridConfiguration.SCRATCH_DIR_PLACEHOLDER);
		c.setResultsDirectory(fileService.generateJobSoftwareBaseFolderName(job, gatk));
		List<SoftwarePackage> dependencies = new ArrayList<>();
		VcfTools vcfTools = (VcfTools) gatk.getSoftwareDependencyByIname("vcftools");
		SnpEff snpEff = (SnpEff) gatk.getSoftwareDependencyByIname("snpEff");
		dependencies.add(gatk);
		dependencies.add(snpEff);
		dependencies.add(vcfTools);
		c.setSoftwareDependencies(dependencies);
		return c;
	}

	@Override
	@Transactional("entityManager")
	public WorkUnit buildWorkUnit(StepExecution stepExecution) throws Exception {
		WorkUnit w = new WorkUnit(configureWorkUnit(stepExecution));
		
		w.setSecureResults(true);
		Build build = null;
		
		List<FileHandle> inFilelist = new ArrayList<FileHandle>();
		for (Integer fgId : this.getInputFilegroupIds()){
			FileGroup fg = fileService.getFileGroupById(fgId);
			if (inFilelist.isEmpty()) // first entry not yet entered
				build = gatkService.getBuildForFg(fg);
			inFilelist.addAll(fg.getFileHandles());
		}
		w.setRequiredFiles(inFilelist);
		
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
		String inputVcfFileName = "${" + WorkUnit.INPUT_FILE + "[0]}";
		String subsetVcfFileName = "subset.${" + WorkUnit.OUTPUT_FILE + "[0]}.vcf";
		String outputVcfFileName = "${" + WorkUnit.OUTPUT_FILE + "[0]}";
		String outputHtmlSummaryFileName = "${" + WorkUnit.OUTPUT_FILE + "[1]}";
		String outputGenesSummaryFileName = "${" + WorkUnit.OUTPUT_FILE + "[2]}";
		VcfTools vcfTools = (VcfTools) gatk.getSoftwareDependencyByIname("vcftools");
		SnpEff snpEff = (SnpEff) gatk.getSoftwareDependencyByIname("snpEff");
		w.addCommand(vcfTools.getVcfSubsetCommand(inputVcfFileName, subsetVcfFileName, sampleIdentifierSet, false));
		List<String> sampleIdentifierList = Arrays.asList(StringUtils.commaDelimitedListToStringArray(sampleIdentifierSet));
		if (sampleIdentifierList.size() == 2){ // e.g. cancer Tumor / Normal pairs
			w.addCommand(snpEff.getAddPedigreeToHeaderCommand(subsetVcfFileName, sampleIdentifierList.get(0), sampleIdentifierList.get(1)));
			w.addCommand(snpEff.getAnnotateCancerVcfCommand(subsetVcfFileName, outputVcfFileName, outputHtmlSummaryFileName, outputGenesSummaryFileName, build, true));
		} else {
			w.addCommand(snpEff.getAnnotateVcfCommand(subsetVcfFileName, outputVcfFileName, outputHtmlSummaryFileName, outputGenesSummaryFileName, build, true));
		}
		return w;
	}

	@Override
	public void doCleanupBeforeRestart(ChunkContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
