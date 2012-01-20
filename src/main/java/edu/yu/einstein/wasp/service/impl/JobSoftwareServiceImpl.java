
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

import edu.yu.einstein.wasp.service.JobSoftwareService;
import edu.yu.einstein.wasp.dao.JobSoftwareDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.JobSoftware;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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
	public JobSoftwareDao getJobSoftwareDao() {
		return this.jobSoftwareDao;
	}


  public JobSoftware getJobSoftwareByJobSoftwareId (final Integer jobSoftwareId) {
    return this.getJobSoftwareDao().getJobSoftwareByJobSoftwareId(jobSoftwareId);
  }

  public JobSoftware getJobSoftwareBySoftwareIdJobId (final Integer softwareId, final Integer jobId) {
    return this.getJobSoftwareDao().getJobSoftwareBySoftwareIdJobId(softwareId, jobId);
  }

}

