package edu.yu.einstein.wasp.load;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.load.service.ResourceCategoryLoadService;
import edu.yu.einstein.wasp.model.ResourceCategoryMeta;


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

@Component
public class ResourceCategoryLoader extends WaspLoader implements InitializingBean {

  @Autowired
  ResourceCategoryLoadService resourceCategoryLoadService;

  private String resourceTypeString; 
  public void setResourceType(String resourceTypeString) {this.resourceTypeString = resourceTypeString; }

  private List<ResourceCategoryMeta> meta; 
  public void setMeta(List<ResourceCategoryMeta> meta) {this.meta = meta; }
  
  public void setMetaFromWrapper(MetaLoadWrapper metaLoadWrapper){
	  meta = metaLoadWrapper.getMeta(ResourceCategoryMeta.class);
  }
  
  private Integer isActive;
  
  public Integer getIsActive() {
	return isActive;
  }
	
  public void setIsActive(Integer isActive) {
	this.isActive = isActive;
  }

  @Override 
  public void afterPropertiesSet() throws Exception {
	  resourceCategoryLoadService.update(meta, resourceTypeString, iname, name, isActive);

	  resourceCategoryLoadService.updateUiFields(uiFields); 
  }
}

