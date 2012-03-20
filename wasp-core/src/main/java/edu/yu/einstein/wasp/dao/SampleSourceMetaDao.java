
/**
 *
 * SampleSourceMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSourceMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.SampleSourceMeta;


public interface SampleSourceMetaDao extends WaspDao<SampleSourceMeta> {

  public SampleSourceMeta getSampleSourceMetaBySampleSourceMetaId (final int sampleSourceMetaId);

  public SampleSourceMeta getSampleSourceMetaByKSampleSourceId (final String k, final int sampleSourceId);

  List<SampleSourceMeta> getSampleSourceMetaBySampleSourceId (final int sampleSourceId);

  public void updateBySampleSourceId (final int sampleSourceId, final List<SampleSourceMeta> metaList);




}

