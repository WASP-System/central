package edu.yu.einstein.wasp.load.service;

import edu.yu.einstein.wasp.model.ResourceType;


public interface ResourceTypeLoadService extends WaspLoadService {

	public ResourceType update(String iname, String name, Integer isActive);

}
