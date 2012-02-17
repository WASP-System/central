/**
 *
 * JobDraftService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobDraftDao;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.ResourceCategory;

@Service
public interface JobDraftService extends WaspService<JobDraft> {

	/**
	 * setJobDraftDao(JobDraftDao jobDraftDao)
	 * 
	 * @param jobDraftDao
	 * 
	 */
	public void setJobDraftDao(JobDraftDao jobDraftDao);

	/**
	 * getJobDraftDao();
	 * 
	 * @return jobDraftDao
	 * 
	 */
	public JobDraftDao getJobDraftDao();

	public JobDraft getJobDraftByJobDraftId(final Integer jobDraftId);

	public List<JobDraft> getPendingJobDrafts();

	public List<JobDraft> getPendingJobDraftsOrderBy(String orderByColumnName, String direction);
}
