package edu.yu.einstein.wasp.plugin;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.cli.ClientMessageI;

/**
 * Registry for storing and retrieving plugin bean references
 * 
 * Implements {@link ClientMessageI} cli interface to receive requests
 * to list installed plugins.
 * 
 * The registry uses the MessageChannel "wasp.channel.pluginRegistry"
 * 
 * @author andymac and brent
 *
 */
public class WaspPluginRegistry implements ClientMessageI {

	private Map<String, WaspPlugin> plugins;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * Constructor
	 */
	public  WaspPluginRegistry(){
		plugins = new HashMap<String, WaspPlugin>();
	}
	
	/**
	 * Add a plugin to the registry
	 * @param plugin
	 * @param name
	 */
	public void addPlugin(WaspPlugin plugin, String name) {
		if (plugins.containsKey(name)) {
			logger.warn("Plugin with name '"+name+"' already in the registry, replacing.");
			plugins.remove(name);
		}
			
		plugins.put(name, plugin);
	}
	
	/**
	 * Remove a named plugin from the registry
	 * @param name
	 */
	public void removePlugin(String name) {
		if (plugins.containsKey(name))
			plugins.remove(name);
		else logger.warn("Cannot find plugin with name '"+name+"' in the registry");
	}
	
	/**
	 * gets a named plugin from the registry or returns null if there are no matches
	 * to name or the object obtained cannot be cast to the specified type
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends WaspPlugin> T getChannel(String name, Class<T> clazz){
		if (plugins.containsKey(name) && clazz.isInstance(plugins.get(name)))
			return (T) plugins.get(name);
		return null;	
	}
	
	public Set<String> getNames(){
		return plugins.keySet();
	}

	@Override
	public Message process(Message m) throws RemoteException {
		if (m.getPayload().toString().equals("list")) {
			return list();
		} else {
			String mstr = "Unknown command: " + m.toString() + "'\n";
			return MessageBuilder.withPayload(mstr).build();
		}
	}
	
	private Message<String> list() {
		String reply = "\nRegistered Wasp System plugins:\n" +
						 "-------------------------------\n\n";
		for ( String name : plugins.keySet() ) {
			reply += name + "\n";
		}
		
		return MessageBuilder.withPayload(reply).build();
	}
	
	
}
