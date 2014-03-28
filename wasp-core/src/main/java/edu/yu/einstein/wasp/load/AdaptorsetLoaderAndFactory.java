package edu.yu.einstein.wasp.load;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.interfacing.IndexingStrategy;
import edu.yu.einstein.wasp.load.service.AdaptorsetLoadService;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.AdaptorsetMeta;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.SampleType;

/**
 * update/inserts adaptorset from bean definition 
 * 
 * @author asmclellan
 *
 */
public class AdaptorsetLoaderAndFactory extends WaspLoader implements FactoryBean<Adaptorset> {

	@Autowired
	private AdaptorsetLoadService adaptorsetLoadService;

	private SampleType sampleType;
	
	private Adaptorset adaptorset;
	
	private IndexingStrategy indexingStrategy = IndexingStrategy.UNKNOWN;

	public void setSampleType(SampleType sampleType) {
		this.sampleType = sampleType;
	}

	private List<AdaptorsetMeta> meta;

	public void setMeta(List<AdaptorsetMeta> meta) {
		this.meta = meta;
	}

	public void setMetaFromWrapper(MetaLoadWrapper metaLoadWrapper) {
		meta = metaLoadWrapper.getMeta(AdaptorsetMeta.class);
	}

	private List<ResourceCategory> compatibleResources;

	public void setCompatibleResources(List<ResourceCategory> compatibleResources) {
		this.compatibleResources = compatibleResources;
	}

	private List<Adaptor> adaptorList;

	public void setAdaptorList(List<Adaptor> adaptorList) {
		this.adaptorList = adaptorList;
	}

	private int isActive = 1;

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	
	public IndexingStrategy getIndexingStrategy() {
		return indexingStrategy;
	}

	public void setIndexingStrategy(IndexingStrategy indexingStrategy) {
		this.indexingStrategy = indexingStrategy;
	}
	
	public void setIndexingStrategy(String indexingStrategy) {
		this.indexingStrategy = new IndexingStrategy(indexingStrategy);
	}

	@PostConstruct
	public void init(){
		adaptorset = adaptorsetLoadService.update(meta, adaptorList, sampleType, iname, name, indexingStrategy, isActive, compatibleResources);
	}

	@Override
	public Adaptorset getObject() throws Exception {
		return adaptorset;
	}

	@Override
	public Class<?> getObjectType() {
		return Adaptorset.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
