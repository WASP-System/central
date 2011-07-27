
/**
 *
 * JobFileService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobFileService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.JobFileDao;
import edu.yu.einstein.wasp.model.JobFile;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface JobFileService extends WaspService<JobFile> {

  public void setJobFileDao(JobFileDao jobFileDao);
  public JobFileDao getJobFileDao();

  public JobFile getJobFileByJobFileId (final int jobFileId);

}

