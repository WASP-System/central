package edu.yu.einstein.wasp.messages;

import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.exceptions.WaspMessageBuildingException;

/**
 * Static methods for handling WaspRunStatus messages.
 * @author andymac
 *
 */
public abstract class WaspRunStatusMessage {
	
	/**
	 * Build a Spring Integration Message using the runId header and the runStatus as payload.
	 * @return {@link Message}<{@link WaspRunStatus}>
	 * @throws WaspMessageBuildingException
	 */
	public static Message<WaspRunStatus> build(Integer runId, Integer platformUnitId,WaspRunStatus runStatus) throws WaspMessageBuildingException {
		Message<WaspRunStatus> message = null;
		try {
			message = MessageBuilder.withPayload(runStatus)
					.setHeader("runId", runId)
					.setHeader("platformUnitId", platformUnitId)
					.setPriority(runStatus.getPriority())
					.build();
		} catch(Exception e){
			throw new WaspMessageBuildingException("buildWaspRunStatusMessage() failed to build message: "+e.getMessage());
		}
		return message;
	}
	
	/**
	 * Takes a message and checks it's headers against the supplied runId and/or platformUnitId value (one of these may be null) and the 
	 * payload type to see if the message should be acted upon or not
	 * @param message
	 * @param runId (may be null)
	 * @param platformUnitId (may be null)
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer runId, Integer platformUnitId ){
		if ( ! WaspRunStatus.class.isInstance(message.getPayload()) || (runId == null && platformUnitId == null) )
			return false;
		if (runId != null && platformUnitId != null){
			if ( message.getHeaders().containsKey("runId") &&  ((Integer) message.getHeaders().get("runId")).equals(runId) &&
			 message.getHeaders().containsKey("platformUnitId") && ((Integer) message.getHeaders().get("platformUnitId")).equals(platformUnitId) )
				return true;

		}
		if (runId != null && message.getHeaders().containsKey("runId") && ((Integer) message.getHeaders().get("runId")).equals(runId))
			return true;
		if (platformUnitId != null && message.getHeaders().containsKey("platformUnitId") && ((Integer) message.getHeaders().get("platformUnitId")).equals(platformUnitId))
			return true;
		return false;
	}
}
