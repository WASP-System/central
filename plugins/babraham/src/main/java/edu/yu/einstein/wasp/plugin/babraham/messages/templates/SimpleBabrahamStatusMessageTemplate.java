package edu.yu.einstein.wasp.plugin.babraham.messages.templates;

import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.exception.WaspMessageInitializationException;
import edu.yu.einstein.wasp.integration.messages.WaspMessageType;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspTask;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;
import edu.yu.einstein.wasp.plugin.babraham.messages.BabrahamMessageType;

/**
 * Handling WaspRunStatus messages.
 * 
 * @author asmclellan
 * 
 */
public class SimpleBabrahamStatusMessageTemplate extends WaspStatusMessageTemplate {


	public SimpleBabrahamStatusMessageTemplate() {
		super();
	}
	
	public SimpleBabrahamStatusMessageTemplate(Message<WaspStatus> message){
		super(message);
		if (!isMessageOfCorrectType(message))
			throw new WaspMessageInitializationException("message is not of the correct type");
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
	public boolean actUponMessage(Message<?> message){
		return actUponMessage(message, this.task);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessageIgnoringTask(Message<?> message){
		return actUponMessage(message, null);
	}
	
	// Statics.........

	
	/**
	 * Takes a message and checks its headers to see if the message should be acted upon or not
	 * @param message
	 * @param task
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, String task ){
		if (!message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) ||
				!((String) message.getHeaders().get(WaspMessageType.HEADER_KEY)).equals(BabrahamMessageType.BABRAHAM))
				return false;
		if (task == null)
			return true;
		if (message.getHeaders().containsKey(WaspTask.HEADER_KEY) && message.getHeaders().get(WaspTask.HEADER_KEY).equals(task))
			return true;
		return false;
	}
	
	/**
	 * Returns true is the message is of the correct BabrahamMessageType
	 * @param message
	 * @return
	 */
	public static boolean isMessageOfCorrectType(Message<?> message) {
		return message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) &&  
				message.getHeaders().get(WaspMessageType.HEADER_KEY).equals(BabrahamMessageType.BABRAHAM);
	}

}
