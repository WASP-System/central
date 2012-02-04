
/**
 *
 * SampleLabService.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleLabService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleLabDao;
import edu.yu.einstein.wasp.model.SampleLab;

@Service
public interface SampleLabService extends WaspService<SampleLab> {

	/**
	 * setSampleLabDao(SampleLabDao sampleLabDao)
	 *
	 * @param sampleLabDao
	 *
	 */
	public void setSampleLabDao(SampleLabDao sampleLabDao);

	/**
	 * getSampleLabDao();
	 *
	 * @return sampleLabDao
	 *
	 */
	public SampleLabDao getSampleLabDao();

  public SampleLab getSampleLabBySampleLabId (final int sampleLabId);

  public SampleLab getSampleLabBySampleIdLabId (final int sampleId, final int labId);


}

