
/**
 *
 * JobUserServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobUserService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobUserDao;
import edu.yu.einstein.wasp.model.JobUser;
import edu.yu.einstein.wasp.service.JobUserService;

@Service
public class JobUserServiceImpl extends WaspServiceImpl<JobUser> implements JobUserService {

	/**
	 * jobUserDao;
	 *
	 */
	private JobUserDao jobUserDao;

	/**
	 * setJobUserDao(JobUserDao jobUserDao)
	 *
	 * @param jobUserDao
	 *
	 */
	@Autowired
	public void setJobUserDao(JobUserDao jobUserDao) {
		this.jobUserDao = jobUserDao;
		this.setWaspDao(jobUserDao);
	}

	/**
	 * getJobUserDao();
	 *
	 * @return jobUserDao
	 *
	 */
	public JobUserDao getJobUserDao() {
		return this.jobUserDao;
	}


  public JobUser getJobUserByJobUserId (final int jobUserId) {
    return this.getJobUserDao().getJobUserByJobUserId(jobUserId);
  }

  public JobUser getJobUserByJobIdUserId (final int jobId, final int UserId) {
    return this.getJobUserDao().getJobUserByJobIdUserId(jobId, UserId);
  }

}

