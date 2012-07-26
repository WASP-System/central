package edu.yu.einstein.wasp.load;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.load.service.ResourceLoadService;
import edu.yu.einstein.wasp.model.ResourceCell;
import edu.yu.einstein.wasp.model.ResourceMeta;


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
public class ResourceLoader extends WaspLoader implements InitializingBean {

  @Autowired
  ResourceLoadService resourceLoadService;

  private String resourceCategoryIName; 
  public void setResourceCategory(String resourceCategoryIName) { this.resourceCategoryIName = resourceCategoryIName; }

  private String resourceTypeString; 
  public void setResourceType(String resourceTypeString) {this.resourceTypeString = resourceTypeString; }

  private List<ResourceCell> cells; 
  public void setCell(List<ResourceCell> cell) {this.cells = cell; }

  private List<ResourceMeta> meta; 
  public void setMeta(List<ResourceMeta> meta) {this.meta = meta; }
  
  public void setMetaFromWrapper(MetaLoadWrapper metaLoadWrapper){
	  meta = metaLoadWrapper.getMeta(ResourceMeta.class);
  }



  @Override 
  public void afterPropertiesSet() throws Exception {
 
	  resourceLoadService.update(meta, resourceCategoryIName, resourceTypeString, cells, iname, name);
	  resourceLoadService.updateUiFields(uiFields); 
  }
}

