package edu.yu.einstein.wasp.integration.endpoints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

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
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.launch.wasp.JobOperatorWasp;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.IntegrationMessageHeaderAccessor;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.exception.WaspBatchJobExecutionException;
import edu.yu.einstein.wasp.exception.WaspBatchJobExecutionReadinessException;
import edu.yu.einstein.wasp.integration.messages.WaspMessageType;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.templates.StatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.WaspMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;
import edu.yu.einstein.wasp.interfacing.batch.ManyJobRecipient;


/**
 * 
 * @author asmclellan
 *
 */
public class BatchJobHibernationManager {

	public static final String ABANDON_ON_MESSAGE = "abandonedOnMessage";
	public static final String WOKEN_ON_MESSAGE_STATUS = "wokenOnMessageStatus";
	public static final String WOKEN_ON_TIMEOUT = "wokenOnTimeout";
	public static final String MESSAGES_TO_WAKE = "w_msgs";
	public static final String MESSAGES_TO_ABANDON = "a_msgs";
	public static final String TIME_TO_WAKE = "w_time";
	public static final int RESEND_MESSAGE_MAX_TIMES = 10;
	
	
	/**
	 * Keys for management of parent child relationship in LaunchManyJobsTasklet. 
	 */
	
	public static final String PARENT_JOB_ID_KEY = "parentJobId";
	public static final String PARENT_JOB_CHILD_LIST_KEY = "childJobIds";
	public static final String PARENT_JOB_CHILD_LIST_DELIMITER = "|";
	
	public static final String COMPLETED_CHILD_IDS = "completedChildren";
	public static final String ABANDONED_CHILD_IDS = "abandonedChildren";
	
	/**
	 * Delimited list of failed jobs to be stored in the StepExecutionContext of the LintenForManyStatusMessagesTasklet.
	 */
	public static final String FAILED_CHILD_JOBS_LIST_KEY = "failedchildjobs";
	
	private static final Logger logger = LoggerFactory.getLogger(BatchJobHibernationManager.class);
	
	private JobOperatorWasp jobOperator;
	
	private JobExplorer jobExplorer;
	
	private JobRepository jobRepository;
	
	// make the following variables volatile to prevent caching 
	private volatile Map<WaspStatusMessageTemplate, Set<StepExecution>> messageTemplatesWakingStepExecutions = new HashMap<>(); 
	
	private volatile Map<WaspStatusMessageTemplate, Set<StepExecution>> messageTemplatesAbandoningStepExecutions = new HashMap<>();
	
	private volatile Map<Long, Set<StepExecution>> timesWakingStepExecutions = new TreeMap<>(); 
	
	public enum LockType{ HIBERNATE, WAKE, ABANDON, ANY }
	
	private volatile static Map<Long, LockType> lockedJobExecutions = new HashMap<>();
	
	private volatile Map<UUID, ManyJobRecipient> waitingManyJobs = new HashMap<UUID, ManyJobRecipient>(); 
	
	private volatile Map<UUID, List<String>> completedManyJobs = new HashMap<UUID, List<String>>();
	
	private volatile Map<UUID, List<String>> abandonedManyJobs = new HashMap<UUID, List<String>>();
	
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
	
	public synchronized Map<WaspStatusMessageTemplate, Set<StepExecution>> getMessageTemplatesWakingStepExecutions() {
		return messageTemplatesWakingStepExecutions;
	}


	public synchronized Map<WaspStatusMessageTemplate, Set<StepExecution>> getMessageTemplatesAbandoningStepExecutions() {
		return messageTemplatesAbandoningStepExecutions;
	}


	/**
	 * TaskScheduler executed method: executed periodically at a rate defined in configuration (wasp.hibernation.heartbeat)
	 */
	public synchronized void runTimedTasks() {
		logger.trace("Checking to see if any StepExecutions require waking after timed sleep");
		Set<Long> orderedTimes = new TreeSet<>(timesWakingStepExecutions.keySet());
		Date timeNow = new Date();
		for (Long time: orderedTimes){
			if (time <= timeNow.getTime()){
				Set<StepExecution> ses = new HashSet<>(timesWakingStepExecutions.get(time));
				for (StepExecution seStored : ses){
					JobExecution je = jobExplorer.getJobExecution(seStored.getJobExecutionId());
					StepExecution se = jobRepository.getLastStepExecution(je.getJobInstance(), seStored.getStepName()); // must get fresh as may have new Id
					if (se == null){
						logger.warn("Attempted to get latest StepExecution for step " + seStored.getStepName() + " but it is null");
						continue;
					}
					logger.info("restarting job with JobExecution id=" + je.getId() + " for step " + se.getId() + 
							" on restart wait timeout");
					if (!lockJobExecution(se.getJobExecution(), LockType.WAKE)){
						logger.info("Not restarting job with JobExecution id=" + je.getId() + " for step " + se.getId() + 
								" on restart wait timeout because execution locked");
						continue;
					}
					try{
						reawakenJobExecution(se, WOKEN_ON_TIMEOUT, true);
						timesWakingStepExecutions.remove(time);
					} catch (WaspBatchJobExecutionException e){
						logger.warn("Problem reawakening job execution : " + e.getLocalizedMessage());
						unlockJobExecution(je, LockType.WAKE);
					} catch (Throwable e1){
						logger.warn("Problem reawakening job execution. Caught " + e1.getClass().getName() + " exception.: " + e1.getLocalizedMessage());
						unlockJobExecution(je, LockType.WAKE);
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
	        logger.trace("handleStatusMessage");
		int resendCount = 0;
		boolean resendMessage = false;
		if (message.getHeaders().containsKey(WaspMessageTemplate.RESEND))
			resendCount = message.getHeaders().get(WaspMessageTemplate.RESEND, Integer.class);
		WaspStatusMessageTemplate incomingStatusMessageTemplate = new WaspStatusMessageTemplate((Message<WaspStatus>) message);
		//remove superfluous headers if present (these are not used to make decisions about acting on messages)
		sanitizeHeaders(incomingStatusMessageTemplate);
		logger.info("Handling message: " + incomingStatusMessageTemplate.toString());
		Set<Long> handledJobExecutionIds = new HashSet<Long>();
		if (incomingStatusMessageTemplate.getHeader(WaspMessageType.HEADER_KEY).equals(WaspMessageType.MANY) &&
		        isListenerRegistered(incomingStatusMessageTemplate) == false) {
		    
		    if (!message.getHeaders().containsKey(WaspMessageTemplate.RESEND))
		            resendCount = 0;
		    else
		        resendCount++;
		    
		    Message<WaspStatus> newMessage = getMessageToResend(message, resendCount);
		    
		    logger.trace("RESEND " + resendCount +":"+ waitingManyJobs.containsKey(incomingStatusMessageTemplate.getHeader(WaspMessageTemplate.PARENT_ID)));
		    
		    logger.debug("Result message from " + incomingStatusMessageTemplate.getHeader(WaspMessageTemplate.PARENT_ID) + 
                            " has not been registered yet, resending " + resendCount);
                    logger.info("Returning MANY message to queue for retry " + resendCount + " / " + RESEND_MESSAGE_MAX_TIMES + " :" + newMessage);
                    return newMessage;
		}
		try {
			handledJobExecutionIds.addAll(handleStepExecutionWakingOnMessage(incomingStatusMessageTemplate));
		} catch (WaspBatchJobExecutionReadinessException e) {
			logger.debug("Going to request return message to queue due to at least one job not being ready for it (" + e + ")");
			resendMessage = true;
		}
		try {
			handledJobExecutionIds.addAll(handleStepExecutionAbandonmentOnMessage(incomingStatusMessageTemplate));
		} catch (WaspBatchJobExecutionReadinessException e) {
			logger.debug("Going to request return message to queue due to at least one job not being ready for it (" + e + ")");
			resendMessage = true;
		}
		if (resendMessage){
			resendCount++;
			Message<WaspStatus> newMessage = getMessageToResend(message, resendCount);
			logger.info("Returning message to queue for retry " + resendCount + " / " + RESEND_MESSAGE_MAX_TIMES + " :" + newMessage);
			return newMessage;
		}
		Message<WaspStatus> replyMessage = getReplyMessage(message);
		logger.debug("Returning reply message: " + replyMessage);
		return replyMessage;
	}
	
	private void processManyMessage(WaspStatusMessageTemplate m) {
	    // without the MANY message type or PARENT_ID header, this is a bad or just a normal message, skip.
	    if (m.getHeader(WaspMessageType.HEADER_KEY).equals(WaspMessageType.MANY) && !m.getHeaders().containsKey(WaspStatusMessageTemplate.PARENT_ID)) {
	        logger.warn("received message proporting to be a MANY message, but it does not contain a PARENT_ID!.  Skipping!");
	        return;
	    }
	    
	}
	
	Message<WaspStatus> getMessageToResend(Message<WaspStatus> originalMessage, int resendCount){
		// make new message of high priority
		Message<WaspStatus> newMessage = MessageBuilder
				.fromMessage(originalMessage)
				.setPriority(1)
				.setHeader(WaspMessageTemplate.RESEND, resendCount)
				.build();
		logger.debug("Going to return message to queue due to at least one job not being ready for it : " + newMessage);
		return newMessage;
	}
	
	/**
	 * Checks provided WaspStatusMessage against set of messages that are being listened for in order to abandon batch jobs. Removes message
	 * from set after jobs successfully abandoned.
	 * @param messageTemplate
	 * @return number of steps handled
	 */
	@Transactional
	public synchronized Set<Long> handleStepExecutionAbandonmentOnMessage(WaspStatusMessageTemplate messageTemplate) throws WaspBatchJobExecutionReadinessException{
	        logger.trace("handleStepExecutionAbandonmentOnMessage");
		Set<Long> abandondedJobExecutionIds = new HashSet<>();
		int pushMessageBackIntoQueueRequests = 0;
		if (messageTemplatesAbandoningStepExecutions.keySet().contains(messageTemplate)){
			logger.debug("messageTemplatesAbandoningStepExecutions.keySet() contains message");
			Set<StepExecution> ses = new HashSet<>(messageTemplatesAbandoningStepExecutions.get(messageTemplate));
			for (StepExecution seStored :ses){
				JobExecution je = jobExplorer.getJobExecution(seStored.getJobExecutionId());
				StepExecution se = jobRepository.getLastStepExecution(je.getJobInstance(), seStored.getStepName()); // must get fresh as may have new Id
				if (!abandondedJobExecutionIds.contains(je.getId())){
					abandondedJobExecutionIds.add(je.getId()); // make sure only performed once per jobExecution
					if (!isLockedJobExecution(je, LockType.ABANDON)){
						// if already locked for abandonment do not try and lock again. We might have been stopping and waiting to try again
						if (!lockJobExecution(je, LockType.ABANDON)){
							logger.debug("Unable to abandon JobExecution (id=" + je.getId() + ") because it is locked for another task");
							pushMessageBackIntoQueueRequests++;
							continue;
						}
					}
					if (se == null){
						logger.warn("Attempted to get latest StepExecution for step " + seStored.getStepName() + " but it is null");
						pushMessageBackIntoQueueRequests++;
						continue;
					}
					logger.info("Abandoning job with JobExecution id=" + je.getId() + " for step " + se.getId() + 
							" on receiving message " + messageTemplate.getPayload().toString());
					try{
					        // MANY messages have a different storage mechanism, here we're going to mark the message
                                                // as abandoned.  The ListenForManyStatusMessagesTasklet is responsible for counting
                                                // all messages.  Here we eschew abandonment to allow the listener to scoop up all
					        // remaining messages.

                                                abandonJobExecution(se);                                                
						// remove all step executions being monitored for this abandoned JobExecution
						removeStepExecutionFromMessageMap(je, messageTemplatesWakingStepExecutions);
						removeStepExecutionFromMessageMap(je, messageTemplatesAbandoningStepExecutions);
						unlockJobExecution(je, LockType.ABANDON);
					} catch (WaspBatchJobExecutionReadinessException e1){
						logger.debug(e1.getLocalizedMessage());
						pushMessageBackIntoQueueRequests++;
					} catch (WaspBatchJobExecutionException e2){
						logger.debug("Problem aborting job execution and cleaning up 'messageTemplatesAbandoningStepExecutions': " + e2.getLocalizedMessage());
						unlockJobExecution(je, LockType.ABANDON);
					} catch (Throwable e1){
						logger.warn("Problem aborting job execution and cleaning up 'messageTemplatesAbandoningStepExecutions'. Caught " + 
								e1.getClass().getName() + " exception.: " + e1.getLocalizedMessage());
						unlockJobExecution(je, LockType.ABANDON);
					}
				}
			}
			if (pushMessageBackIntoQueueRequests > 0){
				throw new WaspBatchJobExecutionReadinessException("Need to push message back into queue as " + pushMessageBackIntoQueueRequests + 
						" requests made");
			}
		}	
		else
			logger.debug("messageTemplatesAbandoningStepExecutions.keySet() does not contain message");
		return abandondedJobExecutionIds;
	}
	
	/**
	 * Checks provided WaspStatusMessage against set of messages that are being listened for in order to wake batch jobs. Removes message
	 * from set after jobs successfully woken.
	 * @param messageTemplate
	 * @return number of steps handled
	 * @throws WaspBatchJobExecutionReadinessException 
	 */
	@Transactional
	public synchronized Set<Long> handleStepExecutionWakingOnMessage(WaspStatusMessageTemplate messageTemplate) throws WaspBatchJobExecutionReadinessException{
	        logger.trace("handleStepExecutionWakingOnMessage");
		Set<Long> awakeningJobExecutionIds = new HashSet<>();
		int pushMessageBackIntoQueueRequests = 0;
		if (messageTemplatesWakingStepExecutions.keySet().contains(messageTemplate)){
			logger.debug("messageTemplatesWakingStepExecutions.keySet() contains message");
			Set<StepExecution> ses = new HashSet<>(messageTemplatesWakingStepExecutions.get(messageTemplate));
			for (StepExecution seStored : ses){
				JobExecution je = jobExplorer.getJobExecution(seStored.getJobExecutionId());
				StepExecution se = jobRepository.getLastStepExecution(je.getJobInstance(), seStored.getStepName()); // must get fresh as may have new Id
				if (!awakeningJobExecutionIds.contains(je.getId())){
					awakeningJobExecutionIds.add(je.getId());
					if (se == null){
						logger.warn("Attempted to get latest StepExecution for step " + seStored.getStepName() + " but it is null");
						pushMessageBackIntoQueueRequests++;
						continue;
					}
					logger.info("Waking job with JobExecution id=" +je.getId() + " for step " + se.getId() + 
							" on receiving message " + messageTemplate.getPayload().toString());
					
					if (!lockJobExecution(je, LockType.WAKE)){
						logger.debug("Unable to get lock for JobExecution id=" + je.getId());
						pushMessageBackIntoQueueRequests++;
						continue;
					}
					
					try{
						reawakenJobExecution(se, WOKEN_ON_MESSAGE_STATUS, messageTemplate.getStatus());
						removeStepExecutionFromMessageMap(se, messageTemplatesWakingStepExecutions);
						removeStepExecutionFromMessageMap(se, messageTemplatesAbandoningStepExecutions);
					} catch (WaspBatchJobExecutionException e){
						pushMessageBackIntoQueueRequests++;
						unlockJobExecution(je, LockType.WAKE);
						logger.debug("Problem reawakening job execution and cleaning up 'messageTemplatesWakingStepExecutions': " + e.getLocalizedMessage());
					} catch (Throwable e1){
						logger.warn("Problem reawakening job execution and cleaning up 'messageTemplatesAbandoningStepExecutions'. Caught " + 
								e1.getClass().getName() + " exception.: " + e1.getLocalizedMessage());
						unlockJobExecution(je, LockType.WAKE);
					}
				} 
			}
			if (pushMessageBackIntoQueueRequests > 0){
				throw new WaspBatchJobExecutionReadinessException("Need to push message back into queue as " + pushMessageBackIntoQueueRequests + 
						" requests made");
			}
		} else if (messageTemplate.getHeader(WaspMessageType.HEADER_KEY).equals(WaspMessageType.MANY)) {
		    // MANY messages have a different storage mechanism, if this is a MANY return message we're going 
		    // to mark the message as received and then check to see if all messages are in.  If so, we will wake
		    // the ListenForManyStatusMessagesTasklet to deal with moving on.
		    if (!messageTemplate.getHeaders().containsKey(WaspMessageTemplate.PARENT_ID)) {
		        logger.warn("got message " + messageTemplate.toString() + " which claims to be of type MANY, but does not contain a PARENT_ID");
		    } else {
                        UUID parentId = UUID.fromString((String) messageTemplate.getHeader(WaspMessageTemplate.PARENT_ID));
                        Integer childId = Integer.decode(messageTemplate.getHeader(WaspMessageTemplate.CHILD_MESSAGE_ID).toString());
                        WaspStatus status = messageTemplate.getStatus();
                        if (status == WaspStatus.COMPLETED) {
                            logger.debug("Marking " + parentId.toString() + " child " + childId + " as COMPLETED");
                            markManyJobAsCompleted(waitingManyJobs.get(parentId), childId);
                        } else {
                            // anything other than completed will be treated as abandoned...
                            logger.debug("Marking " + parentId.toString() + " child " + childId + " as ABANDONED");
                            markManyJobAsAbandoned(waitingManyJobs.get(parentId), childId);
                        }
                        int comp = 0;
                        int aban = 0;
                        if (completedManyJobs.get(parentId) != null)
                            comp = completedManyJobs.get(parentId).size();
                        if (abandonedManyJobs.get(parentId) != null)
                            aban = abandonedManyJobs.get(parentId).size();
                        
                        int completed = comp + aban;
                        int waiting = waitingManyJobs.get(parentId).getChildIDs().size();
                        
                        if (completed < waiting) {
                            logger.trace("Received " + completed + " out of " + waiting + " completion messages for MANY job with parent ID " + parentId);
                        } else {
                            logger.debug("Received all " + completed + " out of " + waiting + " completion messages for MANY job with parent ID " + parentId + ", proceeding to wake step.");
                            doHandleManyComplete(parentId);
                        }
		    }
		} else {
                    logger.debug("messageTemplatesWakingStepExecutions.keySet() does not contain message");
                }
		return awakeningJobExecutionIds;
	}
	
    /**
     * Wake up a many job after all messages are accounted for.
     * @param parentId
     */
    private void doHandleManyComplete(UUID parentId) {
        logger.trace("doHandleManyAbandonmentMessage");
        ManyJobRecipient recip = waitingManyJobs.get(parentId);
        JobExecution je = jobExplorer.getJobExecution(recip.getJobExecutionId());
        StepExecution se = jobRepository.getLastStepExecution(je.getJobInstance(), recip.getStepName()); // must get fresh as may have new Id
        try {
            reawakenJobExecution(se, WOKEN_ON_MESSAGE_STATUS, WaspStatus.COMPLETED);
            removeStepExecutionFromMessageMap(se, messageTemplatesWakingStepExecutions);
            removeStepExecutionFromMessageMap(se, messageTemplatesAbandoningStepExecutions);
            unlockJobExecution(je, LockType.ANY);
        } catch (WaspBatchJobExecutionException e) {
            unlockJobExecution(se.getJobExecution(), LockType.WAKE);
            logger.debug("Problem reawakening job execution and cleaning up 'messageTemplatesWakingStepExecutions': " + e.getLocalizedMessage());
        } catch (Exception e1) {
            logger.warn("Problem reawakening job execution and cleaning up 'messageTemplatesAbandoningStepExecutions'. Caught " + e1.getClass().getName()
                    + " exception.: " + e1.getLocalizedMessage());
            unlockJobExecution(se.getJobExecution(), LockType.WAKE);
        }

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
						.setHeader(WaspMessageType.HEADER_KEY, WaspMessageType.REPLY)
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
	
	public synchronized void addMessageTemplatesForActioningJobStep(Long jobExecutionId, Long stepExecutionId, 
			Collection<WaspStatusMessageTemplate> messageTemplates, Map<WaspStatusMessageTemplate, Set<StepExecution>> templateExecutionMap) {
		StepExecution se = jobExplorer.getStepExecution(jobExecutionId, stepExecutionId);
		if (!messageTemplates.isEmpty()){
			logger.info("Populating hibernation manager with " + messageTemplates + " message templates for stepExecution id=" + stepExecutionId);
			for (WaspStatusMessageTemplate messageTemplate: messageTemplates){
				if (!templateExecutionMap.containsKey(messageTemplate))
					templateExecutionMap.put(messageTemplate, new HashSet<StepExecution>());
				Set<StepExecution> ses = new HashSet<>(templateExecutionMap.get(messageTemplate));
				for (StepExecution currentStepExecution: ses){
					// If job re-awoken after hibernation, its original step will have a new id. Remove the old one first.
					if (currentStepExecution.getStepName().equals(se.getStepName()) && currentStepExecution.getJobExecutionId().equals(se.getJobExecutionId())){
						logger.debug("Removing old StepExecution (id=" + currentStepExecution.getId() + 
								") from message template map due to receiving a new step (id=" + se.getId() + ") with the same name (JobExecutionId=" + jobExecutionId + ")");
						templateExecutionMap.get(messageTemplate).remove(currentStepExecution);
						break;
					}
				}
				templateExecutionMap.get(messageTemplate).add(se);
			}
		}
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
			messageTemplates.addAll(getWakeMessagesFromStepExecutionContext(se));
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
		addMessageTemplatesForActioningJobStep(jobExecutionId, stepExecutionId, messageTemplates, messageTemplatesWakingStepExecutions);
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
		addMessageTemplatesForActioningJobStep(jobExecutionId, stepExecutionId, messageTemplates, messageTemplatesAbandoningStepExecutions);
	}
	
	public synchronized void removeStepExecutionFromWakeMessageMap(StepExecution se){
		removeStepExecutionFromMessageMap(se, messageTemplatesWakingStepExecutions);
	}
	
	public synchronized void removeStepExecutionFromAbandonMessageMap(StepExecution se){
		removeStepExecutionFromMessageMap(se, messageTemplatesAbandoningStepExecutions);
	}
	
	public synchronized void removeStepExecutionFromWakeMessageMap(JobExecution je){
		removeStepExecutionFromMessageMap(je, messageTemplatesWakingStepExecutions);
	}
	
	public synchronized void removeStepExecutionFromAbandonMessageMap(JobExecution je){
		removeStepExecutionFromMessageMap(je, messageTemplatesAbandoningStepExecutions);
	}
	
	/**
	 * Removes all handled StepExecutions and messages from the provided map of message/StepExecution associations
	 * @param se
	 * @param m
	 */
	private void removeStepExecutionFromMessageMap(StepExecution se, Map<WaspStatusMessageTemplate, Set<StepExecution>> m){
		Assert.assertParameterNotNull(se, "StepExecution se cannot be null");
		Assert.assertParameterNotNull(m, "Set<StepExecution>> m cannot be null");
		Set<StatusMessageTemplate> templates = new HashSet<StatusMessageTemplate>(m.keySet());
		for (StatusMessageTemplate template :templates){
			if (m.get(template).contains(se)){
				logger.debug("Removing stepExecution id=" + se.getId() + 
						" from list of step executions responding to message template : " + template.toString());
				m.get(template).remove(se);
			}
			if (m.get(template).isEmpty()){
				logger.debug("Removing message template from list of messages to watch for as no more StepExecutions depend on it : " + 
						template.toString());
				m.remove(template);
			}
		}
	}
	
	/**
	 * Removes all handled StepExecutions and messages from the provided map of message/StepExecution associations if they are associated with the given
	 * JobExecution
	 * @param je
	 * @param m
	 */
	private synchronized void removeStepExecutionFromMessageMap(JobExecution je, Map<WaspStatusMessageTemplate, Set<StepExecution>> m){
		Set<StatusMessageTemplate> templates = new HashSet<StatusMessageTemplate>(m.keySet());
		for (StatusMessageTemplate template :templates){
			Set<StepExecution> ses = new HashSet<>(m.get(template));
			for (StepExecution se : ses){
				if (se.getJobExecutionId().equals(je.getId())){
					logger.debug("Removing stepExecution id=" + se.getId() + 
							" from list of step executions responding to message template : " + template.toString());
					m.get(template).remove(se);
				}
				if (m.get(template).isEmpty()){
					logger.debug("Removing message template from list of messages to watch for as no more StepExecutions depend on it : " + 
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
	 * @throws WaspBatchJobExecutionReadinessException 
	 */
	public void processHibernateRequest(Long jobExecutionId, Long stepExecutionId) throws WaspBatchJobExecutionReadinessException{
		logger.debug("Request received to hibernate a JobExecution");
		JobExecution je = jobExplorer.getJobExecution(jobExecutionId);
		ExitStatus exitStatus = je.getExitStatus();
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
	 * @throws WaspBatchJobExecutionReadinessException 
	 */
	public void processHibernateRequest(Long jobExecutionId, Long stepExecutionId, Collection<WaspStatusMessageTemplate> messageTemplates) throws WaspBatchJobExecutionReadinessException{
		addMessageTemplatesForWakingJobStep(jobExecutionId, stepExecutionId, messageTemplates);
		processHibernateRequest(jobExecutionId, stepExecutionId);
	}
	
	/**
	 * Requests hibernation of a specified batch JobExecution by specified StepExecution, to be re-awoken after specified time interval
	 * @param jobExecutionId
	 * @param stepExecutionId
	 * @param timeInterval
	 * @throws WaspBatchJobExecutionReadinessException 
	 */
	public void processHibernateRequest(Long jobExecutionId, Long stepExecutionId, Long timeInterval) throws WaspBatchJobExecutionReadinessException{
		addTimeIntervalForJobStep(jobExecutionId, stepExecutionId, timeInterval);
		processHibernateRequest(jobExecutionId, stepExecutionId);
	}
	
	/**
	 * Abandon JobExecution running provided StepExecution
	 * @param stepExecution
	 * @throws WaspBatchJobExecutionException
	 */
	@Transactional
	public void abandonJobExecution(StepExecution stepExecution) throws WaspBatchJobExecutionException{
		JobExecution je = jobExplorer.getJobExecution(stepExecution.getJobExecutionId());
		if (je.getStatus().isRunning()){
			logger.debug("Going to stop JobExecution (id=" + je.getId() + ") because job is running");
			try{
				jobOperator.stop(stepExecution.getJobExecutionId()); 
			} catch (Exception e) {
				logger.debug("Cannot stop JobExecution (id=" + je.getId() + "): " + e.getLocalizedMessage());
			} 
			throw new WaspBatchJobExecutionReadinessException("Stopping before abandoning JobExecution (id=" + je.getId() + ")");
		} else if (je.getStatus().isUnsuccessful() || je.getStatus().equals(BatchStatus.COMPLETED)){
			logger.info("Not going to abandon JobExecution (id=" + je.getId() + ") as alreaded finished running");
			return;
		}
		try{
			jobOperator.abandon(stepExecution.getJobExecutionId());
			Set<StepExecution> ses = new HashSet<>(je.getStepExecutions());
			for (StepExecution se : ses){
				if (se.getId().equals(stepExecution.getId()))
					se.getExecutionContext().put(ABANDON_ON_MESSAGE, true);
				jobRepository.updateExecutionContext(se);
			}
		} catch (Exception e) {
			throw new WaspBatchJobExecutionException("Unable to abandon job with JobExecution id=" + stepExecution.getJobExecutionId() + 
					" (got " + e.getClass().getName() + " Exception :" + e.getLocalizedMessage() + ")");
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
	private void reawakenJobExecution(StepExecution stepExecution, String contextRecordKey, Object contextRecordValue) throws WaspBatchJobExecutionException{
		JobExecution je = jobExplorer.getJobExecution(stepExecution.getJobExecutionId());
		ExitStatus status = je.getExitStatus();
		if (!status.isHibernating()){
			throw new WaspBatchJobExecutionReadinessException("Unable to wake JobExecution (id=" + je.getId() + ") because it is not hibernating");
		} else if (jobExplorer.getStepExecution(stepExecution.getJobExecutionId(), stepExecution.getId()) == null){
			throw new WaspBatchJobExecutionReadinessException("Unable to wake JobExecution (id=" + je.getId() + ") because StepExecution is stale");
		} else {
			logger.debug("StepExecution (id=" + stepExecution.getId() + ", name=" + stepExecution.getStepName() + 
					", JobExecutionId=" + je.getId() + ") has context=" + stepExecution.getExecutionContext());
			logger.debug("Updating context for StepExecution (id=" + stepExecution.getId() + ", name=" + stepExecution.getStepName() + 
					", JobExecutionId=" + je.getId() + ") setting " + contextRecordKey + "=" + contextRecordValue); 
			stepExecution.getExecutionContext().put(contextRecordKey, contextRecordValue);
			jobRepository.updateExecutionContext(stepExecution);
			try{
				jobOperator.wake(stepExecution.getJobExecutionId());
			} catch (JobInstanceAlreadyCompleteException | NoSuchJobExecutionException | NoSuchJobException | JobRestartException | JobParametersInvalidException e) {
				throw new WaspBatchJobExecutionException("Unable to restart job with JobExecution id=" + stepExecution.getJobExecutionId() + 
						" (got " + e.getClass().getName() + " Exception :" + e.getLocalizedMessage() + ")");
			} 
		}
	}
	
	/**
	 * Hibernate specified JobExection
	 */
	private void hibernateJobExecution(Long jobExecutionId) throws WaspBatchJobExecutionReadinessException{
		JobExecution je = jobExplorer.getJobExecution(jobExecutionId);
		ExitStatus status = je.getExitStatus();
		if (!status.isRunningAndAwake())
			throw new WaspBatchJobExecutionReadinessException("Unable to hibernate JobExecution (id=" + jobExecutionId + ") because job is not running");
		if (!isLockedJobExecution(je, LockType.HIBERNATE))
			throw new WaspBatchJobExecutionReadinessException("Unable to hibernate JobExecution (id=" + jobExecutionId + ") because job is not locked for hibernation");
		try {
			jobOperator.hibernate(jobExecutionId);
		} catch (Throwable e1) {
			unlockJobExecution(je, LockType.HIBERNATE);
			throw new WaspBatchJobExecutionReadinessException("Unable to hibernate JobExecution (id=" + jobExecutionId + 
					" (got " + e1.getClass().getName() + " Exception :" +  e1.getLocalizedMessage() + ")");
		} 
	}
	
	/**
	 * Record in the StepExecutionContext the time interval after which JobExecution running specified StepExecution should be re-awoken
	 * @param jobExecutionId
	 * @param stepExecutionId
	 * @param timeInterval
	 */
	public void setWakeTimeInterval(Long jobExecutionId, Long stepExecutionId, Long timeInterval){
		StepExecution stepExecution = jobExplorer.getStepExecution(jobExecutionId, stepExecutionId);
		logger.debug("Setting wake-time interval=" + timeInterval);
		ExecutionContext executionContext = stepExecution.getExecutionContext();
		executionContext.put(TIME_TO_WAKE, timeInterval);
		jobRepository.updateExecutionContext(stepExecution);
	}
	
	/**
	 * Record in the StepExecutionContext the time interval after which JobExecution running specified StepExecution should be re-awoken
	 * This method does not update the jobRepository. Intended to be called from within a tasklet.
	 * @param stepExecution
	 * @param timeInterval
	 */
	public static void setWakeTimeInterval(StepExecution stepExecution, Long timeInterval){
		logger.debug("Setting wake-time interval=" + timeInterval);
		ExecutionContext executionContext = stepExecution.getExecutionContext();
		executionContext.put(TIME_TO_WAKE, timeInterval);
	}
	
	/**
	 * Retrieve from the StepExecutionContext the time interval after which JobExecution running specified StepExecution should be re-awoken
	 * @param jobExecutionId
	 * @param stepExecutionId
	 * @return
	 */
	public Long getWakeTimeInterval(Long jobExecutionId, Long stepExecutionId){
		return getWakeTimeInterval(jobExplorer.getStepExecution(jobExecutionId, stepExecutionId));
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
		logger.debug("Wake-time interval=" + executionContext.getLong(TIME_TO_WAKE));
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
	 * Record in the StepExecutionContext the messages which should trigger waking of the JobExecution. 
	 * This method does not update the jobRepository. Intended to be called from within a tasklet.
	 * @param stepExecution
	 * @param templates
	 * @throws JSONException
	 */
	public static void setWakeMessagesInStepExecutionContext(StepExecution stepExecution, Set<WaspStatusMessageTemplate> templates) throws JSONException{
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
	public static Set<WaspStatusMessageTemplate> getWakeMessagesFromStepExecutionContext(StepExecution stepExecution) throws JSONException{
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
	 * Lock a JobExecution by type.
	 * @param jobExecution
	 * @param type
	 * @return
	 */
	public synchronized static boolean lockJobExecution(JobExecution jobExecution, LockType type){
		if (jobExecution == null)
			return false;
		Long id = jobExecution.getId();
		if (id == null)
			return false;
		if ( isLockedJobExecution(jobExecution, LockType.ANY) )
			return false;
		logger.info("Locking JobExecution id=" + jobExecution.getId() + " for " + type);
		logger.info("Locked JobExecutions = {" + StringUtils.collectionToCommaDelimitedString(lockedJobExecutions.keySet()) + "}");
		lockedJobExecutions.put(id, type);
		return true;
	}
	
	/**
	 * Verify if a JobExecution is locked by a specified type
	 * @param jobExecution
	 * @param type
	 * @return
	 */
	public synchronized static boolean isLockedJobExecution(JobExecution jobExecution, LockType type){
	    logger.trace("Locked JobExecutions = {" + StringUtils.collectionToCommaDelimitedString(lockedJobExecutions.keySet()) + "}");
	    logger.trace("LOCK: " + lockedJobExecutions.get(jobExecution.getId()));
		if ( lockedJobExecutions.containsKey(jobExecution.getId()))
		    
			if (type.equals(LockType.ANY))
				return true;
			else if (lockedJobExecutions.get(jobExecution.getId()).equals(type))
				return true;
		return false;
	}
	
	/**
	 * Unlock a JobExecution if locked by the specified type
	 * @param jobExecution
	 * @param type
	 * @return
	 */
	public synchronized static boolean unlockJobExecution(JobExecution jobExecution, LockType type){
		if ( lockedJobExecutions.containsKey(jobExecution.getId())){
			if (type.equals(LockType.ANY)){
				logger.info("Unlocking JobExecution id=" + jobExecution.getId() + " for " + type);
				lockedJobExecutions.remove(jobExecution.getId());
				return true;
			} else if (lockedJobExecutions.get(jobExecution.getId()).equals(type)){
				logger.info("Unlocking JobExecution id=" + jobExecution.getId() + " for " + type);
				lockedJobExecutions.remove(jobExecution.getId());
				return true;
			}
		}
		return false;
	}
	


	/**
	 * Remove spring integration headers (specified in {@link MessageHeaders}) and superfluous WASP message headers from header set (if any)
	 * @param headers
	 * @return
	 */
	private void sanitizeHeaders(WaspStatusMessageTemplate template){
		template.removeHeader(MessageHeaders.CONTENT_TYPE);
		template.removeHeader(IntegrationMessageHeaderAccessor.CORRELATION_ID);
		template.removeHeader(MessageHeaders.ERROR_CHANNEL);
		template.removeHeader(IntegrationMessageHeaderAccessor.EXPIRATION_DATE);
		template.removeHeader(MessageHeaders.ID);
		template.removeHeader(IntegrationMessageHeaderAccessor.POSTPROCESS_RESULT);
		template.removeHeader(IntegrationMessageHeaderAccessor.PRIORITY);
		template.removeHeader(MessageHeaders.REPLY_CHANNEL);
		template.removeHeader(IntegrationMessageHeaderAccessor.SEQUENCE_DETAILS);
		template.removeHeader(IntegrationMessageHeaderAccessor.SEQUENCE_NUMBER);
		template.removeHeader(IntegrationMessageHeaderAccessor.SEQUENCE_SIZE);
		template.removeHeader(MessageHeaders.TIMESTAMP);
		template.removeHeader(WaspStatusMessageTemplate.COMMENT_KEY); 
		template.removeHeader(WaspStatusMessageTemplate.USER_KEY);
		template.removeHeader(WaspStatusMessageTemplate.EXIT_DESCRIPTION_HEADER);
		template.removeHeader(WaspStatusMessageTemplate.DESTINATION);
		template.removeHeader(WaspStatusMessageTemplate.RESEND);
	}
	
    public synchronized void markManyJobAsCompleted(ManyJobRecipient recip, Integer childId) {
        logger.trace("getting job execution " + recip.getJobExecutionId());
        JobExecution je = jobExplorer.getJobExecution(recip.getJobExecutionId());
        StepExecution se = jobRepository.getLastStepExecution(je.getJobInstance(), recip.getStepName()); // must get fresh as may have new Id
        ExecutionContext stepContext = se.getExecutionContext();
        
        List<String> completed = new ArrayList<String>();
        
        if (stepContext.containsKey(COMPLETED_CHILD_IDS)) {
            String cids = stepContext.getString(COMPLETED_CHILD_IDS);
            completed.addAll(Arrays.asList(StringUtils.delimitedListToStringArray(cids, PARENT_JOB_CHILD_LIST_DELIMITER)));
        }
        completed.add(childId.toString());
        
        completedManyJobs.put(recip.getParentID(), completed);
        
        stepContext.putString(COMPLETED_CHILD_IDS, StringUtils.collectionToDelimitedString(completed, PARENT_JOB_CHILD_LIST_DELIMITER));
        logger.debug("persisting completed: " + StringUtils.collectionToDelimitedString(completed, PARENT_JOB_CHILD_LIST_DELIMITER));
        jobRepository.updateExecutionContext(se);
    }

    public synchronized void markManyJobAsAbandoned(ManyJobRecipient recip, Integer childId) {
        logger.trace("getting job execution " + recip.getJobExecutionId());
        JobExecution je = jobExplorer.getJobExecution(recip.getJobExecutionId());
        StepExecution se = jobRepository.getLastStepExecution(je.getJobInstance(), recip.getStepName()); // must get fresh as may have new Id
        ExecutionContext stepContext = se.getExecutionContext();
        
        List<String> abandoned = new ArrayList<String>();
        
        if (stepContext.containsKey(ABANDONED_CHILD_IDS)) {
            String acids = stepContext.getString(ABANDONED_CHILD_IDS);
            abandoned.addAll(Arrays.asList(StringUtils.delimitedListToStringArray(acids, PARENT_JOB_CHILD_LIST_DELIMITER)));
        }
        abandoned.add(childId.toString());
        
        abandonedManyJobs.put(recip.getParentID(), abandoned);
        
        stepContext.putString(ABANDONED_CHILD_IDS, StringUtils.collectionToDelimitedString(abandoned, PARENT_JOB_CHILD_LIST_DELIMITER));
        logger.debug("persisting abandoned: " + StringUtils.collectionToDelimitedString(abandoned, PARENT_JOB_CHILD_LIST_DELIMITER));
        jobRepository.updateExecutionContext(se);
    }
    
    public synchronized void registerManyStepCompletionListener(ManyJobRecipient jobRecipient) {
        logger.debug("registering step with parent id " + jobRecipient.getParentID());
        waitingManyJobs.put(jobRecipient.getParentID(), jobRecipient);
    }
    
    public synchronized void unregisterManyStepCompletionListener(ManyJobRecipient jobRecipient) {
        logger.debug("unregistering step with parent id " + jobRecipient.getParentID());
        waitingManyJobs.remove(jobRecipient.getParentID());
        completedManyJobs.remove(jobRecipient.getParentID());
        abandonedManyJobs.remove(jobRecipient.getParentID());
    }
    
    public synchronized boolean isListenerRegistered(WaspStatusMessageTemplate template) {
        return waitingManyJobs.containsKey(UUID.fromString((String)template.getHeader(WaspMessageTemplate.PARENT_ID)));
    }
    
}
