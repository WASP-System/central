package edu.yu.einstein.wasp.integration.messages.templates;

import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;



/**
 * Abstract base class defining common attributes.
 * @author asmclellan
 *
 */
public abstract class WaspStatusMessageTemplate extends WaspMessageTemplate implements StatusMessageTemplate{
	
	public static final String EXIT_DESCRIPTION_HEADER = "description";
	
	protected WaspStatus status;
	
	public WaspStatusMessageTemplate(Message<WaspStatus> message){
		super(message);
		if (message.getHeaders().containsKey(EXIT_DESCRIPTION_HEADER))
			addHeader(EXIT_DESCRIPTION_HEADER, message.getHeaders().get(EXIT_DESCRIPTION_HEADER));
		status = message.getPayload();
	}
	
	public WaspStatusMessageTemplate(){
		super();
		this.status = null;
	}	
	
	public WaspStatusMessageTemplate(String target){
		super(target); 
		this.status = null;
	}
	
	public WaspStatusMessageTemplate(String target, String task){
		super(target, task);
		this.status = null;
	}
	
	public String getExitDescription() {
		return (String) getHeader(EXIT_DESCRIPTION_HEADER);
	}

	public void setExitDescription(String exitDescription) {
		addHeader(EXIT_DESCRIPTION_HEADER, exitDescription);
	}

	@Override
	public WaspStatus getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = WaspStatus.valueOf(status);
	}

	public void setStatus(WaspStatus status) {
		this.status = status;
	}
	
	/**
	 * Build a Spring Integration Message using the ANALYSIS header, task header if not null, and the WaspStatus as payload .
	 * @return
	 * @throws WaspMessageBuildingException
	 */
	@Override
	public Message<WaspStatus> build() throws WaspMessageBuildingException{
		if (this.status == null)
			throw new WaspMessageBuildingException("no status message defined");
		Message<WaspStatus> message = null;
		try {
			message = MessageBuilder.withPayload(status)
						.copyHeaders(getHeaders())
						.setPriority(status.getPriority())
						.build();
		} catch(Exception e){
			throw new WaspMessageBuildingException("build() failed to build message: "+e.getMessage());
		}
		return message;
	}
	
	
}
	
