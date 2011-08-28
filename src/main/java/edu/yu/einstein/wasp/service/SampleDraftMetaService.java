
/**
 *
 * SampleDraftMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleDraftMetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.SampleDraftMetaDao;
import edu.yu.einstein.wasp.model.SampleDraftMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface SampleDraftMetaService extends WaspService<SampleDraftMeta> {

  public void setSampleDraftMetaDao(SampleDraftMetaDao sampleDraftMetaDao);
  public SampleDraftMetaDao getSampleDraftMetaDao();

  public SampleDraftMeta getSampleDraftMetaBySampleDraftMetaId (final int sampleDraftMetaId);

  public SampleDraftMeta getSampleDraftMetaByKSampledraftId (final String k, final int sampledraftId);

  public void updateBySampledraftId (final int sampledraftId, final List<SampleDraftMeta> metaList);

}

