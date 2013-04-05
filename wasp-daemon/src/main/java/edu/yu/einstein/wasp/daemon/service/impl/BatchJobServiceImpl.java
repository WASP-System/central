package edu.yu.einstein.wasp.daemon.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import edu.yu.einstein.wasp.batch.core.extension.JobExplorerWasp;
import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.daemon.service.BatchJobService;
import edu.yu.einstein.wasp.exception.JobContextInitializationException;
import edu.yu.einstein.wasp.exception.SoftwareConfigurationException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.util.SoftwareConfiguration;
import edu.yu.einstein.wasp.util.WaspJobContext;

/**
 * 
 * @author asmclellan
 * 
 */
@Service
@Transactional("entityManager")
public class BatchJobServiceImpl implements BatchJobService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private JobExplorerWasp jobExplorer;
	
	private WaspPluginRegistry waspPluginRegistry;
	
	private JobService jobService;

	private QueueChannel launchChannel; // channel to send messages out of system

	@Autowired
	@Qualifier(MessageChannelRegistry.LAUNCH_MESSAGE_CHANNEL)
	public void setLaunchChannel(QueueChannel launchChannel) {
		this.launchChannel = launchChannel;
	}

	@Override
	@Autowired
	public void setJobExplorer(JobExplorer jobExplorer) {
		this.jobExplorer = (JobExplorerWasp) jobExplorer;
	}

	@Override
	@Autowired
	public void setWaspPluginRegistry(WaspPluginRegistry waspPluginRegistry){
		this.waspPluginRegistry = waspPluginRegistry;
	}

	@Override
	@Autowired
	public void setJobService(JobService jobService){
		this.jobService = jobService;
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.yu.einstein.wasp.daemon.service.impl.JobService#
	 * findAllRunningJobExecutions()
	 */
	@Override
	public Set<JobExecution> findAllRunningJobExecutions() {
		Set<JobExecution> runningJobExecutionSet = new HashSet<JobExecution>();
		for (String jobName : jobExplorer.getJobNames()) {
			Set<JobExecution> runningJobExecutionSubset = jobExplorer.findRunningJobExecutions(jobName);
			if (runningJobExecutionSubset != null && !runningJobExecutionSubset.isEmpty()) {
				runningJobExecutionSet.addAll(runningJobExecutionSubset);
			}
		}
		return runningJobExecutionSet;
	}

	@Override
	public Message<?> launchAnalysisJob(Integer jobId, ResourceType softwareResourceType, List<Integer> libraryCellIds, 
			String batchJobType, int messageTimeoutInMillis) throws JobContextInitializationException, SoftwareConfigurationException {
		logger.debug("launch for jobId: " + jobId);
		WaspJobContext waspJobContext = new WaspJobContext(jobService.getJobByJobId(jobId));
		SoftwareConfiguration softwareConfig = waspJobContext.getConfiguredSoftware(softwareResourceType);
		if (softwareConfig == null) {
			throw new SoftwareConfigurationException("No software could be configured for jobId=" + jobId + " with resourceType iname=" + softwareResourceType.getIName());
		}
		Map<String, String> jobParameters = softwareConfig.getParameters();
		jobParameters.put(WaspSoftwareJobParameters.LIBRARY_CELL_ID_LIST, WaspSoftwareJobParameters.getLibraryCellListAsParameterValue(libraryCellIds));
		jobParameters.put(WaspSoftwareJobParameters.JOB_ID, jobId.toString());
		MessagingTemplate messagingTemplate = new MessagingTemplate();
		messagingTemplate.setReceiveTimeout(messageTimeoutInMillis);
		BatchJobProviding softwarePlugin = waspPluginRegistry.getPlugin(softwareConfig.getSoftware().getIName(), BatchJobProviding.class);
		Assert.notNull(softwarePlugin);
		String flowName = softwarePlugin.getBatchJobName(batchJobType);
		if (flowName == null)
			logger.warn("No generic flow found for plugin so cannot launch software : " + softwareConfig.getSoftware().getIName());
		BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate(
				new BatchJobLaunchContext(flowName, jobParameters));
		Message<?> reply = null;
		try {
			Message<BatchJobLaunchContext> launchMessage = batchJobLaunchMessageTemplate.build();
			logger.debug("Sending the following launch message via channel " + MessageChannelRegistry.LAUNCH_MESSAGE_CHANNEL + " : " + launchMessage);
			reply = messagingTemplate.sendAndReceive(launchChannel, launchMessage);
		} catch (WaspMessageBuildingException e) {
			throw new MessagingException(e.getLocalizedMessage(), e);
		}
		return reply;
	}

}
