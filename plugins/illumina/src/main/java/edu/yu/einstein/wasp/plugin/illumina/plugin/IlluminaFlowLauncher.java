package edu.yu.einstein.wasp.plugin.illumina.plugin;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.MessagingException;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspTask;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.RunStatusMessageTemplate;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.service.RunService;

@Transactional("entityManager")
public class IlluminaFlowLauncher {
	
	private RunService runService;
	
	@Autowired
	public void setRunService(RunService runService) {
		this.runService = runService;
	}
	
	@Autowired
	@Qualifier("hiseqPluginArea")
	private String hiseqPluginArea;
	
	@Autowired
	@Qualifier("personalSeqPluginArea")
	private String personalSeqPluginArea;

	private static final Logger logger = LoggerFactory.getLogger(IlluminaFlowLauncher.class);
	
	public IlluminaFlowLauncher(){}
	
	
	@Transformer
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
		Run run = runService.getRunDao().getRunByRunId(runStatusMessageTemplate.getRunId());
		String rcIname = run.getResourceCategory().getIName();
		String flowName = null;
		if (rcIname.equals(hiseqPluginArea))
			flowName = WaspIlluminaHiseqPlugin.FLOW_NAME;
		//else if (rcIname.equals(personalSeqPluginArea))
		//	flowName = ;
		if (flowName == null){
			logger.debug("Run with id=" + run.getId() + " has a resource-category iname=" + rcIname + " which is not applicable to this plugin");
			return null;
		}
		// all checks out so create the batch job launch message
		Map<String, String> jobParameters = new HashMap<String, String>();
		jobParameters.put(WaspJobParameters.RUN_ID, run.getId().toString() );
		jobParameters.put(WaspJobParameters.RUN_NAME, run.getName());
		BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate(new BatchJobLaunchContext(flowName, jobParameters) );
		try {
			Message<BatchJobLaunchContext> launchMessage = batchJobLaunchMessageTemplate.build();
			if (message.getHeaders().containsKey(MessageHeaders.REPLY_CHANNEL))
				launchMessage.getHeaders().put(MessageHeaders.REPLY_CHANNEL, message.getHeaders().get(MessageHeaders.REPLY_CHANNEL));
			logger.debug("preparing new message to send: " + launchMessage);
			return launchMessage;
		} catch (WaspMessageBuildingException e) {
			throw new MessagingException(e.getLocalizedMessage(), e);
		}
	}

}
