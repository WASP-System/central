package edu.yu.einstein.wasp.load.service;

import java.util.List;

import edu.yu.einstein.wasp.interfacing.IndexingStrategy;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.AdaptorsetMeta;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.SampleType;

/**
 * 
 * @author asmclellan
 *
 */
public interface AdaptorsetLoadService extends WaspLoadService{
	
	public Adaptorset update(List<AdaptorsetMeta> adaptorsetmeta, List<Adaptor> adaptorList, SampleType sampleType, String iname,
			String name, IndexingStrategy indexingStrategy, int isActive, List<ResourceCategory> compatibleResources);

}
