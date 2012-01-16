
/**
 *
 * JobFileServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobFileService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobFileDao;
import edu.yu.einstein.wasp.model.JobFile;
import edu.yu.einstein.wasp.service.JobFileService;

@Service
public class JobFileServiceImpl extends WaspServiceImpl<JobFile> implements JobFileService {

	/**
	 * jobFileDao;
	 *
	 */
	private JobFileDao jobFileDao;

	/**
	 * setJobFileDao(JobFileDao jobFileDao)
	 *
	 * @param jobFileDao
	 *
	 */
	@Autowired
	public void setJobFileDao(JobFileDao jobFileDao) {
		this.jobFileDao = jobFileDao;
		this.setWaspDao(jobFileDao);
	}

	/**
	 * getJobFileDao();
	 *
	 * @return jobFileDao
	 *
	 */
	public JobFileDao getJobFileDao() {
		return this.jobFileDao;
	}


  public JobFile getJobFileByJobFileId (final int jobFileId) {
    return this.getJobFileDao().getJobFileByJobFileId(jobFileId);
  }

}

