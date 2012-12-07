package edu.yu.einstein.wasp.load.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.AdaptorDao;
import edu.yu.einstein.wasp.dao.AdaptorMetaDao;
import edu.yu.einstein.wasp.dao.AdaptorsetDao;
import edu.yu.einstein.wasp.dao.AdaptorsetMetaDao;
import edu.yu.einstein.wasp.dao.AdaptorsetResourceCategoryDao;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.exception.NullResourceCategoryException;
import edu.yu.einstein.wasp.load.service.AdaptorsetLoadService;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.AdaptorMeta;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.AdaptorsetMeta;
import edu.yu.einstein.wasp.model.AdaptorsetResourceCategory;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.SampleType;

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
	private AdaptorsetResourceCategoryDao adaptorsetResourceCategoryDao;
	  
	@Autowired
	private ResourceCategoryDao resourceCategoryDao;
	
	@Autowired
	private SampleTypeDao sampleTypeDao;
	
	private Adaptorset addOrUpdateAdaptorset(String sampleTypeIName, String iname, String name, Integer isActive){
		Adaptorset adaptorset = adaptorsetDao.getAdaptorsetByIName(iname);
		SampleType sampleType = sampleTypeDao.getSampleTypeByIName(sampleTypeIName);
	    // inserts or update adaptorset
	    if (adaptorset.getAdaptorsetId() == null) { 
	    	// new
	    	adaptorset.setIName(iname);
	    	adaptorset.setName(name);
	    	adaptorset.setSampleType(sampleType);
	    	adaptorset.setIsActive(isActive.intValue());

	    	adaptorsetDao.save(adaptorset);

	    	// refreshes
	    	adaptorset = adaptorsetDao.getAdaptorsetByIName(iname); 

	    } else {
	      boolean changed = false;	
	      if (!adaptorset.getName().equals(name)){
	    	  adaptorset.setName(name);
	    	  changed = true;
	      }
	      if (!adaptorset.getSampleType().equals(sampleType)){
	    	  adaptorset.setSampleType(sampleType);
	    	  changed = true;
	      }
	      if (adaptorset.getIsActive().intValue() != isActive.intValue()){
	    	  adaptorset.setIsActive(isActive.intValue());
	    	  changed = true;
	      }
	      
	      if (changed)
	    	  adaptorsetDao.save(adaptorset);
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

	      adaptorsetMeta.setAdaptorsetId(adaptorset.getAdaptorsetId()); 
	      adaptorsetMetaDao.save(adaptorsetMeta); 
	    }

	   for (String adaptorsetMetaKey : oldAdaptorsetMetas.keySet()) {
	      AdaptorsetMeta adaptorsetMeta = oldAdaptorsetMetas.get(adaptorsetMetaKey); 
	      adaptorsetMetaDao.remove(adaptorsetMeta); 
	      adaptorsetMetaDao.flush(adaptorsetMeta); 
	    }
	}
	
	private void syncAdaptorsetResources(Adaptorset adaptorset, List<String> compatibleResourcesByIName){
		Map<String, AdaptorsetResourceCategory> oldAdaptorsetResourceCats  = new HashMap<String, AdaptorsetResourceCategory>();
	    for (AdaptorsetResourceCategory adaptorsetResourceCat: safeList(adaptorset.getAdaptorsetResourceCategory())) {
	    	oldAdaptorsetResourceCats.put(adaptorsetResourceCat.getResourceCategory().getIName(), adaptorsetResourceCat);
	    } 
	    
	    for (String resourceIName: safeList(compatibleResourcesByIName)) {
	    	if (oldAdaptorsetResourceCats.containsKey(resourceIName)) {
	    		oldAdaptorsetResourceCats.remove(resourceIName);
	    		continue;
	    	}
	    	ResourceCategory resourceCat = resourceCategoryDao.getResourceCategoryByIName(resourceIName);
	    	if (resourceCat.getResourceCategoryId() != null){
	    		AdaptorsetResourceCategory adaptorsetresource = new AdaptorsetResourceCategory();
	    		adaptorsetresource.setResourcecategoryId(resourceCat.getResourceCategoryId());
	    		adaptorsetresource.setAdaptorsetId(adaptorset.getAdaptorsetId());
	    		adaptorsetResourceCategoryDao.save(adaptorsetresource);
	    		oldAdaptorsetResourceCats.remove(resourceIName);
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
	    adaptorSearchMap.put("adaptorsetId", adaptorset.getAdaptorsetId());
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
	    	if (adaptor.getAdaptorId() != null){
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
	    		if (adaptor.getAdaptorsetId().intValue() != adaptorset.getAdaptorsetId().intValue()){
	    			adaptor.setAdaptorsetId(adaptorset.getAdaptorsetId());
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
		    	    adaptorMeta.setAdaptorId(adaptor.getAdaptorId()); 
		    	    adaptorMetaDao.save(adaptorMeta); 
	    	    }

	    	    // delete the left overs
	    	    for (String adaptorMetaKey : oldAdaptorMetas.keySet()) {
	    	    	AdaptorMeta adaptorMeta = oldAdaptorMetas.get(adaptorMetaKey); 
	    	    	adaptorMetaDao.remove(adaptorMeta); 
	    	    	adaptorMetaDao.flush(adaptorMeta); 
	    	    }
	    	} else {
	    		// new adaptor
	    		adaptor = adaptorIn;
	    		adaptor.setAdaptorsetId(adaptorset.getAdaptorsetId());
	    		adaptor.setIsActive(isActive);
	    		adaptor.setIName(adaptorIn.getIName());
	    		adaptor.setName(adaptorIn.getName());
	    		adaptorDao.save(adaptor);
	    		adaptor = adaptorDao.getAdaptorByIName(iname); // refresh
	    		for (AdaptorMeta adaptorMeta: safeList(adaptor.getAdaptorMeta()) ) {
	    			adaptorMeta.setAdaptorId(adaptor.getAdaptorId()); 
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
	public void update(List<AdaptorsetMeta>  adaptorsetmeta, List<Adaptor> adaptorList, String sampleTypeIName, String iname, String name, Integer isActive, List<String> compatibleResourcesByIName){
	    
	    if (isActive == null)
	    	  isActive = 1;
		
		Adaptorset adaptorset = addOrUpdateAdaptorset(sampleTypeIName, iname, name, isActive);

	    syncAdaptorsetMeta(adaptorset,adaptorsetmeta);

	    syncAdaptorsetResources(adaptorset, compatibleResourcesByIName);
	    
	    addOrUpdateAdaptors(adaptorset, adaptorsetmeta, adaptorList, isActive, iname);
	    
	}

}
