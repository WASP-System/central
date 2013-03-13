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
	 * @return the pluginName
	 */
	public String getPluginName();
	
	

}
