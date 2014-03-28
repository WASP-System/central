package edu.yu.einstein.wasp.load.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.dao.AdaptorDao;
import edu.yu.einstein.wasp.dao.AdaptorMetaDao;
import edu.yu.einstein.wasp.dao.AdaptorsetDao;
import edu.yu.einstein.wasp.dao.AdaptorsetMetaDao;
import edu.yu.einstein.wasp.dao.AdaptorsetResourceCategoryDao;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.NullResourceCategoryException;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.interfacing.IndexingStrategy;
import edu.yu.einstein.wasp.load.service.AdaptorsetLoadService;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.AdaptorMeta;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.AdaptorsetMeta;
import edu.yu.einstein.wasp.model.AdaptorsetResourceCategory;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.service.AdaptorService;

/**
 * 
 * @author asmclellan
 *
 */
@Service
@Transactional("entityManager")
public class AdaptorsetLoadServiceImpl extends WaspLoadServiceImpl implements AdaptorsetLoadService{
	
	@Autowired
	private AdaptorDao adaptorDao;
	  
	@Autowired
	private AdaptorMetaDao adaptorMetaDao;
	  
	@Autowired
	private AdaptorsetDao adaptorsetDao;
	
	@Autowired
	private AdaptorsetMetaDao adaptorsetMetaDao;
	

	@Autowired
	private AdaptorService adaptorService;
	
	@Autowired
	private AdaptorsetResourceCategoryDao adaptorsetResourceCategoryDao;
	  
	@Autowired
	private ResourceCategoryDao resourceCategoryDao;
	
	@Autowired
	private SampleTypeDao sampleTypeDao;
	
	private Adaptorset addOrUpdateAdaptorset(SampleType sampleType, String iname, String name, IndexingStrategy indexingStrategy, Integer isActive){
		Adaptorset adaptorset = adaptorsetDao.getAdaptorsetByIName(iname);
		// inserts or update adaptorset
	    if (adaptorset.getId() == null) { 
	    	// new
	    	adaptorset.setIName(iname);
	    	adaptorset.setName(name);
	    	adaptorset.setSampleType(sampleType);
	    	adaptorset.setIsActive(isActive.intValue());
	    	adaptorset = adaptorsetDao.save(adaptorset);
	    } else {
	      if (adaptorset.getName() == null || !adaptorset.getName().equals(name))
	    	  adaptorset.setName(name);
	      if (adaptorset.getSampleType() == null || !adaptorset.getSampleType().equals(sampleType))
	    	  adaptorset.setSampleType(sampleType);
	      if (adaptorset.getIsActive().intValue() != isActive.intValue())
	    	  adaptorset.setIsActive(isActive.intValue());
	    }
	    try {
			adaptorService.setIndexingStrategy(adaptorset, indexingStrategy);
		} catch (MetadataException e) {
			throw new WaspRuntimeException("Unable to set indexing strategy for adaptor. Rolling back changes.");
		}
	    return adaptorset;
	}
	
	public void syncAdaptorsetMeta(Adaptorset adaptorset, List<AdaptorsetMeta>  adaptorsetmeta){
		int lastPosition = 0;
	    Map<String, AdaptorsetMeta> oldAdaptorsetMetas  = new HashMap<String, AdaptorsetMeta>();
	    for (AdaptorsetMeta adaptorsetMeta: safeList(adaptorset.getAdaptorsetMeta())) {
	    	oldAdaptorsetMetas.put(adaptorsetMeta.getK(), adaptorsetMeta);
	    } 
	    for (AdaptorsetMeta adaptorsetMeta: safeList(adaptorsetmeta) ) {

	      // incremental position numbers. 
	      if ( adaptorsetMeta.getPosition() == 0 ||
	    		  adaptorsetMeta.getPosition() <= lastPosition
	        )  {
	    	  adaptorsetMeta.setPosition(lastPosition +1); 
	      }
	      lastPosition = adaptorsetMeta.getPosition();

	      if (oldAdaptorsetMetas.containsKey(adaptorsetMeta.getK())) {
	    	AdaptorsetMeta old = oldAdaptorsetMetas.get(adaptorsetMeta.getK());
	        boolean changed = false;
	        if (!old.getV().equals(adaptorsetMeta.getV())){
	        	old.setV(adaptorsetMeta.getV());
	        	changed = true;
	        }
	        if (old.getPosition().intValue() != adaptorsetMeta.getPosition()){
	        	old.setPosition(adaptorsetMeta.getPosition());
	        	changed = true;
	        }
	        if (changed)
	        	adaptorsetMetaDao.save(old);

	        oldAdaptorsetMetas.remove(old.getK()); // remove the meta from the old meta list as we're done with it
	        continue;
	      }

	      adaptorsetMeta.setAdaptorsetId(adaptorset.getId()); 
	      adaptorsetMetaDao.save(adaptorsetMeta); 
	    }

	    // The next block was commented out by Dubin; 10-07-2013 as it removes meta 
	    //that is added any time after the initial data upload
	    /*
	   for (String adaptorsetMetaKey : oldAdaptorsetMetas.keySet()) {
	      AdaptorsetMeta adaptorsetMeta = oldAdaptorsetMetas.get(adaptorsetMetaKey); 
	      adaptorsetMetaDao.remove(adaptorsetMeta); 
	      adaptorsetMetaDao.flush(adaptorsetMeta); 
	    }
	    */
	}
	
	private void syncAdaptorsetResources(Adaptorset adaptorset, List<ResourceCategory> compatibleResources){
		Map<String, AdaptorsetResourceCategory> oldAdaptorsetResourceCats  = new HashMap<String, AdaptorsetResourceCategory>();
	    for (AdaptorsetResourceCategory adaptorsetResourceCat: safeList(adaptorset.getAdaptorsetResourceCategory())) {
	    	oldAdaptorsetResourceCats.put(adaptorsetResourceCat.getResourceCategory().getIName(), adaptorsetResourceCat);
	    } 
	    
	    for (ResourceCategory resourceCat: safeList(compatibleResources)) {
	    	if (oldAdaptorsetResourceCats.containsKey(resourceCat.getIName())) {
	    		oldAdaptorsetResourceCats.remove(resourceCat.getIName());
	    		continue;
	    	}
	    	if (resourceCat.getId() != null){
	    		AdaptorsetResourceCategory adaptorsetresource = new AdaptorsetResourceCategory();
	    		adaptorsetresource.setResourcecategoryId(resourceCat.getId());
	    		adaptorsetresource.setAdaptorsetId(adaptorset.getId());
	    		adaptorsetResourceCategoryDao.save(adaptorsetresource);
	    		oldAdaptorsetResourceCats.remove(resourceCat.getIName());
	    	} else {
	    		throw new NullResourceCategoryException();
	    	}
	    }

	    // remove the left overs
	    for (String adaptorsetKey : oldAdaptorsetResourceCats.keySet()) {
	    	ResourceCategory resourceCat = resourceCategoryDao.getResourceCategoryByIName(adaptorsetKey);
	    	AdaptorsetResourceCategory adaptorsetResource = adaptorsetResourceCategoryDao.getAdaptorsetResourceCategoryByAdaptorsetIdResourcecategoryId(adaptorset.getAdaptorsetId(), resourceCat.getResourceCategoryId());
	    	adaptorsetResourceCategoryDao.remove(adaptorsetResource);
	    }
	}
	
	private void addOrUpdateAdaptors(Adaptorset adaptorset, List<AdaptorsetMeta>  adaptorsetmeta, List<Adaptor> adaptorList, Integer isActive, String iname){
		Map<String, Integer> adaptorSearchMap = new HashMap<String, Integer>();
	    adaptorSearchMap.put("adaptorsetId", adaptorset.getId());
	    List<Adaptor> adaptorsInAdaptorset = adaptorDao.findByMap(adaptorSearchMap);
	    Map<String, Adaptor> oldAdaptors = new HashMap<String, Adaptor>();
	    for (Adaptor adaptor : adaptorsInAdaptorset){
	    	oldAdaptors.put(adaptor.getIName(), adaptor);
	    }
	    
	    for (Adaptor adaptorIn : safeList(adaptorList)){
	    	String adaptorKey = adaptorIn.getIName();
	    	// we should try and get an existing adaptor using the iName and use that 
	    	// as it is possible the adaptor set may have changed (i.e. don't use the oldAdaptors objects 
	    	// obtained using the current adaptorset ID.
	    	Adaptor adaptor = adaptorDao.getAdaptorByIName(adaptorKey);
	    	if (adaptor.getId() != null){
	    		// adaptor exists
	    		boolean changed = false;
	    		if (!adaptor.getName().equals(adaptorIn.getName())){
	    			adaptor.setName(adaptorIn.getName());
	    			changed = true;
	    		}
	    		if (!adaptor.getSequence().equals(adaptorIn.getSequence())){
	    			adaptor.setSequence(adaptorIn.getSequence());
	    			changed = true;
	    		}
	    		if (!adaptor.getBarcodesequence().equals(adaptorIn.getBarcodesequence())){
	    			adaptor.setBarcodesequence(adaptorIn.getBarcodesequence());
	    			changed = true;
	    		}
	    		if (adaptor.getBarcodenumber().intValue() != adaptorIn.getBarcodenumber().intValue()){
	    			adaptor.setBarcodenumber(adaptorIn.getBarcodenumber());
	    			changed = true;
	    		}
	    		if (adaptor.getAdaptorsetId().intValue() != adaptorset.getId().intValue()){
	    			adaptor.setAdaptorsetId(adaptorset.getId());
	    			changed = true;
	    		}
	    		if (adaptor.getIsActive().intValue() != isActive.intValue()){
	    			adaptor.setIsActive(isActive.intValue());
	    	    	changed = true;
	    	    }
	    		if (changed)
	    			adaptorDao.save(adaptor);
	    		if (oldAdaptors.containsKey(adaptorKey))
	    			oldAdaptors.remove(adaptor.getIName());
	    		
	    		int lastPosition = 0;
	    	    Map<String, AdaptorMeta> oldAdaptorMetas  = new HashMap<String, AdaptorMeta>();
	    	    for (AdaptorMeta adaptorMeta: safeList(adaptor.getAdaptorMeta())) {
	    	    	oldAdaptorMetas.put(adaptorMeta.getK(), adaptorMeta);
	    	    } 
	    	    // sync adaptor metas 
	    	    for (AdaptorMeta adaptorMeta: safeList(adaptor.getAdaptorMeta()) ) {

		    	    // incremental position numbers. 
		    	    if ( adaptorMeta.getPosition() == 0 ||
		    	    		adaptorMeta.getPosition() <= lastPosition
		    	        )  {
		    	    	adaptorMeta.setPosition(lastPosition +1); 
		    	    }
		    	    lastPosition = adaptorMeta.getPosition();
		
		    	    if (oldAdaptorMetas.containsKey(adaptorMeta.getK())) {
		    	    	// is existing metadata
		    	    	AdaptorMeta oldMeta = oldAdaptorMetas.get(adaptorMeta.getK());
		    	        boolean metaChanged = false;
		    	        if (!oldMeta.getV().equals(adaptorMeta.getV())){
		    	        	oldMeta.setV(adaptorMeta.getV());
		    	        	metaChanged = true;
		    	        }
		    	        if (oldMeta.getPosition().intValue() != adaptorMeta.getPosition()){
		    	        	oldMeta.setPosition(adaptorMeta.getPosition());
		    	        	metaChanged = true;
		    	        }
		    	        if (metaChanged)
		    	        	adaptorMetaDao.save(oldMeta);
		
		    	        oldAdaptorMetas.remove(oldMeta.getK()); // remove the meta from the old meta list as we're done with it
		    	        continue; 
		    	    }
		    	    // is new metadata
		    	    adaptorMeta.setAdaptorId(adaptor.getId()); 
		    	    adaptorMetaDao.save(adaptorMeta); 
	    	    }

	    	    // delete the left overs
	    	    // The next block was commented out by Dubin; 10-07-2013 as it removes meta 
	    	    //that is added any time after the initial data upload
	    	    /*
	    	    for (String adaptorMetaKey : oldAdaptorMetas.keySet()) {
	    	    	AdaptorMeta adaptorMeta = oldAdaptorMetas.get(adaptorMetaKey); 
	    	    	adaptorMetaDao.remove(adaptorMeta); 
	    	    	adaptorMetaDao.flush(adaptorMeta); 
	    	    }
	    	    */
	    	} else {
	    		// new adaptor
	    		adaptor = adaptorIn;
	    		adaptor.setAdaptorsetId(adaptorset.getId());
	    		adaptor.setIsActive(isActive);
	    		adaptor.setIName(adaptorIn.getIName());
	    		adaptor.setName(adaptorIn.getName());
	    		adaptor = adaptorDao.save(adaptor);
	    		for (AdaptorMeta adaptorMeta: safeList(adaptor.getAdaptorMeta()) ) {
	    			adaptorMeta.setAdaptorId(adaptor.getId()); 
		    	    adaptorMetaDao.save(adaptorMeta);
	    		}
	    	}
	    }
	    // inactivate the leftover adaptors
		for (String adaptorIName : oldAdaptors.keySet()){
			Adaptor adaptorToInactivate = adaptorDao.getAdaptorByIName(adaptorIName);
			adaptorToInactivate.setIsActive(0);
			adaptorDao.save(adaptorToInactivate);
		}
	}
	
	
	@Transactional("entityManager")
	@Override 
	public Adaptorset update(List<AdaptorsetMeta>  adaptorsetmeta, List<Adaptor> adaptorList, SampleType sampleType, String iname, String name, IndexingStrategy indexingStrategy, int isActive, List<ResourceCategory> compatibleResources){
		Assert.assertParameterNotNull(iname, "iname Cannot be null");
		Assert.assertParameterNotNull(name, "name Cannot be null");
		Assert.assertParameterNotNull(sampleType, "sampleType Cannot be null");
	    Adaptorset adaptorset = addOrUpdateAdaptorset(sampleType, iname, name, indexingStrategy, isActive);

	    syncAdaptorsetMeta(adaptorset,adaptorsetmeta);

	    syncAdaptorsetResources(adaptorset, compatibleResources);
	    
	    addOrUpdateAdaptors(adaptorset, adaptorsetmeta, adaptorList, isActive, iname);
	    
	    return adaptorset;
	    
	}

}
