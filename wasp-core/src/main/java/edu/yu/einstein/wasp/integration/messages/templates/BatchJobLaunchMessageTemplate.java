package edu.yu.einstein.wasp.integration.messages.templates;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.exception.WaspMessageInitializationException;
import edu.yu.einstein.wasp.integration.messages.WaspMessageType;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;

/**
 * Handling WASP Batch Job Launch messages.
 * @author asmclellan
 *
 */
public class BatchJobLaunchMessageTemplate extends WaspMessageTemplate{
	
	
	private BatchJobLaunchContext batchJobLaunchContext;
	
	public BatchJobLaunchMessageTemplate(BatchJobLaunchContext batchJobLaunchContext){
		super();
		addHeader(WaspMessageType.HEADER_KEY, WaspMessageType.LAUNCH_BATCH_JOB);
		this.batchJobLaunchContext = batchJobLaunchContext;
	}
	
	public BatchJobLaunchMessageTemplate(Message<BatchJobLaunchContext> message){
		super(message);
		if (!isMessageOfCorrectType(message))
			throw new WaspMessageInitializationException("message is not of the correct type");
		batchJobLaunchContext = message.getPayload();
	}
	
	
	public BatchJobLaunchContext getBatchJobLaunchContext() {
		return batchJobLaunchContext;
	}

	public void setBatchJobLaunchContext(BatchJobLaunchContext batchJobLaunchContext) {
		this.batchJobLaunchContext = batchJobLaunchContext;
	}
	
		
	/**
	 * Build a Spring Integration Message using the WaspMessageType.HEADER_KEY header and the BatchJobLaunchContext as payload.
	 * @return {@link Message}<{@link BatchJobLaunchContext}>
	 * @throws WaspMessageBuildingException
	 */
	@Override
	public Message<BatchJobLaunchContext> build() throws WaspMessageBuildingException{
		if (this.batchJobLaunchContext == null)
			throw new WaspMessageBuildingException("no BatchJobLaunchContext message defined");
		Message<BatchJobLaunchContext> message = null;

		try {
			message = MessageBuilder.withPayload(batchJobLaunchContext)
					.copyHeaders(getHeaders())
					.build();
		} catch(Exception e){
			throw new WaspMessageBuildingException("build() failed to build message: "+e.getMessage());
		}
		return message;
	}
	
	/**
	 * Takes a message and checks its headers against the WaspMessageType header and the payload type to see if the message should be acted upon or not
	 * @param message
	 * @param batchJobLaunchContext
	 * @param task
	 * @return
	 */
	@Override
	public boolean actUponMessage(Message<?> message){
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		if (task != null)
			return actUponMessage(message);
		return actUponMessage(message, task);
	}
	
	@Override
	public boolean actUponMessageIgnoringTask(Message<?> message) {
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		if (task == null)
			return actUponMessage(message);
		return actUponMessage(message, null);
	}
	// Statics.........

	
	/**
	 * Takes a message and checks its headers against the WaspMessageType header and the payload type to see if the message should be acted upon or not
	 * @param message
	 * @param batchJobLaunchContext
	 * @return
	 */
	public static boolean actUponMessageS(Message<?> message){
		
		if ( message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) &&  message.getHeaders().get(WaspMessageType.HEADER_KEY).equals(WaspMessageType.LAUNCH_BATCH_JOB) &&
				BatchJobLaunchContext.class.isInstance(message.getPayload()) )
			return true;
		return false;
	}
	
	/**
	 * Takes a message and checks its headers against the WaspMessageType header, task and the payload type to see if the message should be acted upon or not
	 * @param message
	 * @param batchJobLaunchContext
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, String task){
		if (! actUponMessageS(message) )
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
				message.getHeaders().get(WaspMessageType.HEADER_KEY).equals(WaspMessageType.LAUNCH_BATCH_JOB);
	}
	
	
	@Override
	public Object getPayload(){
		return getBatchJobLaunchContext();
	}
	
}
