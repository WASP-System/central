package edu.yu.einstein.wasp.integration.messaging;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;

/**
 * Registry for storing and retrieving message channel bean references
 * @author asmclellan
 *
 */
public class MessageChannelRegistry {

	private Map<String, MessageChannel> messageChannels;
	
	
	public static final String OUTBOUND_MESSAGE_CHANNEL = "wasp.channel.remoting.outbound";
	public static final String BATCH_MESSAGE_CHANNEL = "wasp.channel.batch";
	public static final String REPLY_MESSAGE_CHANNEL = "wasp.channel.reply";
	public static final String LAUNCH_MESSAGE_CHANNEL = "wasp.channel.queue.launch";
	public static final String JOB_MESSAGE_CHANNEL = "wasp.channel.notification.job";
	public static final String RUN_MESSAGE_CHANNEL = "wasp.channel.notification.run";
	public static final String SAMPLE_MESSAGE_CHANNEL = "wasp.channel.notification.sample";
	public static final String ANALYSIS_MESSAGE_CHANNEL = "wasp.channel.notification.analysis";
	public static final String LIBRARY_MESSAGE_CHANNEL = "wasp.channel.notification.library";
	public static final String DEFAULT_MESSAGE_CHANNEL = "wasp.channel.notification.default";
	public static final String ABORT_MESSAGE_CHANNEL = "wasp.channel.notification.abort";
	
	/**
	 * Constructor
	 */
	public  MessageChannelRegistry(){
		messageChannels = new HashMap<String, MessageChannel>();
	}
	
	/**
	 * Add a message channel to the registry
	 * @param channel
	 * @param name
	 * @throws MessagingException
	 */
	public void addChannel(MessageChannel channel, String name) throws MessagingException{
		if (messageChannels.containsKey(name))
			throw new MessagingException("Channel with name '"+name+"' already in the registry");
		messageChannels.put(name, channel);
	}
	
	/**
	 * Remove a named channel from the registry
	 * @param name
	 * @throws MessagingException
	 */
	public void removeChannel(String name) throws MessagingException{
		if (messageChannels.containsKey(name))
			messageChannels.remove(name);
		else throw new MessagingException("Cannot find channel with name '"+name+"' in the registry");
	}
	
	/**
	 * gets a named message channel from the registry or returns null if there are no matches
	 * to name or the object obtained cannot be cast to the specified type
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends MessageChannel> T getChannel(String name, Class<T> clazz){
		if (messageChannels.containsKey(name) && clazz.isInstance(messageChannels.get(name)))
			return (T) messageChannels.get(name);
		return null;	
	}
	
	public Set<String> getNames(){
		return messageChannels.keySet();
	}
	
	
}
