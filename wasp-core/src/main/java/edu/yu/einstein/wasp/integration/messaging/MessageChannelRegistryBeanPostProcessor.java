package edu.yu.einstein.wasp.integration.messaging;


import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.messaging.MessageChannel;
import org.springframework.util.Assert;

/**
 * Registers any beans implementing the {@link MessageChannel} interface with the injected {@link MessageChannelRegistry}
 * @author asmclellan
 *
 */
public class MessageChannelRegistryBeanPostProcessor implements BeanPostProcessor {
	
	private Logger logger = LoggerFactory.getLogger(MessageChannelRegistryBeanPostProcessor.class);
	
	private MessageChannelRegistry messageRegistry;
	
	
	public MessageChannelRegistry getMessageRegistry() {
		return messageRegistry;
	}
	
	/**
	 * Make sure the registry is set before use.
	 * 
	*/
	@PostConstruct
	public void initialization() throws Exception {
		Assert.notNull(messageRegistry, "messageRegistry must not be null");
	}
	
	@Required
	public void setMessageRegistry(MessageChannelRegistry messageRegistry) {
		this.messageRegistry = messageRegistry;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (MessageChannel.class.isInstance(bean)){
			logger.debug("Adding MessageChannel bean '"+beanName+"' to message registry");
			messageRegistry.addChannel((MessageChannel) bean, beanName);
		}
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)	throws BeansException {
		// do nothing
		return bean;
	}
	

}
