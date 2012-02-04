
/**
 *
 * SampleDraftDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleDraftDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.SampleDraft;


public interface SampleDraftDao extends WaspDao<SampleDraft> {

  public SampleDraft getSampleDraftBySampleDraftId (final int sampleDraftId);

  List<SampleDraft> getSampleDraftByJobId (final int jobdraftId);
  
}

