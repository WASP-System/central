package edu.yu.einstein.wasp.helptag.batch.tasklet;

/**
 * AJ Jing
 */

import java.util.ArrayList;
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
import edu.yu.einstein.wasp.daemon.batch.tasklets.LaunchManyJobsTasklet;
import edu.yu.einstein.wasp.exception.SoftwareConfigurationException;
import edu.yu.einstein.wasp.helptag.integration.messages.HelptagSoftwareJobParameters;
import edu.yu.einstein.wasp.helptag.service.HelptagService;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.interfacing.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwareConfiguration;
import edu.yu.einstein.wasp.util.WaspJobContext;

@Transactional("entityManager")
public class AggrAngleMakerTasklet extends LaunchManyJobsTasklet {
	
	@Autowired
	private HelptagService helptagService;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private WaspPluginRegistry waspPluginRegistry;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private ResourceType softwareResourceType;

	public AggrAngleMakerTasklet() {
		// proxy
	}

	public AggrAngleMakerTasklet(ResourceType softwareResourceType) {
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
			logger.debug("approvedCellLibraryList for jobId " + jobIdFromJobParameter + ":\n" + approvedCellLibraryList.toString());
		}catch(Exception e){
			logger.debug("unable to obtain approvedCellLibraryList in AngleMakerTasklet; message = " + e.getMessage());
		}
		Assert.assertTrue(approvedCellLibraryList != null && !approvedCellLibraryList.isEmpty());
		Assert.assertTrue(helptagService.confirmCellLibrariesAssociatedWithHcountFiles(approvedCellLibraryList));

		Map<Sample, List<SampleSource>> approvedSampleApprovedCellLibraryListMap = sampleService.associateUppermostSampleWithCellLibraries(approvedCellLibraryList);
		Set<Sample> setOfApprovedSamples = new HashSet<Sample>();//for a specific job (note: this really could have been a list)
		for (Sample approvedSample : approvedSampleApprovedCellLibraryListMap.keySet()) {
			setOfApprovedSamples.add(approvedSample);
		}
		
		Set<Sample> sampleSetOfHpaIIPairedWithMspI = new HashSet<Sample>();
		for(Sample approvedSample : setOfApprovedSamples){
			logger.debug("approvedSample : " + approvedSample.getName());
			if (helptagService.isHpaII(approvedSample) || helptagService.isBetaGTMspI(approvedSample)) {
				// only make angles for HpaII or beta-GT-MspI samples
				List<SampleSource> cellLibraryListForHpaII = approvedSampleApprovedCellLibraryListMap.get(approvedSample);

				for(SampleSource ss : sampleService.getSamplePairsByJob(job)){
					Sample hpaSample = ss.getSample();
					Assert.assertTrue(helptagService.isHpaII(hpaSample) || helptagService.isBetaGTMspI(hpaSample));

    				Sample mspSample = ss.getSourceSample();
					Assert.assertTrue(helptagService.isMspI(mspSample));

    				if (approvedSample.getId().intValue() == hpaSample.getId().intValue()) {
						if (setOfApprovedSamples.contains(mspSample)) {
							Assert.assertTrue(sampleService.confirmSamplePairIsOfSameSpecies(hpaSample, mspSample));
							List<SampleSource> cellLibraryListForMspI = approvedSampleApprovedCellLibraryListMap.get(mspSample);
							sampleSetOfHpaIIPairedWithMspI.add(approvedSample);
							prepareAndLaunchMessage(job, sampleService.convertCellLibraryListToIdList(cellLibraryListForHpaII),
													sampleService.convertCellLibraryListToIdList(cellLibraryListForMspI));
						}
					}
				}
				if (!sampleSetOfHpaIIPairedWithMspI.contains(approvedSample)) {
					// not on samplePair List, or it is on samplePair List but there are no BAM files for the requested mspI
					prepareAndLaunchMessage(job, sampleService.convertCellLibraryListToIdList(cellLibraryListForHpaII), new ArrayList<Integer>());
				}
			}
		}		
	}

	private void prepareAndLaunchMessage(Job job, List<Integer> hpa2CellLibraryIdList, List<Integer> msp1CellLibraryIdList) {
		try{
			//this used to work in next line, but it no longer functions; instead we get error: lazy load problem    new WaspJobContext(jobId, jobService)
			WaspJobContext waspJobContext = new WaspJobContext(jobService.getJobAndSoftware(job));
			logger.debug("softwareResourceType iname=" + this.softwareResourceType.getIName());
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
			logger.warn("Flowname : " + flowName);// for helptagHAM, flowname will be: edu.yu.einstein.wasp.helptagham.mainFlow
			Map<String, String> jobParameters = softwareConfig.getParameters();
			jobParameters.put(HelptagSoftwareJobParameters.HPA2_LIBRARY_CELL_ID_LIST,
							  WaspSoftwareJobParameters.getCellLibraryListAsParameterValue(hpa2CellLibraryIdList));
			jobParameters.put(HelptagSoftwareJobParameters.MSP1_LIBRARY_CELL_ID_LIST,
							  WaspSoftwareJobParameters.getCellLibraryListAsParameterValue(msp1CellLibraryIdList));
			jobParameters.put(HelptagSoftwareJobParameters.JOB_ID, job.getId().toString());
			
			requestLaunch(flowName, jobParameters);
		}	
		catch(Exception e){
			logger.warn("e.message()   =   " + e.getMessage());
			throw new MessagingException(e.getLocalizedMessage(), e);
		}
	}

}
