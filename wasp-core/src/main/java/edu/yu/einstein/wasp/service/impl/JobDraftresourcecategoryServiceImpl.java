
/**
 *
 * JobDraftresourcecategoryServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftresourcecategoryService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobDraftresourcecategoryDao;
import edu.yu.einstein.wasp.model.JobDraftresourcecategory;
import edu.yu.einstein.wasp.service.JobDraftresourcecategoryService;

@Service
public class JobDraftresourcecategoryServiceImpl extends WaspServiceImpl<JobDraftresourcecategory> implements JobDraftresourcecategoryService {

	/**
	 * jobDraftresourcecategoryDao;
	 *
	 */
	private JobDraftresourcecategoryDao jobDraftresourcecategoryDao;

	/**
	 * setJobDraftresourcecategoryDao(JobDraftresourcecategoryDao jobDraftresourcecategoryDao)
	 *
	 * @param jobDraftresourcecategoryDao
	 *
	 */
	@Override
	@Autowired
	public void setJobDraftresourcecategoryDao(JobDraftresourcecategoryDao jobDraftresourcecategoryDao) {
		this.jobDraftresourcecategoryDao = jobDraftresourcecategoryDao;
		this.setWaspDao(jobDraftresourcecategoryDao);
	}

	/**
	 * getJobDraftresourcecategoryDao();
	 *
	 * @return jobDraftresourcecategoryDao
	 *
	 */
	@Override
	public JobDraftresourcecategoryDao getJobDraftresourcecategoryDao() {
		return this.jobDraftresourcecategoryDao;
	}


  @Override
public JobDraftresourcecategory getJobDraftresourcecategoryByJobDraftresourcecategoryId (final Integer jobDraftresourcecategoryId) {
    return this.getJobDraftresourcecategoryDao().getJobDraftresourcecategoryByJobDraftresourcecategoryId(jobDraftresourcecategoryId);
  }

  @Override
public JobDraftresourcecategory getJobDraftresourcecategoryByResourcecategoryIdJobdraftId (final Integer resourcecategoryId, final Integer jobdraftId) {
    return this.getJobDraftresourcecategoryDao().getJobDraftresourcecategoryByResourcecategoryIdJobdraftId(resourcecategoryId, jobdraftId);
  }

}

