
package edu.yu.einstein.wasp.load;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import util.spring.PostInitialize;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeMetaDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeResourceCategoryDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.exception.NullResourceCategoryException;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleSubtypeMeta;
import edu.yu.einstein.wasp.model.SampleSubtypeResourceCategory;
import edu.yu.einstein.wasp.model.SampleType;


/**
 * update/inserts db copy of subtype sample from bean definition
 * takes in  properties
 *   - iname
 *   - name
 *   - uifields (List<UiFields)
 *   - sampleType (string to sampletype iname table)
 *
 */

@Transactional
public class SampleSubtypeLoadService extends WaspLoadService {

  @Autowired
  private SampleSubtypeDao sampleSubtypeDao;

  @Autowired
  private SampleTypeDao sampleTypeDao;

  @Autowired
  private SampleSubtypeMetaDao sampleSubtypeMetaDao;
  
  @Autowired
  private ResourceCategoryDao resourceCategoryDao;
  
  @Autowired
  private SampleSubtypeResourceCategoryDao sampleSubtypeResourceCategoryDao;

  private String sampleTypeString; 
  public void setSampleType(String sampleTypeString) {this.sampleTypeString = sampleTypeString; }

  private List<SampleSubtypeMeta> meta;
  public void setMeta(List<SampleSubtypeMeta> sampleSubtypeMeta) {this.meta = sampleSubtypeMeta; }
  
  public void setMetaFromWrapper(MetaLoadWrapper metaLoadWrapper){
	  meta = metaLoadWrapper.getMeta(SampleSubtypeMeta.class);
  }
  
  private Integer isActive;
  
  public Integer getIsActive() {
	return isActive;
  }
	
  public void setIsActive(Integer isActive) {
	this.isActive = isActive;
  }
  
  private List<String> compatibleResourcesByIName; 
  public void setCompatibleResourcesByIName(List<String> compatibleResourcesByIName) {this.compatibleResourcesByIName = compatibleResourcesByIName; }

  
  public SampleSubtypeLoadService(){}
  
  public SampleSubtypeLoadService(SampleSubtypeLoadService inheritFromLoadService){
	  super(inheritFromLoadService);
  }

  @Override
  @Transactional
  @PostInitialize 
  public void postInitialize() {
    // skips component scanned  (if scanned in)
    if (name == null) { return; }

    SampleType sampleType = sampleTypeDao.getSampleTypeByIName(sampleTypeString); 

    if (isActive == null)
  	  isActive = 1;
    
    SampleSubtype sampleSubtype = sampleSubtypeDao.getSampleSubtypeByIName(iname); 
    String areaList = StringUtils.join(getAreaListFromUiFields(this.uiFields), ",");
    // inserts or update sampleSubtype
    if (sampleSubtype.getSampleSubtypeId() == null) {
      sampleSubtype = new SampleSubtype();

      sampleSubtype.setIName(iname);
      sampleSubtype.setName(name);
      sampleSubtype.setIsActive(isActive.intValue());
      sampleSubtype.setSampleTypeId(sampleType.getSampleTypeId());
      sampleSubtype.setAreaList(areaList);

      sampleSubtypeDao.save(sampleSubtype); 

      // refreshes
      sampleSubtype = sampleSubtypeDao.getSampleSubtypeByIName(iname); 

    } else {
      boolean changed = false;
      if (!sampleSubtype.getName().equals(name)){
    	  sampleSubtype.setName(name);
    	  changed = true;
      }
      if (sampleSubtype.getSampleTypeId().intValue() != sampleType.getSampleTypeId().intValue()){
    	  sampleSubtype.setSampleTypeId(sampleType.getSampleTypeId());
    	  changed = true;
      }
      if (!sampleSubtype.getAreaList().equals(areaList)){
    	  sampleSubtype.setAreaList(areaList);
    	  changed = true;
      }
      if (sampleSubtype.getIsActive().intValue() != isActive.intValue()){
    	  sampleSubtype.setIsActive(isActive.intValue());
    	  changed = true;
      }
      if (changed)
    	  sampleSubtypeDao.save(sampleSubtype); 
    }

    // sync metas
    int lastPosition = 0;
    Map<String, SampleSubtypeMeta> oldSampleSubtypeMetas  = new HashMap<String, SampleSubtypeMeta>();

    if (sampleSubtype != null && sampleSubtype.getSampleSubtypeMeta() != null) {
      for (SampleSubtypeMeta sampleSubtypeMeta: safeList(sampleSubtype.getSampleSubtypeMeta())) {
        oldSampleSubtypeMetas.put(sampleSubtypeMeta.getK(), sampleSubtypeMeta);
      }
    }

    if (meta != null) {
      for (SampleSubtypeMeta sampleSubtypeMeta: safeList(meta) ) {
  
        // incremental position numbers.
  
        if ( sampleSubtypeMeta.getPosition() == null ||
             sampleSubtypeMeta.getPosition().intValue() <= lastPosition
          )  {
          sampleSubtypeMeta.setPosition(lastPosition +1);
        }
        lastPosition = sampleSubtypeMeta.getPosition().intValue();
        
        if (oldSampleSubtypeMetas.containsKey(sampleSubtypeMeta.getK())) {
        	SampleSubtypeMeta old = oldSampleSubtypeMetas.get(sampleSubtypeMeta.getK());
            boolean changed = false;
            if (!old.getV().equals(sampleSubtypeMeta.getV())){
            	old.setV(sampleSubtypeMeta.getV());
            	changed = true;
            }
            if (old.getPosition().intValue() != sampleSubtypeMeta.getPosition()){
            	old.setPosition(sampleSubtypeMeta.getPosition());
            	changed = true;
            }
            if (changed)
            	sampleSubtypeMetaDao.save(old);

            oldSampleSubtypeMetas.remove(old.getK()); // remove the meta from the old meta list as we're done with it
            continue; 
          }

        sampleSubtypeMeta.setSampleSubtypeId(sampleSubtype.getSampleSubtypeId());
        sampleSubtypeMeta.setPosition(1);
        sampleSubtypeMetaDao.save(sampleSubtypeMeta);
      }
    }

    // delete the left overs
    for (String sampleSubtypeMetaKey : oldSampleSubtypeMetas.keySet()) {
      SampleSubtypeMeta sampleSubtypeMeta = oldSampleSubtypeMetas.get(sampleSubtypeMetaKey);
      sampleSubtypeMetaDao.remove(sampleSubtypeMeta);
      sampleSubtypeMetaDao.flush(sampleSubtypeMeta);
    }
    
    // sync sampleSubtypeResourceCategories
    Map<String, SampleSubtypeResourceCategory> oldSampleSubtypeResourceCats  = new HashMap<String, SampleSubtypeResourceCategory>();
    for (SampleSubtypeResourceCategory sampleSubtypeResourceCat: safeList(sampleSubtype.getSampleSubtypeResourceCategory())) {
    	oldSampleSubtypeResourceCats.put(sampleSubtypeResourceCat.getResourceCategory().getIName(), sampleSubtypeResourceCat);
    } 
    
    for (String resourceIName: safeList(compatibleResourcesByIName)) {
    	if (oldSampleSubtypeResourceCats.containsKey(resourceIName)) {
    		oldSampleSubtypeResourceCats.remove(resourceIName);
    		continue;
    	}
    	ResourceCategory resourceCat = resourceCategoryDao.getResourceCategoryByIName(resourceIName);
    	if (resourceCat.getResourceCategoryId() != null){
    		SampleSubtypeResourceCategory sampleSubtypeResourceCategory = new SampleSubtypeResourceCategory();
    		sampleSubtypeResourceCategory.setResourcecategoryId(resourceCat.getResourceCategoryId());
    		sampleSubtypeResourceCategory.setSampleSubtypeId(sampleSubtype.getSampleSubtypeId());
    		sampleSubtypeResourceCategoryDao.save(sampleSubtypeResourceCategory);
    		oldSampleSubtypeResourceCats.remove(resourceIName);
    	} else {
    		throw new NullResourceCategoryException();
    	}
    }
    
    // remove the left overs
    for (String adaptorsetKey : oldSampleSubtypeResourceCats.keySet()) {
    	ResourceCategory resourceCat = resourceCategoryDao.getResourceCategoryByIName(adaptorsetKey);
    	SampleSubtypeResourceCategory ssrc = sampleSubtypeResourceCategoryDao.getSampleSubtypeResourceCategoryBySampleSubtypeIdResourceCategoryId(sampleSubtype.getSampleSubtypeId(), resourceCat.getResourceCategoryId());
    	sampleSubtypeResourceCategoryDao.remove(ssrc);
    }
    
    updateUiFields(); 

  }

}

