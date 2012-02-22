
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

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.model.Sample;

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
  
  public List<Sample> getSamplesByJobId (final int jobId);

  public List<Sample> getActiveSamples();

  public Sample getSampleByName(final String name);

}

