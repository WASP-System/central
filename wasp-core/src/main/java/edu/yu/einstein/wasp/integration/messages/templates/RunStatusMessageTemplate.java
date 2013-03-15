package edu.yu.einstein.wasp.integration.messages.templates;

import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.exception.WaspMessageInitializationException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspMessageType;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;

/**
 * Handling WaspRunStatus messages.
 * 
 * @author andymac
 * 
 */
public class RunStatusMessageTemplate extends WaspStatusMessageTemplate {

	private Integer runId;

	public Integer getRunId() {
		return runId;
	}

	public void setRunId(Integer runId) {
		this.runId = runId;
	}

	public RunStatusMessageTemplate(Integer runId) {
		super();
		this.runId = runId;
	}
	
	public RunStatusMessageTemplate(Message<WaspStatus> message){
		super(message);
		if (!isMessageOfCorrectType(message))
			throw new WaspMessageInitializationException("message is not of the correct type");
		if (message.getHeaders().containsKey(WaspJobParameters.RUN_ID))
			runId = (Integer) message.getHeaders().get(WaspJobParameters.RUN_ID);
	}

	/**
	 * Build a Spring Integration Message using the runId header and the
	 * runStatus as payload.
	 * 
	 * @return {@link Message}<{@link WaspStatus}>
	 * @throws WaspMessageBuildingException
	 */
	@Override
	public Message<WaspStatus> build() throws WaspMessageBuildingException {
		if (this.status == null)
			throw new WaspMessageBuildingException("no status message defined");
		Message<WaspStatus> message = null;

		try {
			message = MessageBuilder.withPayload(status)
						.setHeader(WaspMessageType.HEADER_KEY, WaspMessageType.RUN)
						.setHeader(TARGET_KEY, "batch")
						.setHeader(USER_KEY, userCreatingMessage)
						.setHeader(COMMENT_KEY, comment)
						.setHeader(EXIT_DESCRIPTION_HEADER, exitDescription)
						.setHeader(WaspJobParameters.RUN_ID, runId)
						.setHeader(WaspJobTask.HEADER_KEY, task)
						.setPriority(status.getPriority())
						.build();
		} catch (Exception e) {
			throw new WaspMessageBuildingException("build() failed to build message: " + e.getMessage());
		}
		return message;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessage(Message<?> message) {
		if (this.task == null)
			return actUponMessage(message, this.runId);
		return actUponMessage(message, this.runId, this.task);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessageIgnoringTask(Message<?> message) {
		if (this.task == null)
			return actUponMessage(message, this.runId);
		return actUponMessage(message, this.runId, null);
	}

	// Statics.........

	/**
	 * Takes a message and checks its headers against the supplied runId and the payload type to
	 * see if the message should be acted upon or not
	 * 
	 * @param message
	 * @param runId
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer runId) {
		if (runId == null) {
			return false;
		} else if (runId != null && message.getHeaders().containsKey(WaspJobParameters.RUN_ID) 
				&& ((Integer) message.getHeaders().get(WaspJobParameters.RUN_ID)).equals(runId)) {
			return true;
		}
		return false;
	}

	/**
	 * Takes a message and checks its headers against the supplied runId, task and the payload
	 * type to see if the message should be acted upon or not
	 * 
	 * @param message
	 * @param runId
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer runId, String task) {
		if (!actUponMessage(message, runId))
			return false;
		if (task == null)
			return true;
		if (message.getHeaders().containsKey(WaspJobTask.HEADER_KEY) &&	message.getHeaders().get(WaspJobTask.HEADER_KEY).equals(task))
			return true;
		return false;
	}
	
	/**
	 * Returns true is the message is of the correct WaspMessageType
	 * @param message
	 * @return
	 */
	public static boolean isMessageOfCorrectType(Message<?> message) {
		return message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) &&  
				message.getHeaders().get(WaspMessageType.HEADER_KEY).equals(WaspMessageType.RUN);
	}

}
