
/**
 *
 * JobFileService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobFileService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobFileDao;
import edu.yu.einstein.wasp.model.JobFile;

@Service
public interface JobFileService extends WaspService<JobFile> {

	/**
	 * setJobFileDao(JobFileDao jobFileDao)
	 *
	 * @param jobFileDao
	 *
	 */
	public void setJobFileDao(JobFileDao jobFileDao);

	/**
	 * getJobFileDao();
	 *
	 * @return jobFileDao
	 *
	 */
	public JobFileDao getJobFileDao();

  public JobFile getJobFileByJobFileId (final int jobFileId);


}

