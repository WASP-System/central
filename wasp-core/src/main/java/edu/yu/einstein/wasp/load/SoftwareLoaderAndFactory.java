package edu.yu.einstein.wasp.load;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import edu.yu.einstein.wasp.load.service.SoftwareLoadService;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.SoftwareMeta;

/**
 * update/inserts db copy of subtype sample from bean definition
 * 
 * @author asmclellan
 * 
 */

public class SoftwareLoaderAndFactory<T extends Software> extends WaspResourceLoader implements FactoryBean<T>, ApplicationContextAware {

	@SuppressWarnings("unchecked")
	private Class<T> clazz = (Class<T>) Software.class ; // default
	
	private ApplicationContext ctx;
	
	public void setType(Class<T> clazz){
		this.clazz = clazz;
	}

	@Autowired
	private SoftwareLoadService softwareLoadService;

	private ResourceType resourceType;
	
	private T software;
	
	private String description = "";
	
	private List<SoftwareMeta> meta;

	private int isActive = 1;
	
	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public void setMeta(List<SoftwareMeta> meta) {
		this.meta = meta;
	}

	public void setMetaFromWrapper(MetaLoadWrapper metaLoadWrapper) {
		meta = metaLoadWrapper.getMeta(SoftwareMeta.class);
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@PostConstruct
	public void init(){
		SoftwareMeta descriptionMeta = new SoftwareMeta();
		descriptionMeta.setK(area + ".description");
		descriptionMeta.setV(description);
		if (meta == null)
			meta = new ArrayList<SoftwareMeta>();
		meta.add(descriptionMeta);
		softwareLoadService.updateUiFields(uiFields);
		software = softwareLoadService.update(resourceType, meta, iname, name, isActive, clazz);
	}

	@Override
	public T getObject() throws Exception {
		ctx.getAutowireCapableBeanFactory().autowireBean(software);
		return software;
	}

	@Override
	public Class<?> getObjectType() {
		return clazz;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		this.ctx = ctx;
	}
}
