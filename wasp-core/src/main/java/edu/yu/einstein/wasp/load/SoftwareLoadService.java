package edu.yu.einstein.wasp.load;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import util.spring.PostInitialize;
import edu.yu.einstein.wasp.exception.NullTypeResourceException;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.SoftwareMeta;
import edu.yu.einstein.wasp.model.TypeResource;
import edu.yu.einstein.wasp.service.SoftwareMetaService;
import edu.yu.einstein.wasp.service.SoftwareService;
import edu.yu.einstein.wasp.service.TypeResourceService;


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
  private SoftwareService softwareService;

  @Autowired
  private SoftwareMetaService softwareMetaService;

  @Autowired
  private TypeResourceService typeResourceService;

  private String resourceType; 
  public void setResourceType(String resourceType) {this.resourceType = resourceType; }

  private List<SoftwareMeta> meta; 
  public void setMeta(List<SoftwareMeta> meta) {this.meta = meta; }

  @Override
  @Transactional
  @PostInitialize 
  public void postInitialize() {
    // skips component scanned  (if scanned in)
    if (name == null) { return; }

    TypeResource typeResource = typeResourceService.getTypeResourceByIName(resourceType); 
    if (typeResource == null){
    	throw new NullTypeResourceException();
    }

    Software software = softwareService.getSoftwareByIName(iname);

    // inserts or update workflow
    if (software.getSoftwareId() == null) { 
      software = new Software();

      software.setIName(iname);
      software.setName(name);
      software.setTypeResourceId(typeResource.getTypeResourceId());
      softwareService.save(software); 

      // refreshes
      software = softwareService.getSoftwareByIName(iname); 

    } else {
      boolean changed = false;	
      if (!software.getName().equals(name)){
    	  software.setName(name);
    	  changed = true;
      }
      if (changed)
    	  softwareService.save(software); 
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
        	softwareMetaService.save(old);

        oldSoftwareMetas.remove(old.getK()); // remove the meta from the old meta list as we're done with it
        continue; 
      }

      softwareMeta.setSoftwareId(software.getSoftwareId()); 
      softwareMetaService.save(softwareMeta); 
    }

    // delete the left overs
    for (String softwareMetaKey : oldSoftwareMetas.keySet()) {
      SoftwareMeta softwareMeta = oldSoftwareMetas.get(softwareMetaKey); 
      softwareMetaService.remove(softwareMeta); 
      softwareMetaService.flush(softwareMeta); 
    }

    updateUiFields(); 
  }
}

