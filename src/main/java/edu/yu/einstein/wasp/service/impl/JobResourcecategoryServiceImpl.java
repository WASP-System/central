
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobResourcecategoryDao;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.service.JobResourcecategoryService;

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
	@Override
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
	@Override
	public JobResourcecategoryDao getJobResourcecategoryDao() {
		return this.jobResourcecategoryDao;
	}


  @Override
public JobResourcecategory getJobResourcecategoryByJobResourcecategoryId (final Integer jobResourcecategoryId) {
    return this.getJobResourcecategoryDao().getJobResourcecategoryByJobResourcecategoryId(jobResourcecategoryId);
  }

  @Override
public JobResourcecategory getJobResourcecategoryByResourcecategoryIdJobId (final Integer resourcecategoryId, final Integer jobId) {
    return this.getJobResourcecategoryDao().getJobResourcecategoryByResourcecategoryIdJobId(resourcecategoryId, jobId);
  }

}

