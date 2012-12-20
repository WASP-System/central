package edu.yu.einstein.wasp.load.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.ResourceCellDao;
import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.dao.ResourceMetaDao;
import edu.yu.einstein.wasp.dao.ResourceTypeDao;
import edu.yu.einstein.wasp.exception.NullResourceCategoryException;
import edu.yu.einstein.wasp.exception.NullResourceTypeException;
import edu.yu.einstein.wasp.load.service.ResourceLoadService;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.ResourceCell;
import edu.yu.einstein.wasp.model.ResourceMeta;
import edu.yu.einstein.wasp.model.ResourceType;

@Service
@Transactional("entityManager")
public class ResourceLoadServiceImpl extends WaspLoadServiceImpl implements	ResourceLoadService {
	
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
	
	private Resource addOrUpdateResourceCategory(String resourceCategoryIName, String resourceTypeString, String iname, String name){
		Integer resourceCategoryId = resourceCategoryDao.getResourceCategoryByIName(resourceCategoryIName).getResourceCategoryId();
	    if (resourceCategoryId == null){
	    	throw new NullResourceCategoryException("ResourceCategoryId is null for '"+resourceCategoryIName+"'");
	    }

	    ResourceType resourceType = resourceTypeDao.getResourceTypeByIName(resourceTypeString); 
	    if (resourceType.getResourceTypeId() == null){
	    	throw new NullResourceTypeException("No resource type returned for '"+resourceTypeString+"'");
	    }
	    Resource resource = resourceDao.getResourceByIName(iname); 

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
		return resource;
	}
	
	private void syncResourceCategoryMeta(Resource resource, List<ResourceMeta> meta){
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

	}
	
	private void syncResourceCells(Resource resource, List<ResourceCell> cells){
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
	}
	
	@Override
	public void update(List<ResourceMeta> meta, String resourceCategoryIName, String resourceTypeString, List<ResourceCell> cells, String iname, String name){
		
		Resource resource = addOrUpdateResourceCategory(resourceCategoryIName, resourceTypeString, iname, name);
		
		syncResourceCategoryMeta(resource, meta);
		
		syncResourceCells(resource, cells);
	    
	}

}
