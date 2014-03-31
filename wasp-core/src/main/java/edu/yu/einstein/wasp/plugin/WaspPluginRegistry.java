package edu.yu.einstein.wasp.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import edu.yu.einstein.wasp.interfacing.plugin.cli.ClientMessageI;

/**
 * Registry for storing and retrieving plugin bean references.  {@link WaspPlugin}
 * beans are registered through the BeanPostProcessor interface.
 * 
 * Implements {@link ClientMessageI} cli interface to receive requests to list
 * installed plugins and uses the MessageChannel "wasp.channel.pluginRegistry"
 * 
 * @author asmclellan and brent
 * 
 */
public class WaspPluginRegistry implements BeanPostProcessor {

	private Map<String, WaspPlugin> plugins;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Constructor
	 */
	public WaspPluginRegistry() {
		plugins = new HashMap<String, WaspPlugin>();
	}
	
	public Map<String, WaspPlugin> getPlugins() {
		return plugins;
	}

	/**
	 * Add a plugin to the registry
	 * 
	 * @param plugin
	 * @param name
	 */
	public void addPlugin(WaspPlugin plugin) {
		String name = plugin.getIName();
		if (plugins.containsKey(name)) {
			logger.warn("Plugin with name '" + name
					+ "' already in the registry, replacing.");
			plugins.remove(name);
		}
		plugins.put(name, plugin);
		logger.info("Registered Wasp System plugin: " + name);
	}

	/**
	 * Remove a named plugin from the registry
	 * 
	 * @param name
	 */
	public void removePlugin(String name) {
		if (plugins.containsKey(name))
			plugins.remove(name);
		else
			logger.warn("Cannot find plugin with name '" + name
					+ "' in the registry");
	}

	/**
	 * gets a named plugin from the registry or returns null if there are no
	 * matches to name or the object obtained cannot be cast to the specified
	 * type
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends WaspPlugin> T getChannel(String name, Class<T> clazz) {
		if (plugins.containsKey(name) && clazz.isInstance(plugins.get(name)))
			return (T) plugins.get(name);
		return null;
	}

	public Set<String> getNames() {
		return plugins.keySet();
	}

	

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	/**
	 * Auto register WaspPlugin beans.
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		if (WaspPlugin.class.isInstance(bean)) {
			this.addPlugin((WaspPlugin) bean);
			return bean;
		} else {
			return bean;
		}
	}
	
	
	/**
	 * gets all plugins handling a known area from the registry
	 * @param name
	 * @return
	 */
	public Set<WaspPlugin> getPluginsHandlingArea(String area){
		Set<WaspPlugin> pluginsHandlingArea = new HashSet<WaspPlugin>();
		for (String name : plugins.keySet()) {
			WaspPlugin plugin = plugins.get(name);
			Set<String> handles = plugin.getHandles();
			if (handles == null || ! handles.contains(area))
				continue;
			pluginsHandlingArea.add(plugin);
		}
		return pluginsHandlingArea;
	}
	
	/**
	 * gets all plugins from the registry which handle a known area and which can be cast to an instance of the provided type.
	 * @param name
	 * @return
	 */
	public <T> List<T> getPluginsHandlingArea(String area, Class<T> clazz){
		// Note: was trying to use Set here but for some reason casting to T prevented
		// any more than the first entry to be returned
		List<T> pluginsHandlingArea = new ArrayList<T>();
		for (String name : plugins.keySet()) {
			WaspPlugin plugin = plugins.get(name);
			Set<String> handles = plugin.getHandles();
			if (handles == null || ! handles.contains(area) || !clazz.isInstance(plugin))
				continue;
			pluginsHandlingArea.add((T) plugin);
		}
		return pluginsHandlingArea;
	}
	
	/**
	 * gets a set of plugins from the registry which can be cast to an instance of the provided type
	 * @param clazz
	 * @return
	 */
	public <T> List<T> getPlugins(Class<T> clazz){
		// Note: was trying to use Set here but for some reason casting to T prevented
		// any more than the first entry to be returned
		List<T> pluginsMatchingType = new ArrayList<T>();
		for (String name : plugins.keySet()) {
			WaspPlugin plugin = plugins.get(name);
			if (clazz.isInstance(plugin))
				pluginsMatchingType.add((T) plugin);
		}
		return pluginsMatchingType;
	}
	
	/**
	 * gets a named plugin from the registry or returns null if there are no matches
	 * to name or the object obtained cannot be cast to the specified type
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getPlugin(String name, Class<T> clazz){
		if (plugins.containsKey(name) && clazz.isInstance(plugins.get(name)))
			return (T) plugins.get(name);
		return null;	
	}
	

}
