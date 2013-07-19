package edu.yu.einstein.wasp.plugin.babraham.messages.templates;

import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.exception.WaspMessageInitializationException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspMessageType;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;
import edu.yu.einstein.wasp.plugin.babraham.messages.BabrahamMessageType;

/**
 * Handling Babraham Status messages.
 * 
 * @author asmclellan
 * 
 */
public class BabrahamStatusMessageTemplate extends SimpleBabrahamStatusMessageTemplate {

	private Integer fileGroupId;

	public Integer getFileGroupId() {
		return fileGroupId;
	}

	public void setFileGroupId(Integer fileGroupId) {
		this.fileGroupId = fileGroupId;
	}

	public BabrahamStatusMessageTemplate(Integer fileGroupId) {
		super();
		this.fileGroupId = fileGroupId;
	}
	
	public BabrahamStatusMessageTemplate(Message<WaspStatus> message){
		super(message);
		if (!isMessageOfCorrectType(message))
			throw new WaspMessageInitializationException("message is not of the correct type");
		if (message.getHeaders().containsKey(WaspJobParameters.FILE_GROUP_ID))
			fileGroupId = (Integer) message.getHeaders().get(WaspJobParameters.FILE_GROUP_ID);
	}

	/**
	 * Build a Spring Integration Message using the fileGroupId header and the
	 * runStatus as payload.
	 * 
	 * @return {@link Message}<{@link WaspStatus}>
	 * @throws WaspMessageBuildingException
	 */
	@Override
	public Message<WaspStatus> build() throws WaspMessageBuildingException {
		if (this.status == null)
			throw new WaspMessageBuildingException("no status message defined");
		Message<WaspStatus> message = null;

		try {
			message = MessageBuilder.withPayload(status)
						.setHeader(WaspMessageType.HEADER_KEY, BabrahamMessageType.BABRAHAM)
						.setHeader(TARGET_KEY, "batch")
						.setHeader(USER_KEY, userCreatingMessage)
						.setHeader(COMMENT_KEY, comment)
						.setHeader(EXIT_DESCRIPTION_HEADER, exitDescription)
						.setHeader(WaspJobParameters.FILE_GROUP_ID, fileGroupId)
						.setHeader(WaspJobTask.HEADER_KEY, task)
						.setPriority(status.getPriority())
						.build();
		} catch (Exception e) {
			throw new WaspMessageBuildingException("build() failed to build message: " + e.getMessage());
		}
		return message;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessage(Message<?> message) {
		if (this.task == null)
			return actUponMessage(message, this.fileGroupId);
		return actUponMessage(message, this.fileGroupId, this.task);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessageIgnoringTask(Message<?> message) {
		if (this.task == null)
			return actUponMessage(message, this.fileGroupId);
		return actUponMessage(message, this.fileGroupId, null);
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
