package edu.yu.einstein.wasp.load;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import util.spring.PostInitialize;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.ResourceCellDao;
import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.dao.ResourceMetaDao;
import edu.yu.einstein.wasp.dao.ResourceTypeDao;
import edu.yu.einstein.wasp.exception.NullResourceCategoryException;
import edu.yu.einstein.wasp.exception.NullResourceTypeException;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.ResourceCell;
import edu.yu.einstein.wasp.model.ResourceMeta;
import edu.yu.einstein.wasp.model.ResourceType;


/**
 * update/inserts db copy of subtype sample from bean definition
 * takes in  properties
 *   - iname / internalname
 *   - name / label
 *   - uifields (List<UiFields>)
 *   - platform
 *   - meta (List<ResourceMeta>)
 *   - cell (List<ResourceCells>)
 *
 */

@Transactional
public class ResourceLoadService extends WaspLoadService {

  @Autowired
  private ResourceDao resourceDao;

  @Autowired
  private ResourceCellDao resourceCellDao;

  @Autowired
  private ResourceMetaDao resourceMetaDao;

  @Autowired
  private ResourceTypeDao resourceTypeDao;
  
  @Autowired
  private ResourceCategoryDao resourceCategoryDao;

  private String resourceCategoryIName; 
  public void setResourceCategory(String resourceCategoryIName) { this.resourceCategoryIName = resourceCategoryIName; }

  private String resourceTypeString; 
  public void setResourceType(String resourceTypeString) {this.resourceTypeString = resourceTypeString; }

  private List<ResourceCell> cells; 
  public void setCell(List<ResourceCell> cell) {this.cells = cell; }

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

    ResourceType resourceType = resourceTypeDao.getResourceTypeByIName(resourceTypeString); 
    if (resourceType == null){
    	throw new NullResourceTypeException();
    }

    Resource resource = resourceDao.getResourceByIName(iname); 

    // inserts or update workflow
    if (resource.getResourceId() == null) { 
      resource = new Resource();

      resource.setIName(iname);
      resource.setName(name);
      resource.setResourcecategoryId(resourceCategoryId);
      resource.setResourceTypeId(resourceType.getResourceTypeId());
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
      if (resource.getResourceTypeId().intValue() != resourceType.getResourceTypeId().intValue()){
    	  resource.setResourceTypeId(resourceType.getResourceTypeId());
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
    Map<String, ResourceCell> oldResourceCells  = new HashMap<String, ResourceCell>();
    for (ResourceCell resourceCell: safeList(resource.getResourceCell())) {
      oldResourceCells.put(resourceCell.getIName(), resourceCell);
    } 
    
    // sync 
    for (ResourceCell resourceCell: safeList(cells)) {
      if (oldResourceCells.containsKey(resourceCell.getIName())) {
        ResourceCell old = oldResourceCells.get(resourceCell.getIName()); 

        if (!old.getIName().equals(resourceCell.getIName())) { 	
        	old.setName(resourceCell.getName());
        	old.setIsActive(1);
        	resourceCellDao.save(old);
        }
        oldResourceCells.remove(old.getIName());
        continue;
      }

      resourceCell.setResourceId(resource.getResourceId()); 
      resourceCell.setIsActive(1);
      resourceCellDao.save(resourceCell); 
    }


    // inactivate the left overs (cannot delete due to foreign key constraints
    for (String resourceCellKey : oldResourceCells.keySet()) {
      ResourceCell resourceCell = oldResourceCells.get(resourceCellKey); 
      resourceCell.setIsActive(0); 
    }

    updateUiFields(); 
  }
}

