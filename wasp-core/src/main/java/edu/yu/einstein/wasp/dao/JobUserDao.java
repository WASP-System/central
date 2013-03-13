
/**
 *
 * JobUserDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobUser Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.JobUser;


public interface JobUserDao extends WaspDao<JobUser> {

  public JobUser getJobUserByJobUserId (final int jobUserId);

  public JobUser getJobUserByJobIdUserId (final int jobId, final int userId);


}

