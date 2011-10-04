
/**
 *
 * SampleDraftMetaServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleDraftMetaService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.SampleDraftMetaService;
import edu.yu.einstein.wasp.dao.SampleDraftMetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.SampleDraftMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SampleDraftMetaServiceImpl extends WaspServiceImpl<SampleDraftMeta> implements SampleDraftMetaService {

	/**
	 * sampleDraftMetaDao;
	 *
	 */
	private SampleDraftMetaDao sampleDraftMetaDao;

	/**
	 * setSampleDraftMetaDao(SampleDraftMetaDao sampleDraftMetaDao)
	 *
	 * @param sampleDraftMetaDao
	 *
	 */
	@Autowired
	public void setSampleDraftMetaDao(SampleDraftMetaDao sampleDraftMetaDao) {
		this.sampleDraftMetaDao = sampleDraftMetaDao;
		this.setWaspDao(sampleDraftMetaDao);
	}

	/**
	 * getSampleDraftMetaDao();
	 *
	 * @return sampleDraftMetaDao
	 *
	 */
	public SampleDraftMetaDao getSampleDraftMetaDao() {
		return this.sampleDraftMetaDao;
	}


  public SampleDraftMeta getSampleDraftMetaBySampleDraftMetaId (final int sampleDraftMetaId) {
    return this.getSampleDraftMetaDao().getSampleDraftMetaBySampleDraftMetaId(sampleDraftMetaId);
  }

  public SampleDraftMeta getSampleDraftMetaByKSampledraftId (final String k, final int sampledraftId) {
    return this.getSampleDraftMetaDao().getSampleDraftMetaByKSampledraftId(k, sampledraftId);
  }

  public void updateBySampledraftId (final String area, final int sampledraftId, final List<SampleDraftMeta> metaList) {
    this.getSampleDraftMetaDao().updateBySampledraftId(area, sampledraftId, metaList); 
  }

  public void updateBySampledraftId (final int sampledraftId, final List<SampleDraftMeta> metaList) {
    this.getSampleDraftMetaDao().updateBySampledraftId(sampledraftId, metaList); 
  }

  public Map<SubtypeSample,List<SampleDraftMeta>> getAllowableMetaFields(int workflowId) {
    return this.getSampleDraftMetaDao().getAllowableMetaFields(workflowId);
  }


}

