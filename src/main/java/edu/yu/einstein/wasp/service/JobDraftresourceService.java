
/**
 *
 * JobDraftresourceService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftresourceService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobDraftresourceDao;
import edu.yu.einstein.wasp.model.JobDraftresource;

@Service
public interface JobDraftresourceService extends WaspService<JobDraftresource> {

	/**
	 * setJobDraftresourceDao(JobDraftresourceDao jobDraftresourceDao)
	 *
	 * @param jobDraftresourceDao
	 *
	 */
	public void setJobDraftresourceDao(JobDraftresourceDao jobDraftresourceDao);

	/**
	 * getJobDraftresourceDao();
	 *
	 * @return jobDraftresourceDao
	 *
	 */
	public JobDraftresourceDao getJobDraftresourceDao();

  public JobDraftresource getJobDraftresourceByJobDraftresourceId (final int jobDraftresourceId);

  public JobDraftresource getJobDraftresourceByResourceIdJobdraftId (final int resourceId, final int jobdraftId);


}

