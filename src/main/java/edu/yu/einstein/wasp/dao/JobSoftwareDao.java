
/**
 *
 * JobSoftwareDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSoftware Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.JobSoftware;


public interface JobSoftwareDao extends WaspDao<JobSoftware> {

  public JobSoftware getJobSoftwareByJobSoftwareId (final Integer jobSoftwareId);

  public JobSoftware getJobSoftwareBySoftwareIdJobId (final Integer softwareId, final Integer jobId);


}

