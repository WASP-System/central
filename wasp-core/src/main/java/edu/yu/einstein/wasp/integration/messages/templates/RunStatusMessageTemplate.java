package edu.yu.einstein.wasp.integration.messages.templates;

import org.springframework.messaging.Message;

import edu.yu.einstein.wasp.exception.WaspMessageInitializationException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspMessageType;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;

/**
 * Handling WaspRunStatus messages.
 * 
 * @author asmclellan
 * 
 */
public class RunStatusMessageTemplate extends WaspStatusMessageTemplate {
	
	public RunStatusMessageTemplate(Integer runId) {
		super();
		addHeader(WaspMessageType.HEADER_KEY, WaspMessageType.RUN);
		setRunId(runId);
	}
	
	public RunStatusMessageTemplate(Message<WaspStatus> message){
		super(message);
		if (!isMessageOfCorrectType(message))
			throw new WaspMessageInitializationException("message is not of the correct type");
		if (message.getHeaders().containsKey(WaspJobParameters.RUN_ID))
			setRunId((Integer) message.getHeaders().get(WaspJobParameters.RUN_ID));
	}

	public Integer getRunId() {
		return (Integer) getHeader(WaspJobParameters.RUN_ID);
	}

	public void setRunId(Integer runId) {
		addHeader(WaspJobParameters.RUN_ID, runId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessage(Message<?> message) {
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		if (task == null)
			return actUponMessage(message, getRunId());
		return actUponMessage(message, getRunId(), task);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessageIgnoringTask(Message<?> message) {
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		if (task == null)
			return actUponMessage(message, getRunId());
		return actUponMessage(message, getRunId(), null);
	}

	// Statics.........

	/**
	 * Takes a message and checks its headers against the supplied runId and the payload type to
	 * see if the message should be acted upon or not
	 * 
	 * @param message
	 * @param runId
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer runId) {
		if (runId == null) {
			return false;
		} else if (runId != null && message.getHeaders().containsKey(WaspJobParameters.RUN_ID) 
				&& ((Integer) message.getHeaders().get(WaspJobParameters.RUN_ID)).equals(runId)) {
			return true;
		}
		return false;
	}

	/**
	 * Takes a message and checks its headers against the supplied runId, task and the payload
	 * type to see if the message should be acted upon or not
	 * 
	 * @param message
	 * @param runId
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer runId, String task) {
		if (!actUponMessage(message, runId))
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
				message.getHeaders().get(WaspMessageType.HEADER_KEY).equals(WaspMessageType.RUN);
	}
	
	@Override
	public RunStatusMessageTemplate getNewInstance(WaspStatusMessageTemplate messageTemplate){
		RunStatusMessageTemplate newTemplate = new RunStatusMessageTemplate(((RunStatusMessageTemplate) messageTemplate).getRunId());
		copyCommonProperties(messageTemplate, newTemplate);
		return newTemplate;
	}

}
