
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

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobDraftDao;
import edu.yu.einstein.wasp.model.JobDraft;

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

  public JobDraft getJobDraftByJobDraftId (final Integer jobDraftId);


}
