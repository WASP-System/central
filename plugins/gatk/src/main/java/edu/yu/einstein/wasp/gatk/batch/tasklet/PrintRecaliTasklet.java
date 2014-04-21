package edu.yu.einstein.wasp.gatk.batch.tasklet;


/**
 * 
 */

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.filetype.service.FileTypeService;
import edu.yu.einstein.wasp.gatk.service.GatkService;
import edu.yu.einstein.wasp.gatk.software.GATKSoftwareComponent;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.SampleService;


/**
 * @author jcai
 * @author asmclellan
 */
public class PrintRecaliTasklet extends WaspRemotingTasklet implements StepExecutionListener {

	@Autowired
	private SampleService sampleService;

	@Autowired
	private FileService fileService;
	
	@Autowired
	private FileTypeService fileTypeService;
	
	@Autowired
	private GatkService gatkService;

	@Autowired
	private GridHostResolver gridHostResolver;

	@Autowired
	private FileType bamFileType;
	
	@Autowired
	private FileType baiFileType;

	@Autowired
	private GATKSoftwareComponent gatk;
	
	public PrintRecaliTasklet() {
		// proxy
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		// retrieve stored properties
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		ExecutionContext stepExecutionContext = stepExecution.getExecutionContext();
		ExecutionContext jobExecutionContext = stepExecution.getJobExecution().getExecutionContext();
		String scratchDirectory = jobExecutionContext.getString("scrDir");
		String localAlignJobName = jobExecutionContext.getString("localAlignJobName");
		String recaliTableJobName = jobExecutionContext.getString("recaliTableJobName");
		
		SampleSource cellLib = sampleService.getSampleSourceDao().findById(jobExecutionContext.getInt("cellLibId"));

		Job job = sampleService.getJobOfLibraryOnCell(cellLib);

		logger.debug("Beginning GATK print recalibrated sequence step for cellLibrary " + cellLib.getId() + " from job " + job.getId());
		logger.debug("Starting from previously recali-table'd scratch directory " + scratchDirectory);
		
		String baiOutput = fileService.generateUniqueBaseFileName(cellLib) + "gatk_dedup_realn_recal.bai";
		
		String bamOutput = fileService.generateUniqueBaseFileName(cellLib) + "gatk_dedup_realn_recal.bam";
		FileGroup bamG = new FileGroup();
		FileHandle bam = new FileHandle();
		bam.setFileName(bamOutput);
		bam = fileService.addFile(bam);
		bamG.addFileHandle(bam);
		bamG.setFileType(bamFileType);
		bamG.setDescription(bamOutput);
		bamG.setSoftwareGeneratedBy(gatk);
		bamG = fileService.addFileGroup(bamG);
		fileTypeService.setAttributes(bamG, gatkService.getCompleteGatkPreprocessBamFileAttributeSet());
		Integer bamGId = bamG.getId();
		// save in step context  for use later
		stepExecutionContext.put("bamGID", bamGId);
		
		
		FileGroup baiG = new FileGroup();
		FileHandle bai = new FileHandle();
		bai.setFileName(baiOutput);
		bai = fileService.addFile(bai);
		baiG.addFileHandle(bai);
		baiG.setFileType(baiFileType);
		baiG.setDescription(baiOutput);
		baiG = fileService.addFileGroup(baiG);
		baiG.setSoftwareGeneratedBy(gatk);
		Integer baiGId = baiG.getId();
		// save in step context for use later
		stepExecutionContext.put("baiGID", baiGId);
		
		WorkUnit w = gatk.getPrintRecali(cellLib, scratchDirectory, localAlignJobName, recaliTableJobName);
		
		w.getResultFiles().add(bamG);
		w.getResultFiles().add(baiG);
		w.setResultsDirectory(WorkUnit.RESULTS_DIR_PLACEHOLDER + "/" + job.getId());
		
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
		
		// register .bam and .bai file groups with cellLib so as to make available to views
		SampleSource cellLib = sampleService.getSampleSourceDao().findById(jobExecutionContext.getInt("cellLibId"));
		if (bamGId != null && cellLib.getId() != 0)
			fileService.setSampleSourceFile(fileService.getFileGroupById(bamGId), cellLib);
		if (baiGId != null && cellLib.getId() != 0)
			fileService.setSampleSourceFile(fileService.getFileGroupById(baiGId), cellLib);	
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

