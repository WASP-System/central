package edu.yu.einstein.wasp.load;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import util.spring.PostInitialize;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.dao.ResourceLaneDao;
import edu.yu.einstein.wasp.dao.ResourceMetaDao;
import edu.yu.einstein.wasp.dao.TypeResourceDao;
import edu.yu.einstein.wasp.exception.NullResourceCategoryException;
import edu.yu.einstein.wasp.exception.NullTypeResourceException;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.ResourceLane;
import edu.yu.einstein.wasp.model.ResourceMeta;
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
public class ResourceLoadService extends WaspLoadService {

  @Autowired
  private ResourceDao resourceDao;

  @Autowired
  private ResourceLaneDao resourceLaneDao;

  @Autowired
  private ResourceMetaDao resourceMetaDao;

  @Autowired
  private TypeResourceDao typeResourceDao;
  
  @Autowired
  private ResourceCategoryDao resourceCategoryDao;

  private String resourceCategoryIName; 
  public void setResourceCategory(String resourceCategoryIName) { this.resourceCategoryIName = resourceCategoryIName; }

  private String resourceType; 
  public void setResourceType(String resourceType) {this.resourceType = resourceType; }

  private List<ResourceLane> cells; 
  public void setCell(List<ResourceLane> cell) {this.cells = cell; }

  private List<ResourceMeta> meta; 
  public void setMeta(List<ResourceMeta> meta) {this.meta = meta; }
  
  public void setMetaFromWrapper(MetaLoadWrapper metaLoadWrapper){
	  meta = metaLoadWrapper.getMeta(ResourceMeta.class);
  }

  @Override
  @Transactional
  @PostInitialize 
  public void postInitialize() {
    // skips component scanned  (if scanned in)
    if (name == null) { return; }
    
    Integer resourceCategoryId = resourceCategoryDao.getResourceCategoryByIName(resourceCategoryIName).getResourceCategoryId();
    if (resourceCategoryId == null){
    	throw new NullResourceCategoryException();
    }

    TypeResource typeResource = typeResourceDao.getTypeResourceByIName(resourceType); 
    if (typeResource == null){
    	throw new NullTypeResourceException();
    }

    Resource resource = resourceDao.getResourceByIName(iname); 

    // inserts or update workflow
    if (resource.getResourceId() == null) { 
      resource = new Resource();

      resource.setIName(iname);
      resource.setName(name);
      resource.setResourcecategoryId(resourceCategoryId);
      resource.setTypeResourceId(typeResource.getTypeResourceId());
      resource.setIsActive(1);

      resourceDao.save(resource); 

      // refreshes
      resource = resourceDao.getResourceByIName(iname); 

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
    	  resourceDao.save(resource); 
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
        	resourceMetaDao.save(old);

        oldResourceMetas.remove(old.getK()); // remove the meta from the old meta list as we're done with it
        continue; 
      }

      resourceMeta.setResourceId(resource.getResourceId()); 
      resourceMetaDao.save(resourceMeta); 
    }

    // delete the left overs
    for (String resourceMetaKey : oldResourceMetas.keySet()) {
      ResourceMeta resourceMeta = oldResourceMetas.get(resourceMetaKey); 
      resourceMetaDao.remove(resourceMeta); 
      resourceMetaDao.flush(resourceMeta); 
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
        	resourceLaneDao.save(old);
        }
        oldResourceLanes.remove(old.getIName());
        continue;
      }

      resourceCell.setResourceId(resource.getResourceId()); 
      resourceCell.setIsActive(1);
      resourceLaneDao.save(resourceCell); 
    }


    // inactivate the left overs (cannot delete due to foreign key constraints
    for (String resourceLaneKey : oldResourceLanes.keySet()) {
      ResourceLane resourceLane = oldResourceLanes.get(resourceLaneKey); 
      resourceLane.setIsActive(0); 
    }

    updateUiFields(); 
  }
}

