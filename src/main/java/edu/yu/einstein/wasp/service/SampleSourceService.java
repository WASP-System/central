
/**
 *
 * SampleSourceService.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSourceService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.model.SampleSource;

@Service
public interface SampleSourceService extends WaspService<SampleSource> {

	/**
	 * setSampleSourceDao(SampleSourceDao sampleSourceDao)
	 *
	 * @param sampleSourceDao
	 *
	 */
	public void setSampleSourceDao(SampleSourceDao sampleSourceDao);

	/**
	 * getSampleSourceDao();
	 *
	 * @return sampleSourceDao
	 *
	 */
	public SampleSourceDao getSampleSourceDao();

  public SampleSource getSampleSourceBySampleSourceId (final int sampleSourceId);

  public SampleSource getSampleSourceBySampleIdMultiplexindex (final int sampleId, final int multiplexindex);


}

