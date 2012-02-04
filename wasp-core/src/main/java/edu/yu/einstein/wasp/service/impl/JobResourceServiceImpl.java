
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
	@Override
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
	@Override
	public JobResourceDao getJobResourceDao() {
		return this.jobResourceDao;
	}


  @Override
public JobResource getJobResourceByJobResourceId (final Integer jobResourceId) {
    return this.getJobResourceDao().getJobResourceByJobResourceId(jobResourceId);
  }

  @Override
public JobResource getJobResourceByResourceIdJobId (final Integer resourceId, final Integer jobId) {
    return this.getJobResourceDao().getJobResourceByResourceIdJobId(resourceId, jobId);
  }

}

