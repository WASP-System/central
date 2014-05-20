package edu.yu.einstein.wasp.integration.messages.templates;

import org.springframework.messaging.Message;

import edu.yu.einstein.wasp.exception.WaspMessageInitializationException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspMessageType;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.service.FileService;

/**
 * Handling WaspFileStatus messages. If not task is defined the default is WaspTask.NOTIFY_STATUS
 * @author asmclellan
 *
 */
public class FileStatusMessageTemplate extends WaspStatusMessageTemplate {
	
	public FileStatusMessageTemplate(Integer fileGroupId){
		super();
		addHeader(WaspMessageType.HEADER_KEY, WaspMessageType.FILE);
		setFileGroupId(fileGroupId);
	}
	
	public FileStatusMessageTemplate(Message<WaspStatus> message){
		super(message);
		if (!isMessageOfCorrectType(message))
			throw new WaspMessageInitializationException("message is not of the correct type");
		if (message.getHeaders().containsKey(WaspJobParameters.FILE_GROUP_ID))
			setFileGroupId((Integer) message.getHeaders().get(WaspJobParameters.FILE_GROUP_ID));
	}
	
	public Integer getFileGroupId() {
		return (Integer) getHeader(WaspJobParameters.FILE_GROUP_ID);
	}

	public void setFileGroupId(Integer fileGroupId) {
		addHeader(WaspJobParameters.FILE_GROUP_ID, fileGroupId);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessage(Message<?> message){
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		if (task == null)
			return actUponMessage(message, getFileGroupId());
		return actUponMessage(message, getFileGroupId(), task);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessageIgnoringTask(Message<?> message){
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		if (task == null)
			return actUponMessage(message, getFileGroupId());
		return actUponMessage(message, getFileGroupId(), null);
	}
	
	
	// Statics.........

	/**
	 * Returns true if the message is concerning a file with a FileType attribute that matches the one provided,
	 * otherwise returns false
	 * @param message
	 * @param fileType
	 * @param fileService
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, FileType fileType, FileService fileService){
		if (fileType != null && fileService != null && 
				message.getHeaders().containsKey(WaspJobParameters.FILE_GROUP_ID) && 
				message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) && 
				((String) message.getHeaders().get(WaspMessageType.HEADER_KEY)).equals(WaspMessageType.FILE)){
			FileGroup fileGroup = fileService.getFileGroupById( (Integer) message.getHeaders().get(WaspJobParameters.FILE_GROUP_ID));
			if (fileGroup.getFileType().equals(fileType))
				return true;
		}
		return false;
	}
	
	/**
	 * Returns true if the message is concerning a file with a FileType attribute that matches the one provided and 
	 * the message concers a task identical to the one provided 
	 * otherwise returns false
	 * @param message
	 * @param fileType
	 * @param fileService
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, FileType fileType, FileService fileService, String task){
		if (! actUponMessage(message, fileType, fileService) )
			return false;
		if (task == null)
			return true;
		if (message.getHeaders().containsKey(WaspJobTask.HEADER_KEY) &&	message.getHeaders().get(WaspJobTask.HEADER_KEY).equals(task))
			return true;
		return false;
	}
	
	/**
	 * Takes a message and checks its headers against the supplied fileGroupId value to see if the message should be acted upon or not
	 * @param message
	 * @param fileGroupId 
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer fileGroupId ){
		if (fileGroupId != null &&
				message.getHeaders().containsKey(WaspJobParameters.FILE_GROUP_ID) && 
				((Integer) message.getHeaders().get(WaspJobParameters.FILE_GROUP_ID)).equals(fileGroupId) &&
				message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) && 
				((String) message.getHeaders().get(WaspMessageType.HEADER_KEY)).equals(WaspMessageType.FILE))
			return true;
		return false;
	}
	
	/**
	 * Takes a message and checks its headers against the supplied fileGroupId value and task to see if the message should be acted upon or not
	 * @param message
	 * @param fileGroupId 
	 * @param task
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer fileGroupId, String task ){
		if (! actUponMessage(message, fileGroupId) )
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
				message.getHeaders().get(WaspMessageType.HEADER_KEY).equals(WaspMessageType.FILE);
	}
	
	@Override
	public FileStatusMessageTemplate getNewInstance(WaspStatusMessageTemplate messageTemplate){
		FileStatusMessageTemplate newTemplate = new FileStatusMessageTemplate(((FileStatusMessageTemplate) messageTemplate).getFileGroupId());
		copyCommonProperties(messageTemplate, newTemplate);
		return newTemplate;
	}
	
}
