package edu.yu.einstein.wasp.load;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.load.service.ResourceCategoryLoadService;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.ResourceCategoryMeta;
import edu.yu.einstein.wasp.model.ResourceType;

/**
 * update/inserts db copy of subtype sample from bean definition
 * 
 * @author asmclellan 
 */

public class ResourceCategoryLoaderAndFactory extends WaspLoader implements	FactoryBean<ResourceCategory> {

	@Autowired
	ResourceCategoryLoadService resourceCategoryLoadService;

	private ResourceCategory resourceCategory;
	
	private ResourceType resourceType;

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	private List<ResourceCategoryMeta> meta;

	public void setMeta(List<ResourceCategoryMeta> meta) {
		this.meta = meta;
	}

	public void setMetaFromWrapper(MetaLoadWrapper metaLoadWrapper) {
		meta = metaLoadWrapper.getMeta(ResourceCategoryMeta.class);
	}

	private int isActive = 1;

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	
	@PostConstruct
	public void init(){
		resourceCategory =  resourceCategoryLoadService.update(meta, resourceType, iname, name, isActive);
	}

	@Override
	public ResourceCategory getObject() throws Exception {
		return resourceCategory;
	}

	@Override
	public Class<?> getObjectType() {
		return ResourceCategory.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
