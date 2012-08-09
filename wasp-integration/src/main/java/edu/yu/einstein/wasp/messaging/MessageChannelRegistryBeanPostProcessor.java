package edu.yu.einstein.wasp.messaging;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.integration.MessageChannel;

/**
 * Registers any beans implementing the {@link MessageChannel} interface with the injected {@link MessageChannelRegistry}
 * @author andymac
 *
 */
public class MessageChannelRegistryBeanPostProcessor implements BeanPostProcessor {
	
	private static Logger logger = Logger.getLogger(MessageChannelRegistryBeanPostProcessor.class);
	
	private MessageChannelRegistry messageRegistry;
	
	
	public MessageChannelRegistry getMessageRegistry() {
		return messageRegistry;
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
