
/**
 *
 * SampleDraftService.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleDraftService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleDraftDao;
import edu.yu.einstein.wasp.model.SampleDraft;

@Service
public interface SampleDraftService extends WaspService<SampleDraft> {

  public void setSampleDraftDao(SampleDraftDao sampleDraftDao);
  public SampleDraftDao getSampleDraftDao();

  public SampleDraft getSampleDraftBySampleDraftId (final int sampleDraftId);

  List<SampleDraft> getSampleDraftByJobId (final int jobdraftId);
  
}

