package edu.yu.einstein.wasp.integration.messages.templates;

import org.springframework.messaging.Message;

import edu.yu.einstein.wasp.exception.WaspMessageInitializationException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspMessageType;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;

/**
 * Handling Wasp Sample Status Messages. If not task is defined the default is WaspTask.NOTIFY_STATUS
 * @author asmclellan
 *
 */
public class SampleStatusMessageTemplate extends WaspStatusMessageTemplate{
	
	public SampleStatusMessageTemplate(Integer sampleId){
		super();
		addHeader(WaspMessageType.HEADER_KEY, WaspMessageType.SAMPLE);
		setSampleId(sampleId);
	}
	
	public SampleStatusMessageTemplate(Message<WaspStatus> message){
		super(message);
		if (!isMessageOfCorrectType(message))
			throw new WaspMessageInitializationException("message is not of the correct type");
		if (message.getHeaders().containsKey(WaspJobParameters.SAMPLE_ID))
			setSampleId((Integer) message.getHeaders().get(WaspJobParameters.SAMPLE_ID));
	}
	
	public Integer getSampleId() {
		return (Integer) getHeader(WaspJobParameters.SAMPLE_ID);
	}

	public void setSampleId(Integer sampleId) {
		addHeader(WaspJobParameters.SAMPLE_ID, sampleId);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessage(Message<?> message){
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		if (task == null)
			return actUponMessage(message, getSampleId());
		return actUponMessage(message, getSampleId(), task);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessageIgnoringTask(Message<?> message){
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		if (task == null)
			return actUponMessage(message, getSampleId());
		return actUponMessage(message, getSampleId(), null);
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
				((String) message.getHeaders().get(WaspMessageType.HEADER_KEY)).equals(WaspMessageType.SAMPLE))
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
				message.getHeaders().get(WaspMessageType.HEADER_KEY).equals(WaspMessageType.SAMPLE);
	}
	
	@Override
	public SampleStatusMessageTemplate getNewInstance(WaspStatusMessageTemplate messageTemplate){
		SampleStatusMessageTemplate newTemplate = new SampleStatusMessageTemplate(((SampleStatusMessageTemplate) messageTemplate).getSampleId());
		copyCommonProperties(messageTemplate, newTemplate);
		return newTemplate;
	}
	
}
	
