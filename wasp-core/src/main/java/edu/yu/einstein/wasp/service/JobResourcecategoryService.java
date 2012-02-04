
/**
 *
 * JobResourcecategoryService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobResourcecategoryService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobResourcecategoryDao;
import edu.yu.einstein.wasp.model.JobResourcecategory;

@Service
public interface JobResourcecategoryService extends WaspService<JobResourcecategory> {

	/**
	 * setJobResourcecategoryDao(JobResourcecategoryDao jobResourcecategoryDao)
	 *
	 * @param jobResourcecategoryDao
	 *
	 */
	public void setJobResourcecategoryDao(JobResourcecategoryDao jobResourcecategoryDao);

	/**
	 * getJobResourcecategoryDao();
	 *
	 * @return jobResourcecategoryDao
	 *
	 */
	public JobResourcecategoryDao getJobResourcecategoryDao();

  public JobResourcecategory getJobResourcecategoryByJobResourcecategoryId (final Integer jobResourcecategoryId);

  public JobResourcecategory getJobResourcecategoryByResourcecategoryIdJobId (final Integer resourcecategoryId, final Integer jobId);


}

