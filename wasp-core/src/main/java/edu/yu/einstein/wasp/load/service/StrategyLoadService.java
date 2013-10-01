package edu.yu.einstein.wasp.load.service;

import edu.yu.einstein.wasp.additionalClasses.Strategy;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.SampleTypeCategory;

public interface StrategyLoadService extends WaspLoadService {

	public Strategy update(String k, String v);

}
