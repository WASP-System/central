package edu.yu.einstein.wasp.batchint.messages;

import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.integration.exceptions.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspMessageType;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.WaspStatusMessageTemplate;

/**
 * Handling WaspRunStatus messages.
 * @author andymac
 *
 */
public  class RunStatusMessageTemplate extends WaspStatusMessageTemplate{
	
	
	private Integer runId;
	
	private Integer platformUnitId;
	
	
	public Integer getRunId() {
		return runId;
	}

	public void setRunId(Integer runId) {
		this.runId = runId;
	}

	public Integer getPlatformUnitId() {
		return platformUnitId;
	}

	public void setPlatformUnitId(Integer platformUnitId) {
		this.platformUnitId = platformUnitId;
	}
	
	public RunStatusMessageTemplate(Integer runId, Integer platformUnitId){
		super();
		this.runId = runId;
		this.platformUnitId = platformUnitId;
	}
	
		
	/**
	 * Build a Spring Integration Message using the runId header and the runStatus as payload.
	 * @return {@link Message}<{@link WaspStatus}>
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
						.setHeader(WaspMessageType.HEADER_KEY, WaspMessageType.RUN)
						.setHeader(TARGET_KEY, "batch")
						.setHeader(WaspJobParameters.RUN_ID, runId)
						.setHeader(WaspJobParameters.PLATFORM_UNIT_ID, platformUnitId)
						.setPriority(status.getPriority())
						.build();
			} else {
				message = MessageBuilder.withPayload(status)
						.setHeader(WaspMessageType.HEADER_KEY, WaspMessageType.RUN)
						.setHeader(TARGET_KEY, "batch")
						.setHeader(WaspJobParameters.RUN_ID, runId)
						.setHeader(WaspJobParameters.PLATFORM_UNIT_ID, platformUnitId)
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
	 * Takes a message and checks it's headers against the supplied runId and/or platformUnitId value (one of these may be null) and task to see if the message should be acted upon or not
	 * @param message
	 * @param jobId 
	 * @param task
	 * @return
	 */
	@Override
	public boolean actUponMessage(Message<?> message){
		if (this.task == null)
			return actUponMessage(message, this.runId, this.platformUnitId);
		return actUponMessage(message, this.runId, this.platformUnitId, this.task);
	}
	
	// Statics.........

	
	/**
	 * Takes a message and checks it's headers against the supplied runId and/or platformUnitId value (one of these may be null) and the 
	 * payload type to see if the message should be acted upon or not
	 * @param message
	 * @param runId (may be null)
	 * @param platformUnitId (may be null)
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer runId, Integer platformUnitId ){
		if ( runId == null && platformUnitId == null )
			return false;
		if (runId != null && platformUnitId != null){
			if ( message.getHeaders().containsKey(WaspJobParameters.RUN_ID) &&  ((Integer) message.getHeaders().get(WaspJobParameters.RUN_ID)).equals(runId) &&
			 message.getHeaders().containsKey(WaspJobParameters.PLATFORM_UNIT_ID) && ((Integer) message.getHeaders().get(WaspJobParameters.PLATFORM_UNIT_ID)).equals(platformUnitId) )
				return true;

		}
		if (runId != null && message.getHeaders().containsKey(WaspJobParameters.RUN_ID) && ((Integer) message.getHeaders().get(WaspJobParameters.RUN_ID)).equals(runId))
			return true;
		if (platformUnitId != null && message.getHeaders().containsKey(WaspJobParameters.PLATFORM_UNIT_ID) && ((Integer) message.getHeaders().get(WaspJobParameters.PLATFORM_UNIT_ID)).equals(platformUnitId))
			return true;
		return false;
	}
	
	/**
	 * Takes a message and checks it's headers against the supplied runId and/or platformUnitId value (one of these may be null), task and the 
	 * payload type to see if the message should be acted upon or not
	 * @param message
	 * @param runId (may be null)
	 * @param platformUnitId (may be null)
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer runId, Integer platformUnitId, String task){
		if (! actUponMessage(message, runId, platformUnitId) )
			return false;
		if (task != null && 
				message.getHeaders().containsKey(WaspJobTask.HEADER_KEY) && 
				message.getHeaders().get(WaspJobTask.HEADER_KEY).equals(task))
			return true;
		return false;
	}
	
}
