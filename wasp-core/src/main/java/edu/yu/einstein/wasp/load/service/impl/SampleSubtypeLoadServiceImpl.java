package edu.yu.einstein.wasp.load.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeMetaDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeResourceCategoryDao;
import edu.yu.einstein.wasp.exception.InvalidRoleException;
import edu.yu.einstein.wasp.exception.NullResourceCategoryException;
import edu.yu.einstein.wasp.load.service.SampleSubtypeLoadService;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleSubtypeMeta;
import edu.yu.einstein.wasp.model.SampleSubtypeResourceCategory;
import edu.yu.einstein.wasp.model.SampleType;

/**
 * 
 * @author asmclellan
 *
 */
@Service
@Transactional("entityManager")
public class SampleSubtypeLoadServiceImpl extends WaspLoadServiceImpl implements SampleSubtypeLoadService {
	
	  @Autowired
	  private SampleSubtypeDao sampleSubtypeDao;

	  @Autowired
	  private SampleSubtypeMetaDao sampleSubtypeMetaDao;
	  
	  @Autowired
	  private ResourceCategoryDao resourceCategoryDao;
	  
	  @Autowired
	  private SampleSubtypeResourceCategoryDao sampleSubtypeResourceCategoryDao;
	  
	  @Autowired
	  private RoleDao roleDao;

	  
	  private SampleSubtype addOrUpdateSampleSubtype(SampleType sampleType, String iname, String name, int isActive, List<String> areaList){
		  	SampleSubtype sampleSubtype = sampleSubtypeDao.getSampleSubtypeByIName(iname); 
		    String areaListString = StringUtils.join(areaList, ",");
		    // inserts or update sampleSubtype
		    if (sampleSubtype.getSampleSubtypeId() == null) {
		      sampleSubtype = new SampleSubtype();

		      sampleSubtype.setIName(iname);
		      sampleSubtype.setName(name);
		      sampleSubtype.setIsActive(isActive);
		      sampleSubtype.setSampleTypeId(sampleType.getSampleTypeId());
		      sampleSubtype.setAreaList(areaListString);

		      sampleSubtype = sampleSubtypeDao.save(sampleSubtype); 

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
		    	  sampleSubtype.setAreaList(areaListString);
		    	  changed = true;
		      }
		      if (sampleSubtype.getIsActive().intValue() != isActive){
		    	  sampleSubtype.setIsActive(isActive);
		    	  changed = true;
		      }
		      if (changed)
		    	  sampleSubtypeDao.save(sampleSubtype); 
		    }
		  return sampleSubtype;
	  }
	  
	  private void syncSampleSubtypeMeta(SampleSubtype sampleSubtype, List<SampleSubtypeMeta> meta, String iname, String applicableRoles ){
		  int lastPosition = 0;
		    Map<String, SampleSubtypeMeta> oldSampleSubtypeMetas  = new HashMap<String, SampleSubtypeMeta>();

		    if (sampleSubtype != null && sampleSubtype.getSampleSubtypeMeta() != null) {
		      for (SampleSubtypeMeta sampleSubtypeMeta: safeList(sampleSubtype.getSampleSubtypeMeta())) {
		        oldSampleSubtypeMetas.put(sampleSubtypeMeta.getK(), sampleSubtypeMeta);
		      }
		    }
		    
		    if (applicableRoles != null) {
		    	if (meta == null)
		    		meta = new ArrayList<SampleSubtypeMeta>();
		    	SampleSubtypeMeta ssm = new SampleSubtypeMeta();
		    	ssm.setK(iname+".includeRoles");
		    	ssm.setV(applicableRoles);
		    	meta.add(ssm);
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
	  }
	  
	  private void syncSampleSubtypeResourceCategories(SampleSubtype sampleSubtype, List<ResourceCategory> compatibleResources){
		  Map<String, SampleSubtypeResourceCategory> oldSampleSubtypeResourceCats  = new HashMap<String, SampleSubtypeResourceCategory>();
		    for (SampleSubtypeResourceCategory sampleSubtypeResourceCat: safeList(sampleSubtype.getSampleSubtypeResourceCategory())) {
		    	oldSampleSubtypeResourceCats.put(sampleSubtypeResourceCat.getResourceCategory().getIName(), sampleSubtypeResourceCat);
		    } 
		    
		    for (ResourceCategory resourceCat: safeList(compatibleResources)) {
		    	if (oldSampleSubtypeResourceCats.containsKey(resourceCat.getIName())) {
		    		oldSampleSubtypeResourceCats.remove(resourceCat.getIName());
		    		continue;
		    	}
		    	if (resourceCat.getResourceCategoryId() != null){
		    		SampleSubtypeResourceCategory sampleSubtypeResourceCategory = new SampleSubtypeResourceCategory();
		    		sampleSubtypeResourceCategory.setResourcecategoryId(resourceCat.getResourceCategoryId());
		    		sampleSubtypeResourceCategory.setSampleSubtypeId(sampleSubtype.getSampleSubtypeId());
		    		sampleSubtypeResourceCategoryDao.save(sampleSubtypeResourceCategory);
		    		oldSampleSubtypeResourceCats.remove(resourceCat.getIName());
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
	  }
	  
	  @Override
	  public SampleSubtype update(String iname, String name, SampleType sampleType, int isActive, 
			  List<ResourceCategory> compatibleResources, String applicableRoles, List<SampleSubtypeMeta> meta, 
			  List<String> areaList){
		  Assert.assertParameterNotNull(iname, "iname Cannot be null");
		  Assert.assertParameterNotNull(name, "name Cannot be null");
		  Assert.assertParameterNotNull(sampleType, "sampleType Cannot be null");
		  SampleSubtype sampleSubtype = addOrUpdateSampleSubtype(sampleType, iname, name, isActive, areaList);
		  
		  syncSampleSubtypeMeta(sampleSubtype, meta, iname, applicableRoles );
	    
		  syncSampleSubtypeResourceCategories(sampleSubtype, compatibleResources);
	    
		  return sampleSubtype;
	  }


	  @Override
	  public void validateApplicableRoles(String applicableRolesString){
		  for (String applicableRole : applicableRolesString.split(",")){
			  if (roleDao.getRoleByRoleName(applicableRole).getRoleId() == null)
				  throw new InvalidRoleException("Role '"+applicableRole+"' is not in the list of valid roles");
		  }
	  }

}
