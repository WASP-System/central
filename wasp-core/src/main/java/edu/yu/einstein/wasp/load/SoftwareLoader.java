package edu.yu.einstein.wasp.load;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.load.service.SoftwareLoadService;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.SoftwareMeta;


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


public class SoftwareLoader extends WaspResourceLoader {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	

  @Autowired
  private SoftwareLoadService softwareLoadService;

  private ResourceType resourceType; 
  
  public void setResourceTypeByIName(String resourceTypeString) {
	  this.resourceType = softwareLoadService.getSoftwareTypeByIName(resourceTypeString);
  }
  
  public void setResourceType(ResourceType resourceType){
	  this.resourceType = resourceType;
  }

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

  @PostConstruct
  public void init() throws Exception {
	  
	  Assert.assertParameterNotNull(resourceType);
	  logger.debug("Initialized with resourceType: " + resourceType.toString());
	  
	softwareLoadService.update(resourceType, meta, iname, name, isActive);
	
    softwareLoadService.updateUiFields(uiFields); 
  }
}

