
/**
 *
 * JobService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.model.Job;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface JobService extends WaspService<Job> {

  public void setJobDao(JobDao jobDao);
  public JobDao getJobDao();

  public Job getJobByJobId (final int jobId);

  public Job getJobByNameLabId (final String name, final int labId);

}

