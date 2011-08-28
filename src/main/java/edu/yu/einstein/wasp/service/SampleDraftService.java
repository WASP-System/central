
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

import edu.yu.einstein.wasp.dao.SampleDraftDao;
import edu.yu.einstein.wasp.model.SampleDraft;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface SampleDraftService extends WaspService<SampleDraft> {

  public void setSampleDraftDao(SampleDraftDao sampleDraftDao);
  public SampleDraftDao getSampleDraftDao();

  public SampleDraft getSampleDraftBySampleDraftId (final int sampleDraftId);

}

