
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

import edu.yu.einstein.wasp.service.JobResourceService;
import edu.yu.einstein.wasp.dao.JobResourceDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.JobResource;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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

