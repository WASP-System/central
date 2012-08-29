package edu.yu.einstein.wasp.messages;

import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.exceptions.WaspMessageBuildingException;

/**
 * Handling WaspJobStatus messages.
 * @author andymac
 *
 */
public class WaspJobStatusMessageTemplate implements StatusMessageTemplate{
	
	private String task;
	
	private Integer jobId;
	
	private WaspStatus status;
	
	public WaspStatus getStatus() {
		return status;
	}

	public void setStatus(WaspStatus status) {
		this.status = status;
	}
	
	public void setStatus(String status) {
		this.status = WaspStatus.valueOf(status);
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}
	
	public WaspJobStatusMessageTemplate(Integer jobId){
		this.jobId = jobId;
		this.task = null;
		this.status = null;
	}
	
	/**
	 * Build a Spring Integration Message using the jobId header and the WaspStatus as payload.
	 * The message-type header is not set so the message will be routed generically
	 * @return
	 * @throws WaspMessageBuildingException
	 */
	@Override
	public Message<WaspStatus> buildGeneric() throws WaspMessageBuildingException{
		if (this.status == null)
			throw new WaspMessageBuildingException("no status message defined");
		return buildGeneric(this.jobId, this.status);
	}
	
	/**
	 * Build a Spring Integration Message using the jobId header, task header if not null, and the WaspStatus as payload .
	 * @return
	 * @throws WaspMessageBuildingException
	 */
	@Override
	public Message<WaspStatus> build() throws WaspMessageBuildingException{
		if (this.status == null)
			throw new WaspMessageBuildingException("no status message defined");
		if (this.task == null)
			return build(this.jobId, this.status);
		return build(this.jobId, this.status, this.task);
	}
	
	/**
	 * Takes a message and checks it's headers against the supplied jobId value and task to see if the message should be acted upon or not
	 * @param message
	 * @param jobId 
	 * @param task
	 * @return
	 */
	@Override
	public boolean actUponMessage(Message<?> message){
		if (this.task == null)
			return actUponMessage(message, this.jobId);
		return actUponMessage(message, this.jobId, this.task);
	}
	
	// Statics.........

	/**
	 * Build a Spring Integration Message using the jobId header and the WaspStatus as payload.
	 * The message-type header is not set so the message will be routed generically
	 * @param jobId
	 * @param status
	 * @return
	 * @throws WaspMessageBuildingException
	 */
	public static Message<WaspStatus> buildGeneric(Integer jobId, WaspStatus status) throws WaspMessageBuildingException {
		Message<WaspStatus> message = null;
		try {
			message = MessageBuilder.withPayload(status)
					.setHeader("target", "batch")
					.setHeader("jobId", jobId)
					.setPriority(status.getPriority())
					.build();
		} catch(Exception e){
			throw new WaspMessageBuildingException("buildGeneric() failed to build message: "+e.getMessage());
		}
		return message;
	}
	
	/**
	 * Build a Spring Integration Message using the jobId header and the WaspStatus as payload.
	 * @param jobId
	 * @param status
	 * @return
	 * @throws WaspMessageBuildingException
	 */
	public static Message<WaspStatus> build(Integer jobId, WaspStatus status) throws WaspMessageBuildingException {
		Message<WaspStatus> message = null;
		try {
			message = MessageBuilder.withPayload(status)
					.setHeader(WaspMessageType.HEADER, WaspMessageType.JOB)
					.setHeader("target", "batch")
					.setHeader("jobId", jobId)
					.setPriority(status.getPriority())
					.build();
		} catch(Exception e){
			throw new WaspMessageBuildingException("build() failed to build message: "+e.getMessage());
		}
		return message;
	}
	
	/**
	 * Build a Spring Integration Message using the jobId header, task header and the WaspStatus as payload.
	 * @param jobId
	 * @param status
	 * @param task
	 * @return
	 * @throws WaspMessageBuildingException
	 */
	public static Message<WaspStatus> build(Integer jobId, WaspStatus status, String task) throws WaspMessageBuildingException {
		Message<WaspStatus> message = null;
		try {
			message = MessageBuilder.withPayload(status)
					.setHeader(WaspMessageType.HEADER, WaspMessageType.JOB)
					.setHeader("target", "batch")
					.setHeader(WaspJobTask.HEADER, task)
					.setHeader("jobId", jobId)
					.setPriority(status.getPriority())
					.build();
		} catch(Exception e){
			throw new WaspMessageBuildingException("build() failed to build message: "+e.getMessage());
		}
		return message;
	}
	
	
	
	/**
	 * Takes a message and checks it's headers against the supplied jobId value to see if the message should be acted upon or not
	 * @param message
	 * @param jobId 
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer jobId ){
		if (jobId != null &&
				message.getHeaders().containsKey("jobId") && 
				((Integer) message.getHeaders().get("jobId")).equals(jobId) )
			return true;
		return false;
	}
	
	/**
	 * Takes a message and checks it's headers against the supplied jobId value and task to see if the message should be acted upon or not
	 * @param message
	 * @param jobId 
	 * @param task
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer jobId, String task ){
		if (! actUponMessage(message, jobId) )
			return false;
		if (task != null && 
				message.getHeaders().containsKey(WaspJobTask.HEADER) && 
				message.getHeaders().get(WaspJobTask.HEADER).equals(task))
			return true;
		return false;
	}
}
