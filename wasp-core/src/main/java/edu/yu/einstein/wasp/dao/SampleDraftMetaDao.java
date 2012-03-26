
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
import edu.yu.einstein.wasp.model.SampleSubtype;


public interface SampleDraftMetaDao extends WaspDao<SampleDraftMeta> {

  public SampleDraftMeta getSampleDraftMetaBySampleDraftMetaId (final int sampleDraftMetaId);

  public SampleDraftMeta getSampleDraftMetaByKSampledraftId (final String k, final int sampledraftId);

  public void updateBySampledraftId (final int sampledraftId, final List<SampleDraftMeta> metaList);
  
  /**
   * Returns a Map of sampleSubtypes and associated field metadata (i.e. that with a metaposition) associated with the provided workflow
   * @param workflowId
   * @return Map< {@link SampleSubtype}, List<{@link SampleDraftMeta}> >
   */
  Map<SampleSubtype,List<SampleDraftMeta>> getAllowableMetaFields(final int workflowId);
 
}

