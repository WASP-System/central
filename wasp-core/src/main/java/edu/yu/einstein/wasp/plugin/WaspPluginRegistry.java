package edu.yu.einstein.wasp.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Registry for storing and retrieving plugin bean references
 * @author andymac and brent
 *
 */
public class WaspPluginRegistry {

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
	
	
}
