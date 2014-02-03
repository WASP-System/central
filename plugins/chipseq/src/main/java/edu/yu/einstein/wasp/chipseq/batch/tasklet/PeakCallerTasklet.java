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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessagingTemplate;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionExponential;
import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.chipseq.software.ChipSeqSoftwareComponent;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.SoftwareConfigurationException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
//import edu.yu.einstein.wasp.gatk.software.GATKSoftwareComponent;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.util.SoftwareConfiguration;
import edu.yu.einstein.wasp.util.WaspJobContext;


public class PeakCallerTasklet extends WaspTasklet implements StepExecutionListener {

	private int messageTimeoutInMillis;
	
	/**
	 * Set the timeout when waiting for reply (in millis).  Default 5000 (5s).
	 */
	@Value(value="${wasp.message.timeout:5000}")
	public void setMessageTimeoutInMillis(int messageTimeout) {
		this.messageTimeoutInMillis = messageTimeout;
	}
	
	private List<Integer> approvedCellLibraryIdList;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private GenomeService genomeService;

	
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
		Assert.assertTrue( ! this.approvedCellLibraryIdList.isEmpty() );
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
		List<SampleSource> approvedCellLibraryList = getApprovedCellLibraries(this.approvedCellLibraryIdList);
		logger.debug("***************in PeakCallerTasklet.execute(): approvedCellLibraryList.size() is " + approvedCellLibraryList.size());
		confirmCellLibrariesAssociatedWithBamFiles(approvedCellLibraryList);//throws exception if no
		Job job = confirmCellLibrariesFromSingleJob(approvedCellLibraryList);//throws exception if no; need job this since samplePairs are by job 
		Assert.assertTrue(job!=null&&job.getId()!=null);
		Map<Sample, List<SampleSource>> approvedSampleApprovedCellLibraryListMap = associateSampleWithCellLibraries(approvedCellLibraryList);//new HashMap<Sample, List<SampleSource>>();
		Set<Sample> setOfApprovedSamples = new HashSet<Sample>();//for a specific job
		for (Map.Entry<Sample, List<SampleSource>> entry : approvedSampleApprovedCellLibraryListMap.entrySet()) {
			Sample approvedSample = (Sample) entry.getKey();
			setOfApprovedSamples.add(approvedSample);			
		}	
		
		Set<SampleSource> samplePairsAsSampleSourceSet = sampleService.getSamplePairsByJob(job);
		Map<Sample, List<Sample>> testSampleControlSampleListMap = associateTestWithControls(setOfApprovedSamples, samplePairsAsSampleSourceSet);
		confirmSamplePairsAreOfSameSpecies(testSampleControlSampleListMap);//throws exception if no
		/*  
		//output for testing purposes only:
		//first, print out recorded samplePairs, if any
		logger.debug("*************************");
		logger.debug("*************************");
		logger.debug("Recorded samplePairs, from the job submission:");		
		for(SampleSource ss : samplePairsAsSampleSourceSet){//for each recorded samplePair
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
		 */
		for(Sample testSample : setOfApprovedSamples){
			//prepare message for peakcaller plugin (here, MACS2)
			List<SampleSource> cellLibraryListForTest = approvedSampleApprovedCellLibraryListMap.get(testSample);
			Build build = genomeService.getBuild(testSample);//we already confirmed (above) that testSample and controlSample have same genome	(BUT WE NEVER DID THIS FOR THE BAM FILES THAT WILL BE USED)		
			Assert.assertTrue( ! cellLibraryListForTest.isEmpty() );
			List<Sample> controlSampleList = testSampleControlSampleListMap.get(testSample);
			if(controlSampleList.isEmpty()){//no control (input sample)
				launchMessage(job.getId(), convertCellLibraryListToIdList(cellLibraryListForTest), new ArrayList<Integer>());
			}
			else{
				for(Sample controlSample : controlSampleList){
					List<SampleSource> cellLibraryListForControl = approvedSampleApprovedCellLibraryListMap.get(controlSample);
					Assert.assertTrue( ! cellLibraryListForControl.isEmpty() );
					launchMessage(job.getId(), convertCellLibraryListToIdList(cellLibraryListForTest), convertCellLibraryListToIdList(cellLibraryListForControl));
				}
			}
		}
		return RepeatStatus.FINISHED;
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
	private Job confirmCellLibrariesFromSingleJob(List<SampleSource> cellLibraryList) throws Exception{
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
		return job;
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
	private Map<Sample, List<Sample>> associateTestWithControls(Set<Sample> sampleSet, Set<SampleSource> samplePairsAsSampleSourceSet){
		Map<Sample, List<Sample>> testSampleControlSampleListMap = new HashMap<Sample, List<Sample>>();
		
		//do not forget that a sample destined for a paired analysis (chipseq or helptag) 
		//MAY NOT be on the samplePair list (but still needs to be dealt with).
		//For chipseq, since the grid of pairs (on the submission forms) can be skipped, we may not know
		//which are IP and which are inputs. However, for helptag it is be different, 
		//since, regardless of the gird of pairs, we DO STILL KNOW which are MspI and which are HpaII
		for(Sample sample : sampleSet){
			List<Sample> controlList = new ArrayList<Sample>();
			for(SampleSource ss : samplePairsAsSampleSourceSet){//for each recorded samplePair
				Sample test = ss.getSample();//IP (or HpaII)
				Sample control = ss.getSourceSample();//input (or MspI)
				//System.out.println("----control = " + control.getName() + " AND test = " + test.getName());
				if(sample.getId().intValue() == test.getId().intValue()){
					if(sampleSet.contains(control)){//no sense adding to controlList if the control is not also within sampleSet
						controlList.add(control);
					}
				}
			}
			testSampleControlSampleListMap.put(sample, controlList);//execute this line even if controlList is empty; approvedSample could be an IP without any input paired with it (and approvedSample could also be an input)				
		}
		return testSampleControlSampleListMap;
	}
	private void confirmSamplePairsAreOfSameSpecies(Map<Sample, List<Sample>> testSampleControlSampleListMap) throws Exception{
		for (Map.Entry<Sample, List<Sample>> entry : testSampleControlSampleListMap.entrySet()) {
			Sample testSample = (Sample) entry.getKey();
			Integer testSampleOrganismId = sampleService.getIdOfOrganism(testSample);
			List<Sample> controlSampleList = testSampleControlSampleListMap.get(testSample);
			if(controlSampleList.isEmpty()){
				continue;
			}
			for(Sample controlSample : controlSampleList){
				if(testSampleOrganismId.intValue()!=sampleService.getIdOfOrganism(controlSample)){
					logger.debug("samplePairs must be of same organism"); 
					throw new Exception("samplePairs must be of same organism");
				}
			}
		}
	}
	private List<Integer> convertCellLibraryListToIdList(List<SampleSource> cellLibraryList){
		List<Integer> list = new ArrayList<Integer>();
		for(SampleSource ss : cellLibraryList){
			list.add(ss.getId());
		}
		return list;
	}
	
	private void launchMessage(Integer jobId, List<Integer> testCellLibraryIdList, List<Integer> controlCellLibraryIdList){
		/*
		WaspJobContext waspJobContext = new WaspJobContext(jobId, jobService);
		SoftwareConfiguration softwareConfig = waspJobContext.getConfiguredSoftware(this.softwareResourceType);
		if (softwareConfig == null){
			throw new SoftwareConfigurationException("No software could be configured for jobId=" + jobId + " with resourceType iname=" + softwareResourceType.getIName());
		}
		Map<String, String> jobParameters = softwareConfig.getParameters();
		jobParameters.put(WaspSoftwareJobParameters.TEST_LIBRARY_CELL_ID_LIST, WaspSoftwareJobParameters.getLibraryCellListAsParameterValue(testCellLibraryIdList));
		jobParameters.put(WaspSoftwareJobParameters.CONTROL_LIBRARY_CELL_ID_LIST, WaspSoftwareJobParameters.getLibraryCellListAsParameterValue(controlCellLibraryIdList));
		MessagingTemplate messagingTemplate = new MessagingTemplate();
		messagingTemplate.setReceiveTimeout(messageTimeoutInMillis);
		BatchJobProviding softwarePlugin = waspPluginRegistry.getPlugin(softwareConfig.getSoftware().getIName(), BatchJobProviding.class);
		String flowName = softwarePlugin.getBatchJobName(this.task);
		if (flowName == null)
			logger.warn("No generic flow found for plugin so cannot launch software : " + softwareConfig.getSoftware().getIName());
		BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = 
				new BatchJobLaunchMessageTemplate( new BatchJobLaunchContext(flowName, jobParameters) );
		try {
			Message<BatchJobLaunchContext> launchMessage = batchJobLaunchMessageTemplate.build();
			logger.debug("Sending the following launch message via channel " + MessageChannelRegistry.LAUNCH_MESSAGE_CHANNEL + " : " + launchMessage);
			messagingTemplate.sendAndReceive(launchChannel, launchMessage);
		} catch (WaspMessageBuildingException e) {
			throw new MessagingException(e.getLocalizedMessage(), e);
		}
		*/
	}
}
