package edu.yu.einstein.wasp.integration.messages.templates;

import org.springframework.messaging.Message;

import edu.yu.einstein.wasp.exception.WaspMessageInitializationException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspMessageType;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;

/**
 * Handling WaspJobStatus messages. If not task is defined the default is WaspTask.NOTIFY_STATUS
 * @author asmclellan
 *
 */
public class BatchJobStepActionStatusMessageTemplate extends WaspStatusMessageTemplate {
	
	public BatchJobStepActionStatusMessageTemplate(Long jobExecutionId, String stepName){
		super();
		addHeader(WaspMessageType.HEADER_KEY, WaspMessageType.ACTION_BATCH_JOB);
		setJobExecutionId(jobExecutionId);
		setStepName(stepName);
	}
	
	public BatchJobStepActionStatusMessageTemplate(Message<WaspStatus> message){
		super(message);
		if (!isMessageOfCorrectType(message))
			throw new WaspMessageInitializationException("message is not of the correct type");
		if (message.getHeaders().containsKey(WaspJobParameters.JOB_EXECUTION_ID))
			setJobExecutionId((Long) message.getHeaders().get(WaspJobParameters.JOB_EXECUTION_ID));
		if (message.getHeaders().containsKey(WaspJobParameters.STEP_NAME))
			setStepName((String) message.getHeaders().get(WaspJobParameters.STEP_NAME));
	}
	
	public Long getJobExecutionId() {
		return (Long) getHeader(WaspJobParameters.JOB_EXECUTION_ID);
	}

	public void setJobExecutionId(Long jobExecutionId) {
		addHeader(WaspJobParameters.JOB_EXECUTION_ID, jobExecutionId);
	}
	
	public String getStepName() {
		return (String) getHeader(WaspJobParameters.STEP_NAME);
	}

	public void setStepName(String stepName) {
		addHeader(WaspJobParameters.STEP_NAME, stepName);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessage(Message<?> message){
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		if (task == null)
			return actUponMessage(message, getJobExecutionId(), getStepName());
		return actUponMessage(message, getJobExecutionId(), getStepName(), task);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessageIgnoringTask(Message<?> message){
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		if (task == null)
			return actUponMessage(message, getJobExecutionId(), getStepName());
		return actUponMessage(message, getJobExecutionId(), getStepName(), null);
	}
	
	
	// Statics.........

	
	/**
	 * Takes a message and checks its headers against the supplied jobExecutionId value to see if the message should be acted upon or not
	 * @param message
	 * @param jobExecutionId 
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Long jobExecutionId, String stepName){
		if (jobExecutionId != null &&
				message.getHeaders().containsKey(WaspJobParameters.JOB_EXECUTION_ID) && 
				((Long) message.getHeaders().get(WaspJobParameters.JOB_EXECUTION_ID)).equals(jobExecutionId) &&
				message.getHeaders().containsKey(WaspJobParameters.STEP_NAME) && 
				((String) message.getHeaders().get(WaspJobParameters.STEP_NAME)).equals(stepName) &&
				message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) && 
				((String) message.getHeaders().get(WaspMessageType.HEADER_KEY)).equals(WaspMessageType.ACTION_BATCH_JOB))
			return true;
		return false;
	}
	
	/**
	 * Takes a message and checks its headers against the supplied jobExecutionId value and task to see if the message should be acted upon or not
	 * @param message
	 * @param jobExecutionId 
	 * @param task
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Long jobExecutionId, String stepName, String task ){
		if (! actUponMessage(message, jobExecutionId, stepName) )
			return false;
		if (task == null)
			return true;
		if (message.getHeaders().containsKey(WaspJobTask.HEADER_KEY) &&	message.getHeaders().get(WaspJobTask.HEADER_KEY).equals(task))
			return true;
		return false;
	}
	
	/**
	 * Returns true is the message is of the correct WaspMessageType
	 * @param message
	 * @return
	 */
	public static boolean isMessageOfCorrectType(Message<?> message) {
		return message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) &&  
				message.getHeaders().get(WaspMessageType.HEADER_KEY).equals(WaspMessageType.ACTION_BATCH_JOB);
	}
	
	@Override
	public BatchJobStepActionStatusMessageTemplate getNewInstance(WaspStatusMessageTemplate messageTemplate){
		BatchJobStepActionStatusMessageTemplate newTemplate = new BatchJobStepActionStatusMessageTemplate(
				((BatchJobStepActionStatusMessageTemplate) messageTemplate).getJobExecutionId(),
				((BatchJobStepActionStatusMessageTemplate) messageTemplate).getStepName() );
		copyCommonProperties(messageTemplate, newTemplate);
		return newTemplate;
	}
	
}
