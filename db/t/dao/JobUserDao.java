
/**
 *
 * JobUserDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobUserDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface JobUserDao extends WaspDao<JobUser> {

  public JobUser getJobUserByJobUserId (final int jobUserId);

  public JobUser getJobUserByJobIdUserId (final int jobId, final int UserId);

}

