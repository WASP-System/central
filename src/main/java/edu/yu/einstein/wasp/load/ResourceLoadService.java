package edu.yu.einstein.wasp.load;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import util.spring.PostInitialize;
import edu.yu.einstein.wasp.exception.NullResourceCategoryException;
import edu.yu.einstein.wasp.exception.NullTypeResourceException;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.ResourceLane;
import edu.yu.einstein.wasp.model.ResourceMeta;
import edu.yu.einstein.wasp.model.TypeResource;
import edu.yu.einstein.wasp.service.ResourceCategoryService;
import edu.yu.einstein.wasp.service.ResourceLaneService;
import edu.yu.einstein.wasp.service.ResourceMetaService;
import edu.yu.einstein.wasp.service.ResourceService;
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
public class ResourceLoadService extends WaspLoadService {

  @Autowired
  private ResourceService resourceService;

  @Autowired
  private ResourceLaneService resourceLaneService;

  @Autowired
  private ResourceMetaService resourceMetaService;

  @Autowired
  private TypeResourceService typeResourceService;
  
  @Autowired
  private ResourceCategoryService resourceCategoryService;

  private String resourceCategoryIName; 
  public void setResourceCategory(String resourceCategoryIName) { this.resourceCategoryIName = resourceCategoryIName; }

  private String resourceType; 
  public void setResourceType(String resourceType) {this.resourceType = resourceType; }

  private List<ResourceLane> cells; 
  public void setCell(List<ResourceLane> cell) {this.cells = cell; }

  private List<ResourceMeta> meta; 
  public void setMeta(List<ResourceMeta> meta) {this.meta = meta; }

  @Override
  @Transactional
  @PostInitialize 
  public void postInitialize() {
    // skips component scanned  (if scanned in)
    if (name == null) { return; }
    
    Integer resourceCategoryId = resourceCategoryService.getResourceCategoryByIName(resourceCategoryIName).getResourceCategoryId();
    if (resourceCategoryId == null){
    	throw new NullResourceCategoryException();
    }

    TypeResource typeResource = typeResourceService.getTypeResourceByIName(resourceType); 
    if (typeResource == null){
    	throw new NullTypeResourceException();
    }

    Resource resource = resourceService.getResourceByIName(iname); 

    // inserts or update workflow
    if (resource.getResourceId() == null) { 
      resource = new Resource();

      resource.setIName(iname);
      resource.setName(name);
      resource.setResourcecategoryId(resourceCategoryId);
      resource.setTypeResourceId(typeResource.getTypeResourceId());
      resource.setIsActive(1);

      resourceService.save(resource); 

      // refreshes
      resource = resourceService.getResourceByIName(iname); 

    } else {
      boolean changed = false;	
      if (!resource.getName().equals(name)){
    	  resource.setName(name);
    	  changed = true;
      }
      if (resource.getResourcecategoryId().intValue() != resourceCategoryId.intValue()){
    	  resource.setResourcecategoryId(resourceCategoryId);
    	  changed = true;
      }
      if (resource.getTypeResourceId().intValue() != typeResource.getTypeResourceId().intValue()){
    	  resource.setTypeResourceId(typeResource.getTypeResourceId());
    	  changed = true;
      }
      if (changed)
    	  resourceService.save(resource); 
    }



    // sync metas
    int lastPosition = 0;
    Map<String, ResourceMeta> oldResourceMetas  = new HashMap<String, ResourceMeta>();
    for (ResourceMeta resourceMeta: safeList(resource.getResourceMeta())) {
      oldResourceMetas.put(resourceMeta.getK(), resourceMeta);
    } 
    for (ResourceMeta resourceMeta: safeList(meta) ) {

      // incremental position numbers. 
      if ( resourceMeta.getPosition() == 0 ||
           resourceMeta.getPosition() <= lastPosition
        )  {
        resourceMeta.setPosition(lastPosition +1); 
      }
      lastPosition = resourceMeta.getPosition();

      if (oldResourceMetas.containsKey(resourceMeta.getK())) {
        ResourceMeta old = oldResourceMetas.get(resourceMeta.getK());
        boolean changed = false;
        if (!old.getV().equals(resourceMeta.getV())){
        	old.setV(resourceMeta.getV());
        	changed = true;
        }
        if (old.getPosition().intValue() != resourceMeta.getPosition()){
        	old.setPosition(resourceMeta.getPosition());
        	changed = true;
        }
        if (changed)
        	resourceMetaService.save(old);

        oldResourceMetas.remove(old.getK()); // remove the meta from the old meta list as we're done with it
        continue; 
      }

      resourceMeta.setResourceId(resource.getResourceId()); 
      resourceMetaService.save(resourceMeta); 
    }

    // delete the left overs
    for (String resourceMetaKey : oldResourceMetas.keySet()) {
      ResourceMeta resourceMeta = oldResourceMetas.get(resourceMetaKey); 
      resourceMetaService.remove(resourceMeta); 
      resourceMetaService.flush(resourceMeta); 
    }


    lastPosition = 0;
    Map<String, ResourceLane> oldResourceLanes  = new HashMap<String, ResourceLane>();
    for (ResourceLane resourceCell: safeList(resource.getResourceLane())) {
      oldResourceLanes.put(resourceCell.getIName(), resourceCell);
    } 
    
    // sync 
    for (ResourceLane resourceCell: safeList(cells)) {
      if (oldResourceLanes.containsKey(resourceCell.getIName())) {
        ResourceLane old = oldResourceLanes.get(resourceCell.getIName()); 

        if (!old.getIName().equals(resourceCell.getIName())) { 	
        	old.setName(resourceCell.getName());
        	old.setIsActive(1);
        	resourceLaneService.save(old);
        }
        oldResourceLanes.remove(old.getIName());
        continue;
      }

      resourceCell.setResourceId(resource.getResourceId()); 
      resourceCell.setIsActive(1);
      resourceLaneService.save(resourceCell); 
    }


    // inactivate the left overs (cannot delete due to foreign key constraints
    for (String resourceLaneKey : oldResourceLanes.keySet()) {
      ResourceLane resourceLane = oldResourceLanes.get(resourceLaneKey); 
      resourceLane.setIsActive(0); 
    }

    updateUiFields(iname, uiFields); 
  }
}

