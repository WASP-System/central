package edu.yu.einstein.wasp.load.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
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
	
	protected  Logger logger = LoggerFactory.getLogger(SoftwareLoadServiceImpl.class);
	
	@Autowired
	private SoftwareDao softwareDao;

	@Autowired
	private SoftwareMetaDao softwareMetaDao;

	@Autowired
	private ResourceTypeDao resourceTypeDao;
	
	private <T extends Software> T addOrUpdateSoftware(ResourceType resourceType, String iname, String name, int isActive, Class<T> clazz){
		Assert.assertParameterNotNull(resourceType, "ResourceType cannot be null");
		Assert.assertParameterNotNull(iname, "iname Cannot be null");
		Assert.assertParameterNotNull(name, "name Cannot be null");
		if (resourceType == null || resourceType.getIName() == null || resourceType.getIName().isEmpty()){
	    	throw new NullResourceTypeException();
	    }
			    
	    Software baseSoftware = softwareDao.getSoftwareByIName(iname);
	    
	    	    
	    // inserts or update workflow
	    if (baseSoftware.getSoftwareId() == null) { 
	    	baseSoftware = new Software();

	    	baseSoftware.setIName(iname);
	    	baseSoftware.setName(name);
	    	baseSoftware.setIsActive(isActive);
	    	baseSoftware.setResourceTypeId(resourceType.getResourceTypeId());
	      softwareDao.save(baseSoftware); 

	      // refreshes
	      baseSoftware = softwareDao.getSoftwareByIName(iname); 

	    } else {
	      boolean changed = false;	
	      if (!baseSoftware.getName().equals(name)){
	    	  baseSoftware.setName(name);
	    	  changed = true;
	      }
	      if (baseSoftware.getResourceTypeId() != resourceType.getResourceTypeId()){
	    	  baseSoftware.setResourceTypeId(resourceType.getResourceTypeId());
	    	  changed = true;
	      }
	      if (baseSoftware.getIsActive().intValue() != isActive){
	    	  baseSoftware.setIsActive(isActive);
	    	  changed = true;
	      }
	      if (changed)
	    	  softwareDao.save(baseSoftware); 
	    }
	    if (clazz.getName().equals(baseSoftware.getClass().getName()))
	    	return (T) baseSoftware;
	    T software;
		try {
			software = clazz.newInstance();
			software.setSoftwareId(baseSoftware.getSoftwareId());
			software.setIName(iname);
			software.setName(name);
			software.setIsActive(isActive);
			software.setResourceTypeId(resourceType.getResourceTypeId());
		} catch (Exception e) {
			logger.warn("Cannot create instance of " + clazz.getName() + ". Going to return as a Software object");
			return (T) baseSoftware;
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
	public <T extends Software> T update(ResourceType resourceType, List<SoftwareMeta> meta, String iname, String name, Integer isActive, Class<T> clazz){
		T software = addOrUpdateSoftware(resourceType, iname, name, isActive, clazz);
		syncMetas(software, meta);
		return software;
	    
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
