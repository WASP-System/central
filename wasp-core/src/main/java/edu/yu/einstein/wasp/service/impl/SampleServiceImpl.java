
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.model.Run;
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
	@Override
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
	@Override
	public SampleDao getSampleDao() {
		return this.sampleDao;
	}


  @Override
public Sample getSampleBySampleId (final int sampleId) {
    return this.getSampleDao().getSampleBySampleId(sampleId);
  }

  @Override
public List<Sample> getSamplesByJobId (final int jobId) {
	  return this.getSampleDao().getSamplesByJobId(jobId);
  }
  
  @Override
  public List<Sample> getActiveSamples(){
	  Map queryMap = new HashMap();
	  queryMap.put("isActive", 1);
	  return this.findByMap(queryMap);
  }
}

