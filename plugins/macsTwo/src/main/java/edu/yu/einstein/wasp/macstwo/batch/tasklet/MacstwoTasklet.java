/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package edu.yu.einstein.wasp.macstwo.batch.tasklet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import edu.yu.einstein.wasp.Assert;

import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionExponential;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.macstwo.software.Macstwo;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Genome;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * @author 
 * 
 */
public class MacstwoTasklet extends WaspTasklet implements StepExecutionListener {
	
	@Autowired
	private JobService jobService;
	
	
	private List<Integer> testCellLibraryIdList;//treated, such as IP
	private List<Integer> controlCellLibraryIdList;//contol, such as input 
	private StepExecution stepExecution;
		
	@Autowired
	private FileType bamFileType;

	@Autowired
	private SampleService sampleService;
	@Autowired
	private FileService fileService;

	@Autowired
	private GridHostResolver gridHostResolver;
	
	@Autowired
	private Macstwo macs2;
	

	public MacstwoTasklet() {
		// proxy
	}

	public MacstwoTasklet(String testCellLibraryIdListAsString, String controlCellLibraryIdListAsString) throws Exception {
		logger.debug("*************************Starting MacstwoTasklet constructor");
		logger.debug("*************************testCellLibraryIdListAsString: " + testCellLibraryIdListAsString);
		logger.debug("*************************controlCellLibraryIdListAsString: " + controlCellLibraryIdListAsString);
		this.testCellLibraryIdList = WaspSoftwareJobParameters.getLibraryCellIdList(testCellLibraryIdListAsString);//should be all from same job
		Assert.assertTrue(!this.testCellLibraryIdList.isEmpty());
		this.controlCellLibraryIdList = WaspSoftwareJobParameters.getLibraryCellIdList(controlCellLibraryIdListAsString);//may be empty
		logger.debug("*************************Ending MacstwoTasklet constructor");
	}

	/**
	 * 
	 * @param contrib
	 * @param context
	 * @return
	 * @throws Exception
	 */
	@Override
	@RetryOnExceptionExponential
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		// if the work has already been started, then check to see if it is finished
		// if not, throw an exception that is caught by the repeat policy.
		
		logger.debug("*************************Starting MacstwoTasklet execute");		
		
		RepeatStatus repeatStatus = super.execute(contrib, context);
		if (repeatStatus.equals(RepeatStatus.FINISHED))
			return RepeatStatus.FINISHED;
			
		Map<String,Object> jobParameters = context.getStepContext().getJobParameters();		
		for (String key : jobParameters.keySet()) {
			logger.debug("Key: " + key + " Value: " + jobParameters.get(key).toString());
		}

		SampleSource firstTestCellLibrary = sampleService.getCellLibraryBySampleSourceId(this.testCellLibraryIdList.get(0));
		Job job = sampleService.getJobOfLibraryOnCell(firstTestCellLibrary);//should all be from same job
		logger.debug("*************************job name : id = " + job.getName() + " : " + job.getId());
		List<JobMeta> jobMetaList = jobService.getJobMeta(job.getId());
		logger.debug("*************************Size of jobMeta = " + jobMetaList.size());
		Sample testSample = sampleService.getLibrary(firstTestCellLibrary);//all these cellLibraries are from the same library or macromoleucle
		while(testSample.getParent()!=null){
			testSample = testSample.getParent();
		}
		logger.debug("*************************  testSample.name = " + testSample.getName());		
		
		List<FileHandle> testFileHandleList = new ArrayList<FileHandle>();		
		for(Integer id : this.testCellLibraryIdList){
			SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(id);
			Set<FileGroup> fileGroups = fileService.getFilesForCellLibraryByType(cellLibrary, bamFileType);
			logger.debug("************************* test fileGroups size = " + fileGroups.size());
			for(FileGroup fileGroup : fileGroups){
				for(FileHandle fileHandle : fileGroup.getFileHandles()){
					testFileHandleList.add(fileHandle);
					logger.debug("************************* test fileHandle = " + fileHandle.getFileName());
				}				
			}
		}
		Assert.assertTrue(!testFileHandleList.isEmpty());
		logger.debug("************************* test bam files size = " + testFileHandleList.size());

		Sample controlSample = null;
		if(!controlCellLibraryIdList.isEmpty()){
			controlSample = sampleService.getLibrary(sampleService.getCellLibraryBySampleSourceId(controlCellLibraryIdList.get(0)));//all these cellLibraries are from the same library or macromoleucle
			while(controlSample.getParent()!=null){
				controlSample = controlSample.getParent();
			}
		}
		if(controlSample==null){
			logger.debug("************************* controlSample IS NULL");
		}
		else{
			logger.debug("************************* controlSample.name = " + controlSample.getName());
		}
		
		
		List<FileHandle> controlFileHandleList = new ArrayList<FileHandle>();
		for(Integer id : this.controlCellLibraryIdList){
			SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(id);
			Set<FileGroup> fileGroups = fileService.getFilesForCellLibraryByType(cellLibrary, bamFileType);
			logger.debug("************************* control fileGroups size = " + fileGroups.size());
			for(FileGroup fileGroup : fileGroups){
				for(FileHandle fileHandle : fileGroup.getFileHandles()){
					controlFileHandleList.add(fileHandle);//can be empty
					logger.debug("************************* control fileHandle = " + fileHandle.getFileName());
				}
			}
		}
		logger.debug("************************* controlFileHandleList.size = " + controlFileHandleList.size());
			
		ExecutionContext stepContext = this.stepExecution.getExecutionContext();
		//TODO: need way to tell that this is using testSample and (perhpas) controlSample
		stepContext.put("testSampleId", testSample.getId()); //place in the step context
		if(controlSample!=null){
			stepContext.put("controlSampleId", controlSample.getId()); //place in the step context			
		}
		WorkUnit w = macs2.getPeaks(jobMetaList, testFileHandleList, controlFileHandleList, jobParameters);//configure
		
		if(1==1)
			throw new Exception("**************throwing exception to test");
		
		
		
		w.setResultsDirectory(WorkUnit.RESULTS_DIR_PLACEHOLDER + "/" + job.getId());
   
		GridResult result = gridHostResolver.execute(w);
		
		//place the grid result in the step context
		storeStartedResult(context, result);

		return RepeatStatus.CONTINUABLE;
	}
	
	@BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
		logger.debug("BeforeStep saving StepExecution");
        this.stepExecution = stepExecution;
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
		this.stepExecution = stepExecution;		
	}
}
