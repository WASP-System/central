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
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.load.service.SoftwareLoadService;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.SoftwareMeta;
import edu.yu.einstein.wasp.software.SoftwarePackage;

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
	      if (software.getIName() == null || !software.getIName().equals(iname))
	    	  software.setIName(iname);
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
	    for (SoftwareMeta softwareMeta: meta ) {

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
        // The next block was commented out by Dubin; 10-07-2013 as it removes meta 
	    //that is added any time after the initial data upload
	    /*
	    for (String softwareMetaKey : oldSoftwareMetas.keySet()) {
	      SoftwareMeta softwareMeta = oldSoftwareMetas.get(softwareMetaKey); 
	      softwareMetaDao.remove(softwareMeta); 
	      softwareMetaDao.flush(softwareMeta); 
	    }
	    */
	}

	@Override
	public <T extends SoftwarePackage> T update(ResourceType resourceType, List<SoftwareMeta> meta, String iname, String name, String description, String version, List<SoftwarePackage> softwareDependencies, int isActive, Class<T> clazz){
		Assert.assertParameterNotNull(resourceType, "ResourceType cannot be null");
		Assert.assertParameterNotNull(iname, "iname cannot be null");
		Assert.assertParameterNotNull(name, "name cannot be null");
		Assert.assertParameterNotNull(description, "description cannot be null");
		Assert.assertParameterNotNull(clazz, "A class has not been specified");
		Software software = addOrUpdateSoftware(resourceType, iname, name, description, isActive);
		syncMetas(software, meta);
		T softwarePackage;
		try {
			logger.debug("instantiating new instance of type = " + clazz.getName());
			softwarePackage = clazz.newInstance();
			softwarePackage.setId(software.getId());
			softwarePackage.setIName(software.getIName());
			softwarePackage.setName(software.getName());
			softwarePackage.setDescription(software.getDescription());
			softwarePackage.setIsActive(software.getIsActive());
			softwarePackage.setResourceType(software.getResourceType());
			softwarePackage.setJobDraftSoftware(software.getJobDraftSoftware());
			softwarePackage.setJobSoftware(software.getJobSoftware());
			softwarePackage.setSoftwareMeta(software.getSoftwareMeta());
			softwarePackage.setWorkflowSoftware(software.getWorkflowSoftware());
			if (version != null)
				softwarePackage.setSoftwareVersion(version);
			softwarePackage.setSoftwareDependencies(softwareDependencies);
		} catch (Exception e) {
			logger.warn("Cannot create instance of " + clazz.getName());
			throw new WaspRuntimeException(e);
		}
		return softwarePackage;
	    
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
