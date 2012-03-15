package edu.yu.einstein.wasp.load;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import util.spring.PostInitialize;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.ResourceCategoryMetaDao;
import edu.yu.einstein.wasp.dao.TypeResourceDao;
import edu.yu.einstein.wasp.exception.NullTypeResourceException;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.ResourceCategoryMeta;
import edu.yu.einstein.wasp.model.TypeResource;


/**
 * update/inserts db copy of subtype sample from bean definition
 * takes in  properties
 *   - iname / internalname
 *   - name / label
 *   - uifields (List<UiFields>)
 *   - platform
 *   - meta (List<ResourceMeta>)
 *   - cell (List<ResourceLanes>)
 *
 */

@Transactional
public class ResourceCategoryLoadService extends WaspLoadService {

  @Autowired
  private ResourceCategoryDao resourceCategoryDao;

  @Autowired
  private ResourceCategoryMetaDao resourceCategoryMetaDao;

  @Autowired
  private TypeResourceDao typeResourceDao;

  private String resourceType; 
  public void setResourceType(String resourceType) {this.resourceType = resourceType; }

  private List<ResourceCategoryMeta> meta; 
  public void setMeta(List<ResourceCategoryMeta> meta) {this.meta = meta; }
  
  public void setMetaFromWrapper(MetaLoadWrapper metaLoadWrapper){
	  meta = metaLoadWrapper.getMeta(ResourceCategoryMeta.class);
  }
  
  private Integer isActive;
  
  public Integer getIsActive() {
	return isActive;
  }
	
  public void setIsActive(Integer isActive) {
	this.isActive = isActive;
  }

@Override
  @Transactional
  @PostInitialize 
  public void postInitialize() {
    // skips component scanned  (if scanned in)
    if (name == null) { return; }

    TypeResource typeResource = typeResourceDao.getTypeResourceByIName(resourceType); 
    if (typeResource == null){
    	throw new NullTypeResourceException();
    }

    ResourceCategory resourceCat = resourceCategoryDao.getResourceCategoryByIName(iname);
    
    if (isActive == null)
    	  isActive = 1;

    // inserts or update workflow
    if (resourceCat.getResourceCategoryId() == null) { 
      resourceCat = new ResourceCategory();

      resourceCat.setIName(iname);
      resourceCat.setName(name);
      resourceCat.setIsActive(isActive.intValue());
      resourceCat.setTypeResourceId(typeResource.getTypeResourceId());
      resourceCategoryDao.save(resourceCat); 

      // refreshes
      resourceCat = resourceCategoryDao.getResourceCategoryByIName(iname); 

    } else {
      boolean changed = false;	
      if (!resourceCat.getName().equals(name)){
    	  resourceCat.setName(name);
    	  changed = true;
      }
      if (resourceCat.getIsActive().intValue() != isActive.intValue()){
    	  resourceCat.setIsActive(isActive.intValue());
    	  changed = true;
      }
      if (changed)
    	  resourceCategoryDao.save(resourceCat); 
    }



    // sync metas
    int lastPosition = 0;
    Map<String, ResourceCategoryMeta> oldResourceCatMetas  = new HashMap<String, ResourceCategoryMeta>();
    for (ResourceCategoryMeta resourceCatMeta: safeList(resourceCat.getResourceCategoryMeta())) {
    	oldResourceCatMetas.put(resourceCatMeta.getK(), resourceCatMeta);
    } 
    for (ResourceCategoryMeta resourceCatMeta: safeList(meta) ) {

      // incremental position numbers. 
      if ( resourceCatMeta.getPosition() == 0 ||
    		  resourceCatMeta.getPosition() <= lastPosition
        )  {
    	  resourceCatMeta.setPosition(lastPosition +1); 
      }
      lastPosition = resourceCatMeta.getPosition();

      if (oldResourceCatMetas.containsKey(resourceCatMeta.getK())) {
        ResourceCategoryMeta old = oldResourceCatMetas.get(resourceCatMeta.getK());
        boolean changed = false;
        if (!old.getV().equals(resourceCatMeta.getV())){
        	old.setV(resourceCatMeta.getV());
        	changed = true;
        }
        if (old.getPosition().intValue() != resourceCatMeta.getPosition()){
        	old.setPosition(resourceCatMeta.getPosition());
        	changed = true;
        }
        if (changed)
        	resourceCategoryMetaDao.save(old);

        oldResourceCatMetas.remove(old.getK()); // remove the meta from the old meta list as we're done with it
        continue; 
      }

      resourceCatMeta.setResourcecategoryId(resourceCat.getResourceCategoryId()); 
      resourceCategoryMetaDao.save(resourceCatMeta); 
    }

    // delete the left overs
    for (String resourceMetaKey : oldResourceCatMetas.keySet()) {
      ResourceCategoryMeta resourceCatMeta = oldResourceCatMetas.get(resourceMetaKey); 
      resourceCategoryMetaDao.remove(resourceCatMeta); 
      resourceCategoryMetaDao.flush(resourceCatMeta); 
    }

    updateUiFields(); 
  }
}

