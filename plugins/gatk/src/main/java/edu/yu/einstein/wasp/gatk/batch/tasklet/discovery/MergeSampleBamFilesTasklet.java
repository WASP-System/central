package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;
import edu.yu.einstein.wasp.plugin.picard.software.Picard;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * 
 * @author asmclellan
 *
 */
public class MergeSampleBamFilesTasklet extends AbstractGatkTasklet {
	
	private static Logger logger = LoggerFactory.getLogger(MergeSampleBamFilesTasklet.class);
	
	private boolean isDedup = false;
	
	public MergeSampleBamFilesTasklet(String inputFilegroupIds, String outputFilegroupIds, Integer jobId, Boolean isDedup) {
		super(inputFilegroupIds, outputFilegroupIds, jobId);
		this.isDedup = isDedup;
	}
	
	
	@Override
	@Transactional("entityManager")
	public void beforeStep(StepExecution stepExecution){
		super.beforeStep(stepExecution);
	}

	@Override
	@Transactional("entityManager")
	public GenomeIndexStatus getGenomeIndexStatus(StepExecution stepExecution) {
		// no buildable resources required.
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
		dependencies.add(gatk);
		dependencies.add(gatk.getSoftwareDependencyByIname("picard"));
		c.setSoftwareDependencies(dependencies);
		return c;
	}

	@Override
	@Transactional("entityManager")
	public WorkUnit buildWorkUnit(StepExecution stepExecution) throws Exception {
		WorkUnit w = new WorkUnit(configureWorkUnit(stepExecution));
		
		w.setSecureResults(true);
		
		List<FileHandle> fhlist = new ArrayList<FileHandle>();
		for (Integer fgId : this.getInputFilegroupIds()){
			FileGroup fg = fileService.getFileGroupById(fgId);
			fhlist.addAll(fg.getFileHandles());
		}
		w.setRequiredFiles(fhlist);
		
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
		Picard picard = (Picard) gatk.getSoftwareDependencyByIname("picard");
		for (int i=0; i < fhlist.size(); i++)
			inputBamFilenames.add("${" + WorkUnit.INPUT_FILE + "[" + i + "]}");
		String mergedBamFilename = "${" + WorkUnit.OUTPUT_FILE + "[0]}";
		String mergedBaiFilename = "${" + WorkUnit.OUTPUT_FILE + "[1]}";
		if (isDedup){
			String mergedPreDedupBamFilename = "mergedPreDedup.${"+ WorkUnit.OUTPUT_FILE+ "[0]}";
			String mergedDedupMetricsFilename = "${" + WorkUnit.OUTPUT_FILE + "[2]}";
			w.addCommand(picard.getMergeBamCmd(inputBamFilenames, mergedPreDedupBamFilename, null, MEMORY_GB_4));
			w.addCommand(picard.getMarkDuplicatesCmd(mergedPreDedupBamFilename, mergedBamFilename, mergedBaiFilename, mergedDedupMetricsFilename, MEMORY_GB_4));
		} else {
			w.addCommand(picard.getMergeBamCmd(inputBamFilenames, mergedBamFilename, mergedBaiFilename, MEMORY_GB_4));
		}
		return w;
	}


	@Override
	public void doCleanupBeforeRestart(ChunkContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
