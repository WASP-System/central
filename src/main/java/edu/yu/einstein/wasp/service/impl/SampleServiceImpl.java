
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

import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Sample;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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

