package edu.yu.einstein.wasp.load;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.load.service.ResourceTypeLoadService;
import edu.yu.einstein.wasp.model.ResourceType;

/**
 * update/inserts db copy of ResourceType from bean definition
 * 
 */

public class ResourceTypeLoaderAndFactory extends WaspLoader implements	FactoryBean<ResourceType> {

	@Autowired
	private ResourceTypeLoadService resourceTypeLoadService;

	private int isActive = 1;

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	@Override
	public ResourceType getObject() throws Exception {
		resourceTypeLoadService.updateUiFields(uiFields);
		return resourceTypeLoadService.update(iname, name, isActive);
	}

	@Override
	public Class<?> getObjectType() {
		return ResourceType.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
