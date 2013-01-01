package edu.yu.einstein.wasp.load;

import java.util.List;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.load.service.SoftwareLoadService;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.SoftwareMeta;

/**
 * update/inserts db copy of subtype sample from bean definition
 * 
 */

public class SoftwareLoaderAndFactory<T extends Software> extends WaspResourceLoader implements FactoryBean<T> {

	private Class<T> clazz = (Class<T>) Software.class ; // default
	
	public void setType(Class<T> clazz){
		this.clazz = clazz;
	}

	@Autowired
	private SoftwareLoadService softwareLoadService;

	private ResourceType resourceType;

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	private List<SoftwareMeta> meta;

	public void setMeta(List<SoftwareMeta> meta) {
		this.meta = meta;
	}

	public void setMetaFromWrapper(MetaLoadWrapper metaLoadWrapper) {
		meta = metaLoadWrapper.getMeta(SoftwareMeta.class);
	}

	private int isActive = 1;

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	@Override
	public T getObject() throws Exception {
		softwareLoadService.updateUiFields(uiFields);
		T software = softwareLoadService.update(resourceType, meta, iname, name, isActive, clazz);
		System.out.println("getObject() ANDY: clazz = "+clazz.getName());
		System.out.println("getObject() ANDY: returned object class = "+software.getClass().getName());
		System.out.println("getObjectType() ANDY: class = "+getObjectType().getName());
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
}
