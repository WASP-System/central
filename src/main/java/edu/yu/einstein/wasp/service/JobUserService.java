
/**
 *
 * JobUserService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobUserService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobUserDao;
import edu.yu.einstein.wasp.model.JobUser;

@Service
public interface JobUserService extends WaspService<JobUser> {

	/**
	 * setJobUserDao(JobUserDao jobUserDao)
	 *
	 * @param jobUserDao
	 *
	 */
	public void setJobUserDao(JobUserDao jobUserDao);

	/**
	 * getJobUserDao();
	 *
	 * @return jobUserDao
	 *
	 */
	public JobUserDao getJobUserDao();

  public JobUser getJobUserByJobUserId (final int jobUserId);

  public JobUser getJobUserByJobIdUserId (final int jobId, final int UserId);


}

