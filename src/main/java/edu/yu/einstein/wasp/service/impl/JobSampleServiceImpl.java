
/**
 *
 * JobSampleServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSampleService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobSampleDao;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.service.JobSampleService;

@Service
public class JobSampleServiceImpl extends WaspServiceImpl<JobSample> implements JobSampleService {

	/**
	 * jobSampleDao;
	 *
	 */
	private JobSampleDao jobSampleDao;

	/**
	 * setJobSampleDao(JobSampleDao jobSampleDao)
	 *
	 * @param jobSampleDao
	 *
	 */
	@Autowired
	public void setJobSampleDao(JobSampleDao jobSampleDao) {
		this.jobSampleDao = jobSampleDao;
		this.setWaspDao(jobSampleDao);
	}

	/**
	 * getJobSampleDao();
	 *
	 * @return jobSampleDao
	 *
	 */
	public JobSampleDao getJobSampleDao() {
		return this.jobSampleDao;
	}


  public JobSample getJobSampleByJobSampleId (final int jobSampleId) {
    return this.getJobSampleDao().getJobSampleByJobSampleId(jobSampleId);
  }

  public JobSample getJobSampleByJobIdSampleId (final int jobId, final int sampleId) {
    return this.getJobSampleDao().getJobSampleByJobIdSampleId(jobId, sampleId);
  }

}

