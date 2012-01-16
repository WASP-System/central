
/**
 *
 * SampleServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.service.SampleService;

@Service
public class SampleServiceImpl extends WaspServiceImpl<Sample> implements SampleService {

	/**
	 * sampleDao;
	 *
	 */
	private SampleDao sampleDao;

	/**
	 * setSampleDao(SampleDao sampleDao)
	 *
	 * @param sampleDao
	 *
	 */
	@Autowired
	public void setSampleDao(SampleDao sampleDao) {
		this.sampleDao = sampleDao;
		this.setWaspDao(sampleDao);
	}

	/**
	 * getSampleDao();
	 *
	 * @return sampleDao
	 *
	 */
	public SampleDao getSampleDao() {
		return this.sampleDao;
	}


  public Sample getSampleBySampleId (final int sampleId) {
    return this.getSampleDao().getSampleBySampleId(sampleId);
  }

  public List<Sample> getSamplesByJobId (final int jobId) {
	  return this.getSampleDao().getSamplesByJobId(jobId);
  }
}

