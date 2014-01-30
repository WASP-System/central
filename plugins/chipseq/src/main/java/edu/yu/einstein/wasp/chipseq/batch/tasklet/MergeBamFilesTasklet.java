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
import edu.yu.einstein.wasp.exception.SampleTypeException;
//import edu.yu.einstein.wasp.gatk.software.GATKSoftwareComponent;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
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


public class MergeBamFilesTasklet extends WaspTasklet implements StepExecutionListener {

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

	public MergeBamFilesTasklet() {
		// proxy
	}

	public MergeBamFilesTasklet(String cellLibraryIdListAsString, ResourceType softwareResourceType) {
		
		this.approvedCellLibraryIdList = WaspSoftwareJobParameters.getLibraryCellIdList(cellLibraryIdListAsString);
		Assert.assertTrue( ! approvedCellLibraryIdList.isEmpty() );
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
		
		RepeatStatus repeatStatus = super.execute(contrib, context);
		if (repeatStatus.equals(RepeatStatus.FINISHED))
			return RepeatStatus.FINISHED;
		
		//the software for this should be picard
		
		logger.debug("***************in MergeBamFilesTasklet.execute(): softwareResourceType for peakcaller is " + this.softwareResourceType.getName());
		logger.debug("***************in MergeBamFilesTasklet.execute(): approvedCellLibraryIdList.size() is " + this.approvedCellLibraryIdList.size());
		
		List<SampleSource> approvedCellLibraryList = getApprovedCellLibraries(this.approvedCellLibraryIdList);
		confirmCellLibrariesFromSingleJob(approvedCellLibraryList);//throws exception if no
		confirmCellLibrariesAssociatedWithBamFiles(approvedCellLibraryList);//throws exception if no
		
		Map<Sample, List<SampleSource>> approvedSampleApprovedCellLibraryListMap = associateSampleWithCellLibraries(approvedCellLibraryList);//new HashMap<Sample, List<SampleSource>>();
		Map<Sample, List<SampleSource>> approvedSampleApprovedCellLibraryListMapRequiringBamMerge = new HashMap<Sample, List<SampleSource>>();
		StringBuilder approvedSampleIdListAsStringBuilder = new StringBuilder();
		for (Map.Entry<Sample, List<SampleSource>> entry : approvedSampleApprovedCellLibraryListMap.entrySet()) {
			Sample sample = (Sample) entry.getKey();
			if(approvedSampleIdListAsStringBuilder.length()>0){
				approvedSampleIdListAsStringBuilder.append(",");
			}
			approvedSampleIdListAsStringBuilder.append(sample.getId().toString());
			Set<FileGroup> fileGroupSetFromSample = fileService.getFilesForMacromoleculeOrLibraryByType(sample, bamFileType);
			if( ! fileGroupSetFromSample.isEmpty() ){//bam files for sample already exist, so apparently already merged. nothing to do here for this sample
				continue;
			}
			List<SampleSource> cellLibraryList = approvedSampleApprovedCellLibraryListMap.get(sample);
			if(cellLibraryList.size()==1){//single cellLibrary for the sample, so no merge necessary
				Set<FileGroup> fileGroupSetFromCellLibrary = fileService.getFilesForCellLibraryByType(cellLibraryList.get(0), bamFileType);
				if(fileGroupSetFromCellLibrary.isEmpty()){//very unlikely, as we checked above
					logger.debug("no Bam files associated with cellLibrary"); 
					throw new Exception("no Bam files associated with cellLibrary");
				}
				for(FileGroup fg : fileGroupSetFromCellLibrary){
					fileService.setSampleFile(fg, sample);
				}
			}
			else{//more than one cellLibrary for this sample, so prepare to merge
				approvedSampleApprovedCellLibraryListMapRequiringBamMerge.put(sample, approvedCellLibraryList);
			}
		}
		
		if(approvedSampleApprovedCellLibraryListMapRequiringBamMerge.isEmpty()){
			return RepeatStatus.FINISHED;
		}
		String approvedSampleIdListAsString = new String(approvedSampleIdListAsStringBuilder);
		////ADD approvedSampleIdListAsString to context for reuse later
		
		chipseq = new ChipSeqSoftwareComponent();
		WorkUnit w = chipseq.getMergedBam(approvedSampleApprovedCellLibraryListMapRequiringBamMerge);

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

	private List<SampleSource> getApprovedCellLibraries(List<Integer> approvedCellLibraryIdList) throws SampleTypeException{
		List<SampleSource> approvedCellLibraryList = new ArrayList<SampleSource>();
		for(Integer approvedCellLibraryId : approvedCellLibraryIdList){
			SampleSource approvedCellLibrary = sampleService.getCellLibraryBySampleSourceId(approvedCellLibraryId);
			approvedCellLibraryList.add(approvedCellLibrary);
		}
		return approvedCellLibraryList;
	}
	private void confirmCellLibrariesFromSingleJob(List<SampleSource> cellLibraryList) throws Exception{
		Job job = null;
		for(SampleSource cellLibrary : cellLibraryList){
			if(job==null){
				job = sampleService.getJobOfLibraryOnCell(cellLibrary);
			}
			else{
				if(job.getId().intValue()!=sampleService.getJobOfLibraryOnCell(cellLibrary).getId().intValue()){
					logger.debug("NOT ALL cellLibraries ARE FROM THE SAME JOB! Do not proceed!");
					throw new Exception("Not all cellLibraries are from the same job");
				}
			}
		}
	}
	private void confirmCellLibrariesAssociatedWithBamFiles(List<SampleSource> cellLibraryList) throws Exception{
		for(SampleSource cellLibrary : cellLibraryList){
			Set<FileGroup> fileGroupSetFromCellLibrary = fileService.getFilesForCellLibraryByType(cellLibrary, bamFileType);
			if(fileGroupSetFromCellLibrary.isEmpty()){//very unexpected
				logger.debug("no Bam files associated with cellLibrary"); 
				throw new Exception("no Bam files associated with cellLibrary");
			}
		}
	}
	
	private Map<Sample, List<SampleSource>> associateSampleWithCellLibraries(List<SampleSource> cellLibraryList){
		Map<Sample, List<SampleSource>> sampleCellLibraryListMap = new HashMap<Sample, List<SampleSource>>();
		for(SampleSource cellLibrary : cellLibraryList){
			
			Sample parentSample = sampleService.getLibrary(cellLibrary);
			
			while(parentSample.getParentId()!=null){
				parentSample = parentSample.getParent();//get the uppermost sample associated with these cellLibraries
			}
			if(sampleCellLibraryListMap.containsKey(parentSample)){
				sampleCellLibraryListMap.get(parentSample).add(cellLibrary);
			}
			else{
				List<SampleSource> cellLibraryListForASample = new ArrayList<SampleSource>();
				cellLibraryListForASample.add(cellLibrary);
				sampleCellLibraryListMap.put(parentSample, cellLibraryListForASample);
			}
		}
		return sampleCellLibraryListMap;
	}

}

