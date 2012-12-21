package edu.yu.einstein.wasp.load.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.ResourceTypeDao;
import edu.yu.einstein.wasp.load.service.ResourceTypeLoadService;
import edu.yu.einstein.wasp.model.ResourceType;


@Service
@Transactional("entityManager")
public class ResourceTypeLoadServiceImpl extends WaspLoadServiceImpl implements ResourceTypeLoadService {
	
	  @Autowired
	  private ResourceTypeDao resourceTypeDao;

	  @Override
	  public void update(String iname, String name, String resourceTypeCategoryIname, Integer isActive){
	    
		  if (isActive == null)
		  	  isActive = 1;
		  
		 		  
		  ResourceType resourceType = resourceTypeDao.getResourceTypeByIName(iname); 
		    // inserts or update sampleSubtype
		    if (resourceType.getResourceTypeId() == null) {
		    	resourceType.setIName(iname);
		    	resourceType.setName(name);
		    	resourceType.setIsActive(isActive.intValue());
		    	resourceTypeDao.save(resourceType);
		    } else {
		      if (!resourceType.getName().equals(name)){
		    	  resourceType.setName(name);
		      }
		      if (resourceType.getIsActive().intValue() != isActive.intValue()){
		    	  resourceType.setIsActive(isActive.intValue());
		      }
		}
	}
}
