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
	 * @return the plugin name
	 */
	public String getPluginName();
	
	/**
	 * @return the plugin description
	 */
	public String getPluginDescription();
	
	

}
