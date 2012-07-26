package edu.yu.einstein.wasp.load.service;

import java.util.List;

import edu.yu.einstein.wasp.model.ResourceCategoryMeta;

public interface ResourceCategoryLoadService extends WaspLoadService{

	public void update(List<ResourceCategoryMeta> meta, String resourceTypeString, String iname, String name, Integer isActive);
	
}
