package edu.yu.einstein.wasp.integration.messages;

import edu.yu.einstein.wasp.integration.messages.payload.WaspStatus;


/**
 * Abstract base class defining common attributes.
 * @author andymac
 *
 */
public abstract class WaspStatusMessageTemplate extends WaspMessageTemplate implements StatusMessageTemplate{
	
	protected WaspStatus status;
	
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
	
