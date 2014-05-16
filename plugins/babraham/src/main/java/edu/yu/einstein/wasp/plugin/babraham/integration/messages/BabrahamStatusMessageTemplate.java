package edu.yu.einstein.wasp.plugin.babraham.integration.messages;

import org.springframework.integration.Message;

import edu.yu.einstein.wasp.exception.WaspMessageInitializationException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;

/**
 * Handling Babraham Status messages.
 * 
 * @author asmclellan
 * 
 */
public class BabrahamStatusMessageTemplate extends SimpleBabrahamStatusMessageTemplate {
	
	public BabrahamStatusMessageTemplate(Integer id) {
		super();
		setId(id);
	}
	
	public BabrahamStatusMessageTemplate(Message<WaspStatus> message){
		super(message);
		if (!isMessageOfCorrectType(message))
			throw new WaspMessageInitializationException("message is not of the correct type");
		if (message.getHeaders().containsKey(WaspJobParameters.ID))
			setId((Integer) message.getHeaders().get(WaspJobParameters.ID));
	}

	public Integer getId() {
		return (Integer) getHeader(WaspJobParameters.ID);
	}

	public void setId(Integer id) {
		addHeader(WaspJobParameters.ID, id);
	}

	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessage(Message<?> message) {
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		if (task == null)
			return actUponMessage(message, getId());
		return actUponMessage(message, getId(), task);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessageIgnoringTask(Message<?> message) {
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		if (task == null)
			return actUponMessage(message, getId());
		return actUponMessage(message, getId(), null);
	}

	// Statics.........

	/**
	 * Takes a message and checks its headers against the supplied fileGroupId and the payload type to
	 * see if the message should be acted upon or not
	 * 
	 * @param message
	 * @param fileGroupId
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer id) {
		if (id == null) {
			return false;
		} else if (id != null && message.getHeaders().containsKey(WaspJobParameters.ID) 
				&& ((Integer) message.getHeaders().get(WaspJobParameters.ID)).equals(id)) {
			return true;
		}
		return false;
	}

	/**
	 * Takes a message and checks its headers against the supplied fileGroupId, task and the payload
	 * type to see if the message should be acted upon or not
	 * 
	 * @param message
	 * @param fileGroupId
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer id, String task) {
		if (!actUponMessage(message, id))
			return false;
		if (task == null)
			return true;
		if (message.getHeaders().containsKey(WaspJobTask.HEADER_KEY) &&	message.getHeaders().get(WaspJobTask.HEADER_KEY).equals(task))
			return true;
		return false;
	}
	
	@Override
	public BabrahamStatusMessageTemplate getNewInstance(WaspStatusMessageTemplate messageTemplate){
		BabrahamStatusMessageTemplate newTemplate = new BabrahamStatusMessageTemplate(((BabrahamStatusMessageTemplate) messageTemplate).getId());
		copyCommonProperties(messageTemplate, newTemplate);
		return newTemplate;
	}

}
