/**
 * 
 */
package edu.yu.einstein.wasp.plugin;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.interfaces.cli.ClientMessageI;
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
 * This abstract parent class implements reflection to determine methods that 
 * are capable of responding to external messages.  Methods of the format: 
 * public Message methodName(Message m) will have messages distributed using
 * the "task" header value by the process(Message m) method. 
 * 
 * @author calder
 * 
 */
public abstract class WaspPlugin extends HashMap<String, String> implements
		InitializingBean, DisposableBean, ClientMessageI {

	private Set<SoftwarePackage> provides = new HashSet<SoftwarePackage>();

	private Set<String> handles = new HashSet<String>();

	private String pluginName;

	private Properties waspSiteProperties;

	private MessageChannel messageChannel;

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
			MessageChannel channel) {
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
	}
	
	@Override
	public Message process(Message m) throws RemoteException {
		if (!m.getHeaders().containsKey("help") && !m.getHeaders().containsKey("task")) return getStandardHelp();
		if (m.getHeaders().containsKey("help") && !m.getHeaders().containsKey("task")) return getStandardHelp();
		
		Method method = getMethod((String) m.getHeaders().get("task"));
		
		if (m.getHeaders().containsKey("help") && m.getHeaders().containsKey("task")) {
			if (method == null) {
				return MessageBuilder.withPayload("Error: unknown task").build();
			}
			try {
				return (Message) method.invoke(this, MessageBuilder.withPayload("help").build());
			} catch (Exception e) {
				logger.warn("Error invoking method: " + method.getName() + " via request: " + m.getHeaders().toString());
				return MessageBuilder.withPayload("Unable to request help from " + method.getName()).build();
			}
		}
		try {
			return (Message) method.invoke(this, m);
		} catch (Exception e) {
			return MessageBuilder.withPayload("Unable to execute task " + method.getName()).build();
		}
		
	}
	
	private Message getStandardHelp() {
		Set<String> methods = getMethods();
		String retval = "";
		retval += this.getPluginName() + "\n" 
				+ "------------------------------\n"
				+ "available tasks:\n\n";
		for (String m : methods) {
			retval += m + "\n";
		}
		return MessageBuilder.withPayload(retval).build();
	}
	
	private Method getMethod(String m) {
		try {
			return this.getClass().getMethod(m, Message.class);
		} catch (Exception e) {
			return null;
		}
	}
	
	protected Set<String> getMethods() {
		TreeSet<String> methods = new TreeSet<String>();
		for ( Method m : this.getClass().getMethods() ) {
			if (m.getReturnType().equals(Message.class)
					&& m.getParameterTypes().length == 1
					&& m.getParameterTypes()[0].equals(Message.class)
					&& m.getName() != "process")
				methods.add(m.getName());
		}
		return methods;
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

}