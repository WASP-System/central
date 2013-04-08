package edu.yu.einstein.wasp.daemon.batch.tasklets.analysis;

import java.util.ArrayList;
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

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet;
import edu.yu.einstein.wasp.daemon.service.BatchJobService;
import edu.yu.einstein.wasp.exception.SoftwareConfigurationException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.util.SoftwareConfiguration;
import edu.yu.einstein.wasp.util.WaspJobContext;

public class WaspJobSoftwareLaunchTasklet extends WaspTasklet {
	
	private static Logger logger = LoggerFactory.getLogger("WaspJobSoftwareLaunchTasklet");
	
	private int messageTimeoutInMillis;
	
	@Autowired
	private BatchJobService batchJobService;
	
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
	
	private List<Integer> libraryCellIds;
	
	private String task;
	
	// jobId may be set via setter in which case it overrides any values associated with the libraryCells.
	// If not set, at initialization step an attempt is made to obtain a unique jobId across the supplied libraryCells.
	private Integer jobId;
	
	private ResourceType softwareResourceType;
	 
	@Autowired
	public void setWaspPluginRegistry(WaspPluginRegistry waspPluginRegistry) {
		this.waspPluginRegistry = waspPluginRegistry;
	}
	
	@Autowired
	public void setJobService(JobService jobService) {
		this.jobService = jobService;
	}
	
	@Autowired
	public void setSampleService(SampleService sampleService) {
		this.sampleService = sampleService;
	}
	
	public WaspJobSoftwareLaunchTasklet(List<Integer> libraryCellIds, ResourceType softwareResourceType) {
		this.libraryCellIds = libraryCellIds;
		this.softwareResourceType = softwareResourceType;
		this.task = BatchJobTask.GENERIC; // default
	}
	
	public WaspJobSoftwareLaunchTasklet(List<Integer> libraryCellIds, ResourceType softwareResourceType, String task) {
		this.libraryCellIds = libraryCellIds;
		this.softwareResourceType = softwareResourceType;
		this.task = task;
	}
	
	public WaspJobSoftwareLaunchTasklet(Integer libraryCellId, ResourceType softwareResourceType) {
		setLibraryCellId(libraryCellId);
		this.softwareResourceType = softwareResourceType;
	}

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		Message<?> reply = batchJobService.launchAnalysisJob(jobId, softwareResourceType, libraryCellIds, this.task, messageTimeoutInMillis);
		return RepeatStatus.FINISHED;
	}

	public void setLibraryCellId(Integer libraryCellId) {
		this.libraryCellIds = new ArrayList<Integer>();
		this.libraryCellIds.add(libraryCellId);
	}
	
	public void setLibraryCellIds(List<Integer> libraryCellIds) {
		this.libraryCellIds = libraryCellIds;
	}
	
	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}
	
	public void setTask(String task) {
		this.task = task;
	}
	
	@PostConstruct
	public void init(){
		// if jobId is not set, get it from the first libraryCell in the list and check it is unique across all in the list
		// in this scenario (otherwise we have no idea which is supposed to be used)
		if (this.jobId == null){
			for (Integer libraryCellId: libraryCellIds){
				if (this.jobId == null){
					this.jobId = sampleService.getJobOfLibraryOnCell(
							sampleService.getSampleSourceDao().getSampleSourceBySampleSourceId(libraryCellId) ).getId();
					continue;
				}
				if (!sampleService.getJobOfLibraryOnCell(sampleService.getSampleSourceDao().getSampleSourceBySampleSourceId(libraryCellId)).getId()
						.equals(jobId))
					throw new RuntimeException("No master Wasp jobId was provided and the libraryCells do not all reference the same job so no master can be determined");
			}
		}
			
	}
	

}
