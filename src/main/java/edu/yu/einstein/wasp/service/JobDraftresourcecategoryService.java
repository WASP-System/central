
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

import edu.yu.einstein.wasp.dao.JobDraftresourcecategoryDao;
import edu.yu.einstein.wasp.model.JobDraftresourcecategory;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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

