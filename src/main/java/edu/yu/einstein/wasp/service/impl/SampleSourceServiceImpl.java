
/**
 *
 * SampleSourceServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSourceService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.service.SampleSourceService;

@Service
public class SampleSourceServiceImpl extends WaspServiceImpl<SampleSource> implements SampleSourceService {

	/**
	 * sampleSourceDao;
	 *
	 */
	private SampleSourceDao sampleSourceDao;

	/**
	 * setSampleSourceDao(SampleSourceDao sampleSourceDao)
	 *
	 * @param sampleSourceDao
	 *
	 */
	@Override
	@Autowired
	public void setSampleSourceDao(SampleSourceDao sampleSourceDao) {
		this.sampleSourceDao = sampleSourceDao;
		this.setWaspDao(sampleSourceDao);
	}

	/**
	 * getSampleSourceDao();
	 *
	 * @return sampleSourceDao
	 *
	 */
	@Override
	public SampleSourceDao getSampleSourceDao() {
		return this.sampleSourceDao;
	}


  @Override
public SampleSource getSampleSourceBySampleSourceId (final int sampleSourceId) {
    return this.getSampleSourceDao().getSampleSourceBySampleSourceId(sampleSourceId);
  }

  @Override
public SampleSource getSampleSourceBySampleIdMultiplexindex (final int sampleId, final int multiplexindex) {
    return this.getSampleSourceDao().getSampleSourceBySampleIdMultiplexindex(sampleId, multiplexindex);
  }

}

