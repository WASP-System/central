
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

  SampleMeta getSampleMetaBySampleMetaId (final int sampleMetaId);

  SampleMeta getSampleMetaByKSampleId (final String k, final int sampleId);

  void updateBySampleId (final int sampleId, final List<SampleMeta> metaList);

  List<SampleMeta> getSamplesMetaBySampleId (final int sampleId);

}

