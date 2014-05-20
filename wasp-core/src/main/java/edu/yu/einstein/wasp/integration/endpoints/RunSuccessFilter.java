package edu.yu.einstein.wasp.integration.endpoints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.integration.annotation.Filter;

import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspTask;
import edu.yu.einstein.wasp.integration.messages.templates.RunStatusMessageTemplate;

/**
 * 
 * @author asmclellan
 *
 */
public class RunSuccessFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(RunSuccessFilter.class);

	@Filter
	public boolean isSuccessfulRun(Message<WaspStatus> runStatusMessage) {
		if (!RunStatusMessageTemplate.isMessageOfCorrectType(runStatusMessage)){
			logger.warn("Message is not of the correct type (a Run message). Check filter and imput channel are correct");
			return false; 
		}
		RunStatusMessageTemplate runStatusMessageTemplate = new RunStatusMessageTemplate(runStatusMessage);
		if (runStatusMessageTemplate.getStatus().equals(WaspStatus.COMPLETED) && runStatusMessageTemplate.getTask().equals(WaspTask.NOTIFY_STATUS)){
			return true;
		}
		return false;
	}

}
