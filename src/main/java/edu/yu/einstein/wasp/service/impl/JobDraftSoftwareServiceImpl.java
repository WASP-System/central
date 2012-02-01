
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobDraftSoftwareDao;
import edu.yu.einstein.wasp.model.JobDraftSoftware;
import edu.yu.einstein.wasp.service.JobDraftSoftwareService;

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
	@Override
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
	@Override
	public JobDraftSoftwareDao getJobDraftSoftwareDao() {
		return this.jobDraftSoftwareDao;
	}


  @Override
public JobDraftSoftware getJobDraftSoftwareByJobDraftSoftwareId (final Integer jobDraftSoftwareId) {
    return this.getJobDraftSoftwareDao().getJobDraftSoftwareByJobDraftSoftwareId(jobDraftSoftwareId);
  }

  @Override
public JobDraftSoftware getJobDraftSoftwareBySoftwareIdJobdraftId (final Integer softwareId, final Integer jobdraftId) {
    return this.getJobDraftSoftwareDao().getJobDraftSoftwareBySoftwareIdJobdraftId(softwareId, jobdraftId);
  }

}

