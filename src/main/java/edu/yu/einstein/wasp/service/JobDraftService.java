
/**
 *
 * JobDraftService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.JobDraftDao;
import edu.yu.einstein.wasp.model.JobDraft;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface JobDraftService extends WaspService<JobDraft> {

  public void setJobDraftDao(JobDraftDao jobDraftDao);
  public JobDraftDao getJobDraftDao();

  public JobDraft getJobDraftByJobDraftId (final int jobDraftId);

}

