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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.service.AdaptorService;

@Service
@Transactional
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
