
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

import edu.yu.einstein.wasp.service.JobFileService;
import edu.yu.einstein.wasp.dao.JobFileDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.JobFile;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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

