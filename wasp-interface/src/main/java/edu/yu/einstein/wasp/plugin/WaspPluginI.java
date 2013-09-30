package edu.yu.einstein.wasp.plugin;

import java.util.Set;

public interface WaspPluginI {
	
	/**
	 * @return the provides
	 */
	public Set<?> getProvides();
	
	/**
	 * @return the handles
	 */
	public Set<?> getHandles();
	
	/**
	 * @return the plugin Iname
	 */
	public String getPluginIName();
	
	/**
	 * @return the plugin name
	 */
	String getPluginName();
	
	/**
	 * @return the plugin description
	 */
	public String getPluginDescription();

}
