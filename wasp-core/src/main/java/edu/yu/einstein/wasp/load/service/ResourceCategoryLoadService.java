package edu.yu.einstein.wasp.load.service;

import java.util.List;

import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.ResourceCategoryMeta;
import edu.yu.einstein.wasp.model.ResourceType;

public interface ResourceCategoryLoadService extends WaspLoadService{

	public ResourceCategory update(List<ResourceCategoryMeta> meta, ResourceType resourceType, String iname, String name, int isActive);
	
}
