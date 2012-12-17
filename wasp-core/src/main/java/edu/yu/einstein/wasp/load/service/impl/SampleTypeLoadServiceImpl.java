package edu.yu.einstein.wasp.load.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.SampleTypeCategoryDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.exception.NullSampleSubtypeException;
import edu.yu.einstein.wasp.load.service.SampleTypeLoadService;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.SampleTypeCategory;
import edu.yu.einstein.wasp.model.UiField;

@Service
@Transactional("entityManager")
public class SampleTypeLoadServiceImpl extends WaspLoadServiceImpl implements SampleTypeLoadService {
	
	  @Autowired
	  private SampleTypeCategoryDao sampleTypeCategoryDao;
	  
	  @Autowired
	  private SampleTypeDao sampleTypeDao;
 
	  
	  
	  @Override
	  public void update(String iname, String name, String sampleTypeCategoryIname, Integer isActive, List<UiField> uiFields){
	    
		  if (isActive == null)
		  	  isActive = 1;
		  
		 		  
		  SampleTypeCategory sampleTypeCategory = sampleTypeCategoryDao.getSampleTypeCategoryByIName(sampleTypeCategoryIname); 
		  if (sampleTypeCategory.getSampleTypeCategoryId() == null)
			  throw new NullSampleSubtypeException("SampleTypeCategoryId is null for '"+sampleTypeCategoryIname+"'");

		  	SampleType sampleType = sampleTypeDao.getSampleTypeByIName(iname); 
		    // inserts or update sampleSubtype
		    if (sampleType.getSampleTypeId() == null) {
		    	sampleType.setIName(iname);
		    	sampleType.setName(name);
		    	sampleType.setIsActive(isActive.intValue());
		    	sampleType.setSampleTypeCategory(sampleTypeCategory);
		    	sampleTypeDao.save(sampleType);
		    } else {
		      if (!sampleType.getName().equals(name)){
		    	  sampleType.setName(name);
		      }
		      if (sampleType.getSampleTypeCategoryId().intValue() != sampleTypeCategory.getSampleTypeCategoryId().intValue()){
		    	  sampleType.setSampleTypeCategoryId(sampleTypeCategory.getSampleTypeCategoryId());
		      }
		      if (sampleType.getIsActive().intValue() != isActive.intValue()){
		    	  sampleType.setIsActive(isActive.intValue());
		      }
		}
	}
}
