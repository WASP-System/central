/**
 * 
 */
package edu.yu.einstein.wasp.plugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * @author calder
 * 
 */
public abstract class WaspPlugin extends HashMap<String, String> implements InitializingBean, DisposableBean {
	
	private Set<SoftwarePackage> provides = new HashSet<SoftwarePackage>();
	
	private Set<String> handles = new HashSet<String>(); 

	private String pluginName;

	private Properties waspSiteProperties;
	
	private final Logger logger = Logger.getLogger(this.getClass());

	public WaspPlugin(String pluginName, Properties waspSiteProperties) {
		this.setPluginName(pluginName);
		this.waspSiteProperties = waspSiteProperties;
		Assert.assertParameterNotNull(pluginName, "plugin must be assigned a name");
		String prefix = "plugin." + pluginName;
		for (String key : this.waspSiteProperties.stringPropertyNames()) {
			if (key.startsWith(prefix)) {
				String newKey = key.replaceFirst(prefix, "");
				String value = this.waspSiteProperties.getProperty(key);
				this.put(newKey, value);
				logger.debug("Configured plugin " + pluginName + " with "
						+ newKey + "=" + value);
			}
		}
	}

	/**
	 * @return the provides
	 */
	public Set<SoftwarePackage> getProvides() {
		return provides;
	}

	/**
	 * @param provides the provides to set
	 */
	public void setProvides(Set<SoftwarePackage> provides) {
		this.provides = provides;
	}

	/**
	 * @return the handles
	 */
	public Set<String> getHandles() {
		return handles;
	}

	/**
	 * @param handles the handles to set
	 */
	public void setHandles(Set<String> handles) {
		this.handles = handles;
	}

	/**
	 * @return the pluginName
	 */
	public String getPluginName() {
		return pluginName;
	}

	/**
	 * @param pluginName the pluginName to set
	 */
	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}
	
	public abstract Set<String> getBatchJobNames();


}
