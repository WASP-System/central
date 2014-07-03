package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Job;
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
	public void doExecute(ChunkContext context) throws Exception {
		Job job = jobService.getJobByJobId(jobId);
		WorkUnit w = new WorkUnit();
		w.setMode(ExecutionMode.PROCESS);
		w.setProcessMode(ProcessMode.SINGLE);
		w.setMemoryRequirements(MEMORY_GB_4);
		w.setWorkingDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		w.setResultsDirectory(fileService.generateJobSoftwareBaseFolderName(job, gatk));
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
		List<SoftwarePackage> dependencies = new ArrayList<>();
		VcfTools vcfTools = (VcfTools) gatk.getSoftwareDependencyByIname("vcftools");
		SnpEff snpEff = (SnpEff) gatk.getSoftwareDependencyByIname("snpEff");
		dependencies.add(gatk);
		dependencies.add(snpEff);
		dependencies.add(vcfTools);
		w.setSoftwareDependencies(dependencies);
		String inputVcfFileName = "${" + WorkUnit.INPUT_FILE + "[0]}";
		String subsetVcfFileName = "subset.${" + WorkUnit.OUTPUT_FILE + "[0]}.vcf";
		String outputVcfFileName = "${" + WorkUnit.OUTPUT_FILE + "[0]}";
		String outputHtmlSummaryFileName = "${" + WorkUnit.OUTPUT_FILE + "[1]}";
		String outputGenesSummaryFileName = "${" + WorkUnit.OUTPUT_FILE + "[2]}";
		w.addCommand(vcfTools.getVcfSubsetCommand(inputVcfFileName, subsetVcfFileName, sampleIdentifierSet, false));
		List<String> sampleIdentifierList = Arrays.asList(StringUtils.commaDelimitedListToStringArray(sampleIdentifierSet));
		if (sampleIdentifierList.size() == 2){ // e.g. cancer Tumor / Normal pairs
			w.addCommand(snpEff.getAddPedigreeToHeaderCommand(subsetVcfFileName, sampleIdentifierList.get(0), sampleIdentifierList.get(1)));
			w.addCommand(snpEff.getAnnotateCancerVcfCommand(subsetVcfFileName, outputVcfFileName, outputHtmlSummaryFileName, outputGenesSummaryFileName, build, true));
		} else {
			w.addCommand(snpEff.getAnnotateVcfCommand(subsetVcfFileName, outputVcfFileName, outputHtmlSummaryFileName, outputGenesSummaryFileName, build, true));
		}
		
		GridResult result = gridHostResolver.execute(w);

		// place the grid result in the step context
		storeStartedResult(context, result);
	}

}
