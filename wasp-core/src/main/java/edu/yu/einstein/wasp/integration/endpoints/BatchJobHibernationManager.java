package edu.yu.einstein.wasp.integration.endpoints;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.integration.annotation.ServiceActivator;

import edu.yu.einstein.wasp.batch.MessageAwokenBatchJobStep;
import edu.yu.einstein.wasp.batch.core.extension.WaspBatchExitStatus;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.templates.MessageAwokenHibernationMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.TimeoutAwokenHibernationMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;


/**
 * 
 * @author asmclellan
 *
 */
public class BatchJobHibernationManager {

	public static final String HIBERNATING_CODE = "HIBERNATING";
	public static final String WOKEN_ON_MESSAGE_KEY = "wokenOnMessage";
	public static final String MESSAGES_TO_WAKE = "w_msgs";
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private JobOperator jobOperator;
	
	private JobExplorer jobExplorer;
	
	private JobRepository jobRepository;
	
	private Map<WaspStatusMessageTemplate, Set<MessageAwokenBatchJobStep>> messageTemplatesForJob = new HashMap<>(); // use HashMap for fast message searching

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
		this.jobOperator = jobOperator;
	}
	
	private void addMessageTemplatesForJobStep(Long jobExecutionId, Long stepExecutionId, Collection<WaspStatusMessageTemplate> messagesTemplates) {
		for (WaspStatusMessageTemplate messageTemplate: messagesTemplates){
			if (!messageTemplatesForJob.containsKey(messageTemplate))
				messageTemplatesForJob.put(messageTemplate, new HashSet<MessageAwokenBatchJobStep>());
			messageTemplatesForJob.get(messageTemplate).add(new MessageAwokenBatchJobStep(jobExecutionId, stepExecutionId, messagesTemplates));
		}
	}
	
	/**
	 * Attempts to extract message templates pre-stored in the stepExecutionContext
	 * @param jobExecutionId
	 * @param stepExecutionId
	 */
	public void addMessageTemplatesForJobStep(Long jobExecutionId, Long stepExecutionId) {
		Set<WaspStatusMessageTemplate> messageTemplates = new HashSet<>();
		try {
			messageTemplates.addAll(getStoredWakeMessages(jobExplorer.getStepExecution(jobExecutionId, stepExecutionId)));
		} catch (Exception e) {
			logger.warn("Unable to obtain stored wake message templates from stepExecution id=" + stepExecutionId + ": " + e.getLocalizedMessage());
		}
		if (!messageTemplates.isEmpty()){
			logger.debug("Populating hibernation manager with " + messageTemplates + " message templates for stepExecution id=" + stepExecutionId);
			addMessageTemplatesForJobStep(jobExecutionId, stepExecutionId, messageTemplates);
		}
	}
	
	public void addMessageTemplateForJob(MessageAwokenBatchJobStep mabjs) {
		for (WaspStatusMessageTemplate messageTemplate: mabjs.getMessageTemplatesToWakeJob()){
			if (!messageTemplatesForJob.containsKey(messageTemplate))
				messageTemplatesForJob.put(messageTemplate, new HashSet<MessageAwokenBatchJobStep>());
			messageTemplatesForJob.get(messageTemplate).add(mabjs);
		}
	}
	
	@ServiceActivator
	public void handleMessage(Message<?> message){
		logger.debug("handling message: " + message);
		if (MessageAwokenHibernationMessageTemplate.actUponMessageIgnoringExecutionIds(message)){
			logger.debug("Message is to request stop and re-awaken on message");
			MessageAwokenHibernationMessageTemplate messageTemplate = new MessageAwokenHibernationMessageTemplate(message);
			addMessageTemplatesForJobStep(messageTemplate.getJobExecutionId(), messageTemplate.getStepExecutionId(), messageTemplate.getAwakenJobExecutionOnMessages());
			StepExecution se = jobExplorer.getStepExecution(messageTemplate.getJobExecutionId(), messageTemplate.getStepExecutionId());
			try {
				se.getExecutionContext().put(MESSAGES_TO_WAKE, getJsonStringForAwakenMessages(messageTemplate.getAwakenJobExecutionOnMessages()));
			} catch (JSONException e) {
				logger.warn("Unable to get JSON string for wake up messages: " + e.getLocalizedMessage());
				e.printStackTrace();
			} // persist messages in case of restart
			jobRepository.updateExecutionContext(se);
			WaspBatchExitStatus exitStatus = new WaspBatchExitStatus(jobExplorer.getJobExecution(messageTemplate.getJobExecutionId()).getExitStatus());
			if (exitStatus.isRunningAndAwake()){
				logger.debug("Going to hibernate JobExecution id=" + messageTemplate.getJobExecutionId() + " (requested from step Id=" + 
						messageTemplate.getStepExecutionId() + ")");
				hibernateJobExecution(messageTemplate.getJobExecutionId());
			}
		} else if (TimeoutAwokenHibernationMessageTemplate.actUponMessageIgnoringExecutionIds(message)){
			logger.debug("Message is to request stop and re-awaken on timeout");
			// TODO: functionality here
		} else {
			logger.debug("Message is not a request stop message");
			if (WaspStatus.class.isInstance(message.getPayload())){
				logger.debug("Message payload is of type WaspStatus");
				WaspStatusMessageTemplate incomingStatusMessageTemplate = new WaspStatusMessageTemplate((Message<WaspStatus>) message);
				logger.debug("Handling message: " + incomingStatusMessageTemplate.toString());
				if (messageTemplatesForJob.keySet().contains(incomingStatusMessageTemplate)){
					logger.debug("messageTemplatesForJob.keySet() contains message");
					for (MessageAwokenBatchJobStep messageAwokenBatchJobStep : messageTemplatesForJob.get(incomingStatusMessageTemplate)){
						logger.debug("restarting job with JobExecution id=" + messageAwokenBatchJobStep.getJobExecutionId() + " on receiving message " + message);
						reawakenJobExecution(messageAwokenBatchJobStep);
					}
				}	
				else
					logger.debug("messageTemplatesForJob.keySet() does not contain message");
					for (WaspStatusMessageTemplate mt : messageTemplatesForJob.keySet())
						logger.debug("messageTemplatesForJob entry : " + mt.toString());
			}
		}	
	}
	
	private void reawakenJobExecution(MessageAwokenBatchJobStep messageAwokenBatchJobStep){
		try {
			StepExecution se = jobExplorer.getStepExecution(messageAwokenBatchJobStep.getJobExecutionId(), messageAwokenBatchJobStep.getStepExecutionId());
			se.getExecutionContext().put(WOKEN_ON_MESSAGE_KEY, true);
			jobRepository.updateExecutionContext(se);
			jobOperator.restart(messageAwokenBatchJobStep.getJobExecutionId());
		} catch (JobInstanceAlreadyCompleteException | NoSuchJobExecutionException | NoSuchJobException | JobRestartException | JobParametersInvalidException e) {
			logger.warn("Unable to restart job with JobExecution id=" + messageAwokenBatchJobStep.getJobExecutionId() + 
					" (got " + e.getClass().getName() + " Exception :" + e.getLocalizedMessage() + ")");
		}
	}
	
	private void hibernateJobExecution(Long jobExecutionId){
		try {
			jobOperator.stop(jobExecutionId);
			logger.debug("updating exit status of stopped job execution with id=" + jobExecutionId);
			JobExecution je = jobExplorer.getJobExecution(jobExecutionId);
			while (!je.getExitStatus().getExitCode().equals(ExitStatus.STOPPED.getExitCode())){
				try {
					Thread.sleep(50); // defend against delay shutting down job
				} catch (InterruptedException e) {} 
				je = jobExplorer.getJobExecution(jobExecutionId);
			}
			je = jobExplorer.getJobExecution(jobExecutionId); // get fresh
			for (StepExecution se : je.getStepExecutions()){
				se.setExitStatus(WaspBatchExitStatus.HIBERNATING);
				jobRepository.update(se);
			}
			je.setExitStatus(WaspBatchExitStatus.HIBERNATING);
			jobRepository.update(je);
			je.getExecutionContext().put(BatchJobHibernationManager.HIBERNATING_CODE, true);
			jobRepository.updateExecutionContext(je);
			logger.debug("status updated :)");
		} catch (NoSuchJobExecutionException | JobExecutionNotRunningException e1) {
			logger.warn("Unable to stop job with JobExecution id=" + jobExecutionId + " (got " + e1.getClass().getName() + " Exception :" + 
					e1.getLocalizedMessage() + ")");
		}
	}
	
	private String getJsonStringForAwakenMessages(Set<WaspStatusMessageTemplate> templates) throws JSONException{
		JSONArray jsonForMessages = new JSONArray();
		for (WaspStatusMessageTemplate template: templates){
			logger.debug("template as json : " + template.getAsJson());
			jsonForMessages.put(template.getAsJson());
		}
		JSONObject json = new JSONObject();
		json.put(MESSAGES_TO_WAKE, jsonForMessages);
		logger.debug("Created json string to persist : " + json.toString());
		return json.toString();
	}
	
	public Set<WaspStatusMessageTemplate> getStoredWakeMessages(StepExecution stepExecution) throws JSONException{
		Set<WaspStatusMessageTemplate> templates = new HashSet<>();
		ExecutionContext context = stepExecution.getExecutionContext();
		if (!context.containsKey(MESSAGES_TO_WAKE)){
			logger.debug("Execution context of stepExecution id=" + stepExecution.getId() + " contains no wake messages");
			return templates; // empty set
		}
		JSONObject json = new JSONObject(context.getString(MESSAGES_TO_WAKE));
		JSONArray jsonArray = json.getJSONArray(MESSAGES_TO_WAKE);
		for (int i=0; i< jsonArray.length(); i++)
			templates.add(new WaspStatusMessageTemplate(jsonArray.getJSONObject(i)));
		return templates;
	}

}
