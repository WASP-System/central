
/**
 *
 * JobDraftresourcecategoryService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftresourcecategoryService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobDraftresourcecategoryDao;
import edu.yu.einstein.wasp.model.JobDraftresourcecategory;

@Service
public interface JobDraftresourcecategoryService extends WaspService<JobDraftresourcecategory> {

	/**
	 * setJobDraftresourcecategoryDao(JobDraftresourcecategoryDao jobDraftresourcecategoryDao)
	 *
	 * @param jobDraftresourcecategoryDao
	 *
	 */
	public void setJobDraftresourcecategoryDao(JobDraftresourcecategoryDao jobDraftresourcecategoryDao);

	/**
	 * getJobDraftresourcecategoryDao();
	 *
	 * @return jobDraftresourcecategoryDao
	 *
	 */
	public JobDraftresourcecategoryDao getJobDraftresourcecategoryDao();

  public JobDraftresourcecategory getJobDraftresourcecategoryByJobDraftresourcecategoryId (final Integer jobDraftresourcecategoryId);

  public JobDraftresourcecategory getJobDraftresourcecategoryByResourcecategoryIdJobdraftId (final Integer resourcecategoryId, final Integer jobdraftId);


}

