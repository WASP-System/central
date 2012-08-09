package edu.yu.einstein.wasp.load.service;

import java.util.List;

import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.AdaptorsetMeta;

public interface AdaptorsetLoadService extends WaspLoadService{
	
	public void update(List<AdaptorsetMeta>  adaptorsetmeta, List<Adaptor> adaptorList, String sampleTypeIName, String iname, String name, Integer isActive, List<String> compatibleResourcesByIName);

}
