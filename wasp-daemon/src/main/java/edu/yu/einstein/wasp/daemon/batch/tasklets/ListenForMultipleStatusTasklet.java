package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionFixed;
import edu.yu.einstein.wasp.exception.TaskletRetryException;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.templates.StatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;

/**
 * Listens on the provided subscribable channel for a set of messages, the identity of which is
 * obtained from a value promoted to the job execution context.  These message IDs are stored
 * in "messageList" and delimited by ListenForMultipleStatusTasklet.ID_DELIMITER.
 * 
 * @author asmclellan
 */
public class ListenForMultipleStatusTasklet extends ListenForStatusTasklet  {
	
	public static final String ID_DELIMITER = "|";
	
	public ListenForMultipleStatusTasklet() {
		// proxy
	}
	
	public ListenForMultipleStatusTasklet(SubscribableChannel inputSubscribableChannel, SubscribableChannel abortMonitoringChannel, StatusMessageTemplate messageTemplate) {
	    
	    super(inputSubscribableChannel, abortMonitoringChannel, messageTemplate);
		
	}
	
	
}
