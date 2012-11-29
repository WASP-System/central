package edu.yu.einstein.wasp.taskMapping;


import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.Assert;

import edu.yu.einstein.wasp.exception.WaspException;

/**
 * Registers any beans extending the {@link WaspTaskMapping} abstract class with the injected {@link TaskMappingRegistry}
 * @author andymac
 *
 */
public class TaskMappingRegistryBeanPostProcessor implements BeanPostProcessor {
	
	private Logger logger = LoggerFactory.getLogger(TaskMappingRegistryBeanPostProcessor.class);
	
	private TaskMappingRegistry taskMappingRegistry;
	
	
	public TaskMappingRegistry getMessageRegistry() {
		return taskMappingRegistry;
	}
	
	/**
	 * Make sure the registry is set before use.
	 * 
	*/
	@PostConstruct
	public void initialization() throws Exception {
		Assert.notNull(taskMappingRegistry, "taskMappingRegistry must not be null");
	}
	
	@Required
	public void setMessageRegistry(TaskMappingRegistry taskMappingRegistry) {
		this.taskMappingRegistry = taskMappingRegistry;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (WaspTaskMapping.class.isInstance(bean)){
			logger.debug("Adding WaspTaskMapping bean '"+beanName+"' to edu.yu.einstein.wasp.taskMapping registry");
			try {
				taskMappingRegistry.add((WaspTaskMapping) bean, beanName);
			} catch (WaspException e) {
				throw new FatalBeanException(e.getLocalizedMessage(), e);
			}
		}
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)	throws BeansException {
		// do nothing
		return bean;
	}
	

}
