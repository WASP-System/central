
/**
 *
 * JobResourceDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobResource Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.JobResource;


public interface JobResourceDao extends WaspDao<JobResource> {

  public JobResource getJobResourceByJobResourceId (final Integer jobResourceId);

  public JobResource getJobResourceByResourceIdJobId (final Integer resourceId, final Integer jobId);


}

