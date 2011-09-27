
/**
 *
 * JobDraftServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.JobDraftService;
import edu.yu.einstein.wasp.dao.JobDraftDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.JobDraft;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobDraftServiceImpl extends WaspServiceImpl<JobDraft> implements JobDraftService {

	/**
	 * jobDraftDao;
	 *
	 */
	private JobDraftDao jobDraftDao;

	/**
	 * setJobDraftDao(JobDraftDao jobDraftDao)
	 *
	 * @param jobDraftDao
	 *
	 */
	@Autowired
	public void setJobDraftDao(JobDraftDao jobDraftDao) {
		this.jobDraftDao = jobDraftDao;
		this.setWaspDao(jobDraftDao);
	}

	/**
	 * getJobDraftDao();
	 *
	 * @return jobDraftDao
	 *
	 */
	public JobDraftDao getJobDraftDao() {
		return this.jobDraftDao;
	}


  public JobDraft getJobDraftByJobDraftId (final int jobDraftId) {
    return this.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
  }

}

