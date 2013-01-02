package edu.yu.einstein.wasp.load.service;

import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.SampleTypeCategory;


public interface SampleTypeLoadService extends WaspLoadService {

	public SampleType update(String iname, String name, SampleTypeCategory sampleTypeCategory, int isActive);

}
