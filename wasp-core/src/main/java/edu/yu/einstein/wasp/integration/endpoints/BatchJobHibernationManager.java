package edu.yu.einstein.wasp.integration.endpoints;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
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
import org.springframework.integration.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.batch.core.extension.JobOperatorWasp;
import edu.yu.einstein.wasp.batch.core.extension.WaspBatchExitStatus;
import edu.yu.einstein.wasp.exception.ResourceLockException;
import edu.yu.einstein.wasp.exception.WaspBatchJobExecutionException;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.templates.StatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;


/**
 * 
 * @author asmclellan
 *
 */
public class BatchJobHibernationManager {

	public static final String WAS_HIBERNATING = "wasHibernating";
	public static final String HIBERNATION_REQUESTED = "requestHibernation";
	public static final String ABANDON_ON_MESSAGE = "abandonedOnMessage";
	public static final String WOKEN_ON_MESSAGE_STATUS = "wokenOnMessageStatus";
	public static final String WOKEN_ON_TIMEOUT = "wokenOnTimeout";
	public static final String MESSAGES_TO_WAKE = "w_msgs";
	public static final String MESSAGES_TO_ABANDON = "a_msgs";
	public static final String TIME_TO_WAKE = "w_time";
	
	private static final Logger logger = LoggerFactory.getLogger(BatchJobHibernationManager.class);
	
	private JobOperatorWasp jobOperator;
	
	private JobExplorer jobExplorer;
	
	private JobRepository jobRepository;
	
	// make the following variables volatile to prevent caching 
	private volatile Map<WaspStatusMessageTemplate, Set<StepExecution>> messageTemplatesWakingStepExecutions = new HashMap<>(); 
	
	private volatile Map<WaspStatusMessageTemplate, Set<StepExecution>> messageTemplatesAbandoningStepExecutions = new HashMap<>();
	
	private volatile Map<Long, Set<StepExecution>> timesWakingStepExecutions = new TreeMap<>(); 
	
	private static volatile Set<Long> jobExecutionIdsLockedForHibernating = new HashSet<>();

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
	 * TaskScheduler executed method: executed periodically at a rate defined in configuration (wasp.hibernation.heartbeat)
	 */
	public synchronized void runTimedTasks() {
		logger.debug("Checking to see if any StepExecutions require waking after timed sleep");
		Set<Long> orderedTimes = new TreeSet<>(timesWakingStepExecutions.keySet());
		Date timeNow = new Date();
		for (Long time: orderedTimes){
			if (time <= timeNow.getTime()){
				Set<StepExecution> ses = new HashSet<>(timesWakingStepExecutions.get(time));
				for (StepExecution se : ses){
					logger.info("restarting job with JobExecution id=" +se.getJobExecutionId() + " for step " + se.getId() + 
							" on restart wait timeout");
					try{
						reawakenJobExecution(se, WOKEN_ON_TIMEOUT, true);
						timesWakingStepExecutions.remove(time);
					} catch (WaspBatchJobExecutionException e){
						logger.warn("Problem reawakening job execution : " + e.getLocalizedMessage());
					}
				}
			}
		}
	}
	
	/**
	 * ServiceActivator called method: Checks an incoming WaspStatusMessage against two sets of messages that either abandon or wake batch jobs.
	 * @param message
	 * @return
	 */
	public Message<WaspStatus> handleStatusMessage(Message<WaspStatus> message){
		WaspStatusMessageTemplate incomingStatusMessageTemplate = new WaspStatusMessageTemplate((Message<WaspStatus>) message);
		//remove superfluous headers if present (these are not used to make decisions about acting on messages)
		sanitizeHeaders(incomingStatusMessageTemplate);
		logger.info("Handling message: " + incomingStatusMessageTemplate.toString());
		handleStepExecutionAbandonmentOnMessage(incomingStatusMessageTemplate);
		handleStepExecutionWakingOnMessage(incomingStatusMessageTemplate);
		return getReplyMessage(message);
	}
	
	/**
	 * Checks provided WaspStatusMessage against set of messages that are being listened for in order to abandon batch jobs. Removes message
	 * from set after jobs successfully abandoned.
	 * @param messageTemplate
	 */
	private synchronized void handleStepExecutionAbandonmentOnMessage(WaspStatusMessageTemplate messageTemplate){
		if (messageTemplatesAbandoningStepExecutions.keySet().contains(messageTemplate)){
			logger.debug("messageTemplatesAbandoningStepExecutions.keySet() contains message");
			Set<StepExecution> ses = new HashSet<>(messageTemplatesAbandoningStepExecutions.get(messageTemplate));
			Set<Long> abandondedJobExecutionIds = new HashSet<>();
			for (StepExecution se :ses){
				if (!abandondedJobExecutionIds.contains(se.getJobExecutionId())){
					logger.info("Abandoning job with JobExecution id=" +se.getJobExecutionId() + " for step " + se.getId() + 
							" on receiving message " + messageTemplate.getPayload().toString());
					try{
						abandonJobExecution(se);
						abandondedJobExecutionIds.add(se.getJobExecutionId()); // make sure only performed once per jobExecution
						updateMessageStepExecutionMap(se, messageTemplatesAbandoningStepExecutions);
					} catch (WaspBatchJobExecutionException e){
						logger.warn("Problem aborting job execution and cleaning up 'messageTemplatesAbandoningStepExecutions': " + e.getLocalizedMessage());
					}
				}
			}
		}	
		else
			logger.debug("messageTemplatesAbandoningStepExecutions.keySet() does not contain message");
	}
	
	/**
	 * Checks provided WaspStatusMessage against set of messages that are being listened for in order to wake batch jobs. Removes message
	 * from set after jobs successfully woken.
	 * @param messageTemplate
	 */
	private synchronized void handleStepExecutionWakingOnMessage(WaspStatusMessageTemplate messageTemplate){
		if (messageTemplatesWakingStepExecutions.keySet().contains(messageTemplate)){
			logger.debug("messageTemplatesWakingStepExecutions.keySet() contains message");
			Set<Long> hibernatedJobExecutionIds = new HashSet<>();
			Set<StepExecution> ses = new HashSet<>(messageTemplatesWakingStepExecutions.get(messageTemplate));
			for (StepExecution se : ses){
				if (!hibernatedJobExecutionIds.contains(se.getJobExecutionId())){
					logger.info("Waking job with JobExecution id=" +se.getJobExecutionId() + " for step " + se.getId() + 
							" on receiving message " + messageTemplate.getPayload().toString());
					try{
						reawakenJobExecution(se, WOKEN_ON_MESSAGE_STATUS, messageTemplate.getStatus());
						hibernatedJobExecutionIds.add(se.getJobExecutionId()); // make sure only performed once per jobExecution
						updateMessageStepExecutionMap(se, messageTemplatesWakingStepExecutions);
					} catch (WaspBatchJobExecutionException e){
						logger.warn("Problem reawakening job execution and cleaning up 'messageTemplatesWakingStepExecutions': " + e.getLocalizedMessage());
					}
				}
			}
		}	
		else
			logger.debug("messageTemplatesWakingStepExecutions.keySet() does not contain message");
	}
	
	/**
	 * If message specifies a reply channel, send a reply to notify completion of handling
	 * @param message
	 * @return
	 */
	private Message<WaspStatus> getReplyMessage(Message<WaspStatus> message){
		try{
			logger.debug("Going to send reply after handling message: " + message.toString());
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
	 * Adds association of StepExecution and its wake time (determined from the time interval set in the StepExecutionContext relative to the current time) 
	 * to the map of wake-time/StepExecution associations maintained by an instance of this class.
	 * @param jobExecutionId
	 * @param stepExecutionId
	 */
	public void addTimeIntervalForJobStep(Long jobExecutionId, Long stepExecutionId) {
		StepExecution se = jobExplorer.getStepExecution(jobExecutionId, stepExecutionId);
		addTimeIntervalForJobStep(jobExecutionId, stepExecutionId, getWakeTimeInterval(se));
	}
	
	/**
	 * Adds association of StepExecution and a wake time (determined from the time interval provided relative to the current time) 
	 * to the map of wake-time/StepExecution associations maintained by an instance of this class.
	 * @param jobExecutionId
	 * @param stepExecutionId
	 * @param interval
	 */
	public synchronized void addTimeIntervalForJobStep(Long jobExecutionId, Long stepExecutionId, Long interval) {
		if (interval == null){
			logger.warn("Unable to obtain a wake time from stepExecution id=" + stepExecutionId);
			return;
		}
		StepExecution se = jobExplorer.getStepExecution(jobExecutionId, stepExecutionId);
		
		Date timeNow = new Date();
		Long timeToWake = timeNow.getTime() + interval;
		logger.info("Populating hibernation manager with a hibernate time interval of " + interval + " ms for stepExecution id=" + stepExecutionId);
		if (!timesWakingStepExecutions.containsKey(timeToWake))
			timesWakingStepExecutions.put(timeToWake, new HashSet<StepExecution>());
		timesWakingStepExecutions.get(timeToWake).add(se);
	}
	
	/**
	 * Adds association of StepExecution and its wake messages (obtained from StepExecutionContext relative) 
	 * to the map of wake-message/StepExecution associations maintained by an instance of this class.
	 * @param jobExecutionId
	 * @param stepExecutionId
	 */
	public void addMessageTemplatesForWakingJobStep(Long jobExecutionId, Long stepExecutionId) {
		Set<WaspStatusMessageTemplate> messageTemplates = new HashSet<>();
		StepExecution se = jobExplorer.getStepExecution(jobExecutionId, stepExecutionId);
		try {
			messageTemplates.addAll(getWakeMessagesForStep(se));
		} catch (Exception e) {
			logger.warn("Unable to obtain stored wake message templates from stepExecution id=" + stepExecutionId + ": " + e.getLocalizedMessage());
		}
		addMessageTemplatesForWakingJobStep(jobExecutionId, stepExecutionId, messageTemplates);
	}
	
	/**
	 * Adds association of StepExecution and a wake messages provided to the map of wake-message/StepExecution 
	 * associations maintained by an instance of this class.
	 * @param jobExecutionId
	 * @param stepExecutionId
	 * @param messageTemplates
	 */
	public synchronized void addMessageTemplatesForWakingJobStep(Long jobExecutionId, Long stepExecutionId, Collection<WaspStatusMessageTemplate> messageTemplates) {
		StepExecution se = jobExplorer.getStepExecution(jobExecutionId, stepExecutionId);
		if (!messageTemplates.isEmpty()){
			logger.info("Populating hibernation manager with " + messageTemplates + " message templates for stepExecution id=" + stepExecutionId);
			for (WaspStatusMessageTemplate messageTemplate: messageTemplates){
				if (!messageTemplatesWakingStepExecutions.containsKey(messageTemplate))
					messageTemplatesWakingStepExecutions.put(messageTemplate, new HashSet<StepExecution>());
				messageTemplatesWakingStepExecutions.get(messageTemplate).add(se);
			}
		}
	}
	
	/**
	 * Adds association of StepExecution and its abandon messages (obtained from StepExecutionContext relative) 
	 * to the map of abandon-message/StepExecution associations maintained by an instance of this class.
	 * @param jobExecutionId
	 * @param stepExecutionId
	 */
	public void addMessageTemplatesForAbandoningJobStep(Long jobExecutionId, Long stepExecutionId) {
		Set<WaspStatusMessageTemplate> messageTemplates = new HashSet<>();
		StepExecution se = jobExplorer.getStepExecution(jobExecutionId, stepExecutionId);
		try {
			messageTemplates.addAll(getAbandonMessagesForStep(se));
		} catch (Exception e) {
			logger.warn("Unable to obtain stored abandon message templates from stepExecution id=" + stepExecutionId + ": " + e.getLocalizedMessage());
		}
		addMessageTemplatesForAbandoningJobStep(jobExecutionId, stepExecutionId, messageTemplates);
	}
	
	/**
	 * Adds association of StepExecution and a abandon messages provided to the map of abandon-message/StepExecution 
	 * associations maintained by an instance of this class.
	 * @param jobExecutionId
	 * @param stepExecutionId
	 * @param messageTemplates
	 */
	public synchronized void addMessageTemplatesForAbandoningJobStep(Long jobExecutionId, Long stepExecutionId, Collection<WaspStatusMessageTemplate> messageTemplates) {
		StepExecution se = jobExplorer.getStepExecution(jobExecutionId, stepExecutionId);
		if (!messageTemplates.isEmpty()){
			logger.info("Populating hibernation manager with " + messageTemplates + " message templates for stepExecution id=" + stepExecutionId);
			for (WaspStatusMessageTemplate messageTemplate: messageTemplates){
				if (!messageTemplatesAbandoningStepExecutions.containsKey(messageTemplate))
					messageTemplatesAbandoningStepExecutions.put(messageTemplate, new HashSet<StepExecution>());
				messageTemplatesAbandoningStepExecutions.get(messageTemplate).add(se);
			}
		}
	}
	
	
	/**
	 * Removes all handled StepExecutions and messages from the provided map of message/StepExecution associations
	 * @param se
	 * @param m
	 */
	private void updateMessageStepExecutionMap(StepExecution se,  Map<WaspStatusMessageTemplate, Set<StepExecution>> m){
		for (StepExecution seForJob : jobExplorer.getJobExecution(se.getJobExecutionId()).getStepExecutions()){
			Set<StatusMessageTemplate> templates = new HashSet<StatusMessageTemplate>(m.keySet());
			for (StatusMessageTemplate template :templates){
				if (m.get(template).contains(seForJob)){
					logger.info("Removing stepExecution id=" + seForJob.getId() + 
							" from list of step executions responding to message template : " + template.toString());
					m.get(template).remove(seForJob);
				}
				if (m.get(template).isEmpty()){
					logger.info("Removing message template from list of messages to watch for as no more StepExecutions depend on it : " + 
							template.toString());
					m.remove(template);
				}
			}
		}
	}
	
	/**
	 * Requests hibernation of a specified batch JobExecution by specified StepExecution
	 * @param jobExecutionId
	 * @param stepExecutionId
	 */
	public void processHibernateRequest(Long jobExecutionId, Long stepExecutionId){
		logger.info("Request received to hibernate a JobExecution");
		WaspBatchExitStatus exitStatus = new WaspBatchExitStatus(jobExplorer.getJobExecution(jobExecutionId).getExitStatus());
		logger.debug("job with id=" + jobExecutionId + " has ExitStatus of " + exitStatus + " and isRunningAndAwake=" + exitStatus.isRunningAndAwake());
		if (exitStatus.isRunningAndAwake()){
			logger.info("Going to hibernate JobExecution id=" + jobExecutionId + " (requested from step Id=" + stepExecutionId + ")");
			hibernateJobExecution(jobExecutionId);
		}
	}
	
	/**
	 * Requests hibernation of a specified batch JobExecution by specified StepExecution, to be re-awoken on receiving one of the provided messageTemplates
	 * @param jobExecutionId
	 * @param stepExecutionId
	 * @param messageTemplates
	 */
	public void processHibernateRequest(Long jobExecutionId, Long stepExecutionId, Collection<WaspStatusMessageTemplate> messageTemplates){
		addMessageTemplatesForWakingJobStep(jobExecutionId, stepExecutionId, messageTemplates);
		processHibernateRequest(jobExecutionId, stepExecutionId);
	}
	
	/**
	 * Requests hibernation of a specified batch JobExecution by specified StepExecution, to be re-awoken after specified time interval
	 * @param jobExecutionId
	 * @param stepExecutionId
	 * @param timeInterval
	 */
	public void processHibernateRequest(Long jobExecutionId, Long stepExecutionId, Long timeInterval){
		addTimeIntervalForJobStep(jobExecutionId, stepExecutionId, timeInterval);
		processHibernateRequest(jobExecutionId, stepExecutionId);
	}
	
	/**
	 * Abandon JobExecution running provided StepExecution
	 * @param stepExecution
	 * @throws WaspBatchJobExecutionException
	 */
	@Transactional
	private void abandonJobExecution(StepExecution stepExecution) throws WaspBatchJobExecutionException{
		JobExecution je = jobExplorer.getJobExecution(stepExecution.getJobExecutionId());
		Set<StepExecution> ses = new HashSet<>(je.getStepExecutions());
		for (StepExecution se : ses){
			if (se.getId().equals(stepExecution.getId()))
				se.getExecutionContext().put(ABANDON_ON_MESSAGE, true);
			jobRepository.updateExecutionContext(se);
		}
		
		try{
			jobOperator.stop(stepExecution.getJobExecutionId());
		} catch (Exception e) {
			logger.debug("Cannot stop job: " + e.getLocalizedMessage());
		}
		try{
			jobOperator.abandon(stepExecution.getJobExecutionId());
		} catch (Exception e) {
			throw new WaspBatchJobExecutionException("Unable to abandon job with JobExecution id=" + stepExecution.getJobExecutionId() + 
					" (got " + e.getClass().getName() + " Exception :" + e.getLocalizedMessage() + ")");
		} finally{
			removeJobExecutionIdLockedForHibernating(stepExecution.getJobExecutionId()); // remove lock if set
		}
	}
	
	/**
	 * Wake JobExecution running provided StepExecution and mark in the StepExecutionContext that the provided step requested this using the
	 * provided key and value.
	 * @param stepExecution
	 * @param contextRecordKey
	 * @param contextRecordValue
	 * @throws WaspBatchJobExecutionException
	 */
	@Transactional
	private void reawakenJobExecution(StepExecution stepExecution, String contextRecordKey, Object contextRecordValue) throws WaspBatchJobExecutionException{
		JobExecution je = jobExplorer.getJobExecution(stepExecution.getJobExecutionId());
		je.getExecutionContext().remove(HIBERNATION_REQUESTED);
		jobRepository.updateExecutionContext(je);
		Set<StepExecution> ses = new HashSet<>(je.getStepExecutions());
		for (StepExecution se : ses){
			if (se.getId().equals(stepExecution.getId()))
				se.getExecutionContext().put(contextRecordKey, contextRecordValue);
			jobRepository.updateExecutionContext(se);
		}
		
		try{
			jobOperator.wake(stepExecution.getJobExecutionId());
		} catch (JobInstanceAlreadyCompleteException | NoSuchJobExecutionException | NoSuchJobException | JobRestartException | JobParametersInvalidException e) {
			throw new WaspBatchJobExecutionException("Unable to restart job with JobExecution id=" + stepExecution.getJobExecutionId() + 
					" (got " + e.getClass().getName() + " Exception :" + e.getLocalizedMessage() + ")");
		}
	}
	
	/**
	 * Hibernate specified JobExection
	 */
	private void hibernateJobExecution(Long jobExecutionId){
		try {
			jobOperator.hibernate(jobExecutionId);
		} catch (Exception e1) {
			logger.warn("Unable to hibernate job with JobExecution id=" + jobExecutionId + " (got " + e1.getClass().getName() + " Exception :" + 
					e1.getLocalizedMessage() + ")");
			removeJobExecutionIdLockedForHibernating(jobExecutionId); // remove lock  
		}
	}
	
	/**
	 * Registers JobExecution id locked for hibernating
	 * @param jobExecutionId
	 * @throws ResourceLockException 
	 */
	public static synchronized void addJobExecutionIdLockedForHibernating(Long jobExecutionId) throws ResourceLockException{
		logger.debug("Adding lock for hibernation request for JobExecution id=" + jobExecutionId);
		if (jobExecutionIdsLockedForHibernating.contains(jobExecutionId))
			throw new ResourceLockException("Unable to get lock for JobExecution id=" + jobExecutionId + " as already locked");
		jobExecutionIdsLockedForHibernating.add(jobExecutionId); 
	}
	
	/**
	 * Checks if JobExecution id is locked for hibernating
	 * @param jobExecutionId
	 * @return
	 */
	public static synchronized boolean isJobExecutionIdLockedForHibernating(Long jobExecutionId){
		boolean isLocked = jobExecutionIdsLockedForHibernating.contains(jobExecutionId);
		logger.debug("Lock for hibernation request for JobExecution id=" + jobExecutionId + ": isLocked=" + isLocked);
		return isLocked;
	}
	
	/**
	 * Removes JobExecution id from list of those locked for hibernating
	 * @param jobExecutionId
	 * @return
	 */
	public static synchronized void removeJobExecutionIdLockedForHibernating(Long jobExecutionId){
		logger.debug("Removing lock for hibernation request for JobExecution id=" + jobExecutionId);
		jobExecutionIdsLockedForHibernating.remove(jobExecutionId);
	}
	
	/**
	 * Record in the StepExecutionContext the time interval after which JobExecution running specified StepExecution should be re-awoken
	 * @param stepExecution
	 * @param timeInterval
	 */
	public static void setWakeTimeInterval(StepExecution stepExecution, Long timeInterval){
		ExecutionContext executionContext = stepExecution.getExecutionContext();
		executionContext.put(TIME_TO_WAKE, timeInterval);
		
	}
	
	/**
	 * Retrieve from the StepExecutionContext the time interval after which JobExecution running specified StepExecution should be re-awoken
	 * @param stepExecution
	 * @return
	 */
	public static Long getWakeTimeInterval(StepExecution stepExecution){
		ExecutionContext executionContext = stepExecution.getExecutionContext();
		if (!executionContext.containsKey(TIME_TO_WAKE)){
			logger.debug("Execution context of stepExecution id=" + stepExecution.getId() + " contains no wake time interval");
			return null; // empty set
		}
		return executionContext.getLong(TIME_TO_WAKE);
	}
	
	/**
	 * Record in the StepExecutionContext the messages which should trigger JobExecution abandonment
	 * @param stepExecution
	 * @param templates
	 * @throws JSONException
	 */
	public static void setAbandonMessagesForStep(StepExecution stepExecution, Set<WaspStatusMessageTemplate> templates) throws JSONException{
		ExecutionContext executionContext = stepExecution.getExecutionContext();
		JSONArray jsonForMessages = new JSONArray();
		for (WaspStatusMessageTemplate template: templates){
			logger.trace("template as json : " + template.getAsJson());
			jsonForMessages.put(template.getAsJson());
		}
		JSONObject json = new JSONObject();
		json.put(MESSAGES_TO_ABANDON, jsonForMessages);
		logger.debug("Created json string to persist : " + json.toString());
		executionContext.put(MESSAGES_TO_ABANDON, json.toString());
		
	}
	
	/**
	 * Retrieve from the StepExecutionContext the messages which should trigger JobExecution abandonment
	 * @param stepExecution
	 * @return
	 * @throws JSONException
	 */
	public static Set<WaspStatusMessageTemplate> getAbandonMessagesForStep(StepExecution stepExecution) throws JSONException{
		Set<WaspStatusMessageTemplate> templates = new HashSet<>();
		ExecutionContext executionContext = stepExecution.getExecutionContext();
		if (!executionContext.containsKey(MESSAGES_TO_ABANDON)){
			logger.debug("Execution context of stepExecution id=" + stepExecution.getId() + " contains no wake messages");
			return templates; // empty set
		}
		JSONObject json = new JSONObject(executionContext.getString(MESSAGES_TO_ABANDON));
		JSONArray jsonArray = json.getJSONArray(MESSAGES_TO_ABANDON);
		for (int i=0; i< jsonArray.length(); i++)
			templates.add(new WaspStatusMessageTemplate(jsonArray.getJSONObject(i)));
		return templates;
	}
	
	/**
	 * Record in the StepExecutionContext the messages which should trigger waking of the JobExecution 
	 * @param stepExecution
	 * @param templates
	 * @throws JSONException
	 */
	public static void setWakeMessagesForStep(StepExecution stepExecution, Set<WaspStatusMessageTemplate> templates) throws JSONException{
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
	
	/**
	 * Retrieve from the StepExecutionContext the messages which should trigger waking of the JobExecution
	 * @param stepExecution
	 * @return
	 * @throws JSONException
	 */
	public static Set<WaspStatusMessageTemplate> getWakeMessagesForStep(StepExecution stepExecution) throws JSONException{
		Set<WaspStatusMessageTemplate> templates = new HashSet<>();
		ExecutionContext executionContext = stepExecution.getExecutionContext();
		if (!executionContext.containsKey(MESSAGES_TO_WAKE)){
			logger.debug("Execution context of stepExecution id=" + stepExecution.getId() + " contains no wake messages");
			return templates; // empty set
		}
		JSONObject json = new JSONObject(executionContext.getString(MESSAGES_TO_WAKE));
		JSONArray jsonArray = json.getJSONArray(MESSAGES_TO_WAKE);
		for (int i=0; i< jsonArray.length(); i++)
			templates.add(new WaspStatusMessageTemplate(jsonArray.getJSONObject(i)));
		return templates;
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
