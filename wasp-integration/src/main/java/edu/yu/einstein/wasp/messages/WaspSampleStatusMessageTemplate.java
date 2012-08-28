package edu.yu.einstein.wasp.messages;

import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.exceptions.WaspMessageBuildingException;

/**
 * Static methods for handling WaspJobStatus messages.
 * @author andymac
 *
 */
public abstract class WaspSampleStatusMessageTemplate extends StatusMessageTemplate{
	
	/**
	 * Build a Spring Integration Message using the sampleId header and the WaspStatus as payload.
	 * The message-type header is not set so the message will be routed generically
	 * @param sampleId
	 * @param status
	 * @return
	 * @throws WaspMessageBuildingException
	 */
	public static Message<WaspStatus> buildGeneric(Integer sampleId, WaspStatus status) throws WaspMessageBuildingException {
		Message<WaspStatus> message = null;
		try {
			message = MessageBuilder.withPayload(status)
					.setHeader("sampleId", sampleId)
					.setPriority(status.getPriority())
					.build();
		} catch(Exception e){
			throw new WaspMessageBuildingException("buildGeneric() failed to build message: "+e.getMessage());
		}
		return message;
	}
	
	
	
	/**
	 * Build a Spring Integration Message using the sampleId header and the WaspStatus as payload.
	 * @param sampleId
	 * @param status
	 * @return
	 * @throws WaspMessageBuildingException
	 */
	public static Message<WaspStatus> build(Integer sampleId, WaspStatus status) throws WaspMessageBuildingException {
		Message<WaspStatus> message = null;
		try {
			message = MessageBuilder.withPayload(status)
					.setHeader(WaspMessageType.HEADER, WaspMessageType.SAMPLE)
					.setHeader("target", "batch")
					.setHeader("sampleId", sampleId)
					.setPriority(status.getPriority())
					.build();
		} catch(Exception e){
			throw new WaspMessageBuildingException("build() failed to build message: "+e.getMessage());
		}
		return message;
	}
	
	
	
	/**
	 * Takes a message and checks it's headers against the supplied sampleId value to see if the message should be acted upon or not
	 * @param message
	 * @param jobId 
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer sampleId ){
		if (sampleId != null &&
				message.getHeaders().containsKey("sampleId") && 
				((Integer) message.getHeaders().get("sampleId")).equals(sampleId) )
			return true;
		return false;
	}
	
}
	
