
/**
 *
 * JobService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.model.Job;

@Service
public interface JobService extends WaspService<Job> {

	/**
	 * setJobDao(JobDao jobDao)
	 *
	 * @param jobDao
	 *
	 */
	public void setJobDao(JobDao jobDao);

	/**
	 * getJobDao();
	 *
	 * @return jobDao
	 *
	 */
	public JobDao getJobDao();

  public Job getJobByJobId (final int jobId);

  public Job getJobByNameLabId (final String name, final int labId);

  Map<Integer,List<Job>> getJobSamplesByWorkflow(final int workflowId);
}

