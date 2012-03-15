
/**
 *
 * JobDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Job Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;
import java.util.Map;

import edu.yu.einstein.wasp.model.Job;


public interface JobDao extends WaspDao<Job> {

  public Job getJobByJobId (final int jobId);

  public Job getJobByNameLabId (final String name, final int labId);

  public Map<Integer,List<Job>> getJobSamplesByWorkflow(final int workflowId);

  public List<Job> getActiveJobs();

}

