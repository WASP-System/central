package edu.yu.einstein.wasp.load;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import util.spring.PostInitialize;
import edu.yu.einstein.wasp.dao.SoftwareDao;
import edu.yu.einstein.wasp.dao.SoftwareMetaDao;
import edu.yu.einstein.wasp.dao.TypeResourceDao;
import edu.yu.einstein.wasp.exception.NullTypeResourceException;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.SoftwareMeta;
import edu.yu.einstein.wasp.model.TypeResource;


/**
 * update/inserts db copy of subtype sample from bean definition
 * takes in  properties
 *   - iname / internalname
 *   - name / label
 *   - resourceType
 *   - uifields (List<UiFields>)
 *   - platform
 *   - meta (List<SoftwareMeta>)
 *
 */

@Transactional
public class SoftwareLoadService extends WaspLoadService {

  @Autowired
  private SoftwareDao softwareDao;

  @Autowired
  private SoftwareMetaDao softwareMetaDao;

  @Autowired
  private TypeResourceDao typeResourceDao;

  private String resourceType; 
  public void setResourceType(String resourceType) {this.resourceType = resourceType; }

  private List<SoftwareMeta> meta; 
  public void setMeta(List<SoftwareMeta> meta) {this.meta = meta; }
  
  public void setMetaFromWrapper(MetaLoadWrapper metaLoadWrapper){
	  meta = metaLoadWrapper.getMeta(SoftwareMeta.class);
  }
  
  private Integer isActive;
  
  public Integer getIsActive() {
	return isActive;
  }
	
  public void setIsActive(Integer isActive) {
	this.isActive = isActive;
  }

  @Override
  @Transactional
  @PostInitialize 
  public void postInitialize() {
    // skips component scanned  (if scanned in)
    if (name == null) { return; }

    TypeResource typeResource = typeResourceDao.getTypeResourceByIName(resourceType); 
    if (typeResource == null){
    	throw new NullTypeResourceException();
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
      software.setTypeResourceId(typeResource.getTypeResourceId());
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



    // sync metas
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

    updateUiFields(); 
  }
}

