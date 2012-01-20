
/**
 *
 * JobResourcecategoryServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobResourcecategoryService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.JobResourcecategoryService;
import edu.yu.einstein.wasp.dao.JobResourcecategoryDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.JobResourcecategory;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobResourcecategoryServiceImpl extends WaspServiceImpl<JobResourcecategory> implements JobResourcecategoryService {

	/**
	 * jobResourcecategoryDao;
	 *
	 */
	private JobResourcecategoryDao jobResourcecategoryDao;

	/**
	 * setJobResourcecategoryDao(JobResourcecategoryDao jobResourcecategoryDao)
	 *
	 * @param jobResourcecategoryDao
	 *
	 */
	@Autowired
	public void setJobResourcecategoryDao(JobResourcecategoryDao jobResourcecategoryDao) {
		this.jobResourcecategoryDao = jobResourcecategoryDao;
		this.setWaspDao(jobResourcecategoryDao);
	}

	/**
	 * getJobResourcecategoryDao();
	 *
	 * @return jobResourcecategoryDao
	 *
	 */
	public JobResourcecategoryDao getJobResourcecategoryDao() {
		return this.jobResourcecategoryDao;
	}


  public JobResourcecategory getJobResourcecategoryByJobResourcecategoryId (final Integer jobResourcecategoryId) {
    return this.getJobResourcecategoryDao().getJobResourcecategoryByJobResourcecategoryId(jobResourcecategoryId);
  }

  public JobResourcecategory getJobResourcecategoryByResourcecategoryIdJobId (final Integer resourcecategoryId, final Integer jobId) {
    return this.getJobResourcecategoryDao().getJobResourcecategoryByResourcecategoryIdJobId(resourcecategoryId, jobId);
  }

}

