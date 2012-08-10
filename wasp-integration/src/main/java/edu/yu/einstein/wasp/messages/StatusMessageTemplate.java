package edu.yu.einstein.wasp.messages;

import org.springframework.integration.Message;

/**
 * Abstract base class for status messages
 * @author andymac
 *
 */
public abstract class StatusMessageTemplate {
	
	public static boolean isMessageOfExpectedType(Message<?> message, String type){
		if (! WaspStatus.class.isInstance(message.getPayload()) )
			return false;
		if (! message.getHeaders().containsKey(WaspMessageType.MESSAGE_TYPE_FIELD) )
			return false;
		if ( ((String) message.getHeaders().get(WaspMessageType.MESSAGE_TYPE_FIELD)).equals(type) )
			return true;
		return false;
	}

}
