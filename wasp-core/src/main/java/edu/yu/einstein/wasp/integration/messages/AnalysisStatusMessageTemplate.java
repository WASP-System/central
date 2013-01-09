package edu.yu.einstein.wasp.integration.messages;

import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.exception.WaspMessageInitializationException;

/**
 * Handling Wasp Analysis Status Messages. If not task is defined the default is WaspTask.NOTIFY_STATUS
 * @author andymac
 *
 */
public class AnalysisStatusMessageTemplate extends  WaspStatusMessageTemplate{
	
	protected Integer libraryId; // id of library being analysed
	
	public Integer getlibraryId() {
		return libraryId;
	}

	public void setlibraryId(Integer libraryId) {
		this.libraryId = libraryId;
	}
	
	public AnalysisStatusMessageTemplate(Integer libraryId){
		super();
		this.libraryId = libraryId;
	}
	
	public AnalysisStatusMessageTemplate(Message<WaspStatus> message){
		super(message);
		if (!isMessageOfCorrectType(message))
			throw new WaspMessageInitializationException("message is not of the correct type");
		if (message.getHeaders().containsKey(WaspJobParameters.LIBRARY_ID))
			libraryId = (Integer) message.getHeaders().get(WaspJobParameters.LIBRARY_ID);
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
						.setHeader(EXIT_DESCRIPTION_HEADER, exitDescription)
						.setHeader(WaspJobParameters.LIBRARY_ID, libraryId)
						.setPriority(status.getPriority())
						.build();
			} else {
				message = MessageBuilder.withPayload(status)
						.setHeader(WaspMessageType.HEADER_KEY, WaspMessageType.ANALYSIS)
						.setHeader(TARGET_KEY, target)
						.setHeader(EXIT_DESCRIPTION_HEADER, exitDescription)
						.setHeader(WaspJobParameters.LIBRARY_ID, libraryId)
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
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessage(Message<?> message){
		if (this.task == null)
			return actUponMessage(message, this.libraryId);
		return actUponMessage(message, this.libraryId, this.task);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessageIgnoringTask(Message<?> message){
		if (this.task == null)
			return actUponMessage(message, this.libraryId);
		return actUponMessage(message, this.libraryId, null);
	}
	
	// Statics.........
	
	/**
	 * Takes a message and checks its headers against the supplied libraryId value to see if the message should be acted upon or not
	 * @param message
	 * @param libraryId 
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer libraryId ){
		if (libraryId != null &&
				message.getHeaders().containsKey(WaspJobParameters.LIBRARY_ID) && 
				((Integer) message.getHeaders().get(WaspJobParameters.LIBRARY_ID)).equals(libraryId) &&
				message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) && 
				((String) message.getHeaders().get(WaspMessageType.HEADER_KEY)).equals(WaspMessageType.ANALYSIS))
			return true;
		return false;
	}
	
	/**
	 * Takes a message and checks its headers against the supplied libraryId value and task to see if the message should be acted upon or not
	 * @param message
	 * @param jobId 
	 * @param task
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer libraryId, String task ){
		if (! actUponMessage(message, libraryId) )
			return false;
		if (task == null)
			return true;
		if (message.getHeaders().containsKey(WaspJobTask.HEADER_KEY) && message.getHeaders().get(WaspJobTask.HEADER_KEY).equals(task))
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
				((String) message.getHeaders().get(WaspMessageType.HEADER_KEY)).equals(WaspMessageType.ANALYSIS);
	}
	
}
	
