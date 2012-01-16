
/**
 *
 * JobSampleService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSampleService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobSampleDao;
import edu.yu.einstein.wasp.model.JobSample;

@Service
public interface JobSampleService extends WaspService<JobSample> {

	/**
	 * setJobSampleDao(JobSampleDao jobSampleDao)
	 *
	 * @param jobSampleDao
	 *
	 */
	public void setJobSampleDao(JobSampleDao jobSampleDao);

	/**
	 * getJobSampleDao();
	 *
	 * @return jobSampleDao
	 *
	 */
	public JobSampleDao getJobSampleDao();

  public JobSample getJobSampleByJobSampleId (final int jobSampleId);

  public JobSample getJobSampleByJobIdSampleId (final int jobId, final int sampleId);


}

