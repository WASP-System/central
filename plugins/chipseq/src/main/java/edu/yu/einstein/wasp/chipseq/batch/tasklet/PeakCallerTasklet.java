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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.chipseq.integration.messages.ChipSeqSoftwareJobParameters;
import edu.yu.einstein.wasp.daemon.batch.tasklets.LaunchManyJobsTasklet;
import edu.yu.einstein.wasp.exception.SoftwareConfigurationException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.interfacing.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.util.SoftwareConfiguration;
import edu.yu.einstein.wasp.util.WaspJobContext;

@Transactional("entityManager")
//public class PeakCallerTasklet extends WaspRemotingTasklet implements StepExecutionListener {
public class PeakCallerTasklet extends LaunchManyJobsTasklet {
		
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
	
	private ResourceType softwareResourceType;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public PeakCallerTasklet() {
		// proxy
	}

	public PeakCallerTasklet(ResourceType softwareResourceType) {
		this.softwareResourceType = softwareResourceType;
	}

	@Override
	@Transactional("entityManager")
	public void doExecute() {
		
		Map<String,JobParameter> jobParametersMap = getStepExecution().getJobParameters().getParameters();	
		Integer jobIdFromJobParameter = null;
		for (String key : getStepExecution().getJobParameters().getParameters().keySet()) {
			if(key.equalsIgnoreCase(WaspJobParameters.JOB_ID)){
				JobParameter jp = jobParametersMap.get(key);
				jobIdFromJobParameter = new Integer(jp.toString());
			}
		}
		
		Assert.assertTrue(jobIdFromJobParameter>0);
		Job job = jobService.getJobByJobId(jobIdFromJobParameter);
		Assert.assertTrue(job.getId()>0);
		
		List<SampleSource> approvedCellLibraryList = null;
		try{
			approvedCellLibraryList = sampleService.getCellLibrariesPassQCAndNoAggregateAnalysis(job);	
		}catch(Exception e){logger.debug("unable to obtain approvedCellLibraryList in PeakCallerTasklet; message = " + e.getMessage());}
		Assert.assertTrue( approvedCellLibraryList!=null && ! approvedCellLibraryList.isEmpty() );
		
		Assert.assertTrue(confirmCellLibrariesAssociatedWithBamFiles(approvedCellLibraryList));

		Map<Sample, List<SampleSource>> approvedSampleApprovedCellLibraryListMap = associateSampleWithCellLibraries(approvedCellLibraryList);//new HashMap<Sample, List<SampleSource>>();
		Set<Sample> setOfApprovedSamples = new HashSet<Sample>();//for a specific job (note: this really could have been a list)
		for (Sample approvedSample : approvedSampleApprovedCellLibraryListMap.keySet()) {
			setOfApprovedSamples.add(approvedSample);
		}
		Set<SampleSource> samplePairsAsSampleSourceSet = sampleService.getSamplePairsByJob(job);//each SampleSource represents a pair of samples
		Map<Sample, List<Sample>> testSampleControlSampleListMap = associateTestWithControls(setOfApprovedSamples, samplePairsAsSampleSourceSet);
		Assert.assertTrue(confirmSamplePairsAreOfSameSpecies(testSampleControlSampleListMap));

		for(Sample testSample : setOfApprovedSamples){
			if(isIP(testSample)){//as of 6-17-14, only call peaks for IP samples 
				//could also have if(isPunctate(sample) or is destined of IDR with macs, whihc whould need more info passed
				List<SampleSource> cellLibraryListForTest = approvedSampleApprovedCellLibraryListMap.get(testSample);
				Assert.assertTrue( ! cellLibraryListForTest.isEmpty() );
				List<Sample> controlSampleList = testSampleControlSampleListMap.get(testSample);
				if(controlSampleList.isEmpty()){//no control 
					prepareAndLaunchMessage(job, convertCellLibraryListToIdList(cellLibraryListForTest), new ArrayList<Integer>());
				}
				else{
					for(Sample controlSample : controlSampleList){	//currently, only single control permitted for any IP				
						List<SampleSource> cellLibraryListForControl = approvedSampleApprovedCellLibraryListMap.get(controlSample);
						Assert.assertTrue( ! cellLibraryListForControl.isEmpty() );
						prepareAndLaunchMessage(job, convertCellLibraryListToIdList(cellLibraryListForTest), convertCellLibraryListToIdList(cellLibraryListForControl));
					}
				}
			}
		}		
	}
	
	private boolean isIP(Sample sample){
		boolean retValue = false;
		List<SampleMeta> sampleMetaList = sample.getSampleMeta();
		for(SampleMeta sm : sampleMetaList){
			if(sm.getK().contains("inputOrIP")){
				if(sm.getV().equalsIgnoreCase("ip")){
					return true;
				}
			}
		}
		return retValue;
	}
	
	private boolean confirmCellLibrariesAssociatedWithBamFiles(List<SampleSource> cellLibraryList) {
		for(SampleSource cellLibrary : cellLibraryList){
			Set<FileGroup> fileGroupSetFromCellLibrary = fileService.getFilesForCellLibraryByType(cellLibrary, bamFileType);
			if(fileGroupSetFromCellLibrary.isEmpty()){//very unexpected
				logger.debug("no Bam files associated with cellLibrary"); 
				return false;
			}
		}
		return true;
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
	private boolean confirmSamplePairsAreOfSameSpecies(Map<Sample, List<Sample>> testSampleControlSampleListMap) {
		for (Sample testSample : testSampleControlSampleListMap.keySet()) {
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
					return false;
				}
			}
		}
		return true;
	}
	
	private List<Integer> convertCellLibraryListToIdList(List<SampleSource> cellLibraryList){
		List<Integer> list = new ArrayList<Integer>();
		for(SampleSource ss : cellLibraryList){
			list.add(ss.getId());
		}
		return list;
	}
		
	private void prepareAndLaunchMessage(Job job, List<Integer> testCellLibraryIdList, List<Integer> controlCellLibraryIdList){
		try{
			//this used to work in next line, but it no longer functions; instead we get error: lazy load problem    new WaspJobContext(jobId, jobService)
			WaspJobContext waspJobContext = new WaspJobContext(jobService.getJobAndSoftware(job));
			SoftwareConfiguration softwareConfig = waspJobContext.getConfiguredSoftware(this.softwareResourceType);
			if (softwareConfig == null){
				throw new SoftwareConfigurationException("No software could be configured for jobId=" + job.getId() + " with resourceType iname=" + softwareResourceType.getIName());
			}
			BatchJobProviding softwarePlugin = waspPluginRegistry.getPlugin(softwareConfig.getSoftware().getIName(), BatchJobProviding.class);
			String flowName = softwarePlugin.getBatchJobName(BatchJobTask.GENERIC);
			if (flowName == null){
				logger.warn("No generic flow found for plugin so cannot launch software : " + softwareConfig.getSoftware().getIName());
			}
			Assert.assertTrue(flowName != null && !flowName.isEmpty());
			logger.warn("Flowname : " + flowName);//for macstwo, flowname will be: edu.yu.einstein.wasp.macstwo.mainFlow
			Map<String, String> jobParameters = softwareConfig.getParameters();
			jobParameters.put(ChipSeqSoftwareJobParameters.TEST_LIBRARY_CELL_ID_LIST, WaspSoftwareJobParameters.getCellLibraryListAsParameterValue(testCellLibraryIdList));
			jobParameters.put(ChipSeqSoftwareJobParameters.CONTROL_LIBRARY_CELL_ID_LIST, WaspSoftwareJobParameters.getCellLibraryListAsParameterValue(controlCellLibraryIdList));
			jobParameters.put(ChipSeqSoftwareJobParameters.JOB_ID, job.getId().toString());
			
			requestLaunch(flowName, jobParameters);
		}	
		catch(Exception e){
			logger.warn("e.message()   =   " + e.getMessage());
			throw new MessagingException(e.getLocalizedMessage(), e);
		}
	}


}
