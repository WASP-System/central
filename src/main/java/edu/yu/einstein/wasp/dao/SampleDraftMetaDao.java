
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
import java.util.Map;

import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.model.SubtypeSample;


public interface SampleDraftMetaDao extends WaspDao<SampleDraftMeta> {

  public SampleDraftMeta getSampleDraftMetaBySampleDraftMetaId (final int sampleDraftMetaId);

  public SampleDraftMeta getSampleDraftMetaByKSampledraftId (final String k, final int sampledraftId);

  public void updateBySampledraftId (final int sampledraftId, final List<SampleDraftMeta> metaList);
  
  Map<SubtypeSample,List<SampleDraftMeta>> getAllowableMetaFields(final int workflowId);
 
}

