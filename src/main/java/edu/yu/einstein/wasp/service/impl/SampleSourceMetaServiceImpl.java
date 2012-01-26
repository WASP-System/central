
/**
 *
 * SampleSourceMetaServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSourceMetaService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleSourceMetaDao;
import edu.yu.einstein.wasp.model.SampleSourceMeta;
import edu.yu.einstein.wasp.service.SampleSourceMetaService;

@Service
public class SampleSourceMetaServiceImpl extends WaspMetaServiceImpl<SampleSourceMeta> implements SampleSourceMetaService {

	/**
	 * sampleSourceMetaDao;
	 *
	 */
	private SampleSourceMetaDao sampleSourceMetaDao;

	/**
	 * setSampleSourceMetaDao(SampleSourceMetaDao sampleSourceMetaDao)
	 *
	 * @param sampleSourceMetaDao
	 *
	 */
	@Override
	@Autowired
	public void setSampleSourceMetaDao(SampleSourceMetaDao sampleSourceMetaDao) {
		this.sampleSourceMetaDao = sampleSourceMetaDao;
		this.setWaspDao(sampleSourceMetaDao);
	}

	/**
	 * getSampleSourceMetaDao();
	 *
	 * @return sampleSourceMetaDao
	 *
	 */
	@Override
	public SampleSourceMetaDao getSampleSourceMetaDao() {
		return this.sampleSourceMetaDao;
	}


  @Override
public SampleSourceMeta getSampleSourceMetaBySampleSourceMetaId (final int sampleSourceMetaId) {
    return this.getSampleSourceMetaDao().getSampleSourceMetaBySampleSourceMetaId(sampleSourceMetaId);
  }

  @Override
public SampleSourceMeta getSampleSourceMetaByKSampleSourceId (final String k, final int sampleSourceId) {
    return this.getSampleSourceMetaDao().getSampleSourceMetaByKSampleSourceId(k, sampleSourceId);
  }

  @Override
public void updateBySampleSourceId (final String area, final int sampleSourceId, final List<SampleSourceMeta> metaList) {
    this.getSampleSourceMetaDao().updateBySampleSourceId(area, sampleSourceId, metaList); 
  }

  @Override
public void updateBySampleSourceId (final int sampleSourceId, final List<SampleSourceMeta> metaList) {
    this.getSampleSourceMetaDao().updateBySampleSourceId(sampleSourceId, metaList); 
  }

  @Override
public  List<SampleSourceMeta> getSampleSourceMetaBySampleSourceId (final int sampleSourceId) {
    return getSampleSourceMetaDao().getSampleSourceMetaBySampleSourceId(sampleSourceId);
  }


}

