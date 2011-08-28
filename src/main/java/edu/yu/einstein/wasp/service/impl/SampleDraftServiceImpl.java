
/**
 *
 * SampleDraftService.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleDraftService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.SampleDraftService;
import edu.yu.einstein.wasp.dao.SampleDraftDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.SampleDraft;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SampleDraftServiceImpl extends WaspServiceImpl<SampleDraft> implements SampleDraftService {

  private SampleDraftDao sampleDraftDao;
  @Autowired
  public void setSampleDraftDao(SampleDraftDao sampleDraftDao) {
    this.sampleDraftDao = sampleDraftDao;
    this.setWaspDao(sampleDraftDao);
  }
  public SampleDraftDao getSampleDraftDao() {
    return this.sampleDraftDao;
  }

  // **

  
  public SampleDraft getSampleDraftBySampleDraftId (final int sampleDraftId) {
    return this.getSampleDraftDao().getSampleDraftBySampleDraftId(sampleDraftId);
  }
}

