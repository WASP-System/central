package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.WaspException;
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
	
	Build build = null;
	
	@Override
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		
		GridResult result = executeWorkUnit();

		// place the grid result in the step context
		saveGridResult(context, result);
	}

	@Override
	@Transactional("entityManager")
	public GenomeIndexStatus getGenomeIndexStatus() {
		try {
			GenomeIndexStatus s1 = genomeMetadataService.getVcfStatus(getGridWorkService(), build, genomeMetadataService.getDefaultVcf(build, VCF_TYPE.INDEL));
			GenomeIndexStatus s2 = genomeMetadataService.getVcfStatus(getGridWorkService(), build, genomeMetadataService.getDefaultVcf(build, VCF_TYPE.SNP));
			if (! (s1.isAvailable() && s2.isAvailable()) ) {
				return GenomeIndexStatus.UNBUILDABLE;
			} 
			if (s1.isCurrentlyAvailable() && s2.isCurrentlyAvailable()) {
				return GenomeIndexStatus.BUILT;
			} else {
				return GenomeIndexStatus.BUILDING;
			}
		} catch (IOException | GridUnresolvableHostException | MetadataException e) {
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
		w.setProcessMode(ProcessMode.MAX);
		w.setMemoryRequirements(MEMORY_GB_16);
		w.setWorkingDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		w.setResultsDirectory(fileService.generateJobSoftwareBaseFolderName(job, gatk));
		w.setSecureResults(true);
		
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
		List<SoftwarePackage> dependencies = new ArrayList<>();
		Picard picard = (Picard) gatk.getSoftwareDependencyByIname("picard");
		dependencies.add(gatk);
		dependencies.add(picard);
		w.setSoftwareDependencies(dependencies);
		
		LinkedHashSet<String> inputBamFilenames = new LinkedHashSet<>();
		for (int i=0; i < inFiles.size(); i++)
			inputBamFilenames.add("${" + WorkUnit.INPUT_FILE + "[" + i + "]}");
		LinkedHashSet<String> outputFilenames = new LinkedHashSet<>();
		for (int i=0; i < outFiles.size(); i++)
			outputFilenames.add("${" + WorkUnit.OUTPUT_FILE + "[" + i + "]}");
		
		String intervalFileName = "gatk.${" + WorkUnit.OUTPUT_FILE + "}.realign.intervals";
		w.addCommand(gatk.getCreateTargetCmd(build, inputBamFilenames, intervalFileName, MEMORY_GB_16));
		w.addCommand(gatk.getLocalAlignCmd(build, inputBamFilenames, intervalFileName, outputFilenames, MEMORY_GB_16));
		return w;
	}

}
