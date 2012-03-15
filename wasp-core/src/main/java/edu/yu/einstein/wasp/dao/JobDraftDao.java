
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

import java.util.List;

import edu.yu.einstein.wasp.model.JobDraft;


public interface JobDraftDao extends WaspDao<JobDraft> {

  public JobDraft getJobDraftByJobDraftId (final Integer jobDraftId);


	public List<JobDraft> getPendingJobDrafts();

	public List<JobDraft> getPendingJobDraftsOrderBy(String orderByColumnName, String direction);

}

