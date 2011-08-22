
/**
 *
 * JobUserService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobUserService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.JobUserDao;
import edu.yu.einstein.wasp.model.JobUser;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface JobUserService extends WaspService<JobUser> {

  public void setJobUserDao(JobUserDao jobUserDao);
  public JobUserDao getJobUserDao();

  public JobUser getJobUserByJobUserId (final int jobUserId);

  public JobUser getJobUserByJobIdUserId (final int jobId, final int UserId);

}

