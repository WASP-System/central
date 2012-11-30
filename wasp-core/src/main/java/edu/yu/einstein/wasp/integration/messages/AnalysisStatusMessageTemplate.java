package edu.yu.einstein.wasp.integration.messages;

import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.payload.WaspStatus;

/**
 * Handling Wasp Analysis Status Messages. If not task is defined the default is WaspTask.NOTIFY_STATUS
 * @author andymac
 *
 */
public class AnalysisStatusMessageTemplate extends  WaspStatusMessageTemplate{
	
	protected Integer sampleId; // id of library being analysed
	
	public Integer getsampleId() {
		return sampleId;
	}

	public void setsampleId(Integer sampleId) {
		this.sampleId = sampleId;
	}
	
	public AnalysisStatusMessageTemplate(Integer sampleId){
		super();
		this.sampleId = sampleId;
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
			if (this.task == null){
				message = MessageBuilder.withPayload(status)
						.setHeader(WaspMessageType.HEADER_KEY, WaspMessageType.ANALYSIS)
						.setHeader(TARGET_KEY, target)
						.setHeader(WaspJobParameters.SAMPLE_ID, sampleId)
						.setPriority(status.getPriority())
						.build();
			} else {
				message = MessageBuilder.withPayload(status)
						.setHeader(WaspMessageType.HEADER_KEY, WaspMessageType.ANALYSIS)
						.setHeader(TARGET_KEY, target)
						.setHeader(WaspJobParameters.SAMPLE_ID, sampleId)
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
	 * Takes a message and checks its headers against the supplied sampleId value and task to see if the message should be acted upon or not
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
	 * Takes a message and checks its headers against the supplied sampleId value to see if the message should be acted upon or not
	 * @param message
	 * @param sampleId 
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer sampleId ){
		if (sampleId != null &&
				message.getHeaders().containsKey(WaspJobParameters.SAMPLE_ID) && 
				((Integer) message.getHeaders().get(WaspJobParameters.SAMPLE_ID)).equals(sampleId) &&
				message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) && 
				((String) message.getHeaders().get(WaspMessageType.HEADER_KEY)).equals(WaspMessageType.ANALYSIS))
			return true;
		return false;
	}
	
	/**
	 * Takes a message and checks its headers against the supplied sampleId value and task to see if the message should be acted upon or not
	 * @param message
	 * @param jobId 
	 * @param task
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer sampleId, String task ){
		if (! actUponMessage(message, sampleId) )
			return false;
		if (task != null && 
				message.getHeaders().containsKey(WaspJobTask.HEADER_KEY) && 
				message.getHeaders().get(WaspJobTask.HEADER_KEY).equals(task))
			return true;
		return false;
	}
	
}
	
