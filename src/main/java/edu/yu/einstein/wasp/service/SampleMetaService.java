
/**
 *
 * SampleMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleMetaService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.model.SampleMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface SampleMetaService extends WaspMetaService<SampleMeta> {

	/**
	 * setSampleMetaDao(SampleMetaDao sampleMetaDao)
	 *
	 * @param sampleMetaDao
	 *
	 */
	public void setSampleMetaDao(SampleMetaDao sampleMetaDao);

	/**
	 * getSampleMetaDao();
	 *
	 * @return sampleMetaDao
	 *
	 */
	public SampleMetaDao getSampleMetaDao();

  public SampleMeta getSampleMetaBySampleMetaId (final int sampleMetaId);

  public SampleMeta getSampleMetaByKSampleId (final String k, final int sampleId);


  public void updateBySampleId (final String area, final int sampleId, final List<SampleMeta> metaList);

  public void updateBySampleId (final int sampleId, final List<SampleMeta> metaList);


  List<SampleMeta> getSamplesMetaBySampleId (final int sampleId);


}

