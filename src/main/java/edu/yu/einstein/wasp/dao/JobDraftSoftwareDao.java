
/**
 *
 * JobDraftSoftwareDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftSoftware Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.JobDraftSoftware;


public interface JobDraftSoftwareDao extends WaspDao<JobDraftSoftware> {

  public JobDraftSoftware getJobDraftSoftwareByJobDraftSoftwareId (final Integer jobDraftSoftwareId);

  public JobDraftSoftware getJobDraftSoftwareBySoftwareIdJobdraftId (final Integer softwareId, final Integer jobdraftId);


}

