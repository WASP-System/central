
/**
 *
 * SampleLabServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleLabService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleLabDao;
import edu.yu.einstein.wasp.model.SampleLab;
import edu.yu.einstein.wasp.service.SampleLabService;

@Service
public class SampleLabServiceImpl extends WaspServiceImpl<SampleLab> implements SampleLabService {

	/**
	 * sampleLabDao;
	 *
	 */
	private SampleLabDao sampleLabDao;

	/**
	 * setSampleLabDao(SampleLabDao sampleLabDao)
	 *
	 * @param sampleLabDao
	 *
	 */
	@Override
	@Autowired
	public void setSampleLabDao(SampleLabDao sampleLabDao) {
		this.sampleLabDao = sampleLabDao;
		this.setWaspDao(sampleLabDao);
	}

	/**
	 * getSampleLabDao();
	 *
	 * @return sampleLabDao
	 *
	 */
	@Override
	public SampleLabDao getSampleLabDao() {
		return this.sampleLabDao;
	}


  @Override
public SampleLab getSampleLabBySampleLabId (final int sampleLabId) {
    return this.getSampleLabDao().getSampleLabBySampleLabId(sampleLabId);
  }

  @Override
public SampleLab getSampleLabBySampleIdLabId (final int sampleId, final int labId) {
    return this.getSampleLabDao().getSampleLabBySampleIdLabId(sampleId, labId);
  }

}

