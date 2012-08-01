package edu.yu.einstein.wasp.load;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.load.service.AdaptorsetLoadService;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.AdaptorsetMeta;


/**
 * update/inserts adaptorset from bean definition
 * takes in  properties
 *   - iname / internalname
 *   - name / label
 *   - sampleTypeIName / sampleType iName
 *   - compatibleResourcesByIName (List<String> iName of Resources)
 *   - adaptorList (List<Adaptor>)

 */


public class AdaptorsetLoader extends WaspLoader {

  @Autowired
  private AdaptorsetLoadService adaptorsetLoadService;

   
  private String sampleTypeIName;
  public void setSampleTypeIName(String sampleTypeIName){ this.sampleTypeIName = sampleTypeIName; }
  
  private List<AdaptorsetMeta>  meta;
  public void setMeta(List<AdaptorsetMeta> meta){ this.meta = meta; }
  
  public void setMetaFromWrapper(MetaLoadWrapper metaLoadWrapper){
	  meta = metaLoadWrapper.getMeta(AdaptorsetMeta.class);
  }

  private List<String> compatibleResourcesByIName; 
  public void setCompatibleResourcesByIName(List<String> compatibleResourcesByIName) {this.compatibleResourcesByIName = compatibleResourcesByIName; }

  private List<Adaptor> adaptorList; 
  public void setAdaptorList(List<Adaptor> adaptorList) {this.adaptorList = adaptorList; }

  private Integer isActive;
  
  public Integer getIsActive() {
	return isActive;
  }
	
  public void setIsActive(Integer isActive) {
	this.isActive = isActive;
  }

  @PostConstruct 
  public void init() throws Exception {
	  adaptorsetLoadService.update(meta, adaptorList, sampleTypeIName, iname, name, isActive, compatibleResourcesByIName);
	  adaptorsetLoadService.updateUiFields(this.uiFields); 
  }
}

