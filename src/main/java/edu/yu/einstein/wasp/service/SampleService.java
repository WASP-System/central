
/**
 *
 * SampleService.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.model.Sample;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface SampleService extends WaspService<Sample> {

	/**
	 * setSampleDao(SampleDao sampleDao)
	 *
	 * @param sampleDao
	 *
	 */
	public void setSampleDao(SampleDao sampleDao);

	/**
	 * getSampleDao();
	 *
	 * @return sampleDao
	 *
	 */
	public SampleDao getSampleDao();

  public Sample getSampleBySampleId (final int sampleId);


}

