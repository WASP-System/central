package edu.yu.einstein.wasp.load.service;


public interface ResourceTypeLoadService extends WaspLoadService {

	public void update(String iname, String name, String resourceTypeCategoryIname, Integer isActive);

}
