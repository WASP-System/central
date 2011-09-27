
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

import edu.yu.einstein.wasp.service.SampleSourceService;
import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.SampleSource;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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
	public SampleSourceDao getSampleSourceDao() {
		return this.sampleSourceDao;
	}


  public SampleSource getSampleSourceBySampleSourceId (final int sampleSourceId) {
    return this.getSampleSourceDao().getSampleSourceBySampleSourceId(sampleSourceId);
  }

  public SampleSource getSampleSourceBySampleIdMultiplexindex (final int sampleId, final int multiplexindex) {
    return this.getSampleSourceDao().getSampleSourceBySampleIdMultiplexindex(sampleId, multiplexindex);
  }

}

