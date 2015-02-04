/**
 * 
 */
package edu.yu.einstein.wasp.helptag.batch.tasklet;

import java.util.LinkedHashSet;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.helptag.service.HelptagService;
import edu.yu.einstein.wasp.helptag.software.Helptag;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * @author AJ
 *
 */
public class HpaiiCountTasklet extends WaspRemotingTasklet  implements StepExecutionListener {

	@Autowired
	private FileService fileService;

	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private HelptagService helptagService;

	@Autowired
	private Helptag helptag;
	
	@Autowired
	private GridHostResolver gridHostResolver;
	
	@Autowired
	private FileType hcountFileType;

	@Autowired
	private FileType bamFileType;

	private Integer cellLibraryId;
	
	/**
	 * 
	 */
	public HpaiiCountTasklet() {
		// proxy
	}

	public HpaiiCountTasklet(String cellLibraryId) {
		Assert.assertParameterNotNull(cellLibraryId);
		this.cellLibraryId = Integer.valueOf(cellLibraryId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional("entityManager")
	public GridResult doExecute(ChunkContext context) throws Exception {
		ExecutionContext stepExecutionContext = context.getStepContext().getStepExecution().getExecutionContext();

		WorkUnit w = helptag.getHpaiiCounter(cellLibraryId);
		
		LinkedHashSet<FileHandle> files = new LinkedHashSet<FileHandle>();
		SampleSource cl = sampleService.getCellLibraryBySampleSourceId(cellLibraryId);

		String hcountFileName = fileService.generateUniqueBaseFileName(cl) + "hcount";

		// FileHandle hcountFileHandle = helptagService.createAndSaveInnerFileHandle(hcountFileName, hcountFileType);
		//
		// files.add(hcountFileHandle);
		// FileGroup hcountFileGroup = helptagService.createAndSaveInnerFileGroup(hcountFileHandle, helptag,
		// "HELP-tagging pipeline generated hcount file showing hit counts on hpaii loci");
		//
		// hcountFileGroup.setDescription("hcount file for " + cl.getSample().getName() + " - " + cl.getSourceSample().getName());
		// hcountFileGroup.setSoftwareGeneratedBy(helptag);
		// hcountFileGroup.setIsActive(0);
		// hcountFileGroup = fileService.addFileGroup(hcountFileGroup);

		FileGroup hcountG = new FileGroup();
		FileHandle hcountF = new FileHandle();
		hcountF.setFileName(hcountFileName);
		hcountF.setFileType(hcountFileType);
		hcountF = fileService.addFile(hcountF);
		hcountG.setIsActive(0);
		hcountG.addFileHandle(hcountF);
		files.add(hcountF);

		hcountG.setFileType(hcountFileType);
		hcountG.setDerivedFrom(fileService.getFilesForCellLibraryByType(cl, bamFileType));
		hcountG.setDescription(hcountFileName);
		hcountG.setSoftwareGeneratedById(helptag.getId());
		hcountG = fileService.addFileGroup(hcountG);
		fileService.setSampleSourceFile(hcountG, cl);

		// save in step context for use later
		stepExecutionContext.put("hcountGID", hcountG.getId());

		w.setResultFiles(files);

		logger.info("Before submitting hpaii count batch job");

		GridResult result = gridHostResolver.execute(w);

		logger.info("Batch job execution submitted with id=" + result.getGridJobId() + " on host '" + result.getHostname());

		return result;
	}
	
	public static void doWork(int cellLibraryId) {
		
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
		logger.debug("StepExecutionListener beforeStep saving StepExecution");
	}

	@Override
	public void doCleanupBeforeRestart(StepExecution stepExecution) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
