
/**
 *
 * JobSampleDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSample Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.JobSample;


public interface JobSampleDao extends WaspDao<JobSample> {

  public JobSample getJobSampleByJobSampleId (final int jobSampleId);

  public JobSample getJobSampleByJobIdSampleId (final int jobId, final int sampleId);

  public List<JobSample> getJobSampleByJobId(int jobId);
  
}

