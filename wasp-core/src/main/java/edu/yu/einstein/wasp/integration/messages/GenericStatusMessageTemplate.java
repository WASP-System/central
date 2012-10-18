package edu.yu.einstein.wasp.integration.messages;

import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspMessageType;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.WaspStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.WaspTask;

public class GenericStatusMessageTemplate extends WaspStatusMessageTemplate {

	public GenericStatusMessageTemplate(){
		super();
	}
	
	/**
	 * Build a generic Spring Integration Message using task header if not null, and the WaspStatus as payload .
	 * @return
	 * @throws WaspMessageBuildingException
	 */
	@Override
	public Message<WaspStatus> build() throws WaspMessageBuildingException{
		if (this.status == null)
			throw new WaspMessageBuildingException("no status message defined");
		Message<WaspStatus> message = null;
		try {
			if (this.task == null){
				message = MessageBuilder.withPayload(status)
						.setHeader(WaspMessageType.HEADER_KEY, WaspMessageType.GENERIC)
						.setHeader(TARGET_KEY, target)
						.setPriority(status.getPriority())
						.build();
			} else {
				message = MessageBuilder.withPayload(status)
						.setHeader(WaspMessageType.HEADER_KEY, WaspMessageType.GENERIC)
						.setHeader(TARGET_KEY, target)
						.setHeader(WaspTask.HEADER_KEY, task)
						.setPriority(status.getPriority())
						.build();
			}
		} catch(Exception e){
			throw new WaspMessageBuildingException("build() failed to build message: "+e.getMessage());
		}
		return message;
	}
	
	/**
	 * Takes a message and checks it's headers to see if the message should be acted upon or not
	 * @param message
	 * @param task
	 * @return
	 */
	@Override
	public boolean actUponMessage(Message<?> message){
		return actUponMessage(message, this.task);
	}
	
	// Statics.........

	
	/**
	 * Takes a message and checks it's headers to see if the message should be acted upon or not
	 * @param message
	 * @param task
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, String task ){
		if (!message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) ||
				!((String) message.getHeaders().get(WaspMessageType.HEADER_KEY)).equals(WaspMessageType.GENERIC))
				return false;
		if (task == null)
			return true;
		if (message.getHeaders().containsKey(WaspTask.HEADER_KEY) && message.getHeaders().get(WaspTask.HEADER_KEY).equals(task))
			return true;
		return false;
	}

}


