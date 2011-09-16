
/**
 *
 * SampleDraftMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleDraftMetaDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.SampleDraftMeta;


public interface SampleDraftMetaDao extends WaspDao<SampleDraftMeta> {

  public SampleDraftMeta getSampleDraftMetaBySampleDraftMetaId (final int sampleDraftMetaId);

  public SampleDraftMeta getSampleDraftMetaByKSampledraftId (final String k, final int sampledraftId);

  public void updateBySampledraftId (final int sampledraftId, final List<SampleDraftMeta> metaList);
  
  List<SampleDraftMeta> getAllMetaFields();

}

