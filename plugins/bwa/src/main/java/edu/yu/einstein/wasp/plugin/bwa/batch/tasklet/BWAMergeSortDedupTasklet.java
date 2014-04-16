/**
 * 
 */
package edu.yu.einstein.wasp.plugin.bwa.batch.tasklet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.filetype.service.FileTypeService;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.bwa.software.BWABacktrackSoftwareComponent;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.FastqComparator;
import edu.yu.einstein.wasp.plugin.fileformat.service.BamService;
import edu.yu.einstein.wasp.plugin.fileformat.service.FastqService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * @author calder
 *
 */
public class BWAMergeSortDedupTasklet extends WaspRemotingTasklet implements StepExecutionListener {
	
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private FileTypeService fileTypeService;
	
	@Autowired
	private FastqService fastqService;
	
	@Autowired
	private FileType fastqFileType;
	
	@Autowired
	private FileType bamFileType;
	
	@Autowired
	private FileType baiFileType;
	
	@Autowired
	private FileType textFileType;
	
	@Autowired
	private GridHostResolver gridHostResolver;
	
	@Autowired
	private BWABacktrackSoftwareComponent bwa;
	
	@Autowired
	@Qualifier("picard")
	private SoftwarePackage picard;
	
	public BWAMergeSortDedupTasklet() {
		// proxy
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		ExecutionContext stepExecutionContext = stepExecution.getExecutionContext();
		ExecutionContext jobExecutionContext = stepExecution.getJobExecution().getExecutionContext();
		
		// retrieve attributes persisted in jobExecutionContext
		String scratchDirectory = jobExecutionContext.get("scrDir").toString();
		Integer cellLibId = jobExecutionContext.getInt("cellLibId");
		
		SampleSource cellLib = sampleService.getSampleSourceDao().findById(cellLibId);

		Job job = sampleService.getJobOfLibraryOnCell(cellLib);

		logger.debug("Beginning sort/merge of BAM files for " + cellLib.getId() + " from job " + job.getId());
		logger.debug("Starting from previously aln'd scratch directory " + scratchDirectory);
		
		Set<FileGroup> fileGroups = fileService.getFilesForCellLibraryByType(cellLib, fastqFileType);

		Assert.assertTrue(fileGroups.size() == 1);
		FileGroup fg = fileGroups.iterator().next();

		logger.debug("file group: " + fg.getId() + ":" + fg.getDescription());
		
		Map<String,Object> jobParameters = context.getStepContext().getJobParameters();
		boolean markDuplicates = false;
		if (jobParameters.containsKey("markDuplicates") && jobParameters.get("markDuplicates").equals("yes"))
			markDuplicates = true;
		
		WorkUnit w = new WorkUnit();
		w.setMode(ExecutionMode.PROCESS);
		w.setProcessMode(ProcessMode.FIXED);
		w.setProcessorRequirements(2);
		w.setMemoryRequirements(4);
		
		List<FileHandle> fhlist = new ArrayList<FileHandle>();
		fhlist.addAll(fg.getFileHandles());
		Collections.sort(fhlist, new FastqComparator(fastqService));
		w.setRequiredFiles(fhlist);
		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(picard);
		w.setSoftwareDependencies(sd);
		w.setSecureResults(true);
		
		String bamOutput = fileService.generateUniqueBaseFileName(cellLib) + "bwa.bam";
		FileGroup bamG = new FileGroup();
		FileHandle bam = new FileHandle();
		bam.setFileName(bamOutput);
		bam.setFileType(bamFileType);
		bam = fileService.addFile(bam);
		bamG.addFileHandle(bam);
		bamG.setFileType(bamFileType);
		bamG.setDescription(bamOutput);
		bamG = fileService.addFileGroup(bamG);
		bamG.setSoftwareGeneratedBy(bwa);
		fileTypeService.addAttribute(bamG, BamService.BAM_ATTRIBUTE_SORTED);
		Integer bamGId = bamG.getId();
		// save in step context  for use later
		stepExecutionContext.put("bamGID", bamGId);
		
		String baiOutput = fileService.generateUniqueBaseFileName(cellLib) + "bwa.bai";
		FileGroup baiG = new FileGroup();
		FileHandle bai = new FileHandle();
		bai.setFileName(baiOutput);
		bai.setFileType(baiFileType);
		bai = fileService.addFile(bai);
		baiG.addFileHandle(bai);
		baiG.setFileType(baiFileType);
		baiG.setDescription(baiOutput);
		baiG = fileService.addFileGroup(baiG);
		baiG.setSoftwareGeneratedBy(bwa);
		Integer baiGId = baiG.getId();
		// save in step context for use later
		stepExecutionContext.put("baiGID", baiGId);
		
		w.getResultFiles().add(bamG);
		w.getResultFiles().add(baiG);
	
		w.setCommand("shopt -s nullglob\n");
		w.addCommand("for x in sam.*.out; do ln -s ${x} ${x/*:/}.sam ; done\n");
		if (markDuplicates){
			fileTypeService.addAttribute(bamG, BamService.BAM_ATTRIBUTE_DEDUP);
			String metricsOutput = fileService.generateUniqueBaseFileName(cellLib) + "dedupMetrics.txt";
			FileGroup metricsG = new FileGroup();
			FileHandle metrics = new FileHandle();
			metrics.setFileName(metricsOutput);
			metrics.setFileType(textFileType);
			metrics = fileService.addFile(metrics);
			metricsG.addFileHandle(metrics);
			metricsG.setFileType(textFileType);
			metricsG.setDescription(metricsOutput);
			metricsG = fileService.addFileGroup(metricsG);
			metricsG.setSoftwareGeneratedBy(bwa);
			Integer metricsGId = metricsG.getId();
			// save in step context for use later
			stepExecutionContext.put("metricsGID", metricsGId);
			w.getResultFiles().add(metricsG);
			
			w.addCommand("java -Xmx4g -jar $PICARD_ROOT/MergeSamFiles.jar $(printf 'I=%s ' *.out.sam) O=merged.${" + WorkUnit.OUTPUT_FILE + "[0]} " +
					"SO=coordinate TMP_DIR=. CREATE_INDEX=false VALIDATION_STRINGENCY=SILENT");
			w.addCommand("java -Xmx4g -jar $PICARD_ROOT/MarkDuplicates.jar I=merged.${" + WorkUnit.OUTPUT_FILE + "[0]} " +
					"O=${" + WorkUnit.OUTPUT_FILE + "[0]} REMOVE_DUPLICATES=false METRICS_FILE=${" + 
					WorkUnit.OUTPUT_FILE + "[2]} TMP_DIR=. CREATE_INDEX=true VALIDATION_STRINGENCY=SILENT");
		} else {
			w.addCommand("java -Xmx4g -jar $PICARD_ROOT/MergeSamFiles.jar $(printf 'I=%s ' *.out.sam) O=${" + WorkUnit.OUTPUT_FILE + "[0]} " +
					"SO=coordinate TMP_DIR=. CREATE_INDEX=true VALIDATION_STRINGENCY=SILENT");
		}
		w.addCommand("mv ${" + WorkUnit.OUTPUT_FILE + "[0]}.bai ${" + WorkUnit.OUTPUT_FILE + "[1]}");
			
		w.setWorkingDirectory(scratchDirectory);
		w.setResultsDirectory(WorkUnit.RESULTS_DIR_PLACEHOLDER + "/" + job.getId());
		w.setSecureResults(true);
		
		GridResult result = gridHostResolver.execute(w);

		// place the grid result in the step context
		storeStartedResult(context, result);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	@Transactional("entityManager")
	public void doPreFinish(ChunkContext context) throws Exception {
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		ExecutionContext stepExecutionContext = stepExecution.getExecutionContext();
		ExecutionContext jobExecutionContext = stepExecution.getJobExecution().getExecutionContext();
		Integer bamGId = stepExecutionContext.getInt("bamGID");
		Integer baiGId = stepExecutionContext.getInt("baiGID");
		Integer metricsGID = stepExecutionContext.getInt("metricsGID");
		
		// register .bam and .bai file groups with cellLib so as to make available to views
		SampleSource cellLib = sampleService.getSampleSourceDao().findById(jobExecutionContext.getInt("cellLibId"));
		if (bamGId != null && cellLib.getId() != 0)
			fileService.setSampleSourceFile(fileService.getFileGroupById(bamGId), cellLib);
		if (baiGId != null && cellLib.getId() != 0)
			fileService.setSampleSourceFile(fileService.getFileGroupById(baiGId), cellLib);	
		if (metricsGID != null && cellLib.getId() != 0)
			fileService.setSampleSourceFile(fileService.getFileGroupById(metricsGID), cellLib);	
	}
	

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return super.afterStep(stepExecution);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		super.beforeStep(stepExecution);
	}

}
