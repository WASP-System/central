package edu.yu.einstein.wasp.chipseq.batch.tasklet;

/**
 * R. Dubin
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.chipseq.integration.messages.ChipSeqSoftwareJobParameters;
import edu.yu.einstein.wasp.chipseq.software.ChipSeqSoftwareComponent;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.SoftwareConfigurationException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.interfacing.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.WaspMessageHandlingService;
import edu.yu.einstein.wasp.util.SoftwareConfiguration;
import edu.yu.einstein.wasp.util.WaspJobContext;
//import edu.yu.einstein.wasp.gatk.software.GATKSoftwareComponent;

@Transactional("entityManager")
public class PeakCallerTasklet extends WaspRemotingTasklet implements StepExecutionListener {

	private int messageTimeoutInMillis;
	
	/**
	 * Set the timeout when waiting for reply (in millis).  Default 5000 (5s).
	 */
	@Value(value="${wasp.message.timeout:5000}")
	public void setMessageTimeoutInMillis(int messageTimeout) {
		this.messageTimeoutInMillis = messageTimeout;
	}
	
	private QueueChannel launchChannel; // channel to send messages out of system
	
	@Autowired
	@Qualifier(MessageChannelRegistry.LAUNCH_MESSAGE_CHANNEL)
	public void setLaunchChannel(QueueChannel launchChannel) {
		this.launchChannel = launchChannel;
	}
	
	@Autowired
	@Qualifier("waspMessageHandlingServiceImpl") // more than one class of type WaspMessageHandlingService so must specify
	private WaspMessageHandlingService waspMessageHandlingService;

	@Autowired
	private FileService fileService;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private GenomeService genomeService;
	
	@Autowired
	private WaspPluginRegistry waspPluginRegistry;

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

	public PeakCallerTasklet(ResourceType softwareResourceType) {
		this.softwareResourceType = softwareResourceType;
		logger.debug("***************in PeakCallerTasklet() constructor: softwareResourceType for peakcaller is " + this.softwareResourceType.getName());
	}

	//TODO: remove this next method
	@Override
	@Transactional("entityManager")
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		this.doExecute(context);
		return RepeatStatus.FINISHED;
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
		
		Map<String,Object> jobParametersMap = context.getStepContext().getJobParameters();		
		Integer jobIdFromJobParameter = null;
		for (String key : jobParametersMap.keySet()) {
			logger.debug("***************in PeakCallerTasklet.execute(): jobParametersMap Key: " + key + " Value: " + jobParametersMap.get(key).toString());
			if(key.equalsIgnoreCase(WaspJobParameters.JOB_ID)){
				jobIdFromJobParameter = new Integer((String)jobParametersMap.get(key));
			}
		}
		Assert.assertTrue(jobIdFromJobParameter>0);
		Job job = jobService.getJobByJobId(jobIdFromJobParameter);
		logger.debug("***************in PeakCallerTasklet.execute(): job.getId() = " + job.getId().toString());
		Assert.assertTrue(job.getId()>0);

//TODO: ROBERT A DUBIN (another) uncomment next line and comment out the one immediately after for production   !!!!!!!!!!!!!!!!!!!!!!!!  
		List<SampleSource> approvedCellLibraryList = sampleService.getCellLibrariesPassQCAndNoAggregateAnalysis(job);		
		//List<SampleSource> approvedCellLibraryList = new ArrayList<SampleSource>(sampleService.getCellLibrariesForJob(job));//sampleService.getCellLibrariesPassQCAndNoAggregateAnalysis(job);		

		logger.debug("***************in PeakCallerTasklet.execute(): approvedCellLibraryList.size() is " + approvedCellLibraryList.size());
		Assert.assertTrue( ! approvedCellLibraryList.isEmpty() );
		
//TODO: ROBERT A DUBIN (1 of 1) uncomment next line for production   !!!!!!!!!!!!!!!!!!!!!!!!   
		confirmCellLibrariesAssociatedWithBamFiles(approvedCellLibraryList);//throws exception if no

		Map<Sample, List<SampleSource>> approvedSampleApprovedCellLibraryListMap = associateSampleWithCellLibraries(approvedCellLibraryList);//new HashMap<Sample, List<SampleSource>>();
		Set<Sample> setOfApprovedSamples = new HashSet<Sample>();//for a specific job (note: this really could have been a list)
		for (Sample approvedSample : approvedSampleApprovedCellLibraryListMap.keySet()) {
			setOfApprovedSamples.add(approvedSample);
		}
		Set<SampleSource> samplePairsAsSampleSourceSet = sampleService.getSamplePairsByJob(job);//each SampleSource represents a pair of samples
		Map<Sample, List<Sample>> testSampleControlSampleListMap = associateTestWithControls(setOfApprovedSamples, samplePairsAsSampleSourceSet);
		confirmSamplePairsAreOfSameSpecies(testSampleControlSampleListMap);//throws exception if no
		logger.debug("***************in PeakCallerTasklet.execute(): confirmed Sample Pairs are of same species");
		
		//*
		//output for testing purposes only:
		//first, print out recorded jobId and samplePairs, if any
		logger.debug("*************************JobId : " + job.getId());
		logger.debug("*************************SamplePairs");
		logger.debug("Recorded samplePairs, from the job submission:");		
		for(SampleSource ss : samplePairsAsSampleSourceSet){//for each recorded samplePair
			Sample test = ss.getSample();//IP (or HpaII)
			Sample control = ss.getSourceSample();//input (or MspI)
			logger.debug("test - "+test.getName() + " : " + " control - " + control.getName());
		}
		//next, go through each entry of testControlListMap
		//to make it easy, use setOfApprovedSamples as the keys
		logger.debug("*************************Captured data:");		
		for(Sample approvedSample : setOfApprovedSamples){
			
			Sample testSample = approvedSample;
			logger.debug("testSample - "+testSample.getName() + " with sampleId of " + testSample.getId());
			List<Sample> approvedControlSampleList = testSampleControlSampleListMap.get(testSample);
			if(approvedControlSampleList.isEmpty()){
				logger.debug("--No paired control, so generate peaks without any control");
			}
			else{
				logger.debug("Control(s):");
				for(Sample control : approvedControlSampleList){
					logger.debug("Control: " + control.getName());
				}
			}			
		} 
		//*/
		for(Sample testSample : setOfApprovedSamples){
			
			Thread.sleep(10000);//this is here for testing only, but certainly will not hurt, as it's merely a 10 second delay
			
			logger.debug("***************in PeakCallerTasklet.execute(): preparing to launchMessage to Macs2 for testSample: " + testSample.getName());
			List<SampleSource> cellLibraryListForTest = approvedSampleApprovedCellLibraryListMap.get(testSample);
			Assert.assertTrue( ! cellLibraryListForTest.isEmpty() );
			List<Sample> controlSampleList = testSampleControlSampleListMap.get(testSample);
			logger.debug("***************in PeakCallerTasklet.execute(): immediately prior to if statment");
			if(controlSampleList.isEmpty()){//no control (input sample)
				logger.debug("***************in PeakCallerTasklet.execute(): just prior to  launchMessage call where controlSample is empty");
				launchMessage(job.getId(), convertCellLibraryListToIdList(cellLibraryListForTest), new ArrayList<Integer>());
			}
			else{
				for(Sample controlSample : controlSampleList){					
					logger.debug("***************in PeakCallerTasklet.execute(): just prior to  launchMessage call where controlSample is NOT EMPTY");
					List<SampleSource> cellLibraryListForControl = approvedSampleApprovedCellLibraryListMap.get(controlSample);
					Assert.assertTrue( ! cellLibraryListForControl.isEmpty() );
					launchMessage(job.getId(), convertCellLibraryListToIdList(cellLibraryListForTest), convertCellLibraryListToIdList(cellLibraryListForControl));
				}
			}
		}
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
				//get the uppermost sample associated with these cellLibraries
				//parentSample = parentSample.getParent();//this call could have possible lazy loading issues; 02-18-14
				parentSample = sampleService.getSampleById(parentSample.getParentId());//avoids any possible downstream lazy loading issues
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
		//which are IP and which are inputs. (as of 2-24-14, we actually know which are inputs and which are IP samples)
		//However, for helptag it is be different, 
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
		//for (Map.Entry<Sample, List<Sample>> entry : testSampleControlSampleListMap.entrySet()) { //02-18-14
		for (Sample testSample : testSampleControlSampleListMap.keySet()) {
			//Sample testSample = (Sample) entry.getKey();
			//need to get the meta ourselves and load it up, since lazy loading doesn't appear to be working properly
			testSample.setSampleMeta(sampleService.getSampleMetaDao().getSamplesMetaBySampleId(testSample.getId()));
			Integer testSampleOrganismId = sampleService.getIdOfOrganism(testSample);
			List<Sample> controlSampleList = testSampleControlSampleListMap.get(testSample);
			if(controlSampleList.isEmpty()){
				continue;
			}
			for(Sample controlSample : controlSampleList){
				controlSample.setSampleMeta(sampleService.getSampleMetaDao().getSamplesMetaBySampleId(controlSample.getId()));
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
		try{
			logger.warn("*************entered launchMessage method");
			WaspJobContext waspJobContext = new WaspJobContext(jobId, jobService);
			SoftwareConfiguration softwareConfig = waspJobContext.getConfiguredSoftware(this.softwareResourceType);
			if (softwareConfig == null){
				throw new SoftwareConfigurationException("No software could be configured for jobId=" + jobId + " with resourceType iname=" + softwareResourceType.getIName());
			}
			BatchJobProviding softwarePlugin = waspPluginRegistry.getPlugin(softwareConfig.getSoftware().getIName(), BatchJobProviding.class);
			String flowName = softwarePlugin.getBatchJobName(BatchJobTask.GENERIC);
			if (flowName == null){
				logger.warn("No generic flow found for plugin so cannot launch software : " + softwareConfig.getSoftware().getIName());
			}
			logger.warn("Flowname : " + flowName);//for macstwo, flowname will be: edu.yu.einstein.wasp.macstwo.mainFlow
			Map<String, String> jobParameters = softwareConfig.getParameters();
			jobParameters.put(ChipSeqSoftwareJobParameters.TEST_LIBRARY_CELL_ID_LIST, WaspSoftwareJobParameters.getCellLibraryListAsParameterValue(testCellLibraryIdList));
			jobParameters.put(ChipSeqSoftwareJobParameters.CONTROL_LIBRARY_CELL_ID_LIST, WaspSoftwareJobParameters.getCellLibraryListAsParameterValue(controlCellLibraryIdList));
			jobParameters.put(ChipSeqSoftwareJobParameters.JOB_ID, jobId.toString());
			
			//for testing only!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			//DO NOT use this in production
			/*
			jobParameters.put("test", new Date().toString());			
			*/
			
			//this next line works, but was replaced with the subsequent 7 lines, and the WaspMessageBuildingException exception
			//waspMessageHandlingService.launchBatchJob(flowName, jobParameters);
			
			MessagingTemplate messagingTemplate = new MessagingTemplate();
			messagingTemplate.setReceiveTimeout(messageTimeoutInMillis);
			BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate( new BatchJobLaunchContext(flowName, jobParameters) );	
			Message<BatchJobLaunchContext> launchMessage = batchJobLaunchMessageTemplate.build();
			logger.warn("*************launched message from peakcaller");
			logger.debug("Sending the following launch message via channel " + MessageChannelRegistry.LAUNCH_MESSAGE_CHANNEL + " : " + launchMessage);
			messagingTemplate.sendAndReceive(launchChannel, launchMessage);
		}
		 catch (WaspMessageBuildingException e) {
			 logger.warn("*************threw WaspMessageBuildingException exception in peakcallerTasklet.launchMessage()*************");
			throw new MessagingException(e.getLocalizedMessage(), e);
		}	
		catch(Exception e){
			logger.warn("*************threw exception in peakcallerTasklet.launchMessage()*************");
			logger.warn("*************e.message()   =   " + e.getMessage());
			throw new MessagingException(e.getLocalizedMessage(), e);
		}
		

		
	}
}
