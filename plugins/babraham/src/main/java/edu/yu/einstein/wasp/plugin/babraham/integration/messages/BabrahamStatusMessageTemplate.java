package edu.yu.einstein.wasp.plugin.babraham.integration.messages;

import org.springframework.integration.Message;

import edu.yu.einstein.wasp.exception.WaspMessageInitializationException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;

/**
 * Handling Babraham Status messages.
 * 
 * @author asmclellan
 * 
 */
public class BabrahamStatusMessageTemplate extends SimpleBabrahamStatusMessageTemplate {
	
	public BabrahamStatusMessageTemplate(Integer fileGroupId) {
		super();
		setFileGroupId(fileGroupId);
	}
	
	public BabrahamStatusMessageTemplate(Message<WaspStatus> message){
		super(message);
		if (!isMessageOfCorrectType(message))
			throw new WaspMessageInitializationException("message is not of the correct type");
		if (message.getHeaders().containsKey(WaspJobParameters.FILE_GROUP_ID))
			setFileGroupId((Integer) message.getHeaders().get(WaspJobParameters.FILE_GROUP_ID));
	}

	public Integer getFileGroupId() {
		return (Integer) getHeader(WaspJobParameters.FILE_GROUP_ID);
	}

	public void setFileGroupId(Integer fileGroupId) {
		setHeader(WaspJobParameters.FILE_GROUP_ID, fileGroupId);
	}

	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessage(Message<?> message) {
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		if (task == null)
			return actUponMessage(message, getFileGroupId());
		return actUponMessage(message, getFileGroupId(), task);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessageIgnoringTask(Message<?> message) {
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		if (task == null)
			return actUponMessage(message, getFileGroupId());
		return actUponMessage(message, getFileGroupId(), null);
	}

	// Statics.........

	/**
	 * Takes a message and checks its headers against the supplied fileGroupId and the payload type to
	 * see if the message should be acted upon or not
	 * 
	 * @param message
	 * @param fileGroupId
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer fileGroupId) {
		if (fileGroupId == null) {
			return false;
		} else if (fileGroupId != null && message.getHeaders().containsKey(WaspJobParameters.FILE_GROUP_ID) 
				&& ((Integer) message.getHeaders().get(WaspJobParameters.FILE_GROUP_ID)).equals(fileGroupId)) {
			return true;
		}
		return false;
	}

	/**
	 * Takes a message and checks its headers against the supplied fileGroupId, task and the payload
	 * type to see if the message should be acted upon or not
	 * 
	 * @param message
	 * @param fileGroupId
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer fileGroupId, String task) {
		if (!actUponMessage(message, fileGroupId))
			return false;
		if (task == null)
			return true;
		if (message.getHeaders().containsKey(WaspJobTask.HEADER_KEY) &&	message.getHeaders().get(WaspJobTask.HEADER_KEY).equals(task))
			return true;
		return false;
	}

}
