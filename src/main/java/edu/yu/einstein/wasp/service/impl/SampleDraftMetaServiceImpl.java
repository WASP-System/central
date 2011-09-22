
/**
 *
 * SampleDraftMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleDraftMetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleDraftMetaDao;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.model.SubtypeSample;
import edu.yu.einstein.wasp.service.SampleDraftMetaService;

@Service
public class SampleDraftMetaServiceImpl extends WaspServiceImpl<SampleDraftMeta> implements SampleDraftMetaService {

  private SampleDraftMetaDao sampleDraftMetaDao;
  @Autowired
  public void setSampleDraftMetaDao(SampleDraftMetaDao sampleDraftMetaDao) {
    this.sampleDraftMetaDao = sampleDraftMetaDao;
    this.setWaspDao(sampleDraftMetaDao);
  }
  public SampleDraftMetaDao getSampleDraftMetaDao() {
    return this.sampleDraftMetaDao;
  }

  // **

  
  public SampleDraftMeta getSampleDraftMetaBySampleDraftMetaId (final int sampleDraftMetaId) {
    return this.getSampleDraftMetaDao().getSampleDraftMetaBySampleDraftMetaId(sampleDraftMetaId);
  }

  public SampleDraftMeta getSampleDraftMetaByKSampledraftId (final String k, final int sampledraftId) {
    return this.getSampleDraftMetaDao().getSampleDraftMetaByKSampledraftId(k, sampledraftId);
  }

  public void updateBySampledraftId (final int sampledraftId, final List<SampleDraftMeta> metaList) {
    this.getSampleDraftMetaDao().updateBySampledraftId(sampledraftId, metaList); 
  }
  
  public Map<SubtypeSample,List<SampleDraftMeta>> getAllowableMetaFields(int workflowId) {
	  return this.getSampleDraftMetaDao().getAllowableMetaFields(workflowId);	  
  }

}

