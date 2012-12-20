package edu.yu.einstein.wasp.load;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.load.service.ResourceTypeLoadService;

/**
 * update/inserts db copy of ResourceType from bean definition
 * 
 */

public class ResourceTypeLoader extends WaspLoader {

	@Autowired
	private ResourceTypeLoadService resourceTypeLoadService;

	private String resourceTypeCategoryIname;

	private Integer isActive;

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public String getResourceTypeCategoryIname() {
		return resourceTypeCategoryIname;
	}

	public void setResourceTypeCategoryIname(String resourceTypeCategoryIname) {
		this.resourceTypeCategoryIname = resourceTypeCategoryIname;
	}

	public ResourceTypeLoader() {
	}

	public ResourceTypeLoader(ResourceTypeLoader inheritFromLoadService) {
		super(inheritFromLoadService);
	}

	@PostConstruct
	public void init() throws Exception {
		resourceTypeLoadService.update(iname, name, resourceTypeCategoryIname, isActive);
		resourceTypeLoadService.updateUiFields(uiFields);

	}

}
