/**
 *
 * AdaptorServiceImpl.java 
 * @author rdubin
 *  
 * the AdaptorService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.AdaptorDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleSubtypeResourceCategory;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.model.WorkflowSampleSubtype;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.util.MetaHelper;

@Service
public class AdaptorServiceImpl extends WaspServiceImpl implements AdaptorService {


	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public void sortAdaptorsByBarcodenumber(List<Adaptor> adaptors){
		  class AdaptorBarcodenumberComparator implements Comparator<Adaptor> {
			    @Override
			    public int compare(Adaptor arg0, Adaptor arg1) {
			        return arg0.getBarcodenumber().compareTo(arg1.getBarcodenumber());
			    }
		  }
		  Collections.sort(adaptors, new AdaptorBarcodenumberComparator());//sort by sample's name 
	  }
	  
	 
	  
}
