/**
 *
 * AdaptorService.java 
 * @author rdubin
 *  
 * the AdaptorService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleType;

@Service
public interface AdaptorService extends WaspService {

	  /**
	   * Accepts and (in-situ) sorts list of Adaptors (of a particular Adaptorset) by Adaptor barcodenumber 
	   * @param List<Adaptr>
	   * @return void
	   */
	  public void sortAdaptorsByBarcodenumber(List<Adaptor> adaptors);
	  
}
