package edu.yu.einstein.wasp.messages;

import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.exceptions.WaspMessageBuildingException;

/**
 * Handling Wasp Sample Status Messages. If not task is defined the default is WaspTask.NOTIFY_STATUS
 * @author andymac
 *
 */
public class SampleStatusMessageTemplate extends WaspStatusMessageTemplate{
	
	protected Integer sampleId;
	

	public Integer getsampleId() {
		return sampleId;
	}

	public void setsampleId(Integer sampleId) {
		this.sampleId = sampleId;
	}
	
	public SampleStatusMessageTemplate(Integer sampleId){
		super();
		this.sampleId = sampleId;
	}
	
	/**
	 * Build a Spring Integration Message using the sampleId header and the WaspStatus as payload.
	 * The message-type header is not set so the message will be routed generically
	 * @return
	 * @throws WaspMessageBuildingException
	 */
	@Override
	public Message<WaspStatus> buildGeneric() throws WaspMessageBuildingException{
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
	 * Build a Spring Integration Message using the sampleId header, task header if not null, and the WaspStatus as payload .
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
						.setHeader(WaspMessageType.HEADER, WaspMessageType.SAMPLE)
						.setHeader("target", target)
						.setHeader("sampleId", sampleId)
						.setPriority(status.getPriority())
						.build();
			} else {
				message = MessageBuilder.withPayload(status)
						.setHeader(WaspMessageType.HEADER, WaspMessageType.SAMPLE)
						.setHeader("target", target)
						.setHeader("sampleId", sampleId)
						.setHeader(WaspJobTask.HEADER, task)
						.setPriority(status.getPriority())
						.build();
			}
		} catch(Exception e){
			throw new WaspMessageBuildingException("build() failed to build message: "+e.getMessage());
		}
		return message;
	}
	
	/**
	 * Takes a message and checks it's headers against the supplied sampleId value and task to see if the message should be acted upon or not
	 * @param message
	 * @param jobId 
	 * @param task
	 * @return
	 */
	@Override
	public boolean actUponMessage(Message<?> message){
		if (this.task == null)
			return actUponMessage(message, this.sampleId);
		return actUponMessage(message, this.sampleId, this.task);
	}
	
	// Statics.........
	
	/**
	 * Takes a message and checks it's headers against the supplied sampleId value to see if the message should be acted upon or not
	 * @param message
	 * @param sampleId 
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer sampleId ){
		if (sampleId != null &&
				message.getHeaders().containsKey("sampleId") && 
				((Integer) message.getHeaders().get("sampleId")).equals(sampleId) )
			return true;
		return false;
	}
	
	/**
	 * Takes a message and checks it's headers against the supplied sampleId value and task to see if the message should be acted upon or not
	 * @param message
	 * @param jobId 
	 * @param task
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer sampleId, String task ){
		if (! actUponMessage(message, sampleId) )
			return false;
		if (task != null && 
				message.getHeaders().containsKey(WaspJobTask.HEADER) && 
				message.getHeaders().get(WaspJobTask.HEADER).equals(task))
			return true;
		return false;
	}
	
}
	
