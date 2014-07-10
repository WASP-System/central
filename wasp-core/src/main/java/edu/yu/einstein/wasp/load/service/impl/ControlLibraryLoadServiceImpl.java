package edu.yu.einstein.wasp.load.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleTypeCategoryDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.exception.NullSampleSubtypeException;
import edu.yu.einstein.wasp.load.service.ControlLibraryLoadService;
import edu.yu.einstein.wasp.load.service.SampleTypeLoadService;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.SampleTypeCategory;
import edu.yu.einstein.wasp.service.AdaptorService;

/**
 * 
 * @author rdubin
 *
 */
@Service
@Transactional("entityManager")
public class ControlLibraryLoadServiceImpl extends WaspLoadServiceImpl implements ControlLibraryLoadService {
	
	  @Autowired
	  private SampleDao sampleDao;	
	  @Autowired
	  private SampleMetaDao sampleMetaDao;	
	  @Autowired
	  private AdaptorService adaptorService;	
	  
	  @Override
	  public Sample update(String name, SampleType sampleType, SampleSubtype sampleSubtype, int isActive){
		  Assert.assertParameterNotNull(name, "name Cannot be null");
		  Assert.assertParameterNotNull(sampleType, "sampleType Cannot be null");
		  Assert.assertParameterNotNull(sampleSubtype, "sampleSubtype Cannot be null");
		  Assert.assertParameterNotNull(isActive, "isActive Cannot be null");
		  /*
		  if (sampleType.getId() == null ){
			  throw new Exception("SampleTypeId is null for '" + sampleType.getIName() + "'");
		  }
		  if (sampleSubtype.getId()==null){
			  throw new Exception("SampleSubtypeId is null for '" + sampleSubtype.getIName() + "'");
		  }
		  */
		  Map<String,Object> map = new HashMap<String,Object>();
		  map.put("name", name);//better be unique
		  map.put("sampleType", sampleType);
		  map.put("sampleSubtype", sampleSubtype);
		  
		  List<Sample> sampleList = sampleDao.findByMap(map);
		  Sample controlLibrary;
		  if(sampleList.isEmpty()){
			  controlLibrary = new Sample();
			  controlLibrary.setName(name);
			  controlLibrary.setSampleType(sampleType);
			  controlLibrary.setSampleSubtype(sampleSubtype);
			  controlLibrary.setIsActive(isActive);
			  controlLibrary = sampleDao.save(controlLibrary);
			 
		  }
		  else{
			  controlLibrary = sampleList.get(0);
			  controlLibrary.setName(name);
			  controlLibrary.setSampleType(sampleType);
			  controlLibrary.setSampleSubtype(sampleSubtype);
			  controlLibrary.setIsActive(isActive);
			  controlLibrary = sampleDao.save(controlLibrary);
		  }		    
		  return controlLibrary;		    
	}
}


