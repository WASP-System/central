package edu.yu.einstein.wasp.integration.endpoints;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.batch.core.extension.JobOperatorWasp;
import edu.yu.einstein.wasp.batch.core.extension.WaspBatchExitStatus;
import edu.yu.einstein.wasp.exception.WaspBatchJobExecutionException;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.templates.HibernationMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.HibernationMessageTemplate.HibernationType;
import edu.yu.einstein.wasp.integration.messages.templates.StatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;


/**
 * 
 * @author asmclellan
 *
 */
public class BatchJobHibernationManager {

	public static final String HIBERNATING = "HIBERNATING";
	public static final String WOKEN_ON_MESSAGE_KEY = "wokenOnMessage";
	public static final String MESSAGES_TO_WAKE = "w_msgs";
	
	private static final Logger logger = LoggerFactory.getLogger(BatchJobHibernationManager.class);
	
	private JobOperatorWasp jobOperator;
	
	private JobExplorer jobExplorer;
	
	private JobRepository jobRepository;
	
	private Map<WaspStatusMessageTemplate, Set<StepExecution>> messageTemplatesWakingStepExecutions = new HashMap<>(); // use HashMap for fast message searching

	public BatchJobHibernationManager() {}
	
	@Autowired
	public void setJobExplorer(JobExplorer jobExplorer){
		this.jobExplorer = jobExplorer;
	}
	
	@Autowired
	public void setJobRepository(JobRepository jobRepository){
		this.jobRepository = jobRepository;
	}
	
	
	@Autowired
	public void setJobOperator(JobOperator jobOperator){
		this.jobOperator = (JobOperatorWasp) jobOperator;
	}
	
	/**
	 * Attempts to extract message templates pre-stored in the stepExecutionContext
	 * @param jobExecutionId
	 * @param stepExecutionId
	 */
	public void addMessageTemplatesForJobStep(Long jobExecutionId, Long stepExecutionId) {
		Set<WaspStatusMessageTemplate> messageTemplates = new HashSet<>();
		StepExecution se = jobExplorer.getStepExecution(jobExecutionId, stepExecutionId);
		try {
			messageTemplates.addAll(getWakeMessages(se));
		} catch (Exception e) {
			logger.warn("Unable to obtain stored wake message templates from stepExecution id=" + stepExecutionId + ": " + e.getLocalizedMessage());
		}
		if (!messageTemplates.isEmpty()){
			logger.debug("Populating hibernation manager with " + messageTemplates + " message templates for stepExecution id=" + stepExecutionId);
			for (WaspStatusMessageTemplate messageTemplate: messageTemplates){
				if (!messageTemplatesWakingStepExecutions.containsKey(messageTemplate))
					messageTemplatesWakingStepExecutions.put(messageTemplate, new HashSet<StepExecution>());
				messageTemplatesWakingStepExecutions.get(messageTemplate).add(se);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@ServiceActivator
	public Message<WaspStatus> handleMessage(Message<?> message){
		logger.debug("handling message: " + message);
		Message<WaspStatus> replyMessage = null;
		if (HibernationMessageTemplate.isMessageOfCorrectType(message)){
			processHibernateMessage((Message<HibernationType>) message);
		} else {
			logger.trace("Message is not a request stop message");
			if (WaspStatus.class.isInstance(message.getPayload())){
				logger.trace("Message payload is of type WaspStatus");
				processStatusMessage((Message<WaspStatus>) message);
				replyMessage = getReplyMessage((Message<WaspStatus>) message);
			}
		}	
		return replyMessage;
	}
	
	private void processHibernateMessage(Message<HibernationType> message){
		HibernationMessageTemplate messageTemplate = new HibernationMessageTemplate(message);
		if (messageTemplate.getHibernationType().equals(HibernationType.STOP_AND_AWAKE_ON_MESSAGE)){
			logger.debug("Message is to request stop and re-awaken on message");
			addMessageTemplatesForJobStep(messageTemplate.getJobExecutionId(), messageTemplate.getStepExecutionId());
			WaspBatchExitStatus exitStatus = new WaspBatchExitStatus(jobExplorer.getJobExecution(messageTemplate.getJobExecutionId()).getExitStatus());
			if (exitStatus.isRunningAndAwake()){
				logger.debug("Going to hibernate JobExecution id=" + messageTemplate.getJobExecutionId() + " (requested from step Id=" + 
						messageTemplate.getStepExecutionId() + ")");
				hibernateJobExecution(messageTemplate.getJobExecutionId());
			}
		} else if (messageTemplate.getHibernationType().equals(HibernationType.STOP_AND_AWAKE_ON_TIMEOUT)){
			logger.debug("Message is to request stop and re-awaken on timeout");
			// TODO: functionality here
		}
	}
	
	private void processStatusMessage(Message<WaspStatus> message){
		WaspStatusMessageTemplate incomingStatusMessageTemplate = new WaspStatusMessageTemplate((Message<WaspStatus>) message);
		//remove superfluous headers if present (these are not used to make decisions about acting on messages)
		sanitizeHeaders(incomingStatusMessageTemplate);
		logger.debug("Handling message: " + incomingStatusMessageTemplate.toString());
		if (messageTemplatesWakingStepExecutions.keySet().contains(incomingStatusMessageTemplate)){
			logger.debug("messageTemplatesForJob.keySet() contains message");
			for (StepExecution se : messageTemplatesWakingStepExecutions.get(incomingStatusMessageTemplate)){
				logger.debug("restarting job with JobExecution id=" +se.getJobExecutionId() + " for step " + se.getId() + 
						" on receiving message " + message);
				try{
					reawakenJobExecution(se);
					// remove all stepExecutions from the re-awoken job from messageTemplatesWakingStepExecutions and remove awake message
					// if no longer needed
					for (StepExecution seForJob : jobExplorer.getJobExecution(se.getJobExecutionId()).getStepExecutions()){
						Set<StatusMessageTemplate> templates = new HashSet<StatusMessageTemplate>(messageTemplatesWakingStepExecutions.keySet());
						for (StatusMessageTemplate template :templates){
							if (messageTemplatesWakingStepExecutions.get(template).contains(seForJob)){
								logger.debug("Removing re-awoken stepExecution id=" + seForJob.getId() + 
										" from list of step executions re-awoken by message template : " + template.toString());
								messageTemplatesWakingStepExecutions.get(template).remove(seForJob);
							}
							if (messageTemplatesWakingStepExecutions.get(template).isEmpty()){
								logger.debug("Removing message template from list of messages to watch for as no more StepExecutions depend on it : " + 
										template.toString());
								messageTemplatesWakingStepExecutions.remove(template);
							}
						}
					}
				} catch (WaspBatchJobExecutionException e){
					logger.warn("Problem reawakening job execution and cleaning up 'messageTemplatesWakingStepExecutions': " + e.getLocalizedMessage());
				}
			}
		}	
		else
			logger.debug("messageTemplatesForJob.keySet() does not contain message");
			for (WaspStatusMessageTemplate mt : messageTemplatesWakingStepExecutions.keySet())
				logger.debug("messageTemplatesForJob entry : " + mt.toString());
	}
	
	private void reawakenJobExecution(StepExecution se) throws WaspBatchJobExecutionException{
		try {
			se = jobExplorer.getStepExecution(se.getJobExecutionId(), se.getId()); // get fresh object from jobExplorer
			se.getExecutionContext().put(WOKEN_ON_MESSAGE_KEY, true);
			jobRepository.updateExecutionContext(se);
			jobOperator.wake(se.getJobExecutionId());
		} catch (JobInstanceAlreadyCompleteException | NoSuchJobExecutionException | NoSuchJobException | JobRestartException | JobParametersInvalidException e) {
			throw new WaspBatchJobExecutionException("Unable to restart job with JobExecution id=" + se.getJobExecutionId() + 
					" (got " + e.getClass().getName() + " Exception :" + e.getLocalizedMessage() + ")");
		}
	}
	
	// TODO: do this work on another thread
	private void hibernateJobExecution(Long jobExecutionId){
		try {
			jobOperator.hibernate(jobExecutionId);
		} catch (NoSuchJobExecutionException | JobExecutionNotRunningException e1) {
			logger.warn("Unable to stop job with JobExecution id=" + jobExecutionId + " (got " + e1.getClass().getName() + " Exception :" + 
					e1.getLocalizedMessage() + ")");
		}
	}
	
	public static void setWakeMessages(StepExecution stepExecution, Set<WaspStatusMessageTemplate> templates) throws JSONException{
		ExecutionContext executionContext = stepExecution.getExecutionContext();
		JSONArray jsonForMessages = new JSONArray();
		for (WaspStatusMessageTemplate template: templates){
			logger.trace("template as json : " + template.getAsJson());
			jsonForMessages.put(template.getAsJson());
		}
		JSONObject json = new JSONObject();
		json.put(MESSAGES_TO_WAKE, jsonForMessages);
		logger.debug("Created json string to persist : " + json.toString());
		executionContext.put(MESSAGES_TO_WAKE, json.toString());
		
	}
	
	public static Set<WaspStatusMessageTemplate> getWakeMessages(StepExecution stepExecution) throws JSONException{
		Set<WaspStatusMessageTemplate> templates = new HashSet<>();
		ExecutionContext executionContext = stepExecution.getExecutionContext();
		if (!executionContext.containsKey(MESSAGES_TO_WAKE)){
			logger.trace("Execution context of stepExecution id=" + stepExecution.getId() + " contains no wake messages");
			return templates; // empty set
		}
		JSONObject json = new JSONObject(executionContext.getString(MESSAGES_TO_WAKE));
		JSONArray jsonArray = json.getJSONArray(MESSAGES_TO_WAKE);
		for (int i=0; i< jsonArray.length(); i++)
			templates.add(new WaspStatusMessageTemplate(jsonArray.getJSONObject(i)));
		return templates;
	}
	
	
	
	private Message<WaspStatus> getReplyMessage(Message<WaspStatus> message){
		try{
			logger.debug("sending reply to message: " + message.toString());
			// this is a little complex. What we do here is attach the temporary point-to-point reply channel generated by the gateway
			// and attached to the source message to the reply message and send it on the 'wasp.channel.reply' channel. 
			// The Gateway will create a bridge from it to the temporary, anonymous reply channel that is stored in the header.
			// Of course if there is no reply channel specified then no reply will be sent.
			if ( message.getHeaders().containsKey(MessageHeaders.REPLY_CHANNEL)){
				Message<WaspStatus> replyMessage = MessageBuilder
						.withPayload(WaspStatus.COMPLETED)
						.setReplyChannel((MessageChannel) message.getHeaders().get(MessageHeaders.REPLY_CHANNEL))
						.build();
				logger.debug("returning reply message: " + replyMessage.toString());
				return replyMessage;
			} else
				logger.debug("No reply message sent because no reply channel was specified in the original message");
		} catch (Exception e){
			logger.warn("Failure to send reply message (reason: " + e.getLocalizedMessage() + ") to reply channel specified in source message : " +
					message.toString() + ". Original exception stack: ");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Remove spring integration headers (specified in {@link MessageHeaders}) and superfluous WASP message headers from header set (if any)
	 * @param headers
	 * @return
	 */
	private void sanitizeHeaders(WaspStatusMessageTemplate template){
		template.removeHeader(MessageHeaders.CONTENT_TYPE);
		template.removeHeader(MessageHeaders.CORRELATION_ID);
		template.removeHeader(MessageHeaders.ERROR_CHANNEL);
		template.removeHeader(MessageHeaders.EXPIRATION_DATE);
		template.removeHeader(MessageHeaders.ID);
		template.removeHeader(MessageHeaders.POSTPROCESS_RESULT);
		template.removeHeader(MessageHeaders.PRIORITY);
		template.removeHeader(MessageHeaders.REPLY_CHANNEL);
		template.removeHeader(MessageHeaders.SEQUENCE_DETAILS);
		template.removeHeader(MessageHeaders.SEQUENCE_NUMBER);
		template.removeHeader(MessageHeaders.SEQUENCE_SIZE);
		template.removeHeader(MessageHeaders.TIMESTAMP);
		template.removeHeader(WaspStatusMessageTemplate.COMMENT_KEY); 
		template.removeHeader(WaspStatusMessageTemplate.USER_KEY);
		template.removeHeader(WaspStatusMessageTemplate.EXIT_DESCRIPTION_HEADER);
		template.removeHeader(WaspStatusMessageTemplate.DESTINATION);
	}

}
