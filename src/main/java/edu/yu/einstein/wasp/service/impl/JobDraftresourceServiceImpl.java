
/**
 *
 * JobDraftresourceServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftresourceService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobDraftresourceDao;
import edu.yu.einstein.wasp.model.JobDraftresource;
import edu.yu.einstein.wasp.service.JobDraftresourceService;

@Service
public class JobDraftresourceServiceImpl extends WaspServiceImpl<JobDraftresource> implements JobDraftresourceService {

	/**
	 * jobDraftresourceDao;
	 *
	 */
	private JobDraftresourceDao jobDraftresourceDao;

	/**
	 * setJobDraftresourceDao(JobDraftresourceDao jobDraftresourceDao)
	 *
	 * @param jobDraftresourceDao
	 *
	 */
	@Autowired
	public void setJobDraftresourceDao(JobDraftresourceDao jobDraftresourceDao) {
		this.jobDraftresourceDao = jobDraftresourceDao;
		this.setWaspDao(jobDraftresourceDao);
	}

	/**
	 * getJobDraftresourceDao();
	 *
	 * @return jobDraftresourceDao
	 *
	 */
	public JobDraftresourceDao getJobDraftresourceDao() {
		return this.jobDraftresourceDao;
	}


  public JobDraftresource getJobDraftresourceByJobDraftresourceId (final int jobDraftresourceId) {
    return this.getJobDraftresourceDao().getJobDraftresourceByJobDraftresourceId(jobDraftresourceId);
  }

  public JobDraftresource getJobDraftresourceByResourceIdJobdraftId (final int resourceId, final int jobdraftId) {
    return this.getJobDraftresourceDao().getJobDraftresourceByResourceIdJobdraftId(resourceId, jobdraftId);
  }

}

