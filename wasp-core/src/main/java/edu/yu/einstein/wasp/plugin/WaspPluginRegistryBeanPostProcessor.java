package edu.yu.einstein.wasp.plugin;


import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.integration.MessageChannel;
import org.springframework.util.Assert;

/**
 * Registers any beans extending the {@link WaspPlugin} abstract class with the injected {@link WaspPluginRegistry}
 * @author andymac
 *
 */
public class WaspPluginRegistryBeanPostProcessor implements BeanPostProcessor {
	
	private static Logger logger = LoggerFactory.getLogger(WaspPluginRegistryBeanPostProcessor.class);
	
	private WaspPluginRegistry pluginRegistry;
	
	
	public WaspPluginRegistry getPluginRegistry() {
		return pluginRegistry;
	}
	
	/**
	 * Make sure the registry is set before use.
	 * 
	*/
	@PostConstruct
	public void initialization() throws Exception {
		Assert.notNull(pluginRegistry, "pluginRegistry must not be null");
	}
	
	@Required
	public void setMessageRegistry(WaspPluginRegistry pluginRegistry) {
		this.pluginRegistry = pluginRegistry;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (WaspPlugin.class.isInstance(bean)){
			logger.debug("Adding WaspPlugin bean '"+beanName+"' to plugin registry");
			pluginRegistry.addPlugin((WaspPlugin) bean, beanName);
		}
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)	throws BeansException {
		// do nothing
		return bean;
	}
	

}
