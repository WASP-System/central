package edu.yu.einstein.wasp.chipseq.batch.tasklet;

/**
 * R. Dubin
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
//import edu.yu.einstein.wasp.gatk.software.GATKSoftwareComponent;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;


public class PeakCallerTasklet extends WaspTasklet implements StepExecutionListener {

	private List<Integer> cellLibraryIdList;
	
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
	
	private ResourceType softwareResourceType;

//	@Autowired
//	private GATKSoftwareComponent gatk;

	public PeakCallerTasklet() {
		// proxy
	}

	public PeakCallerTasklet(String cellLibraryIdListAsString, ResourceType softwareResourceType) {
		
		//List<Integer> cids = WaspSoftwareJobParameters.getLibraryCellIdList(cellLibraryIdList);
		//Assert.assertTrue(cids.size() == 1);
		//this.cellLibraryIdList = cids.get(0);
		this.cellLibraryIdList = WaspSoftwareJobParameters.getLibraryCellIdList(cellLibraryIdListAsString);
		this.softwareResourceType = softwareResourceType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet#execute(org.
	 * springframework.batch.core.StepContribution,
	 * org.springframework.batch.core.scope.context.ChunkContext)
	 */
	@Override
	@RetryOnExceptionExponential
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		logger.debug("***************in PeakCallerTasklet.execute(): softwareResourceType for peakcaller is " + this.softwareResourceType.getName());
		logger.debug("***************in PeakCallerTasklet.execute(): cellLibraryIdList.size() is " + this.cellLibraryIdList.size());
		List<SampleSource> cellLibraryList = new ArrayList<SampleSource>();
		for(Integer cellLibraryId : cellLibraryIdList){
			logger.debug("**********************in PeakCallerTasklet.execute(): cellLibraryId: " + cellLibraryId);
			SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(cellLibraryId);
			cellLibraryList.add(cellLibrary);
		}
		logger.debug("***************in PeakCallerTasklet.execute(): cellLibraryList.size() is " + cellLibraryList.size());
		Set<Sample> setOfSamples = new HashSet<Sample>();
		Map<Sample, List<SampleSource>> sampleCellLibraryListMap = new HashMap<Sample, List<SampleSource>>();
		Map<Sample, List<Set<FileGroup>>> sampleFileGroupSetListMap = new HashMap<Sample, List<Set<FileGroup>>>();
		Job job = null;
		
		for(SampleSource cellLibrary : cellLibraryList){
			
			if(job==null){
				job = sampleService.getJobOfLibraryOnCell(cellLibrary);
			}
			else{
				if(job.getId().intValue()!=sampleService.getJobOfLibraryOnCell(cellLibrary).getId().intValue()){
					logger.debug("*************************MORE THAN ONE JOB INVOLVED! NOT A GOOD THING HERE!");
					throw new Exception("major problems related to job in PeakCallerTasklet.execute()");
				}
			}
			Sample parentSample = sampleService.getLibrary(cellLibrary);
			
			while(parentSample.getParentId()!=null){
				parentSample = parentSample.getParent();
			}
			
			setOfSamples.add(parentSample);
			
			if(sampleCellLibraryListMap.containsKey(parentSample)){
				sampleCellLibraryListMap.get(parentSample).add(cellLibrary);
			}
			else{
				List<SampleSource> cellLibraryListForASample = new ArrayList<SampleSource>();
				cellLibraryListForASample.add(cellLibrary);
				sampleCellLibraryListMap.put(parentSample, cellLibraryListForASample);
			}
			
			if(sampleFileGroupSetListMap.containsKey(parentSample)){
				Set<FileGroup> fileGroupSet = fileService.getFilesForCellLibraryByType(cellLibrary, bamFileType);
				sampleFileGroupSetListMap.get(parentSample).add(fileGroupSet);
			}
			else{
				List<Set<FileGroup>> fileGroupSetListForASample = new ArrayList<Set<FileGroup>>();
				Set<FileGroup> fileGroupSet = fileService.getFilesForCellLibraryByType(cellLibrary, bamFileType);				
				fileGroupSetListForASample.add(fileGroupSet);
				sampleFileGroupSetListMap.put(parentSample, fileGroupSetListForASample);
			}
			
		}
		
		logger.debug("*************************the jobID is: " + job.getId().toString());
		
		for(Sample sample : setOfSamples){
			logger.debug("*************************Sample: " + sample.getName());
			logger.debug("***************************BamFiles:");
			for(Set<FileGroup> fileGroupSet : sampleFileGroupSetListMap.get(sample)){
				for(FileGroup fileGroup : fileGroupSet){
					for(FileHandle fileHandle : fileGroup.getFileHandles()){
						logger.debug("***************************File: " + fileHandle.getFileName());
					}						
				}
			}			
		}
		
		Set<SampleSource> sampleSourceSet = sampleService.getSamplePairsByJob(job);
		Map<Sample, List<Sample>> ipInputListMap = new HashMap<Sample, List<Sample>>();
		
		for(Sample sample : setOfSamples){
			List<Sample> inputList = new ArrayList<Sample>();
			for(SampleSource ss : sampleSourceSet){
				Sample test = ss.getSample();//IP
				Sample control = ss.getSourceSample();//input
				//System.out.println("----control = " + control.getName() + " AND test = " + test.getName());
				if(sample == test){
					inputList.add(control);
				}
			}
			if(!inputList.isEmpty()){
				ipInputListMap.put(sample, inputList);
				
			}
		}
		 
		
		
		
		
		
		
		
		
		
		if(1==1){return RepeatStatus.FINISHED;}
		// if the work has already been started, then check to see if it is finished
		// if not, throw an exception that is caught by the repeat policy.
		RepeatStatus repeatStatus = super.execute(contrib, context);
		if (repeatStatus.equals(RepeatStatus.FINISHED))
			return RepeatStatus.FINISHED;
		/*
		SampleSource cellLib = sampleService.getSampleSourceDao().findById(cellLibraryId);
		
		ExecutionContext stepContext = this.stepExecution.getExecutionContext();
		stepContext.put("cellLibId", cellLib.getId()); //place in the step context
		
		Job job = sampleService.getJobOfLibraryOnCell(cellLib);
		
		logger.debug("Beginning GATK creat re-alignment target step for cellLibrary " + cellLib.getId() + " from job " + job.getId());
		
		Set<FileGroup> fileGroups = fileService.getFilesForCellLibraryByType(cellLib, fastqFileType); // TODO: change to bamFileType later
		
		logger.debug("ffileGroups.size()="+fileGroups.size());
		Assert.assertTrue(fileGroups.size() == 1);
		FileGroup fg = fileGroups.iterator().next();
		
		logger.debug("file group: " + fg.getId() + ":" + fg.getDescription());
		
		Map<String,Object> jobParameters = context.getStepContext().getJobParameters();
		
		for (String key : jobParameters.keySet()) {
			logger.debug("Key: " + key + " Value: " + jobParameters.get(key).toString());
		}
		
		// TODO: temporary, fix me
		//WorkUnit w = new WorkUnit();
//		WorkUnit w = gatk.getCreatTarget(cellLib, fg);
		
//		w.setResultsDirectory(WorkUnit.RESULTS_DIR_PLACEHOLDER + "/" + job.getId());
   
//		GridResult result = gridHostResolver.execute(w);
		
		//place the grid result in the step context
//		storeStartedResult(context, result);
		
		// place scratch directory in step execution context, to be promoted
		// to the job context at run time.
//        stepContext.put("scrDir", result.getWorkingDirectory());
//        stepContext.put("creatTargetName", result.getId());
    */    

		return RepeatStatus.CONTINUABLE;
	}
	
	public static void doWork(int cellLibraryId) {
		
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
