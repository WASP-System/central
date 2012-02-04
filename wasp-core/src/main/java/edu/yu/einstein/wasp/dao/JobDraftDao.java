
/**
 *
 * JobDraftDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraft Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.JobDraft;


public interface JobDraftDao extends WaspDao<JobDraft> {

  public JobDraft getJobDraftByJobDraftId (final Integer jobDraftId);


}

