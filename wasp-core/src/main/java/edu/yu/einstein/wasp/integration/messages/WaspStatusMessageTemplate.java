package edu.yu.einstein.wasp.integration.messages;

import edu.yu.einstein.wasp.integration.messages.payload.WaspStatus;


/**
 * Abstract base class defining common attributes.
 * @author andymac
 *
 */
public abstract class WaspStatusMessageTemplate extends WaspMessageTemplate implements StatusMessageTemplate{
	
	public static final String EXIT_DESCRIPTION_HEADER = "description";
	
	protected WaspStatus status;
	
	protected String exitDescription = "";
	
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
	
