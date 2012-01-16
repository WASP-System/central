
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

  public JobResource getJobResourceByJobResourceId (final int jobResourceId);

  public JobResource getJobResourceByResourceIdJobId (final int resourceId, final int jobId);


}

