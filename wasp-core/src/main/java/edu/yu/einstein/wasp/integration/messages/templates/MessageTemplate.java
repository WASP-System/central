package edu.yu.einstein.wasp.integration.messages.templates;

import org.springframework.messaging.Message;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;

/**
 * Interface defining message templates
 * @author asmclellan
 *
 */
public interface MessageTemplate {
	
	
	/**
	 * Build a Spring Integration Message.
	 * @return
	 * @throws WaspMessageBuildingException
	 */
	public Message<?> build() throws WaspMessageBuildingException;
	
	/**
	 * Takes a message and checks the message type and task (if present) to see if it should be acted upon or not.
	 * @param message
	 * @return
	 */
	public boolean actUponMessage(Message<?> message);
	
	/**
	 * Takes a message and checks message type to see if it should be acted upon or not. The task (if set) is ignored.
	 * whilst ignoring task header
	 * @param message
	 * @return
	 */
	@Deprecated
	public boolean actUponMessageIgnoringTask(Message<?> message);
	
	public String getTask();
	
	public Object getPayload();

}
