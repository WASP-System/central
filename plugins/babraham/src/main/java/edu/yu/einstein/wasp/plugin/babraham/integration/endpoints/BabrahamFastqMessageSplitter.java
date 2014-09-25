package edu.yu.einstein.wasp.plugin.babraham.integration.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.endpoints.WaspAbstractMessageSplitter;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspTask;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.FileStatusMessageTemplate;
import edu.yu.einstein.wasp.plugin.babraham.plugin.FastQCPlugin;
import edu.yu.einstein.wasp.plugin.babraham.plugin.FastQScreenPlugin;
import edu.yu.einstein.wasp.plugin.fileformat.service.FastqService;
import edu.yu.einstein.wasp.service.FileService;

/**
 * 
 * @author asmclellan
 *
 */
public class BabrahamFastqMessageSplitter extends WaspAbstractMessageSplitter{
	
	@Autowired
	private FastqService fastqService;
	
	@Autowired
	private FileService fileService;
	
	private static final Logger logger = LoggerFactory.getLogger(BabrahamFastqMessageSplitter.class);
	
	@SuppressWarnings("unchecked")
	@Override
	protected List<Message<BatchJobLaunchContext>> splitMessage(Message<?> message) {
		List<Message<BatchJobLaunchContext>> outputMessages = new ArrayList<Message<BatchJobLaunchContext>>();
		if (!FileStatusMessageTemplate.isMessageOfCorrectType(message)){
			logger.warn("Message is not of the correct type (a File notification message). Check filter and input channel are correct. Returning no messages");
			return outputMessages; // empty list
		}
		if (!FileStatusMessageTemplate.actUponMessage(message, fastqService.getFastqFileType(), fileService)){
			logger.debug("Message is not concerning a fastq file. Returning no messages");
			return outputMessages; // empty list
		}
		FileStatusMessageTemplate fileStatusMessageTemplate = new FileStatusMessageTemplate((Message<WaspStatus>) message);
		if (!fileStatusMessageTemplate.getStatus().equals(WaspStatus.CREATED) || !fileStatusMessageTemplate.getTask().equals(WaspTask.NOTIFY_STATUS)){
			logger.warn("Message has the wrong status or payload value. Check filter and input channel are correct. Returning no messages");
			return outputMessages; // empty list
		}
		if (isInDemoMode){
			logger.warn("Jobs are not started when in demo mode");
			return outputMessages;
		}
		Map<String, String> jobParameters = new HashMap<String, String>();
		Integer fileGroupId = message.getHeaders().get(WaspJobParameters.FILE_GROUP_ID, Integer.class);
		jobParameters.put(WaspJobParameters.FILE_GROUP_ID, fileGroupId.toString());
		
		// send messages to initiate job processing
		BatchJobLaunchMessageTemplate fastQCLaunchMessageTemplate = new BatchJobLaunchMessageTemplate( 
				new BatchJobLaunchContext(FastQCPlugin.FLOW_NAME, jobParameters) );
		BatchJobLaunchMessageTemplate fastQScreenLaunchMessageTemplate = new BatchJobLaunchMessageTemplate( 
				new BatchJobLaunchContext(FastQScreenPlugin.FLOW_NAME, jobParameters) );
		try {
			Message<BatchJobLaunchContext> fastQCLaunchMessage = fastQCLaunchMessageTemplate.build();
			logger.debug("preparing new message to send: " + fastQCLaunchMessage);
			outputMessages.add(fastQCLaunchMessage);
			Message<BatchJobLaunchContext> fastQScreenLaunchMessage = fastQScreenLaunchMessageTemplate.build();
			logger.debug("preparing new message to send: " + fastQScreenLaunchMessage);
			outputMessages.add(fastQScreenLaunchMessage);
		} catch (WaspMessageBuildingException e) {
			throw new MessagingException(e.getLocalizedMessage(), e);
		}
		return outputMessages;
	}
	
	
}
