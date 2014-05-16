/**
 * 
 */
package edu.yu.einstein.wasp.plugin.bwa.batch.tasklet;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
import edu.yu.einstein.wasp.plugin.fileformat.plugin.BamFileTypeAttribute;
import edu.yu.einstein.wasp.plugin.fileformat.service.FastqService;
import edu.yu.einstein.wasp.plugin.picard.software.Picard;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.SampleService;


/**
 * @author calder
 *
 */
public class BWAMergeSortDedupTasklet extends WaspRemotingTasklet implements StepExecutionListener {
	
	private static int MEMORY_GB_4 = 4;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private FileTypeService bamServiceImpl;
	
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
		Picard picard = (Picard) bwa.getSoftwareDependencyByIname("picard");
		
		// retrieve attributes persisted in jobExecutionContext
		String scratchDirectory = jobExecutionContext.get("scrDir").toString();
		Integer cellLibId = jobExecutionContext.getInt("cellLibId");
		
		SampleSource cellLib = sampleService.getSampleSourceDao().findById(cellLibId);
		Set<SampleSource> cellLibraries = new HashSet<>();
		cellLibraries.add(cellLib);

		Job job = sampleService.getJobOfLibraryOnCell(cellLib);

		logger.debug("Beginning sort/merge of BAM files for " + cellLib.getId() + " from job " + job.getId());
		logger.debug("Starting from previously aln'd scratch directory " + scratchDirectory);
		
		Set<FileGroup> fastqFileGroups = fileService.getFilesForCellLibraryByType(cellLib, fastqFileType);

		Map<String,Object> jobParameters = context.getStepContext().getJobParameters();
		boolean markDuplicates = false;
		if (!jobParameters.containsKey("markDuplicates") || jobParameters.get("markDuplicates").equals("yes"))
			markDuplicates = true;
		
		WorkUnit w = new WorkUnit();
		w.setMode(ExecutionMode.PROCESS);
		w.setProcessMode(ProcessMode.FIXED);
		w.setMemoryRequirements(MEMORY_GB_4);
		
		w.setSoftwareDependencies(bwa.getSoftwareDependencies());
		w.setSecureResults(true);
		
		String bamOutput = fileService.generateUniqueBaseFileName(cellLib) + "bwa.bam";
		FileGroup bamG = new FileGroup();
		FileHandle bam = new FileHandle();
		bam.setFileName(bamOutput);
		bam.setFileType(bamFileType);
		bam = fileService.addFile(bam);
		bamG.setIsActive(0);
		bamG.addFileHandle(bam);
		bamG.setFileType(bamFileType);
		bamG.setSampleSources(cellLibraries);
		bamG.setDerivedFrom(fastqFileGroups);
		bamG.setDescription(bamOutput);
		bamG.setSoftwareGeneratedBy(bwa);
		
		bamG = fileService.addFileGroup(bamG);
		bamServiceImpl.addAttribute(bamG, BamFileTypeAttribute.SORTED);
		Integer bamGId = bamG.getId();
		
		fileService.setSampleSourceFile(bamG, cellLib);
		// save in step context  for use later
		stepExecutionContext.put("bamGID", bamGId);
		
		String baiOutput = fileService.generateUniqueBaseFileName(cellLib) + "bwa.bai";
		FileGroup baiG = new FileGroup();
		FileHandle bai = new FileHandle();
		bai.setFileName(baiOutput);
		bai.setFileType(baiFileType);
		bai = fileService.addFile(bai);
		baiG.setIsActive(0);
		baiG.addFileHandle(bai);
		baiG.setFileType(baiFileType);
		baiG.setDescription(baiOutput);
		baiG.addDerivedFrom(bamG);
		baiG.setSoftwareGeneratedBy(bwa);
		baiG = fileService.addFileGroup(baiG);
		Integer baiGId = baiG.getId();
		
		// save in step context for use later
		stepExecutionContext.put("baiGID", baiGId);
		
		w.getResultFiles().add(bamG);
		w.getResultFiles().add(baiG);
	
		w.setCommand("shopt -s nullglob\n");
		w.addCommand("for x in sam.*.out; do ln -s ${x} ${x/*:/}.sam ; done\n");
		String outputBamFilename = "${" + WorkUnit.OUTPUT_FILE + "[0]}";
		String outputBaiFilename = "${" + WorkUnit.OUTPUT_FILE + "[1]}";
		if (markDuplicates){
			bamServiceImpl.addAttribute(bamG, BamFileTypeAttribute.DEDUP);
			String metricsOutput = fileService.generateUniqueBaseFileName(cellLib) + "dedupMetrics.txt";
			FileGroup metricsG = new FileGroup();
			FileHandle metrics = new FileHandle();
			metrics.setFileName(metricsOutput);
			metrics.setFileType(textFileType);
			metrics = fileService.addFile(metrics);
			metricsG.setIsActive(0);
			metricsG.addFileHandle(metrics);
			metricsG.setFileType(textFileType);
			metricsG.setDescription(metricsOutput);
			metricsG.setSoftwareGeneratedBy(bwa);
			metricsG.addDerivedFrom(bamG);
			metricsG = fileService.addFileGroup(metricsG);
			Integer metricsGId = metricsG.getId();
			
			fileService.setSampleSourceFile(metricsG, cellLib);
			// save in step context for use later
			stepExecutionContext.put("metricsGID", metricsGId);
			w.getResultFiles().add(metricsG);
			String tempMergedBamFilename = "merged.${" + WorkUnit.OUTPUT_FILE + "[0]}";
			String dedupMetricsFilename = "${" + WorkUnit.OUTPUT_FILE + "[2]}";
			w.addCommand(picard.getMergeBamCmd("*.out.sam", tempMergedBamFilename, null, MEMORY_GB_4));
			w.addCommand(picard.getMarkDuplicatesCmd(tempMergedBamFilename, outputBamFilename, outputBaiFilename, dedupMetricsFilename, MEMORY_GB_4));
		} else {
			w.addCommand(picard.getMergeBamCmd("*.out.sam", outputBamFilename, outputBaiFilename, MEMORY_GB_4));
		}	
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
		Integer bamGId = stepExecutionContext.getInt("bamGID");
		Integer baiGId = stepExecutionContext.getInt("baiGID");
		
		Integer metricsGId = null; 
		if (stepExecutionContext.containsKey("metricsGID"))
		        metricsGId = stepExecutionContext.getInt("metricsGID");
		
		// register .bam and .bai file groups as active to make them available to views
		if (bamGId != null)
			fileService.getFileGroupById(bamGId).setIsActive(1);
		if (baiGId != null)
			fileService.getFileGroupById(baiGId).setIsActive(1);	
		if (metricsGId != null)
			fileService.getFileGroupById(metricsGId).setIsActive(1);	
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
