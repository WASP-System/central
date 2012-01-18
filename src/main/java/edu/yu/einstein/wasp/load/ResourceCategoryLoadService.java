package edu.yu.einstein.wasp.load;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import util.spring.PostInitialize;
import edu.yu.einstein.wasp.exception.NullTypeResourceException;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.ResourceCategoryMeta;
import edu.yu.einstein.wasp.model.TypeResource;
import edu.yu.einstein.wasp.service.ResourceCategoryMetaService;
import edu.yu.einstein.wasp.service.ResourceCategoryService;
import edu.yu.einstein.wasp.service.TypeResourceService;


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
  private ResourceCategoryService resourceCategoryService;

  @Autowired
  private ResourceCategoryMetaService resourceCategoryMetaService;

  @Autowired
  private TypeResourceService typeResourceService;

  private String resourceType; 
  public void setResourceType(String resourceType) {this.resourceType = resourceType; }

  private List<ResourceCategoryMeta> meta; 
  public void setMeta(List<ResourceCategoryMeta> meta) {this.meta = meta; }

  @Override
  @Transactional
  @PostInitialize 
  public void postInitialize() {
    // skips component scanned  (if scanned in)
    if (name == null) { return; }

    TypeResource typeResource = typeResourceService.getTypeResourceByIName(resourceType); 
    if (typeResource == null){
    	throw new NullTypeResourceException();
    }

    ResourceCategory resourceCat = resourceCategoryService.getResourceCategoryByIName(iname);

    // inserts or update workflow
    if (resourceCat.getResourceCategoryId() == null) { 
      resourceCat = new ResourceCategory();

      resourceCat.setIName(iname);
      resourceCat.setName(name);
      resourceCategoryService.save(resourceCat); 

      // refreshes
      resourceCat = resourceCategoryService.getResourceCategoryByIName(iname); 

    } else {
      boolean changed = false;	
      if (!resourceCat.getName().equals(name)){
    	  resourceCat.setName(name);
    	  changed = true;
      }
      if (changed)
    	  resourceCategoryService.save(resourceCat); 
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
        	resourceCategoryMetaService.save(old);

        oldResourceCatMetas.remove(old.getK()); // remove the meta from the old meta list as we're done with it
        continue; 
      }

      resourceCatMeta.setResourcecategoryId(resourceCat.getResourceCategoryId()); 
      resourceCategoryMetaService.save(resourceCatMeta); 
    }

    // delete the left overs
    for (String resourceMetaKey : oldResourceCatMetas.keySet()) {
      ResourceCategoryMeta resourceCatMeta = oldResourceCatMetas.get(resourceMetaKey); 
      resourceCategoryMetaService.remove(resourceCatMeta); 
      resourceCategoryMetaService.flush(resourceCatMeta); 
    }

    updateUiFields(iname, uiFields); 
  }
}

