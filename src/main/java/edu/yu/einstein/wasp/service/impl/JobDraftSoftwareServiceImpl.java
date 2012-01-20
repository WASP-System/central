
/**
 *
 * JobDraftSoftwareServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftSoftwareService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.JobDraftSoftwareService;
import edu.yu.einstein.wasp.dao.JobDraftSoftwareDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.JobDraftSoftware;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobDraftSoftwareServiceImpl extends WaspServiceImpl<JobDraftSoftware> implements JobDraftSoftwareService {

	/**
	 * jobDraftSoftwareDao;
	 *
	 */
	private JobDraftSoftwareDao jobDraftSoftwareDao;

	/**
	 * setJobDraftSoftwareDao(JobDraftSoftwareDao jobDraftSoftwareDao)
	 *
	 * @param jobDraftSoftwareDao
	 *
	 */
	@Autowired
	public void setJobDraftSoftwareDao(JobDraftSoftwareDao jobDraftSoftwareDao) {
		this.jobDraftSoftwareDao = jobDraftSoftwareDao;
		this.setWaspDao(jobDraftSoftwareDao);
	}

	/**
	 * getJobDraftSoftwareDao();
	 *
	 * @return jobDraftSoftwareDao
	 *
	 */
	public JobDraftSoftwareDao getJobDraftSoftwareDao() {
		return this.jobDraftSoftwareDao;
	}


  public JobDraftSoftware getJobDraftSoftwareByJobDraftSoftwareId (final Integer jobDraftSoftwareId) {
    return this.getJobDraftSoftwareDao().getJobDraftSoftwareByJobDraftSoftwareId(jobDraftSoftwareId);
  }

  public JobDraftSoftware getJobDraftSoftwareBySoftwareIdJobdraftId (final Integer softwareId, final Integer jobdraftId) {
    return this.getJobDraftSoftwareDao().getJobDraftSoftwareBySoftwareIdJobdraftId(softwareId, jobdraftId);
  }

}

