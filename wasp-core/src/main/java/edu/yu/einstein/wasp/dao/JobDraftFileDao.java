
/**
 *
 * JobFileDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobFile Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.JobDraftFile;


public interface JobDraftFileDao extends WaspDao<JobDraftFile> {

  public JobDraftFile getJobDraftFileByJobDraftFileId (final int jobDraftFileId);


}

