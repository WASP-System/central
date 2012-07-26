package edu.yu.einstein.wasp.load.service;

import java.util.List;

import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.SoftwareMeta;

public interface SoftwareLoadService extends WaspLoadService {
	
	public void update(ResourceType resourceType, List<SoftwareMeta> meta, String iname, String name, Integer isActive);
	
	public ResourceType getSoftwareTypeByIName(String iname);

}
