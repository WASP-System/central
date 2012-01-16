
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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleDraftDao;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.service.SampleDraftService;

@Service
public class SampleDraftServiceImpl extends WaspServiceImpl<SampleDraft> implements SampleDraftService {

  private SampleDraftDao sampleDraftDao;
  @Override
@Autowired
  public void setSampleDraftDao(SampleDraftDao sampleDraftDao) {
    this.sampleDraftDao = sampleDraftDao;
    this.setWaspDao(sampleDraftDao);
  }
  @Override
public SampleDraftDao getSampleDraftDao() {
    return this.sampleDraftDao;
  }

  // **
  
  @Override
public SampleDraft getSampleDraftBySampleDraftId (final int sampleDraftId) {
    return this.getSampleDraftDao().getSampleDraftBySampleDraftId(sampleDraftId);
  }
  
  @Override
public List<SampleDraft> getSampleDraftByJobId (final int jobdraftId) {
	  return this.getSampleDraftDao().getSampleDraftByJobId(jobdraftId);
  }
  
}

