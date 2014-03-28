/**
 *
 * AdaptorServiceImpl.java 
 * @author rdubin
 *  
 * the AdaptorService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.dao.AdaptorDao;
import edu.yu.einstein.wasp.dao.AdaptorsetDao;
import edu.yu.einstein.wasp.dao.AdaptorsetMetaDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.interfacing.IndexingStrategy;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.AdaptorsetMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.util.MetaHelper;

@Service
@Transactional("entityManager")
public class AdaptorServiceImpl extends WaspServiceImpl implements
		AdaptorService {
	
	
	public static final String ADAPTORSET_AREA = "adaptorset";
	
	public static final String INDEX_STRATEGY_META_KEY = "indexingStrategy";
	
	@Autowired
	AdaptorDao adaptorDao;
	
	@Autowired
	AdaptorsetMetaDao adaptorsetMetaDao;
	
	@Autowired
	AdaptorsetDao adaptorsetDao;

	@Autowired
	SampleService sampleService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sortAdaptorsByBarcodenumber(List<Adaptor> adaptors) {
		class AdaptorBarcodenumberComparator implements Comparator<Adaptor> {
			@Override
			public int compare(Adaptor arg0, Adaptor arg1) {
				return arg0.getBarcodenumber().compareTo(
						arg1.getBarcodenumber());
			}
		}
		Collections.sort(adaptors, new AdaptorBarcodenumberComparator()); // sort by sample's name
	}

	@Override
	public Adaptor getAdaptorByAdaptorId(Integer adaptorId) {
		return adaptorDao.findById(adaptorId);
	}

	@Override
	public Adaptor getAdaptor(Sample library) throws SampleTypeException, MetadataException {
		Sample lib = sampleService.getSampleById(library.getId());
		String adaptorId = MetaHelper.getMetaValue("genericLibrary", "adaptor", lib.getSampleMeta());
		return getAdaptorByAdaptorId(new Integer(adaptorId));
	}
	
	/**
	 * {@inheritDoc}
	 * @throws MetadataException 
	 */
	@Override
	public void setIndexingStrategy(Adaptorset adaptorset, IndexingStrategy indexingStrategy) throws MetadataException {
		Assert.assertParameterNotNull(adaptorset, "adaptorset cannot be null");
		Assert.assertParameterNotNull(indexingStrategy, "indexingStrategy cannot be null");
		AdaptorsetMeta meta = new AdaptorsetMeta();
		meta.setK(ADAPTORSET_AREA + "." + INDEX_STRATEGY_META_KEY);
		meta.setV(indexingStrategy.toString());
		meta.setAdaptorsetId(adaptorset.getId());
		adaptorsetMetaDao.setMeta(meta);
	}
	
	/**
	 * {@inheritDoc}
	 * @param adaptorset
	 * @return
	 */
	@Override
	public IndexingStrategy getIndexingStrategy(Integer adaptorsetId) {
		return getIndexingStrategy(adaptorsetDao.getAdaptorsetByAdaptorsetId(adaptorsetId));
	}
	
	/**
	 * {@inheritDoc}
	 * @param adaptorset
	 * @return
	 */
	@Override
	public IndexingStrategy getIndexingStrategy(Adaptorset adaptorset) {
		Assert.assertParameterNotNull(adaptorset, "adaptorset cannot be null");
		String strategyString = null;
		List<AdaptorsetMeta> metaList = adaptorset.getAdaptorsetMeta();
		if (metaList == null)
			metaList = new ArrayList<AdaptorsetMeta>();
		try{
			strategyString = (String) MetaHelper.getMetaValue(ADAPTORSET_AREA, INDEX_STRATEGY_META_KEY, metaList);
		} catch(MetadataException e) {
			return IndexingStrategy.UNKNOWN;
		}
		return new IndexingStrategy(strategyString);
	}

	@Override
	public AdaptorDao getAdaptorDao() {
		return adaptorDao;
	}

	@Override
	public void setAdaptorDao(AdaptorDao adaptorDao) {
		this.adaptorDao = adaptorDao;
	}

	@Override
	public AdaptorsetMetaDao getAdaptorsetMetaDao() {
		return adaptorsetMetaDao;
	}

	@Override
	public void setAdaptorsetMetaDao(AdaptorsetMetaDao adaptorsetMetaDao) {
		this.adaptorsetMetaDao = adaptorsetMetaDao;
	}

	@Override
	public AdaptorsetDao getAdaptorsetDao() {
		return adaptorsetDao;
	}

	@Override
	public void setAdaptorsetDao(AdaptorsetDao adaptorsetDao) {
		this.adaptorsetDao = adaptorsetDao;
	}

	
	

}
