
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

import edu.yu.einstein.wasp.service.JobDraftresourcecategoryService;
import edu.yu.einstein.wasp.dao.JobDraftresourcecategoryDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.JobDraftresourcecategory;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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
	public JobDraftresourcecategoryDao getJobDraftresourcecategoryDao() {
		return this.jobDraftresourcecategoryDao;
	}


  public JobDraftresourcecategory getJobDraftresourcecategoryByJobDraftresourcecategoryId (final Integer jobDraftresourcecategoryId) {
    return this.getJobDraftresourcecategoryDao().getJobDraftresourcecategoryByJobDraftresourcecategoryId(jobDraftresourcecategoryId);
  }

  public JobDraftresourcecategory getJobDraftresourcecategoryByResourcecategoryIdJobdraftId (final Integer resourcecategoryId, final Integer jobdraftId) {
    return this.getJobDraftresourcecategoryDao().getJobDraftresourcecategoryByResourcecategoryIdJobdraftId(resourcecategoryId, jobdraftId);
  }

}

