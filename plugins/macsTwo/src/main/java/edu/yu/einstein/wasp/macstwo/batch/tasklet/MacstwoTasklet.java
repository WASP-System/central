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
	
	private Job job;
	private List<Integer> testCellLibraryIdList;//treated, such as IP 
	private Sample testSample = null;
	private Sample controlSample = null;
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
		logger.debug("*************************");
		logger.debug("*************************");
		logger.debug("*************************Starting MacstwoTasklet");
		
		this.testCellLibraryIdList = WaspSoftwareJobParameters.getLibraryCellIdList(testCellLibraryIdListAsString);//should be all from same job
		Assert.assertTrue(!this.testCellLibraryIdList.isEmpty());
		Sample testParent = sampleService.getLibrary(sampleService.getCellLibraryBySampleSourceId(testCellLibraryIdList.get(0)));//all these cellLibraries are from the same library or macromoleucle
		while(testParent.getParent()!=null){
			testParent = testParent.getParent();
		}
		this.testSample = testParent;
		this.controlCellLibraryIdList = WaspSoftwareJobParameters.getLibraryCellIdList(controlCellLibraryIdListAsString);//may be empty
		if(!controlCellLibraryIdList.isEmpty()){
			Sample controlParent = sampleService.getLibrary(sampleService.getCellLibraryBySampleSourceId(controlCellLibraryIdList.get(0)));//all these cellLibraries are from the same library or macromoleucle
			while(controlParent.getParent()!=null){
				controlParent = controlParent.getParent();
			}
			controlSample = controlParent;
		}
		this.job = jobService.getJobByJobId(testCellLibraryIdList.get(0));//should all be from same job
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
		RepeatStatus repeatStatus = super.execute(contrib, context);
		if (repeatStatus.equals(RepeatStatus.FINISHED))
			return RepeatStatus.FINISHED;
		
		Map<String,Object> jobParameters = context.getStepContext().getJobParameters();		
		for (String key : jobParameters.keySet()) {
			logger.debug("Key: " + key + " Value: " + jobParameters.get(key).toString());
		}
	
		List<SampleSource> testCellLibraryList = new ArrayList<SampleSource>();//is this list really needed?
		List<FileHandle> testFileHandleList = new ArrayList<FileHandle>();
		
		for(Integer id : this.testCellLibraryIdList){
			SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(id);
			testCellLibraryList.add(sampleService.getCellLibraryBySampleSourceId(id));
			Set<FileGroup> fileGroups = fileService.getFilesForCellLibraryByType(cellLibrary, bamFileType);
			for(FileGroup fileGroup : fileGroups){
				for(FileHandle fileHandle : fileGroup.getFileHandles()){
					testFileHandleList.add(fileHandle);
				}
			}
		}
		Assert.assertTrue(!testCellLibraryList.isEmpty());
		Assert.assertTrue(!testFileHandleList.isEmpty());
		
		List<SampleSource> controlCellLibraryList = new ArrayList<SampleSource>();//is this list really needed?
		List<FileHandle> controlFileHandleList = new ArrayList<FileHandle>();
		for(Integer id : this.controlCellLibraryIdList){
			SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(id);
			controlCellLibraryList.add(sampleService.getCellLibraryBySampleSourceId(id));
			Set<FileGroup> fileGroups = fileService.getFilesForCellLibraryByType(cellLibrary, bamFileType);
			for(FileGroup fileGroup : fileGroups){
				for(FileHandle fileHandle : fileGroup.getFileHandles()){
					controlFileHandleList.add(fileHandle);//can be empty
				}
			}
		}
			
		ExecutionContext stepContext = this.stepExecution.getExecutionContext();
		//TODO: need way to tell that this is using testSample and (perhpas) controlSample
		stepContext.put("testSampleId", testSample.getId()); //place in the step context
		if(controlSample!=null){
			stepContext.put("controlSampleId", controlSample.getId()); //place in the step context			
		}
		WorkUnit w = macs2.getPeaks(testFileHandleList, controlFileHandleList, jobParameters);//configure
		
		if(1==1)
			throw new Exception("throwing exception to test");
		
		
		
		w.setResultsDirectory(WorkUnit.RESULTS_DIR_PLACEHOLDER + "/" + this.job.getId());
   
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
