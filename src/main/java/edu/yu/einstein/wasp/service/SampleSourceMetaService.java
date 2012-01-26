
/**
 *
 * SampleSourceMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSourceMetaService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleSourceMetaDao;
import edu.yu.einstein.wasp.model.SampleSourceMeta;

@Service
public interface SampleSourceMetaService extends WaspMetaService<SampleSourceMeta> {

	/**
	 * setSampleSourceMetaDao(SampleSourceMetaDao sampleSourceMetaDao)
	 *
	 * @param sampleSourceMetaDao
	 *
	 */
	public void setSampleSourceMetaDao(SampleSourceMetaDao sampleSourceMetaDao);

	/**
	 * getSampleSourceMetaDao();
	 *
	 * @return sampleSourceMetaDao
	 *
	 */
	public SampleSourceMetaDao getSampleSourceMetaDao();

  public SampleSourceMeta getSampleSourceMetaBySampleSourceMetaId (final int sampleSourceMetaId);

  public SampleSourceMeta getSampleSourceMetaByKSampleSourceId (final String k, final int sampleSourceId);


  public void updateBySampleSourceId (final String area, final int sampleSourceId, final List<SampleSourceMeta> metaList);

  public void updateBySampleSourceId (final int sampleSourceId, final List<SampleSourceMeta> metaList);


  List<SampleSourceMeta> getSampleSourceMetaBySampleSourceId (final int sampleSourceId);


}

