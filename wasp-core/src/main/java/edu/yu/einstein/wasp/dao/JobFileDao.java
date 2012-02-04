
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

import edu.yu.einstein.wasp.model.JobFile;


public interface JobFileDao extends WaspDao<JobFile> {

  public JobFile getJobFileByJobFileId (final int jobFileId);


}

