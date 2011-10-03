
/**
 *
 * SampleMetaServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleMetaService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.SampleMetaService;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.SampleMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SampleMetaServiceImpl extends WaspServiceImpl<SampleMeta> implements SampleMetaService {

	/**
	 * sampleMetaDao;
	 *
	 */
	private SampleMetaDao sampleMetaDao;

	/**
	 * setSampleMetaDao(SampleMetaDao sampleMetaDao)
	 *
	 * @param sampleMetaDao
	 *
	 */
	@Autowired
	public void setSampleMetaDao(SampleMetaDao sampleMetaDao) {
		this.sampleMetaDao = sampleMetaDao;
		this.setWaspDao(sampleMetaDao);
	}

	/**
	 * getSampleMetaDao();
	 *
	 * @return sampleMetaDao
	 *
	 */
	public SampleMetaDao getSampleMetaDao() {
		return this.sampleMetaDao;
	}


  public SampleMeta getSampleMetaBySampleMetaId (final int sampleMetaId) {
    return this.getSampleMetaDao().getSampleMetaBySampleMetaId(sampleMetaId);
  }

  public SampleMeta getSampleMetaByKSampleId (final String k, final int sampleId) {
    return this.getSampleMetaDao().getSampleMetaByKSampleId(k, sampleId);
  }

  public void updateBySampleId (final int sampleId, final List<SampleMeta> metaList) {
    this.getSampleMetaDao().updateBySampleId(sampleId, metaList); 
  }

  public  List<SampleMeta> getSamplesMetaBySampleId (final int sampleId) {
	  return getSampleMetaDao().getSamplesMetaBySampleId(sampleId);
  }
}

