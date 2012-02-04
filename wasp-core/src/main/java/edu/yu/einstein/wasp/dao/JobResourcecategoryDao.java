
/**
 *
 * JobResourcecategoryDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobResourcecategory Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.JobResourcecategory;


public interface JobResourcecategoryDao extends WaspDao<JobResourcecategory> {

  public JobResourcecategory getJobResourcecategoryByJobResourcecategoryId (final Integer jobResourcecategoryId);

  public JobResourcecategory getJobResourcecategoryByResourcecategoryIdJobId (final Integer resourcecategoryId, final Integer jobId);


}

