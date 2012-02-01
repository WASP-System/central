
/**
 *
 * JobDraftSoftwareService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftSoftwareService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobDraftSoftwareDao;
import edu.yu.einstein.wasp.model.JobDraftSoftware;

@Service
public interface JobDraftSoftwareService extends WaspService<JobDraftSoftware> {

	/**
	 * setJobDraftSoftwareDao(JobDraftSoftwareDao jobDraftSoftwareDao)
	 *
	 * @param jobDraftSoftwareDao
	 *
	 */
	public void setJobDraftSoftwareDao(JobDraftSoftwareDao jobDraftSoftwareDao);

	/**
	 * getJobDraftSoftwareDao();
	 *
	 * @return jobDraftSoftwareDao
	 *
	 */
	public JobDraftSoftwareDao getJobDraftSoftwareDao();

  public JobDraftSoftware getJobDraftSoftwareByJobDraftSoftwareId (final Integer jobDraftSoftwareId);

  public JobDraftSoftware getJobDraftSoftwareBySoftwareIdJobdraftId (final Integer softwareId, final Integer jobdraftId);


}

