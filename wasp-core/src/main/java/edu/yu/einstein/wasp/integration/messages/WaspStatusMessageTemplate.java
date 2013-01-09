package edu.yu.einstein.wasp.integration.messages;

import org.springframework.integration.Message;



/**
 * Abstract base class defining common attributes.
 * @author andymac
 *
 */
public abstract class WaspStatusMessageTemplate extends WaspMessageTemplate implements StatusMessageTemplate{
	
	public static final String EXIT_DESCRIPTION_HEADER = "description";
	
	protected WaspStatus status;
	
	protected String exitDescription = "";
	
	public WaspStatusMessageTemplate(Message<WaspStatus> message){
		super(message);
		if (message.getHeaders().containsKey(EXIT_DESCRIPTION_HEADER))
			exitDescription = (String) message.getHeaders().get(EXIT_DESCRIPTION_HEADER);
		status = message.getPayload();
	}
	
	public String getExitDescription() {
		return exitDescription;
	}

	public void setExitDescription(String exitDescription) {
		this.exitDescription = exitDescription;
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
	
	public WaspStatusMessageTemplate(){
		super();
		this.status = null;
	}	
}
	
