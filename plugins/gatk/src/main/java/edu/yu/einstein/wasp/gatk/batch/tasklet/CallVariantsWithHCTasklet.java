package edu.yu.einstein.wasp.gatk.batch.tasklet;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
import edu.yu.einstein.wasp.gatk.service.GatkService;
import edu.yu.einstein.wasp.gatk.software.GATKSoftwareComponent;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * @author jcai
 * @author asmclellan
 */
public class CallVariantsWithHCTasklet extends WaspRemotingTasklet implements StepExecutionListener {

	private List<Integer> cellLibraryIds;
	
	private List<FileGroup> fileGroups = new ArrayList<>();
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private FileTypeService fileTypeService;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private FileType bamFileType;
	
	@Autowired
	private GridHostResolver gridHostResolver;
	
	@Autowired
	private GatkService gatkService;
	
	private StepExecution stepExecution;
	
	@Autowired
	private GATKSoftwareComponent gatk;


	public CallVariantsWithHCTasklet() {
		// proxy
	}

	public CallVariantsWithHCTasklet(String cellLibraryIds) {
		this.cellLibraryIds = WaspSoftwareJobParameters.getCellLibraryIdList(cellLibraryIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet#execute(org.
	 * springframework.batch.core.StepContribution,
	 * org.springframework.batch.core.scope.context.ChunkContext)
	 */
	@Override
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		SampleSource cellLibrary1 = sampleService.getCellLibraryBySampleSourceId(cellLibraryIds.get(0));
		Job job = sampleService.getJobOfLibraryOnCell(cellLibrary1);
		
		logger.debug("Beginning GATK variant detection for job " + job.getId());
		
		
		for (Integer currentId : cellLibraryIds) {
			SampleSource cellLibrary = sampleService.getSampleSourceDao().findById(currentId);
			Set<FileGroup> tmpFileGroups = fileService.getFilesForCellLibraryByType(cellLibrary, bamFileType, gatkService.getCompleteGatkPreprocessBamFileAttributeSet(), true);
			
			logger.debug("get file group");
			//Assert.assertTrue(tmpFileGroups.size() == 1);
			FileGroup currentFg = null;
			if (tmpFileGroups.iterator().hasNext())
				currentFg = tmpFileGroups.iterator().next();
			else{
				logger.error("No FileGroup returned for CellLibrary with id=" + currentId);
				throw new FileNotFoundException("No FileGroup returned for CellLibrary with id=" + currentId);
			}
			fileGroups.add(currentFg);
			logger.debug("add file group: " + currentFg.getId() + ":" + currentFg.getDescription());
		}

		Map<String,Object> jobParameters = context.getStepContext().getJobParameters();
		
		for (String key : jobParameters.keySet()) {
			logger.debug("Key: " + key + " Value: " + jobParameters.get(key).toString());
		}
		
		WorkUnit w = gatk.getCallVariantsByHaplotypeCaller(cellLibrary1, fileGroups, jobParameters);
		
		w.setResultsDirectory(WorkUnit.RESULTS_DIR_PLACEHOLDER + "/" + job.getId());
   
		GridResult result = gridHostResolver.execute(w);
		
		//place the grid result in the step context
		storeStartedResult(context, result);
		
		// place scratch directory in step execution context, to be promoted
		// to the job context at run time.
		ExecutionContext stepContext = this.stepExecution.getExecutionContext();
        stepContext.put("scrDir", result.getWorkingDirectory());
        stepContext.put("callVariantName", result.getId());
        stepContext.put("jobId", job.getId());
        stepContext.put("cellLibId", cellLibrary1.getId());
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
		this.stepExecution = stepExecution;
		
	}

}
