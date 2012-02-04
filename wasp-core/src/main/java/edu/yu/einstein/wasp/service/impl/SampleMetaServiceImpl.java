
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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.service.SampleMetaService;

@Service
public class SampleMetaServiceImpl extends WaspMetaServiceImpl<SampleMeta> implements SampleMetaService {

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
	@Override
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
	@Override
	public SampleMetaDao getSampleMetaDao() {
		return this.sampleMetaDao;
	}


  @Override
public SampleMeta getSampleMetaBySampleMetaId (final int sampleMetaId) {
    return this.getSampleMetaDao().getSampleMetaBySampleMetaId(sampleMetaId);
  }

  @Override
public SampleMeta getSampleMetaByKSampleId (final String k, final int sampleId) {
    return this.getSampleMetaDao().getSampleMetaByKSampleId(k, sampleId);
  }

  @Override
public void updateBySampleId (final String area, final int sampleId, final List<SampleMeta> metaList) {
    this.getSampleMetaDao().updateBySampleId(area, sampleId, metaList); 
  }

  @Override
public void updateBySampleId (final int sampleId, final List<SampleMeta> metaList) {
    this.getSampleMetaDao().updateBySampleId(sampleId, metaList); 
  }

  @Override
public  List<SampleMeta> getSamplesMetaBySampleId (final int sampleId) {
    return getSampleMetaDao().getSamplesMetaBySampleId(sampleId);
  }


}

