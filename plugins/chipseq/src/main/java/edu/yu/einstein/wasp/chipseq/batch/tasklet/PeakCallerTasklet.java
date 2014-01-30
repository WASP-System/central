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
import edu.yu.einstein.wasp.chipseq.software.ChipSeqSoftwareComponent;
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

	private List<Integer> approvedCellLibraryIdList;
	
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

	private ChipSeqSoftwareComponent chipseq;
//	@Autowired
//	private GATKSoftwareComponent gatk;

	public PeakCallerTasklet() {
		// proxy
	}

	public PeakCallerTasklet(String cellLibraryIdListAsString, ResourceType softwareResourceType) {
		
		//List<Integer> cids = WaspSoftwareJobParameters.getLibraryCellIdList(cellLibraryIdList);
		//Assert.assertTrue(cids.size() == 1);
		//this.cellLibraryIdList = cids.get(0);
		this.approvedCellLibraryIdList = WaspSoftwareJobParameters.getLibraryCellIdList(cellLibraryIdListAsString);
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
		logger.debug("***************in PeakCallerTasklet.execute(): approvedCellLibraryIdList.size() is " + this.approvedCellLibraryIdList.size());
		
		List<SampleSource> approvedCellLibraryList = new ArrayList<SampleSource>();
		Job job = null;
		for(Integer approvedCellLibraryId : approvedCellLibraryIdList){
			
			logger.debug("**********************in PeakCallerTasklet.execute(): approvedCellLibraryId: " + approvedCellLibraryId);
			
			//get the approvedCellLibrary for each approvedCellLibraryId
			SampleSource approvedCellLibrary = sampleService.getCellLibraryBySampleSourceId(approvedCellLibraryId);
			
			//get the job for this list of approvedCellLibraries and confirm all from the same job
			if(job==null){
				job = sampleService.getJobOfLibraryOnCell(approvedCellLibrary);
			}
			else if(job.getId()!=null){
				if(job.getId().intValue()!=sampleService.getJobOfLibraryOnCell(approvedCellLibrary).getId().intValue()){
					logger.debug("*************************NOT ALL ApprovdCellLibraries ARE FROM THE SAME JOB! Do not proceed!");
					throw new Exception("Not all approvedCellLibraries are from the same job");
				}
			}
			
			//confirm that all the approvedCellLibraries are actually associated with bamFiles 
			Set<FileGroup> fileGroupSet = fileService.getFilesForCellLibraryByType(approvedCellLibrary, bamFileType);				
			/*
			TODO: restore this next if condition: (It's commented out for testing purposes only, since for testing, they might not actually have bam files!)
			if(fileGroupSet.isEmpty()){
				logger.debug("*************************NOT ALL ApprovedCellLibraries are associated with bamFiles. Do NOT proceed!");
				throw new Exception("NOT ALL ApprovedCellLibraries are associated with bamFiles. Do NOT proceed!");
			}	
			*/
			
			approvedCellLibraryList.add(approvedCellLibrary);
		}
		logger.debug("***************in PeakCallerTasklet.execute(): approvedCellLibraryList.size() is " + approvedCellLibraryList.size());
		logger.debug("*************************the jobID is: " + job.getId().toString());

		Set<Sample> setOfApprovedSamples = new HashSet<Sample>();//for a specific job
		Map<Sample, List<SampleSource>> approvedSampleApprovedCellLibraryListMap = new HashMap<Sample, List<SampleSource>>();
		
		//get the uppermost sample associated with these cellLibraries
		for(SampleSource cellLibrary : approvedCellLibraryList){
			
			Sample parentSample = sampleService.getLibrary(cellLibrary);
			
			while(parentSample.getParentId()!=null){
				parentSample = parentSample.getParent();
			}
			
			setOfApprovedSamples.add(parentSample);//it is these parent samples that will be included on the samplePairs information
			
			if(approvedSampleApprovedCellLibraryListMap.containsKey(parentSample)){
				approvedSampleApprovedCellLibraryListMap.get(parentSample).add(cellLibrary);
			}
			else{
				List<SampleSource> approvedCellLibraryListForASample = new ArrayList<SampleSource>();//this list references sets of bam files to be merged!
				approvedCellLibraryListForASample.add(cellLibrary);
				approvedSampleApprovedCellLibraryListMap.put(parentSample, approvedCellLibraryListForASample);
			}
		}
		
		Set<SampleSource> sampleSourceSet = sampleService.getSamplePairsByJob(job);
		Map<Sample, List<Sample>> testSampleControlSampleListMap = new HashMap<Sample, List<Sample>>();
		
		//do not forget that a sample destined for a paired analysis (chipseq or helptag) 
		//MAY NOT be on the samplePair list (but still needs to be dealt with).
		//For chipseq, since the grid of pairs (on the submission forms) can be skipped, we may not know
		//which are IP and which are inputs. However, for helptag it is be different, 
		//since, regardless of the gird of pairs, we DO STILL KNOW which are MspI and which are HpaII
		for(Sample approvedSample : setOfApprovedSamples){
			List<Sample> approvedControlList = new ArrayList<Sample>();
			for(SampleSource ss : sampleSourceSet){//for each recorded samplePair
				Sample test = ss.getSample();//IP (or HpaII)
				Sample control = ss.getSourceSample();//input (or MspI)
				//System.out.println("----control = " + control.getName() + " AND test = " + test.getName());
				if(approvedSample.getId().intValue() == test.getId().intValue()){
					if(setOfApprovedSamples.contains(control)){//no sense adding to controlList if the control is not also approved
						approvedControlList.add(control);
					}
				}
			}
			testSampleControlSampleListMap.put(approvedSample, approvedControlList);//execute this line even if approvedControlList is empty; approvedSample could be an IP without any input paired with it (and approvedSample could also be an input)				
		}
		 
		//output for testing purposes only:
		//first, print out recorded samplePairs, if any
		logger.debug("*************************");
		logger.debug("*************************");
		logger.debug("Recorded samplePairs, from the job submission:");		
		for(SampleSource ss : sampleSourceSet){//for each recorded samplePair
			Sample test = ss.getSample();//IP (or HpaII)
			Sample control = ss.getSourceSample();//input (or MspI)
			logger.debug("test - "+test.getName() + " : " + " control - " + control.getName());
		}
		//next, go through each entry of testControlListMap
		//to make it easy, use setOfApprovedSamples as the keys
		logger.debug("*************************");
		logger.debug("*************************");
		logger.debug("*************************Captured data:");		
		for(Sample approvedSample : setOfApprovedSamples){
			
			Sample testSample = approvedSample;
			logger.debug("testSample - "+testSample.getName());
			logger.debug("testSample's cells (if more than one, merge):");
			for(SampleSource cellLibrary : approvedSampleApprovedCellLibraryListMap.get(testSample)){
				logger.debug("--testSample's cellLibrary: " + sampleService.getCell(cellLibrary).getName());
			}
			List<Sample> approvedControlSampleList = testSampleControlSampleListMap.get(testSample);
			if(approvedControlSampleList.isEmpty()){
				logger.debug("--No paired control, so generate peaks without any control");
			}
			else{
				logger.debug("Control(s):");
				for(Sample control : approvedControlSampleList){
					logger.debug("Control: " + control.getName());
					logger.debug("controlSample's cells (if more than one, merge):");
					for(SampleSource cellLibrary : approvedSampleApprovedCellLibraryListMap.get(testSample)){
						logger.debug("--controlSample's cells: " + sampleService.getCell(cellLibrary).getName());
					}
				}
			}			
		}
		//approvedCellLibraryList (from the passed in approvedCellLibraryIdList)
		//setOfApprovedSamples
		//approvedSampleApprovedCellLibraryListMap
		//testSampleControlSampleListMap
		//
		chipseq = new ChipSeqSoftwareComponent();
//		WorkUnit w = chipseq.getChipSeqPeaks(approvedCellLibraryList.get(0), fileService.getFilesForCellLibraryByType(approvedCellLibraryList.get(0), bamFileType), approvedCellLibraryList.get(1), fileService.getFilesForCellLibraryByType(approvedCellLibraryList.get(1), bamFileType));
		
		
		
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
