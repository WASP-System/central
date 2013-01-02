package edu.yu.einstein.wasp.integration.messages;

import org.springframework.integration.Message;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;

/**
 * Interface defining message templates
 * @author andymac
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
	 * Takes a message and checks its headers against to see if the message should be acted upon or not
	 * @param message
	 * @return
	 */
	public boolean actUponMessage(Message<?> message);
	
	/**
	 * Takes a message and checks its headers against to see if the message should be acted upon or not
	 * whilst ignoring task header
	 * @param message
	 * @return
	 */
	public boolean actUponMessageIgnoringTask(Message<?> message);
	
	public String getTask();

}
