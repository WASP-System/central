package edu.yu.einstein.wasp.load.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.ResourceTypeDao;
import edu.yu.einstein.wasp.dao.SoftwareDao;
import edu.yu.einstein.wasp.dao.SoftwareMetaDao;
import edu.yu.einstein.wasp.exception.NullResourceTypeException;
import edu.yu.einstein.wasp.load.service.SoftwareLoadService;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.SoftwareMeta;

@Service
@Transactional("entityManager")
public class SoftwareLoadServiceImpl extends WaspLoadServiceImpl implements	SoftwareLoadService {
	
	@Autowired
	private SoftwareDao softwareDao;

	@Autowired
	private SoftwareMetaDao softwareMetaDao;

	@Autowired
	private ResourceTypeDao resourceTypeDao;
	
	private Software addOrUpdateSoftware(ResourceType resourceType, String iname, String name, Integer isActive){
		if (resourceType == null || resourceType.getIName() == null || resourceType.getIName().isEmpty()){
	    	throw new NullResourceTypeException();
	    }
	    if (resourceType.getResourceTypeId() == null){
	    	ResourceType existingResourceType = resourceTypeDao.getResourceTypeByIName(resourceType.getIName());
		    if (existingResourceType.getResourceTypeId() == null){
		    	// new
		    	resourceType.setIsActive(1);
		    	resourceType = resourceTypeDao.save(resourceType);
		    } else {
		    	// exists, so see if changed
		    	if (!existingResourceType.getName().equals(resourceType.getName())){
		    		existingResourceType.setName(resourceType.getName());
		    	}
		    	if (existingResourceType.getIsActive().intValue() == 0){
		    		existingResourceType.setIsActive(1);
			    }
		    	resourceType = existingResourceType;
		    }
	    }

	    Software software = softwareDao.getSoftwareByIName(iname);
	    
	    if (isActive == null)
	  	  isActive = 1;
	    
	    // inserts or update workflow
	    if (software.getSoftwareId() == null) { 
	      software = new Software();

	      software.setIName(iname);
	      software.setName(name);
	      software.setIsActive(isActive.intValue());
	      software.setResourceTypeId(resourceType.getResourceTypeId());
	      softwareDao.save(software); 

	      // refreshes
	      software = softwareDao.getSoftwareByIName(iname); 

	    } else {
	      boolean changed = false;	
	      if (!software.getName().equals(name)){
	    	  software.setName(name);
	    	  changed = true;
	      }
	      if (software.getIsActive().intValue() != isActive.intValue()){
	    	  software.setIsActive(isActive.intValue());
	    	  changed = true;
	      }
	      if (changed)
	    	  softwareDao.save(software); 
	    }
	    return software;
	}
	
	private void syncMetas(Software software, List<SoftwareMeta> meta){
		int lastPosition = 0;
	    Map<String, SoftwareMeta> oldSoftwareMetas  = new HashMap<String, SoftwareMeta>();
	    for (SoftwareMeta softwareMeta: safeList(software.getSoftwareMeta())) {
	    	oldSoftwareMetas.put(softwareMeta.getK(), softwareMeta);
	    } 
	    for (SoftwareMeta softwareMeta: safeList(meta) ) {

	      // incremental position numbers. 
	      if ( softwareMeta.getPosition() == 0 ||
	    		  softwareMeta.getPosition() <= lastPosition
	        )  {
	    	  softwareMeta.setPosition(lastPosition +1); 
	      }
	      lastPosition = softwareMeta.getPosition();

	      if (oldSoftwareMetas.containsKey(softwareMeta.getK())) {
	        SoftwareMeta old = oldSoftwareMetas.get(softwareMeta.getK());
	        boolean changed = false;
	        if (!old.getV().equals(softwareMeta.getV())){
	        	old.setV(softwareMeta.getV());
	        	changed = true;
	        }
	        if (old.getPosition().intValue() != softwareMeta.getPosition()){
	        	old.setPosition(softwareMeta.getPosition());
	        	changed = true;
	        }
	        if (changed)
	        	softwareMetaDao.save(old);

	        oldSoftwareMetas.remove(old.getK()); // remove the meta from the old meta list as we're done with it
	        continue; 
	      }

	      softwareMeta.setSoftwareId(software.getSoftwareId()); 
	      softwareMetaDao.save(softwareMeta); 
	    }

	    // delete the left overs
	    for (String softwareMetaKey : oldSoftwareMetas.keySet()) {
	      SoftwareMeta softwareMeta = oldSoftwareMetas.get(softwareMetaKey); 
	      softwareMetaDao.remove(softwareMeta); 
	      softwareMetaDao.flush(softwareMeta); 
	    }
	}

	@Override
	public void update(ResourceType resourceType, List<SoftwareMeta> meta, String iname, String name, Integer isActive){
		

		Software software = addOrUpdateSoftware(resourceType, iname, name, isActive);

		syncMetas(software, meta);
	    
	}
	
	@Override
	public ResourceType getSoftwareTypeByIName(String iname){
		ResourceType softwareType = resourceTypeDao.getResourceTypeByIName(iname); 
		if (softwareType.getResourceTypeId() == null){
			throw new NullResourceTypeException();
		}
		return softwareType;
	}

}
