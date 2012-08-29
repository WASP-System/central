package edu.yu.einstein.wasp.messages;


/**
 * Abstract base class defining common attributes.
 * @author andymac
 *
 */
public abstract class WaspStatusMessageTemplate implements StatusMessageTemplate{
	
	protected String task;
	
	protected WaspStatus status;
	
	protected String target;
	
	public WaspStatus getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = WaspStatus.valueOf(status);
	}

	public void setStatus(WaspStatus status) {
		this.status = status;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}
	
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public WaspStatusMessageTemplate(){
		this.task = null;
		this.status = null;
		this.target = "batch"; // default
	}	
}
	
