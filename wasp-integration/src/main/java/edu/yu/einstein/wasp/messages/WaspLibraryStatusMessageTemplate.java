package edu.yu.einstein.wasp.messages;

import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.exceptions.WaspMessageBuildingException;

/**
 * Handling Wasp Library Status Messages.
 * @author andymac
 *
 */
public class WaspLibraryStatusMessageTemplate extends SampleStatusMessageTemplate {
	
	
	public WaspLibraryStatusMessageTemplate(Integer sampleId){
		super(sampleId);
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
						.setHeader(WaspMessageType.HEADER, WaspMessageType.LIBRARY)
						.setHeader("target", target)
						.setHeader("sampleId", sampleId)
						.setPriority(status.getPriority())
						.build();
			} else {
				message = MessageBuilder.withPayload(status)
						.setHeader(WaspMessageType.HEADER, WaspMessageType.LIBRARY)
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
	
}
	
