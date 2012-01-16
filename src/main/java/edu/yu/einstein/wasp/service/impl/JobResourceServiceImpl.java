
/**
 *
 * JobResourceServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobResourceService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobResourceDao;
import edu.yu.einstein.wasp.model.JobResource;
import edu.yu.einstein.wasp.service.JobResourceService;

@Service
public class JobResourceServiceImpl extends WaspServiceImpl<JobResource> implements JobResourceService {

	/**
	 * jobResourceDao;
	 *
	 */
	private JobResourceDao jobResourceDao;

	/**
	 * setJobResourceDao(JobResourceDao jobResourceDao)
	 *
	 * @param jobResourceDao
	 *
	 */
	@Autowired
	public void setJobResourceDao(JobResourceDao jobResourceDao) {
		this.jobResourceDao = jobResourceDao;
		this.setWaspDao(jobResourceDao);
	}

	/**
	 * getJobResourceDao();
	 *
	 * @return jobResourceDao
	 *
	 */
	public JobResourceDao getJobResourceDao() {
		return this.jobResourceDao;
	}


  public JobResource getJobResourceByJobResourceId (final int jobResourceId) {
    return this.getJobResourceDao().getJobResourceByJobResourceId(jobResourceId);
  }

  public JobResource getJobResourceByResourceIdJobId (final int resourceId, final int jobId) {
    return this.getJobResourceDao().getJobResourceByResourceIdJobId(resourceId, jobId);
  }

}

