package edu.yu.einstein.wasp.load.service;

import java.util.List;

import edu.yu.einstein.wasp.model.ResourceCell;
import edu.yu.einstein.wasp.model.ResourceMeta;

public interface ResourceLoadService extends WaspLoadService {

	public void update(List<ResourceMeta> meta, String resourceCategoryIName, String resourceTypeString, List<ResourceCell> cells, String iname, String name);
	
}
