/**
 * 
 */
package edu.yu.einstein.wasp.plugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.MessageChannel;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * Abstract Class for defining Wasp System plugins. Requires that a name (string
 * that refers to the target of messages), siteProperties (which possibly has
 * local configuration for the plugin), a message channel (of the format:
 * wasp.channel.pluginName), and a handle to the {@link WaspPluginRegistry} (in
 * which the bean registers itself, after properties have been set.
 * 
 * Optionally, the plugin may declare properties "provides" and "handles"
 * which declare services that the plugin implements and resources that 
 * it may act upon.  For example, a plugin may declare that it implements
 * "referenceBasedAligner", or "illuminaSequenceRunProcessor".  An
 * illuminaSequenceRunProcessor might additionally handle "illuminaHiSeq2000Area". 
 * 
 * @author calder
 * 
 */
public abstract class WaspPlugin extends HashMap<String, String> implements
		InitializingBean, DisposableBean {

	private Set<SoftwarePackage> provides = new HashSet<SoftwarePackage>();

	private Set<String> handles = new HashSet<String>();

	private String pluginName;

	private Properties waspSiteProperties;

	private MessageChannel messageChannel;

	private WaspPluginRegistry pluginRegistry;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Parent constructor for a WaspPlugin.
	 * 
	 * @param pluginName String that represents a unique name and the name of the message channel
	 * @param waspSiteProperties local configuration bean
	 * @param channel MessageChannel for this plugin (named with the format wasp.channel.pluginName)
	 * @param pluginRegistry handle to the {@link WaspPluginRegistry}
	 */
	public WaspPlugin(String pluginName, Properties waspSiteProperties,
			MessageChannel channel, WaspPluginRegistry pluginRegistry) {
		this.setPluginName(pluginName);
		this.waspSiteProperties = waspSiteProperties;
		Assert.assertParameterNotNull(pluginName,
				"plugin must be assigned a name");
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
		this.messageChannel = channel;
		this.pluginRegistry = pluginRegistry;
	}

	
	/**
	 * After the properties are set, register this bean with the {@link WaspPluginRegistry}.
	 */
	public void afterPropertiesSet() throws Exception {
		pluginRegistry.addPlugin(this, getPluginName());
	}

	/**
	 * @return the provides
	 */
	public Set<SoftwarePackage> getProvides() {
		return provides;
	}

	/**
	 * @param provides
	 *            the provides to set
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
	 * @param handles
	 *            the handles to set
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
	 * @param pluginName
	 *            the pluginName to set
	 */
	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	public abstract Set<String> getBatchJobNames();

	/**
	 * @return the messageChannel
	 */
	public MessageChannel getMessageChannel() {
		return messageChannel;
	}

	/**
	 * @param messageChannel
	 *            the messageChannel to set
	 */
	public void setMessageChannel(MessageChannel messageChannel) {
		this.messageChannel = messageChannel;
	}

	/**
	 * @return the pluginRegistry
	 */
	public WaspPluginRegistry getPluginRegistry() {
		return pluginRegistry;
	}

	/**
	 * @param pluginRegistry
	 *            the pluginRegistry to set
	 */
	public void setPluginRegistry(WaspPluginRegistry pluginRegistry) {
		this.pluginRegistry = pluginRegistry;
	}

}
