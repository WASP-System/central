package edu.yu.einstein.wasp.messages;

import org.springframework.integration.Message;

import edu.yu.einstein.wasp.exceptions.WaspMessageBuildingException;

/**
 * Interface defining status message templates
 * @author andymac
 *
 */
public interface StatusMessageTemplate {
	
	/**
	 * Build a Spring Integration Message.
	 * The message-type header is not set so the message will be routed generically
	 * @return
	 * @throws WaspMessageBuildingException
	 */
	public Message<WaspStatus> buildGeneric() throws WaspMessageBuildingException;
	
	/**
	 * Build a Spring Integration Message.
	 * @return
	 * @throws WaspMessageBuildingException
	 */
	public Message<WaspStatus> build() throws WaspMessageBuildingException;
	
	/**
	 * Takes a message and checks it's headers against to see if the message should be acted upon or not
	 * @param message
	 * @return
	 */
	public boolean actUponMessage(Message<?> message);

}
