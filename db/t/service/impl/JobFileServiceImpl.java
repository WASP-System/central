
/**
 *
 * JobFileService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobFileService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.JobFileService;
import edu.yu.einstein.wasp.dao.JobFileDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.JobFile;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobFileServiceImpl extends WaspServiceImpl<JobFile> implements JobFileService {

  private JobFileDao jobFileDao;
  @Autowired
  public void setJobFileDao(JobFileDao jobFileDao) {
    this.jobFileDao = jobFileDao;
    this.setWaspDao(jobFileDao);
  }
  public JobFileDao getJobFileDao() {
    return this.jobFileDao;
  }

  // **

  
  public JobFile getJobFileByJobFileId (final int jobFileId) {
    return this.getJobFileDao().getJobFileByJobFileId(jobFileId);
  }
}

