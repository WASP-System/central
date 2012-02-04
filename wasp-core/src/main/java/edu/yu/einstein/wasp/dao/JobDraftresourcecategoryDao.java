
/**
 *
 * JobDraftresourcecategoryDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftresourcecategory Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.JobDraftresourcecategory;


public interface JobDraftresourcecategoryDao extends WaspDao<JobDraftresourcecategory> {

  public JobDraftresourcecategory getJobDraftresourcecategoryByJobDraftresourcecategoryId (final Integer jobDraftresourcecategoryId);

  public JobDraftresourcecategory getJobDraftresourcecategoryByResourcecategoryIdJobdraftId (final Integer resourcecategoryId, final Integer jobdraftId);


}

