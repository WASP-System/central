package edu.yu.einstein.wasp.messaging;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.integration.MessageChannel;
import org.springframework.integration.MessagingException;

/**
 * Registry for storing and retrieving message channel bean references
 * @author andymac
 *
 */
public class MessageChannelRegistry {

	private Map<String, MessageChannel> messageChannels;
	
	/**
	 * Initialization
	 */
	@PostConstruct
	public void init(){
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
