
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

import edu.yu.einstein.wasp.dao.JobResourceDao;
import edu.yu.einstein.wasp.model.JobResource;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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

  public JobResource getJobResourceByJobResourceId (final int jobResourceId);

  public JobResource getJobResourceByResourceIdJobId (final int resourceId, final int jobId);


}

