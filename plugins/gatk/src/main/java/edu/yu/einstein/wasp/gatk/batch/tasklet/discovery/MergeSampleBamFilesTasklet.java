package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Job;
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
	public void doExecute(ChunkContext context) throws Exception {
		Job job = jobService.getJobByJobId(jobId);
		WorkUnit w = new WorkUnit();
		w.setMode(ExecutionMode.PROCESS);
		w.setProcessMode(ProcessMode.SINGLE);
		w.setMemoryRequirements(MEMORY_GB_4);
		w.setWorkingDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		w.setResultsDirectory(fileService.generateJobSoftwareBaseFolderName(job, gatk));
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
		List<SoftwarePackage> dependencies = new ArrayList<>();
		dependencies.add(gatk);
		dependencies.add(gatk.getSoftwareDependencyByIname("picard"));
		w.setSoftwareDependencies(dependencies);
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
		
		GridResult result = gridHostResolver.execute(w);

		// place the grid result in the step context
		saveGridResult(context, result);
	}
}
