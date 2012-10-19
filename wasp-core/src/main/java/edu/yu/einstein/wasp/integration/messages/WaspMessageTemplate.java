package edu.yu.einstein.wasp.integration.messages;


/**
 * Abstract base class defining common attributes.
 * @author andymac
 *
 */
public abstract class WaspMessageTemplate implements MessageTemplate{
	
	public static final String TARGET_KEY = "target";
	
	public static final String MESSAGE_TYPE_KEY = "messagetype";
	
	public static final String DEFAULT_TARGET = "batch";
	
	protected String task;
	
	protected String target;
	
	@Override
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

	public WaspMessageTemplate(){
		this.task = WaspTask.NOTIFY_STATUS; // default
		this.target = DEFAULT_TARGET; // default
	}	
}
	
