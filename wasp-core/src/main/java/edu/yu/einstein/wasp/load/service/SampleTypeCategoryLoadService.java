package edu.yu.einstein.wasp.load.service;

import edu.yu.einstein.wasp.model.SampleTypeCategory;



public interface SampleTypeCategoryLoadService extends WaspLoadService {

	public SampleTypeCategory update(String iname, String name, int isActive);

}
