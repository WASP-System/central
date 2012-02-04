
/**
 *
 * JobResourceService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobResourceService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobResourceDao;
import edu.yu.einstein.wasp.model.JobResource;

@Service
public interface JobResourceService extends WaspService<JobResource> {

	/**
	 * setJobResourceDao(JobResourceDao jobResourceDao)
	 *
	 * @param jobResourceDao
	 *
	 */
	public void setJobResourceDao(JobResourceDao jobResourceDao);

	/**
	 * getJobResourceDao();
	 *
	 * @return jobResourceDao
	 *
	 */
	public JobResourceDao getJobResourceDao();

  public JobResource getJobResourceByJobResourceId (final Integer jobResourceId);

  public JobResource getJobResourceByResourceIdJobId (final Integer resourceId, final Integer jobId);


}

