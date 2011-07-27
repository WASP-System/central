
/**
 *
 * JobSampleService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSampleService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.JobSampleDao;
import edu.yu.einstein.wasp.model.JobSample;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface JobSampleService extends WaspService<JobSample> {

  public void setJobSampleDao(JobSampleDao jobSampleDao);
  public JobSampleDao getJobSampleDao();

  public JobSample getJobSampleByJobSampleId (final int jobSampleId);

  public JobSample getJobSampleByJobIdSampleId (final int jobId, final int sampleId);

}

