
/**
 *
 * JobSoftwareService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSoftwareService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobSoftwareDao;
import edu.yu.einstein.wasp.model.JobSoftware;

@Service
public interface JobSoftwareService extends WaspService<JobSoftware> {

	/**
	 * setJobSoftwareDao(JobSoftwareDao jobSoftwareDao)
	 *
	 * @param jobSoftwareDao
	 *
	 */
	public void setJobSoftwareDao(JobSoftwareDao jobSoftwareDao);

	/**
	 * getJobSoftwareDao();
	 *
	 * @return jobSoftwareDao
	 *
	 */
	public JobSoftwareDao getJobSoftwareDao();

  public JobSoftware getJobSoftwareByJobSoftwareId (final Integer jobSoftwareId);

  public JobSoftware getJobSoftwareBySoftwareIdJobId (final Integer softwareId, final Integer jobId);


}

