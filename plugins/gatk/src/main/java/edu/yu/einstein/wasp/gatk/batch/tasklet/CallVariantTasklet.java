package edu.yu.einstein.wasp.gatk.batch.tasklet;


/**
 * 
 */

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

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
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


public class CallVariantTasklet extends WaspRemotingTasklet implements StepExecutionListener {

	private List<Integer> cellLibraryIds;
	private List<FileGroup> fileGroups = new ArrayList<>();
	
	@Autowired
	private FileService fileService;
	

	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private GridHostResolver gridHostResolver;
	
	@Autowired
	private FileType bamFileType;
	@Autowired
	private FileType fastqFileType;
	
	private StepExecution stepExecution;
	
	@Autowired
	private GATKSoftwareComponent gatk;


	public CallVariantTasklet() {
		// proxy
	}

	public CallVariantTasklet(String cellLibraryIds) {
		this.cellLibraryIds = WaspSoftwareJobParameters.getLibraryCellIdList(cellLibraryIds);
		//Assert.assertTrue(cids.size() == 1);
		//this.cellLibraryId = cids.get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet#execute(org.
	 * springframework.batch.core.StepContribution,
	 * org.springframework.batch.core.scope.context.ChunkContext)
	 */
	@Override
	public void doExecute(ChunkContext context) throws Exception {
		// if the work has already been started, then check to see if it is finished
		// if not, throw an exception that is caught by the repeat policy.
		Integer cellLibId0=cellLibraryIds.get(0);
		SampleSource sampleSource0=sampleService.getSampleSourceDao().findById(cellLibId0);
		Job job = sampleService.getJobOfLibraryOnCell(sampleSource0);
		
		logger.debug("Beginning GATK GATK variant detection for job " + job.getId());
		
		
		for (Integer currentId : cellLibraryIds) {
			Set<FileGroup> tmpFileGroups = fileService.getFilesForCellLibraryByType(sampleService.getSampleSourceDao().findById(currentId), fastqFileType); // TODO: change to bamFileType later
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
		
		// TODO: temporary, fix me
		//WorkUnit w = new WorkUnit();
		WorkUnit w = gatk.getCallVariant(sampleSource0, fileGroups, jobParameters);
		
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
        stepContext.put("cellLibId", cellLibId0);
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
		logger.debug("StepExecutionListener beforeStep saving StepExecution");
		this.stepExecution = stepExecution;
		
	}

}
