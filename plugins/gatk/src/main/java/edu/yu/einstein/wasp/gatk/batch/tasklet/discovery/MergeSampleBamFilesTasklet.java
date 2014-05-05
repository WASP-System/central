package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.gatk.software.GATKSoftwareComponent;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * 
 * @author asmclellan
 *
 */
public class MergeSampleBamFilesTasklet extends AbstractGatkTasklet {
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private GATKSoftwareComponent gatk;
	
	@Autowired
	private GridHostResolver gridHostResolver;
	
	public MergeSampleBamFilesTasklet(String inputFilegroupIds, String outputFilegroupIds) {
		super(inputFilegroupIds, outputFilegroupIds);
	}
	
	@Override
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		WorkUnit w = new WorkUnit();
		w.setMode(ExecutionMode.PROCESS);
		w.setProcessMode(ProcessMode.FIXED);
		w.setProcessorRequirements(2);
		w.setMemoryRequirements(4);
		
		List<FileHandle> fhlist = new ArrayList<FileHandle>();
		for (Integer fgId : this.getInputFilegroupIds()){
			FileGroup fg = fileService.getFileGroupById(fgId);
			fhlist.addAll(fg.getFileHandles());
		}
		w.setRequiredFiles(fhlist);
		
		LinkedHashSet<FileGroup> fglist = new LinkedHashSet<FileGroup>();
		for (Integer fgId : this.getOutputFilegroupIds()){
			fglist.add(fileService.getFileGroupById(fgId));
		}
		w.setResultFiles(fglist);
		List<SoftwarePackage> dependencies = new ArrayList<>();
		dependencies.add(gatk);
		dependencies.add(gatk.getSoftwareDependencyByIname("picard"));
		w.setSoftwareDependencies(dependencies);
		w.setSecureResults(true);
		String mergeCmd = "java -Xmx4g -jar $PICARD_ROOT/MergeSamFiles.jar ";
		for (int i=0; i< fhlist.size(); i++)
			mergeCmd += "I=${" + WorkUnit.INPUT_FILE + "[" + i + "]} ";
		mergeCmd += "O=merged.${"+ WorkUnit.OUTPUT_FILE+ "[0]} SO=coordinate TMP_DIR=. CREATE_INDEX=false VALIDATION_STRINGENCY=SILENT";
		w.setCommand(mergeCmd);
		w.addCommand("java -Xmx4g -jar $PICARD_ROOT/MarkDuplicates.jar I=merged.${" + WorkUnit.OUTPUT_FILE + "[0]} " +
				"O=${" + WorkUnit.OUTPUT_FILE + "[0]} REMOVE_DUPLICATES=false METRICS_FILE=${" + 
				WorkUnit.OUTPUT_FILE + "[2]} TMP_DIR=. CREATE_INDEX=true VALIDATION_STRINGENCY=SILENT");
		w.addCommand("mv ${" + WorkUnit.OUTPUT_FILE + "[0]}.bai ${" + WorkUnit.OUTPUT_FILE + "[1]}");
		w.setWorkingDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		w.setResultsDirectory(WorkUnit.RESULTS_DIR_PLACEHOLDER + "/" + jobId);
		w.setSecureResults(true);
		
		GridResult result = gridHostResolver.execute(w);

		// place the grid result in the step context
		storeStartedResult(context, result);
	}

	
	@Transactional("entityManager")
	@Override
	public void doPreFinish(ChunkContext context) throws Exception {
		for (Integer fgId : this.getOutputFilegroupIds())
			fileService.getFileGroupById(fgId).setIsActive(1);
	}
}
