package edu.yu.einstein.wasp.load.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.dao.SampleTypeCategoryDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.exception.NullSampleSubtypeException;
import edu.yu.einstein.wasp.load.service.SampleTypeLoadService;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.SampleTypeCategory;

@Service
@Transactional("entityManager")
public class SampleTypeLoadServiceImpl extends WaspLoadServiceImpl implements SampleTypeLoadService {
	
	  @Autowired
	  private SampleTypeCategoryDao sampleTypeCategoryDao;
	  
	  @Autowired
	  private SampleTypeDao sampleTypeDao;
 
	  
	  
	  @Override
	  public SampleType update(String iname, String name, SampleTypeCategory sampleTypeCategory, int isActive){
		  Assert.assertParameterNotNull(iname, "iname Cannot be null");
		  Assert.assertParameterNotNull(name, "name Cannot be null");
		  Assert.assertParameterNotNull(sampleTypeCategory, "sampleTypeCategory Cannot be null");
		  if (sampleTypeCategory.getSampleTypeCategoryId() == null)
			  throw new NullSampleSubtypeException("SampleTypeCategoryId is null for '" + sampleTypeCategory.getIName() + "'");

		  	SampleType sampleType = sampleTypeDao.getSampleTypeByIName(iname); 
		    // inserts or update sampleSubtype
		    if (sampleType.getSampleTypeId() == null) {
		    	sampleType.setIName(iname);
		    	sampleType.setName(name);
		    	sampleType.setIsActive(isActive);
		    	sampleType.setSampleTypeCategory(sampleTypeCategory);
		    	sampleTypeDao.save(sampleType);
		    } else {
		      if (!sampleType.getName().equals(name)){
		    	  sampleType.setName(name);
		      }
		      if (sampleType.getSampleTypeCategoryId().intValue() != sampleTypeCategory.getSampleTypeCategoryId().intValue()){
		    	  sampleType.setSampleTypeCategoryId(sampleTypeCategory.getSampleTypeCategoryId());
		      }
		      if (sampleType.getIsActive().intValue() != isActive){
		    	  sampleType.setIsActive(isActive);
		      }
		}
		    return sampleType;
	}
}
