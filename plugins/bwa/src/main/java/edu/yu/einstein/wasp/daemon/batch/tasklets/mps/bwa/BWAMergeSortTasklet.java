/**
 * 
 */
package edu.yu.einstein.wasp.daemon.batch.tasklets.mps.bwa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionExponential;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet;
import edu.yu.einstein.wasp.filetype.FastqComparator;
import edu.yu.einstein.wasp.filetype.service.FastqService;
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
import edu.yu.einstein.wasp.mps.tools.Picard;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * @author calder
 *
 */
public class BWAMergeSortTasklet extends WaspTasklet implements StepExecutionListener {
	
	private String scratchDirectory;
	private Integer cellLibId;
	private Integer bamGId;
	private Integer baiGId;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private Picard picard;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private FastqService fastqService;
	
	@Autowired
	private FileType fastqFileType;
	
	@Autowired
	private FileType bamFileType;
	
	@Autowired
	private FileType baiFileType;
	
	@Autowired
	private GridHostResolver gridHostResolver;

	/** 
	 * {@inheritDoc}
	 */
	@Override
	@Transactional("entityManager")
	@RetryOnExceptionExponential
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		
		// if the work has already been started, then check to see if it is
		// finished
		// if not, throw an exception that is caught by the repeat policy.
		RepeatStatus repeatStatus = super.execute(contrib, context);
		if (repeatStatus.equals(RepeatStatus.FINISHED)){
			// register .bam and .bai file groups with cellLib so as to make available to views
			SampleSource cellLib = sampleService.getSampleSourceDao().findById(cellLibId);
			if (bamGId != null && cellLib.getId() != 0)
				fileService.setSampleSourceFile(fileService.getFileGroupById(bamGId), cellLib);
			if (baiGId != null && cellLib.getId() != 0)
				fileService.setSampleSourceFile(fileService.getFileGroupById(baiGId), cellLib);
			return RepeatStatus.FINISHED;
		}

		SampleSource cellLib = sampleService.getSampleSourceDao().findById(cellLibId);

		Job job = sampleService.getJobOfLibraryOnCell(cellLib);

		logger.debug("Beginning sort/merge of BAM files for " + cellLib.getId() + " from job " + job.getId());
		logger.debug("Starting from previously aln'd scratch directory " + scratchDirectory);
		
		Set<FileGroup> fileGroups = fileService.getFilesForCellLibraryByType(cellLib, fastqFileType);

		Assert.assertTrue(fileGroups.size() == 1);
		FileGroup fg = fileGroups.iterator().next();

		logger.debug("file group: " + fg.getId() + ":" + fg.getDescription());
		
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
		
		String baiOutput = fileService.generateUniqueBaseFileName(cellLib) + "bwa.bai";
		
		String bamOutput = fileService.generateUniqueBaseFileName(cellLib) + "bwa.bam";
		FileGroup bamG = new FileGroup();
		FileHandle bam = new FileHandle();
		bam.setFileName(bamOutput);
		bam = fileService.addFile(bam);
		bamG.addFileHandle(bam);
		bamG.setFileType(bamFileType);
		bamG.setDescription(bamOutput);
		bamG = fileService.addFileGroup(bamG);
		bamGId = bamG.getId();
		// save in step context in case of batch restart
		context.getStepContext().getStepExecutionContext().put("bamGID", bamGId);
		
		
		FileGroup baiG = new FileGroup();
		FileHandle bai = new FileHandle();
		bai.setFileName(baiOutput);
		bai = fileService.addFile(bai);
		baiG.addFileHandle(bai);
		baiG.setFileType(baiFileType);
		baiG.setDescription(baiOutput);
		baiG = fileService.addFileGroup(baiG);
		baiGId = baiG.getId();
		// save in step context in case of batch restart
		context.getStepContext().getStepExecutionContext().put("baiGId", baiGId);
		
//		baiG.getDerivedFrom().add(bamG);
//		bamG.getBegat().add(baiG);
//		
//		bamG = fileService.addFileGroup(bamG);
//		baiG = fileService.addFileGroup(baiG);
		
		w.getResultFiles().add(bamG);
		w.getResultFiles().add(baiG);
		
		w.setCommand("shopt -s nullglob\n");
		w.addCommand("for x in sam.*.out; do ln -s ${x} ${x/*:/}.sam ; done\n");
		w.addCommand("java -Xmx4g -jar $PICARD_HOME/MergeSamFiles.jar $(printf 'I=%s ' *.out.sam) O=${" + WorkUnit.OUTPUT_FILE + "[0]} " +
				"SO=coordinate TMP_DIR=. CREATE_INDEX=true VALIDATION_STRINGENCY=SILENT");
		w.addCommand("cp ${" + WorkUnit.OUTPUT_FILE + "[0]}.bai ${" + WorkUnit.OUTPUT_FILE + "[1]}");
			
		w.setWorkingDirectory(scratchDirectory);
		w.setResultsDirectory(WorkUnit.RESULTS_DIR_PLACEHOLDER + "/" + job.getId());
		w.setSecureResults(true);
		
		GridResult result = gridHostResolver.execute(w);

		// place the grid result in the step context
		storeStartedResult(context, result);

		return RepeatStatus.CONTINUABLE;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public ExitStatus afterStep(StepExecution arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		logger.debug("StepExecutionListener beforeStep saving StepExecution");
		JobExecution jobExecution = stepExecution.getJobExecution();
		ExecutionContext jobContext = jobExecution.getExecutionContext();
		this.scratchDirectory = jobContext.get("scrDir").toString();
		this.cellLibId = (Integer) jobContext.get("cellLibId");
		this.bamGId = (Integer) stepExecution.getExecutionContext().get("bamGID");
		this.baiGId = (Integer) stepExecution.getExecutionContext().get("baiGID");
		
	}

}
