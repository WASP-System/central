package edu.yu.einstein.wasp.interfacing.plugin;

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
	public String getIName();
	
	/**
	 * @return the plugin name
	 */
	String getName();
	
	/**
	 * @return the plugin description
	 */
	public String getDescription();

}
