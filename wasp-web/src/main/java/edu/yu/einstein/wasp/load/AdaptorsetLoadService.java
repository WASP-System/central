package edu.yu.einstein.wasp.load;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import util.spring.PostInitialize;
import edu.yu.einstein.wasp.exception.NullResourceCategoryException;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.AdaptorMeta;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.AdaptorsetMeta;
import edu.yu.einstein.wasp.model.AdaptorsetResourceCategory;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.TypeSample;
import edu.yu.einstein.wasp.service.AdaptorMetaService;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.AdaptorsetMetaService;
import edu.yu.einstein.wasp.service.AdaptorsetResourceCategoryService;
import edu.yu.einstein.wasp.service.AdaptorsetService;
import edu.yu.einstein.wasp.service.ResourceCategoryService;
import edu.yu.einstein.wasp.service.TypeSampleService;


/**
 * update/inserts adaptorset from bean definition
 * takes in  properties
 *   - iname / internalname
 *   - name / label
 *   - typeSampleIName / typeSample iName
 *   - compatibleResourcesByIName (List<String> iName of Resources)
 *   - adaptorList (List<Adaptor>)

 */

@Transactional
public class AdaptorsetLoadService extends WaspLoadService {


  @Autowired
  private AdaptorService adaptorService;
  
  @Autowired
  private AdaptorMetaService adaptorMetaService;
  
  @Autowired
  private AdaptorsetService adaptorsetService;
  
  @Autowired
  private AdaptorsetMetaService adaptorsetMetaService;
  
  @Autowired
  private AdaptorsetResourceCategoryService adaptorsetResourceCategoryService;
  
  @Autowired
  private ResourceCategoryService resourceCategoryService;

  @Autowired
  private TypeSampleService typeSampleService;
 
  private String typeSampleIName;
  public void setTypeSampleIName(String typeSampleIName){ this.typeSampleIName = typeSampleIName; }
  
  private List<AdaptorsetMeta>  meta;
  public void setMeta(List<AdaptorsetMeta> meta){ this.meta = meta; }

  private List<String> compatibleResourcesByIName; 
  public void setCompatibleResourcesByIName(List<String> compatibleResourcesByIName) {this.compatibleResourcesByIName = compatibleResourcesByIName; }

  private List<Adaptor> adaptorList; 
  public void setAdaptorList(List<Adaptor> adaptorList) {this.adaptorList = adaptorList; }


  @Override
  @Transactional
  @PostInitialize 
  public void postInitialize() {
    // skips component scanned  (if scanned in)
    if (name == null) { return; }
    
    TypeSample typeSample = typeSampleService.getTypeSampleByIName(typeSampleIName);

    Adaptorset adaptorset = adaptorsetService.getAdaptorsetByIName(iname);
    
    // inserts or update workflow
    if (adaptorset.getAdaptorsetId() == null) { 
    	// new
    	adaptorset.setIName(iname);
    	adaptorset.setName(name);
    	adaptorset.setTypeSample(typeSample);
    	adaptorset.setIsActive(1);

    	adaptorsetService.save(adaptorset);

    	// refreshes
    	adaptorset = adaptorsetService.getAdaptorsetByIName(iname); 

    } else {
      boolean changed = false;	
      if (!adaptorset.getName().equals(name)){
    	  adaptorset.setName(name);
    	  changed = true;
      }
      if (!adaptorset.getTypeSample().equals(typeSample)){
    	  adaptorset.setTypeSample(typeSample);
    	  changed = true;
      }
      
      if (changed)
    	  adaptorsetService.save(adaptorset);
    }

    // sync metas
    int lastPosition = 0;
    Map<String, AdaptorsetMeta> oldAdaptorsetMetas  = new HashMap<String, AdaptorsetMeta>();
    for (AdaptorsetMeta adaptorsetMeta: safeList(adaptorset.getAdaptorsetMeta())) {
    	oldAdaptorsetMetas.put(adaptorsetMeta.getK(), adaptorsetMeta);
    } 
    for (AdaptorsetMeta adaptorsetMeta: safeList(meta) ) {

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
        	adaptorsetMetaService.save(old);

        oldAdaptorsetMetas.remove(old.getK()); // remove the meta from the old meta list as we're done with it
        continue;
      }

      adaptorsetMeta.setAdaptorsetId(adaptorset.getAdaptorsetId()); 
      adaptorsetMetaService.save(adaptorsetMeta); 
    }

   for (String adaptorsetMetaKey : oldAdaptorsetMetas.keySet()) {
      AdaptorsetMeta adaptorsetMeta = oldAdaptorsetMetas.get(adaptorsetMetaKey); 
      adaptorsetMetaService.remove(adaptorsetMeta); 
      adaptorsetMetaService.flush(adaptorsetMeta); 
    }

    // sync adaptorsetResources
    Map<String, AdaptorsetResourceCategory> oldAdaptorsetResourceCats  = new HashMap<String, AdaptorsetResourceCategory>();
    for (AdaptorsetResourceCategory adaptorsetResourceCat: safeList(adaptorset.getAdaptorsetResourceCategory())) {
    	oldAdaptorsetResourceCats.put(adaptorsetResourceCat.getResourceCategory().getIName(), adaptorsetResourceCat);
    } 
    
    for (String resourceIName: safeList(compatibleResourcesByIName)) {
    	if (oldAdaptorsetResourceCats.containsKey(resourceIName)) {
    		oldAdaptorsetResourceCats.remove(resourceIName);
    		continue;
    	}
    	ResourceCategory resourceCat = resourceCategoryService.getResourceCategoryByIName(resourceIName);
    	if (resourceCat.getResourceCategoryId() != null){
    		AdaptorsetResourceCategory adaptorsetresource = new AdaptorsetResourceCategory();
    		adaptorsetresource.setResourcecategoryId(resourceCat.getResourceCategoryId());
    		adaptorsetresource.setAdaptorsetId(adaptorset.getAdaptorsetId());
    		adaptorsetResourceCategoryService.save(adaptorsetresource);
    		oldAdaptorsetResourceCats.remove(resourceIName);
    	} else {
    		throw new NullResourceCategoryException();
    	}
    }

    // remove the left overs
    for (String adaptorsetKey : oldAdaptorsetResourceCats.keySet()) {
    	ResourceCategory resourceCat = resourceCategoryService.getResourceCategoryByIName(adaptorsetKey);
    	AdaptorsetResourceCategory adaptorsetResource = adaptorsetResourceCategoryService.getAdaptorsetResourceCategoryByAdaptorsetIdResourcecategoryId(adaptorset.getAdaptorsetId(), resourceCat.getResourceCategoryId());
    	adaptorsetResourceCategoryService.remove(adaptorsetResource);
    }
    
    // update adaptors
    Map<String, Integer> adaptorSearchMap = new HashMap<String, Integer>();
    adaptorSearchMap.put("adaptorsetId", adaptorset.getAdaptorsetId());
    List<Adaptor> adaptorsInAdaptorset = adaptorService.findByMap(adaptorSearchMap);
    Map<String, Adaptor> oldAdaptors = new HashMap<String, Adaptor>();
    for (Adaptor adaptor : adaptorsInAdaptorset){
    	oldAdaptors.put(adaptor.getIName(), adaptor);
    }
    
    for (Adaptor adaptor : safeList(adaptorList)){
    	String adaptorKey = adaptor.getIName();
    	if (oldAdaptors.containsKey(adaptorKey)){
    		// adaptor exists
    		Adaptor old = oldAdaptors.get(adaptorKey);
    		boolean changed = false;
    		if (!old.getName().equals(adaptor.getName())){
    			old.setName(adaptor.getName());
    			changed = true;
    		}
    		if (!old.getSequence().equals(adaptor.getSequence())){
    			old.setSequence(adaptor.getSequence());
    			changed = true;
    		}
    		if (!old.getBarcodesequence().equals(adaptor.getBarcodesequence())){
    			old.setBarcodesequence(adaptor.getBarcodesequence());
    			changed = true;
    		}
    		if (old.getBarcodenumber().intValue() != adaptor.getBarcodenumber().intValue()){
    			old.setBarcodenumber(adaptor.getBarcodenumber());
    			changed = true;
    		}
    		if (old.getAdaptorsetId().intValue() != adaptorset.getAdaptorsetId().intValue()){
    			old.setAdaptorsetId(adaptorset.getAdaptorsetId());
    			changed = true;
    		}
    		if (changed)
    			adaptorService.save(old);
    		oldAdaptors.remove(adaptor.getIName());
    		
    		lastPosition = 0;
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
	    	        	adaptorMetaService.save(oldMeta);
	
	    	        oldAdaptorMetas.remove(oldMeta.getK()); // remove the meta from the old meta list as we're done with it
	    	        continue; 
	    	    }
	    	    // is new metadata
	    	    adaptorMeta.setAdaptorId(adaptor.getAdaptorId()); 
	    	    adaptorMetaService.save(adaptorMeta); 
    	    }

    	    // delete the left overs
    	    for (String adaptorMetaKey : oldAdaptorMetas.keySet()) {
    	    	AdaptorMeta adaptorMeta = oldAdaptorMetas.get(adaptorMetaKey); 
    	    	adaptorMetaService.remove(adaptorMeta); 
    	    	adaptorMetaService.flush(adaptorMeta); 
    	    }
    	} else {
    		// new adaptor
    		adaptor.setAdaptorsetId(adaptorset.getAdaptorsetId());
    		adaptor.setIsActive(1);
    		adaptorService.save(adaptor);
    		adaptor = adaptorService.getAdaptorByIName(iname); // refresh
    		for (AdaptorMeta adaptorMeta: safeList(adaptor.getAdaptorMeta()) ) {
    			adaptorMeta.setAdaptorId(adaptor.getAdaptorId()); 
	    	    adaptorMetaService.save(adaptorMeta);
    		}
    	}
    	// inactivate the leftovers
    	for (String adaptorIName : oldAdaptors.keySet()){
    		Adaptor adaptorToInactivate = adaptorService.getAdaptorByIName(adaptorIName);
    		adaptorToInactivate.setIsActive(0);
    		adaptorService.save(adaptorToInactivate);
    	}
    }
    	
    updateUiFields(); 
  }
}

