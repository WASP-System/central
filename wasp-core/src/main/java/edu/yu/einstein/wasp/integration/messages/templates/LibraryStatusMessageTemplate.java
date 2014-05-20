package edu.yu.einstein.wasp.integration.messages.templates;

import org.springframework.messaging.Message;

import edu.yu.einstein.wasp.exception.WaspMessageInitializationException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspMessageType;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;

/**
 * Handling Wasp Library Status Messages. If not task is defined the default is WaspTask.NOTIFY_STATUS
 * @author asmclellan
 *
 */
public class LibraryStatusMessageTemplate extends  WaspStatusMessageTemplate{
	
	public LibraryStatusMessageTemplate(Integer libraryId){
		super();
		addHeader(WaspMessageType.HEADER_KEY, WaspMessageType.LIBRARY);
		setLibraryId(libraryId);
	}
	
	public LibraryStatusMessageTemplate(Message<WaspStatus> message){
		super(message);
		if (!isMessageOfCorrectType(message))
			throw new WaspMessageInitializationException("message is not of the correct type");
		if (message.getHeaders().containsKey(WaspJobParameters.LIBRARY_ID))
			setLibraryId((Integer) message.getHeaders().get(WaspJobParameters.LIBRARY_ID));
	}
	
	public Integer getLibraryId() {
		return (Integer) getHeader(WaspJobParameters.LIBRARY_ID);
	}

	public void setLibraryId(Integer libraryId) {
		addHeader(WaspJobParameters.LIBRARY_ID, libraryId);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessage(Message<?> message){
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		if (task == null)
			return actUponMessage(message, getLibraryId());
		return actUponMessage(message, getLibraryId(), task);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessageIgnoringTask(Message<?> message){
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		if (task == null)
			return actUponMessage(message, getLibraryId());
		return actUponMessage(message, getLibraryId(), null);
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
				((String) message.getHeaders().get(WaspMessageType.HEADER_KEY)).equals(WaspMessageType.LIBRARY))
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
				message.getHeaders().get(WaspMessageType.HEADER_KEY).equals(WaspMessageType.LIBRARY);
	}
	
	@Override
	public LibraryStatusMessageTemplate getNewInstance(WaspStatusMessageTemplate messageTemplate){
		LibraryStatusMessageTemplate newTemplate = new LibraryStatusMessageTemplate(((LibraryStatusMessageTemplate) messageTemplate).getLibraryId());
		copyCommonProperties(messageTemplate, newTemplate);
		return newTemplate;
	}
	
}
	
