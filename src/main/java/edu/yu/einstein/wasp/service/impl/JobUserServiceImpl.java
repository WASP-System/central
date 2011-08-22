
/**
 *
 * JobUserService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobUserService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.JobUserService;
import edu.yu.einstein.wasp.dao.JobUserDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.JobUser;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobUserServiceImpl extends WaspServiceImpl<JobUser> implements JobUserService {

  private JobUserDao jobUserDao;
  @Autowired
  public void setJobUserDao(JobUserDao jobUserDao) {
    this.jobUserDao = jobUserDao;
    this.setWaspDao(jobUserDao);
  }
  public JobUserDao getJobUserDao() {
    return this.jobUserDao;
  }

  // **

  
  public JobUser getJobUserByJobUserId (final int jobUserId) {
    return this.getJobUserDao().getJobUserByJobUserId(jobUserId);
  }

  public JobUser getJobUserByJobIdUserId (final int jobId, final int UserId) {
    return this.getJobUserDao().getJobUserByJobIdUserId(jobId, UserId);
  }
}

