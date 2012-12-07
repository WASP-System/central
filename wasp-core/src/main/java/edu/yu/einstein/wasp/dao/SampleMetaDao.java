
/**
 *
 * SampleMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.SampleMeta;


public interface SampleMetaDao extends WaspDao<SampleMeta> {

  public SampleMeta getSampleMetaBySampleMetaId (final int sampleMetaId);

  public SampleMeta getSampleMetaByKSampleId (final String k, final int sampleId);

  public List<SampleMeta> getSamplesMetaBySampleId (final int sampleId);
  
  public void updateBySampleId (final int sampleId, final SampleMeta m);

  public void updateBySampleId (final int sampleId, final List<SampleMeta> metaList);


}

