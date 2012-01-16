
/**
 *
 * JobDraftresourceDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftresource Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.JobDraftresource;


public interface JobDraftresourceDao extends WaspDao<JobDraftresource> {

  public JobDraftresource getJobDraftresourceByJobDraftresourceId (final int jobDraftresourceId);

  public JobDraftresource getJobDraftresourceByResourceIdJobdraftId (final int resourceId, final int jobdraftId);


}

