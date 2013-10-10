package edu.yu.einstein.wasp.integration.messages.templates;

import org.springframework.integration.Message;

import edu.yu.einstein.wasp.exception.WaspMessageInitializationException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspMessageType;
import edu.yu.einstein.wasp.integration.messages.tasks.JobExecutionTask;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;

public abstract class AbstractHibernationMessageTemplate extends WaspMessageTemplate {
	
	public AbstractHibernationMessageTemplate(Long jobExecutionId) {
		super();
		addHeader(WaspMessageType.HEADER_KEY, WaspMessageType.HIBERNATION);
		setJobExecutionId(jobExecutionId);
	}

	public AbstractHibernationMessageTemplate(Long jobExecutionId, String target) {
		super(target);
		setJobExecutionId(jobExecutionId);
	}

	public AbstractHibernationMessageTemplate(Long jobExecutionId, String target, String task) {
		super(target, task);
		setJobExecutionId(jobExecutionId);
	}

	public AbstractHibernationMessageTemplate(Message<?> message) {
		super(message);
		if (!isMessageOfCorrectType(message))
			throw new WaspMessageInitializationException("message is not of the correct type");
		if (message.getHeaders().containsKey(WaspJobParameters.JOB_EXECUTION_ID))
			setJobExecutionId((Long) message.getHeaders().get(WaspJobParameters.JOB_EXECUTION_ID));
	}
	
	public Long getJobExecutionId() {
		return (Long) getHeader(WaspJobParameters.JOB_EXECUTION_ID);
	}

	public void setJobExecutionId(Long jobExecutionId) {
		addHeader(WaspJobParameters.JOB_EXECUTION_ID, jobExecutionId);
	}

	@Override
	public boolean actUponMessage(Message<?> message) {
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		if (task == null)
			return actUponMessage(message, getJobExecutionId());
		return actUponMessage(message, getJobExecutionId(), task);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessageIgnoringTask(Message<?> message){
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		if (task == null)
			return actUponMessage(message, getJobExecutionId());
		return actUponMessage(message, getJobExecutionId(), null);
	}
	
	/**
	 * Takes a message and checks its headers against the supplied jobExecutionId value to see if the message should be acted upon or not
	 * @param message
	 * @param jobExecutionId 
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Long jobExecutionId ){
		if (jobExecutionId != null &&
				message.getHeaders().containsKey(WaspJobParameters.JOB_EXECUTION_ID) && 
				((Long) message.getHeaders().get(WaspJobParameters.JOB_EXECUTION_ID)).equals(jobExecutionId) &&
				message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) && 
				((String) message.getHeaders().get(WaspMessageType.HEADER_KEY)).equals(WaspMessageType.HIBERNATION))
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
	private static boolean actUponMessage(Message<?> message, Long jobExecutionId, String task ){
		if (! actUponMessage(message, jobExecutionId) )
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
				message.getHeaders().get(WaspMessageType.HEADER_KEY).equals(WaspMessageType.HIBERNATION);
	}

}
