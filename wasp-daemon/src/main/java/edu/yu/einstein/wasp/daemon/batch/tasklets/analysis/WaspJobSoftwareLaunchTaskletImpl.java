package edu.yu.einstein.wasp.daemon.batch.tasklets.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
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

import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionFixed;
import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.daemon.batch.tasklets.AbandonMessageHandlingTasklet;
import edu.yu.einstein.wasp.exception.SoftwareConfigurationException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.interfacing.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.util.SoftwareConfiguration;
import edu.yu.einstein.wasp.util.WaspJobContext;


public class WaspJobSoftwareLaunchTaskletImpl extends AbandonMessageHandlingTasklet implements WaspJobSoftwareLaunchTasklet {
	
	private static Logger logger = LoggerFactory.getLogger("WaspJobSoftwareLaunchTaskletImpl");
	
	private int messageTimeoutInMillis;
	
	private Map<String, String> additionalJobParameters = new HashMap<>();
	
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
	
	private JobService jobService;
	
	private SampleService sampleService;
	
	private WaspPluginRegistry waspPluginRegistry;
	
	private List<Integer> cellLibraryIds;
	
	private String task;
	
	// jobId may be set via setter in which case it overrides any values associated with the cellLibrarys.
	// If not set, at initialization step an attempt is made to obtain a unique jobId across the supplied cellLibrarys.
	private Integer jobId;
	
	private ResourceType softwareResourceType;
	 
	@Autowired
	@Override
	public void setWaspPluginRegistry(WaspPluginRegistry waspPluginRegistry) {
		this.waspPluginRegistry = waspPluginRegistry;
	}
	
	@Autowired
	@Override
	public void setJobService(JobService jobService) {
		this.jobService = jobService;
	}
	
	@Autowired
	@Override
	public void setSampleService(SampleService sampleService) {
		this.sampleService = sampleService;
	}
	
	public WaspJobSoftwareLaunchTaskletImpl() {}
	
	public WaspJobSoftwareLaunchTaskletImpl(List<Integer> cellLibraryIds, ResourceType softwareResourceType) {
		this.cellLibraryIds = cellLibraryIds;
		this.softwareResourceType = softwareResourceType;
		this.task = BatchJobTask.GENERIC; // default
	}
	
	public WaspJobSoftwareLaunchTaskletImpl(List<Integer> cellLibraryIds, ResourceType softwareResourceType, String task) {
		this.cellLibraryIds = cellLibraryIds;
		this.softwareResourceType = softwareResourceType;
		this.task = task;
	}
	
	public WaspJobSoftwareLaunchTaskletImpl(Integer cellLibraryId, ResourceType softwareResourceType) {
		setCellLibraryId(cellLibraryId);
		this.softwareResourceType = softwareResourceType;
	}
	
	public WaspJobSoftwareLaunchTaskletImpl(Integer cellLibraryId, ResourceType softwareResourceType, String task) {
		setCellLibraryId(cellLibraryId);
		this.softwareResourceType = softwareResourceType;
		this.task = task;
	}
	
	public WaspJobSoftwareLaunchTaskletImpl(ResourceType softwareResourceType, Integer jobId) {
		setJobId(jobId);
		this.softwareResourceType = softwareResourceType;
	}
	
	public WaspJobSoftwareLaunchTaskletImpl(ResourceType softwareResourceType, Integer jobId, String task) {
		setJobId(jobId);
		this.softwareResourceType = softwareResourceType;
		this.task = task;
	}

	@Override
	@Transactional("entityManager") // Omission of this results in: edu.yu.einstein.wasp.exception.JobContextInitializationException: could not initialize proxy - no Session
	@RetryOnExceptionFixed
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		WaspJobContext waspJobContext = new WaspJobContext(jobId, jobService);
		SoftwareConfiguration softwareConfig = waspJobContext.getConfiguredSoftware(softwareResourceType);
		if (softwareConfig == null){
			throw new SoftwareConfigurationException("No software could be configured for jobId=" + jobId + " with resourceType iname=" + softwareResourceType.getIName());
		}
		Map<String, String> jobParameters = softwareConfig.getParameters();
		jobParameters.putAll(additionalJobParameters);
		if (cellLibraryIds != null)
			jobParameters.put(WaspSoftwareJobParameters.CELL_LIBRARY_ID_LIST, WaspSoftwareJobParameters.getCellLibraryListAsParameterValue(cellLibraryIds));
		jobParameters.put(WaspSoftwareJobParameters.JOB_ID, jobId.toString());
		MessagingTemplate messagingTemplate = new MessagingTemplate();
		messagingTemplate.setReceiveTimeout(messageTimeoutInMillis);
		BatchJobProviding softwarePlugin = waspPluginRegistry.getPlugin(softwareConfig.getSoftware().getIName(), BatchJobProviding.class);
		String flowName = softwarePlugin.getBatchJobName(this.task);
		if (flowName == null)
			logger.warn("No generic flow found for plugin so cannot launch software : " + softwareConfig.getSoftware().getIName());
		BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate( 
				new BatchJobLaunchContext(flowName, jobParameters) );
		try {
			Message<BatchJobLaunchContext> launchMessage = batchJobLaunchMessageTemplate.build();
			logger.debug("Sending the following launch message via channel " + MessageChannelRegistry.LAUNCH_MESSAGE_CHANNEL + " : " + launchMessage);
			messagingTemplate.sendAndReceive(launchChannel, launchMessage);
		} catch (WaspMessageBuildingException e) {
			throw new MessagingException(e.getLocalizedMessage(), e);
		}
		return RepeatStatus.FINISHED;
	}


	@Override
	public void setCellLibraryId(Integer cellLibraryId) {
		this.cellLibraryIds = new ArrayList<Integer>();
		this.cellLibraryIds.add(cellLibraryId);
	}
	

	@Override
	public void setCellLibraryIds(List<Integer> cellLibraryIds) {
		this.cellLibraryIds = cellLibraryIds;
	}
	

	@Override
	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}
	

	@Override
	public void setTask(String task) {
		this.task = task;
	}
	
	@Override
	public Map<String, String> getAdditionalJobParameters() {
		return additionalJobParameters;
	}

	@Override
	public void setAdditionalJobParameters(Map<String, String> additionalJobParameters) {
		this.additionalJobParameters = additionalJobParameters;
	}

	@PostConstruct
	public void init(){
		super.init();
		// if jobId is not set, get it from the first cellLibrary in the list and check it is unique across all in the list
		// in this scenario (otherwise we have no idea which is supposed to be used)
		if (this.jobId == null){
			for (Integer cellLibraryId: cellLibraryIds){
				if (this.jobId == null){
					this.jobId = sampleService.getJobOfLibraryOnCell(
							sampleService.getSampleSourceDao().getSampleSourceBySampleSourceId(cellLibraryId) ).getId();
					continue;
				}
				if (!sampleService.getJobOfLibraryOnCell(sampleService.getSampleSourceDao().getSampleSourceBySampleSourceId(cellLibraryId)).getId()
						.equals(jobId))
					throw new RuntimeException("No master Wasp jobId was provided and the cellLibrarys do not all reference the same job so no master can be determined");
			}
		}
			
	}
	

}
