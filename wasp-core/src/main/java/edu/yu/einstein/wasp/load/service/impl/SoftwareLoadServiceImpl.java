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

/**
 * 
 * @author asmclellan
 *
 */
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
	
	private Software addOrUpdateSoftware(ResourceType resourceType, String iname, String name, String description, int isActive){
		Assert.assertParameterNotNull(resourceType, "ResourceType cannot be null");
		Assert.assertParameterNotNull(iname, "iname cannot be null");
		Assert.assertParameterNotNull(name, "name cannot be null");
		Assert.assertParameterNotNull(description, "description cannot be null");
		if (resourceType == null || resourceType.getIName() == null || resourceType.getIName().isEmpty()){
	    	throw new NullResourceTypeException();
	    }
			    
	    Software software = softwareDao.getSoftwareByIName(iname);
	    	    
	    // inserts or update workflow
	    if (software.getId() == null) { 
	    	software = new Software();

	    	software.setIName(iname);
	    	software.setName(name);
	    	software.setDescription(description);
	    	software.setIsActive(isActive);
	    	software.setResourceTypeId(resourceType.getId());
	    	software = softwareDao.save(software);
	    } else {
	      if (software.getName() == null || !software.getName().equals(name))
	    	  software.setName(name);
	      if (software.getDescription() == null || !software.getDescription().equals(description))
	    	  software.setDescription(description);
	      if (software.getResourceTypeId() != resourceType.getId())
	    	  software.setResourceTypeId(resourceType.getId());
	      if (software.getIsActive() == null || software.getIsActive().intValue() != isActive)
	    	  software.setIsActive(isActive);
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

	      softwareMeta.setSoftwareId(software.getId()); 
	      softwareMetaDao.save(softwareMeta); 
	    }

	    // delete the left overs
	    for (String softwareMetaKey : oldSoftwareMetas.keySet()) {
	      SoftwareMeta softwareMeta = oldSoftwareMetas.get(softwareMetaKey); 
	      softwareMetaDao.remove(softwareMeta); 
	      softwareMetaDao.flush(softwareMeta); 
	    }
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Software> T update(ResourceType resourceType, List<SoftwareMeta> meta, String iname, String name, String description, int isActive, Class<T> clazz){
		Software software = addOrUpdateSoftware(resourceType, iname, name, description, isActive);
		syncMetas(software, meta);
		if (clazz.getName().equals(software.getClass().getName()))
	    	return (T) software;
	    T softwareSpecial;
		try {
			softwareSpecial = clazz.newInstance();
			softwareSpecial.setId(software.getId());
			softwareSpecial.setIName(software.getIName());
			softwareSpecial.setName(software.getName());
			softwareSpecial.setIsActive(software.getIsActive());
			softwareSpecial.setResourceType(software.getResourceType());
			softwareSpecial.setJobDraftSoftware(software.getJobDraftSoftware());
			softwareSpecial.setJobSoftware(software.getJobSoftware());
			softwareSpecial.setSoftwareMeta(software.getSoftwareMeta());
			softwareSpecial.setWorkflowSoftware(software.getWorkflowSoftware());
		} catch (Exception e) {
			logger.warn("Cannot create instance of " + clazz.getName() + ". Going to return as a Software object");
			return (T) software;
		}
		return softwareSpecial;
	    
	}
	
	@Override
	public ResourceType getSoftwareTypeByIName(String iname){
		ResourceType softwareType = resourceTypeDao.getResourceTypeByIName(iname); 
		if (softwareType.getId() == null){
			throw new NullResourceTypeException();
		}
		return softwareType;
	}

}
