package edu.yu.einstein.wasp.load.service;

import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleType;

/**
 * 
 * @author rdubin
 *
 */
public interface ControlLibraryLoadService extends WaspLoadService {

	public Sample update(String name, SampleType sampleType, SampleSubtype sampleSubtype, int isActive);

}

