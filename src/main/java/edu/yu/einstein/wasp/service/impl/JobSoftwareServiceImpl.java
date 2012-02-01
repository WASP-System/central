
/**
 *
 * JobSoftwareServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSoftwareService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobSoftwareDao;
import edu.yu.einstein.wasp.model.JobSoftware;
import edu.yu.einstein.wasp.service.JobSoftwareService;

@Service
public class JobSoftwareServiceImpl extends WaspServiceImpl<JobSoftware> implements JobSoftwareService {

	/**
	 * jobSoftwareDao;
	 *
	 */
	private JobSoftwareDao jobSoftwareDao;

	/**
	 * setJobSoftwareDao(JobSoftwareDao jobSoftwareDao)
	 *
	 * @param jobSoftwareDao
	 *
	 */
	@Override
	@Autowired
	public void setJobSoftwareDao(JobSoftwareDao jobSoftwareDao) {
		this.jobSoftwareDao = jobSoftwareDao;
		this.setWaspDao(jobSoftwareDao);
	}

	/**
	 * getJobSoftwareDao();
	 *
	 * @return jobSoftwareDao
	 *
	 */
	@Override
	public JobSoftwareDao getJobSoftwareDao() {
		return this.jobSoftwareDao;
	}


  @Override
public JobSoftware getJobSoftwareByJobSoftwareId (final Integer jobSoftwareId) {
    return this.getJobSoftwareDao().getJobSoftwareByJobSoftwareId(jobSoftwareId);
  }

  @Override
public JobSoftware getJobSoftwareBySoftwareIdJobId (final Integer softwareId, final Integer jobId) {
    return this.getJobSoftwareDao().getJobSoftwareBySoftwareIdJobId(softwareId, jobId);
  }

}

