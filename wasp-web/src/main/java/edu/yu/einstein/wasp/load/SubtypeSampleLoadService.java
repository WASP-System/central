
package edu.yu.einstein.wasp.load;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import util.spring.PostInitialize;
import edu.yu.einstein.wasp.exception.NullResourceCategoryException;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.SubtypeSample;
import edu.yu.einstein.wasp.model.SubtypeSampleMeta;
import edu.yu.einstein.wasp.model.SubtypeSampleResourceCategory;
import edu.yu.einstein.wasp.model.TypeSample;
import edu.yu.einstein.wasp.service.ResourceCategoryService;
import edu.yu.einstein.wasp.service.SubtypeSampleMetaService;
import edu.yu.einstein.wasp.service.SubtypeSampleResourceCategoryService;
import edu.yu.einstein.wasp.service.SubtypeSampleService;
import edu.yu.einstein.wasp.service.TypeSampleService;


/**
 * update/inserts db copy of subtype sample from bean definition
 * takes in  properties
 *   - iname
 *   - name
 *   - uifields (List<UiFields)
 *   - sampleType (string to typesample iname table)
 *
 */

@Transactional
public class SubtypeSampleLoadService extends WaspLoadService {

  @Autowired
  private SubtypeSampleService subtypeSampleService;

  @Autowired
  private TypeSampleService typeSampleService;

  @Autowired
  private SubtypeSampleMetaService subtypeSampleMetaService;
  
  @Autowired
  private ResourceCategoryService resourceCategoryService;
  
  @Autowired
  private SubtypeSampleResourceCategoryService subtypeSampleResourceCategoryService;

  private String sampleType; 
  public void setSampleType(String sampleType) {this.sampleType = sampleType; }

  private List<SubtypeSampleMeta> meta;
  public void setMeta(List<SubtypeSampleMeta> subtypeSampleMeta) {this.meta = subtypeSampleMeta; }
  
  private List<String> compatibleResourcesByIName; 
  public void setCompatibleResourcesByIName(List<String> compatibleResourcesByIName) {this.compatibleResourcesByIName = compatibleResourcesByIName; }

  
  public SubtypeSampleLoadService(){}
  
  public SubtypeSampleLoadService(SubtypeSampleLoadService inheritFromLoadService){
	  super(inheritFromLoadService);
  }

  @Override
  @Transactional
  @PostInitialize 
  public void postInitialize() {
    // skips component scanned  (if scanned in)
    if (name == null) { return; }

    TypeSample typeSample = typeSampleService.getTypeSampleByIName(sampleType); 

    SubtypeSample subtypeSample = subtypeSampleService.getSubtypeSampleByIName(iname); 
    String areaList = StringUtils.join(getAreaListFromUiFields(this.uiFields), ",");
    // inserts or update subtypeSample
    if (subtypeSample.getSubtypeSampleId() == null) {
      subtypeSample = new SubtypeSample();

      subtypeSample.setIName(iname);
      subtypeSample.setName(name);
      subtypeSample.setTypeSampleId(typeSample.getTypeSampleId());
      subtypeSample.setAreaList(areaList);

      subtypeSampleService.save(subtypeSample); 

      // refreshes
      subtypeSample = subtypeSampleService.getSubtypeSampleByIName(iname); 

    } else {
      boolean changed = false;
      if (!subtypeSample.getName().equals(name)){
    	  subtypeSample.setName(name);
    	  changed = true;
      }
      if (subtypeSample.getTypeSampleId().intValue() != typeSample.getTypeSampleId().intValue()){
    	  subtypeSample.setTypeSampleId(typeSample.getTypeSampleId());
    	  changed = true;
      }
      if (!subtypeSample.getAreaList().equals(areaList)){
    	  logger.debug("ANDY:  '"+subtypeSample.getAreaList()+"' != '"+areaList);
    	  subtypeSample.setAreaList(areaList);
    	  changed = true;
      }
      if (changed)
    	  logger.debug("ANDY: saving: "+subtypeSample.toString());
    	  subtypeSampleService.save(subtypeSample); 
    }

    // sync metas
    int lastPosition = 0;
    Map<String, SubtypeSampleMeta> oldSubtypeSampleMetas  = new HashMap<String, SubtypeSampleMeta>();

    if (subtypeSample != null && subtypeSample.getSubtypeSampleMeta() != null) {
      for (SubtypeSampleMeta subtypeSampleMeta: safeList(subtypeSample.getSubtypeSampleMeta())) {
        oldSubtypeSampleMetas.put(subtypeSampleMeta.getK(), subtypeSampleMeta);
      }
    }

    if (meta != null) {
      for (SubtypeSampleMeta subtypeSampleMeta: safeList(meta) ) {
  
        // incremental position numbers.
  
        if ( subtypeSampleMeta.getPosition() == null ||
             subtypeSampleMeta.getPosition().intValue() <= lastPosition
          )  {
          subtypeSampleMeta.setPosition(lastPosition +1);
        }
        lastPosition = subtypeSampleMeta.getPosition().intValue();
        
        if (oldSubtypeSampleMetas.containsKey(subtypeSampleMeta.getK())) {
        	SubtypeSampleMeta old = oldSubtypeSampleMetas.get(subtypeSampleMeta.getK());
            boolean changed = false;
            if (!old.getV().equals(subtypeSampleMeta.getV())){
            	old.setV(subtypeSampleMeta.getV());
            	changed = true;
            }
            if (old.getPosition().intValue() != subtypeSampleMeta.getPosition()){
            	old.setPosition(subtypeSampleMeta.getPosition());
            	changed = true;
            }
            if (changed)
            	subtypeSampleMetaService.save(old);

            oldSubtypeSampleMetas.remove(old.getK()); // remove the meta from the old meta list as we're done with it
            continue; 
          }

        subtypeSampleMeta.setSubtypeSampleId(subtypeSample.getSubtypeSampleId());
        subtypeSampleMeta.setPosition(1);
        subtypeSampleMetaService.save(subtypeSampleMeta);
      }
    }

    // delete the left overs
    for (String subtypeSampleMetaKey : oldSubtypeSampleMetas.keySet()) {
      SubtypeSampleMeta subtypeSampleMeta = oldSubtypeSampleMetas.get(subtypeSampleMetaKey);
      subtypeSampleMetaService.remove(subtypeSampleMeta);
      subtypeSampleMetaService.flush(subtypeSampleMeta);
    }
    
    // sync subtypeSampleResourceCategories
    Map<String, SubtypeSampleResourceCategory> oldSubtypeSampleResourceCats  = new HashMap<String, SubtypeSampleResourceCategory>();
    for (SubtypeSampleResourceCategory subtypeSampleResourceCat: safeList(subtypeSample.getSubtypeSampleResourceCategory())) {
    	oldSubtypeSampleResourceCats.put(subtypeSampleResourceCat.getResourceCategory().getIName(), subtypeSampleResourceCat);
    } 
    
    for (String resourceIName: safeList(compatibleResourcesByIName)) {
    	if (oldSubtypeSampleResourceCats.containsKey(resourceIName)) {
    		oldSubtypeSampleResourceCats.remove(resourceIName);
    		continue;
    	}
    	ResourceCategory resourceCat = resourceCategoryService.getResourceCategoryByIName(resourceIName);
    	if (resourceCat.getResourceCategoryId() != null){
    		SubtypeSampleResourceCategory subtypeSampleResourceCategory = new SubtypeSampleResourceCategory();
    		subtypeSampleResourceCategory.setResourcecategoryId(resourceCat.getResourceCategoryId());
    		subtypeSampleResourceCategory.setSubtypeSampleId(subtypeSample.getSubtypeSampleId());
    		subtypeSampleResourceCategoryService.save(subtypeSampleResourceCategory);
    		oldSubtypeSampleResourceCats.remove(resourceIName);
    	} else {
    		throw new NullResourceCategoryException();
    	}
    }
    
    // remove the left overs
    for (String adaptorsetKey : oldSubtypeSampleResourceCats.keySet()) {
    	ResourceCategory resourceCat = resourceCategoryService.getResourceCategoryByIName(adaptorsetKey);
    	SubtypeSampleResourceCategory ssrc = subtypeSampleResourceCategoryService.getSubtypeSampleResourceCategoryBySubtypeSampleIdResourceCategoryId(subtypeSample.getSubtypeSampleId(), resourceCat.getResourceCategoryId());
    	subtypeSampleResourceCategoryService.remove(ssrc);
    }
    
    updateUiFields(); 

  }

}

