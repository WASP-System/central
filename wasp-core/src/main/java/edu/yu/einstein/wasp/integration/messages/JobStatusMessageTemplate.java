package edu.yu.einstein.wasp.integration.messages;

import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;

/**
 * Handling WaspJobStatus messages. If not task is defined the default is WaspTask.NOTIFY_STATUS
 * @author andymac
 *
 */
public class JobStatusMessageTemplate extends WaspStatusMessageTemplate {
	
	private Integer jobId;
	
	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}
	
	public JobStatusMessageTemplate(Integer jobId){
		super();
		this.jobId = jobId;
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
		Message<WaspStatus> message = null;
		try {
			if (this.task == null){
				message = MessageBuilder.withPayload(status)
						.setHeader(WaspMessageType.HEADER_KEY, WaspMessageType.JOB)
						.setHeader(TARGET_KEY, target)
						.setHeader(WaspJobParameters.JOB_ID, jobId)
						.setPriority(status.getPriority())
						.build();
			} else {
				message = MessageBuilder.withPayload(status)
						.setHeader(WaspMessageType.HEADER_KEY, WaspMessageType.JOB)
						.setHeader(TARGET_KEY, target)
						.setHeader(WaspJobParameters.JOB_ID, jobId)
						.setHeader(WaspJobTask.HEADER_KEY, task)
						.setPriority(status.getPriority())
						.build();
			}
		} catch(Exception e){
			throw new WaspMessageBuildingException("build() failed to build message: "+e.getMessage());
		}
		return message;
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
	 * Takes a message and checks it's headers against the supplied jobId value to see if the message should be acted upon or not
	 * @param message
	 * @param jobId 
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer jobId ){
		if (jobId != null &&
				message.getHeaders().containsKey(WaspJobParameters.JOB_ID) && 
				((Integer) message.getHeaders().get(WaspJobParameters.JOB_ID)).equals(jobId) &&
				message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) && 
				((String) message.getHeaders().get(WaspMessageType.HEADER_KEY)).equals(WaspMessageType.JOB))
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
				message.getHeaders().containsKey(WaspJobTask.HEADER_KEY) && 
				message.getHeaders().get(WaspJobTask.HEADER_KEY).equals(task))
			return true;
		return false;
	}
}
