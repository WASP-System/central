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
public class JobStatusMessageTemplate extends WaspStatusMessageTemplate {
	
	public JobStatusMessageTemplate(Integer jobId){
		super();
		addHeader(WaspMessageType.HEADER_KEY, WaspMessageType.JOB);
		setJobId(jobId);
	}
	
	public JobStatusMessageTemplate(Message<WaspStatus> message){
		super(message);
		if (!isMessageOfCorrectType(message))
			throw new WaspMessageInitializationException("message is not of the correct type");
		if (message.getHeaders().containsKey(WaspJobParameters.JOB_ID))
			setJobId((Integer) message.getHeaders().get(WaspJobParameters.JOB_ID));
	}
	
	public Integer getJobId() {
		return (Integer) getHeader(WaspJobParameters.JOB_ID);
	}

	public void setJobId(Integer jobId) {
		addHeader(WaspJobParameters.JOB_ID, jobId);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessage(Message<?> message){
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		if (task == null)
			return actUponMessage(message, getJobId());
		return actUponMessage(message, getJobId(), task);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessageIgnoringTask(Message<?> message){
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		if (task == null)
			return actUponMessage(message, getJobId());
		return actUponMessage(message, getJobId(), null);
	}
	
	
	// Statics.........

	
	/**
	 * Takes a message and checks its headers against the supplied jobId value to see if the message should be acted upon or not
	 * @param message
	 * @param jobId 
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer jobId ){
		if (jobId != null &&
				message.getHeaders().containsKey(WaspJobParameters.JOB_ID) && 
				((Integer) message.getHeaders().get(WaspJobParameters.JOB_ID)).equals(jobId) &&
				message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) && 
				((String) message.getHeaders().get(WaspMessageType.HEADER_KEY)).equals(WaspMessageType.JOB))
			return true;
		return false;
	}
	
	/**
	 * Takes a message and checks its headers against the supplied jobId value and task to see if the message should be acted upon or not
	 * @param message
	 * @param jobId 
	 * @param task
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer jobId, String task ){
		if (! actUponMessage(message, jobId) )
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
				message.getHeaders().get(WaspMessageType.HEADER_KEY).equals(WaspMessageType.JOB);
	}
	
	@Override
	public JobStatusMessageTemplate getNewInstance(WaspStatusMessageTemplate messageTemplate){
		JobStatusMessageTemplate newTemplate = new JobStatusMessageTemplate(((JobStatusMessageTemplate) messageTemplate).getJobId());
		copyCommonProperties(messageTemplate, newTemplate);
		return newTemplate;
	}
	
}
