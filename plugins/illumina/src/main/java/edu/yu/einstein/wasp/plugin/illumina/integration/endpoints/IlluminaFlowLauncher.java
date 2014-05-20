package edu.yu.einstein.wasp.plugin.illumina.integration.endpoints;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspTask;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.RunStatusMessageTemplate;
import edu.yu.einstein.wasp.plugin.illumina.plugin.IlluminaResourceCategory;
import edu.yu.einstein.wasp.plugin.illumina.plugin.WaspIlluminaHiseqPlugin;

public class IlluminaFlowLauncher {
	
	private static final Logger logger = LoggerFactory.getLogger(IlluminaFlowLauncher.class);
	
	public IlluminaFlowLauncher(){}
	
	@ServiceActivator
	public Message<BatchJobLaunchContext> launchIlluminaFlowJob(Message<?> message){ 
		if (!RunStatusMessageTemplate.isMessageOfCorrectType(message)){
			logger.warn("Message is not of the correct type (a Run message). Check filter and input channel are correct");
			return null; 
		}
		RunStatusMessageTemplate runStatusMessageTemplate = new RunStatusMessageTemplate((Message<WaspStatus>) message);
		if (!runStatusMessageTemplate.getStatus().equals(WaspStatus.CREATED) || !runStatusMessageTemplate.getTask().equals(WaspTask.NOTIFY_STATUS)){
			logger.debug("Message has the wrong status or payload value.");
			return null;
		}
		Integer runId = runStatusMessageTemplate.getRunId();
		String rcIname = (String) runStatusMessageTemplate.getHeader(WaspJobParameters.RUN_RESOURCE_CATEGORY_INAME);
		String runName = (String) runStatusMessageTemplate.getHeader(WaspJobParameters.RUN_NAME);
		logger.debug("Processing run message: runId=" + runId);
		logger.debug("Processing run message: rcIname=" + rcIname);
		logger.debug("Processing run message: runName=" + runName);
		String flowName = null;
		if (rcIname.equals(IlluminaResourceCategory.HISEQ_2000) || rcIname.equals(IlluminaResourceCategory.HISEQ_2500))
			flowName = WaspIlluminaHiseqPlugin.FLOW_NAME;
		//else if (rcIname.equals(IlluminaResourceCategory.PERSONAL))
		//	flowName = ;
		if (flowName == null){
			logger.debug("Run with id=" + runId + " has a resource-category iname=" + rcIname + " which is not applicable to this plugin");
			return null;
		}
		// all checks out so create the batch job launch message
		Map<String, String> jobParameters = new HashMap<String, String>();
		jobParameters.put(WaspJobParameters.RUN_ID, runId.toString() );
		jobParameters.put(WaspJobParameters.RUN_NAME, runName);
		BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate(new BatchJobLaunchContext(flowName, jobParameters) );
		try {
			if (message.getHeaders().containsKey(MessageHeaders.REPLY_CHANNEL))
				batchJobLaunchMessageTemplate.getHeaders().put(MessageHeaders.REPLY_CHANNEL, message.getHeaders().get(MessageHeaders.REPLY_CHANNEL));
			Message<BatchJobLaunchContext> launchMessage = batchJobLaunchMessageTemplate.build();
			logger.debug("preparing new message to send: " + launchMessage);
			return launchMessage;
		} catch (WaspMessageBuildingException e) {
			throw new MessagingException(e.getLocalizedMessage(), e);
		}
	}

}
