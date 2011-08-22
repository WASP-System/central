
/**
 *
 * JobSampleMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSampleMetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.JobSampleMetaDao;
import edu.yu.einstein.wasp.model.JobSampleMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface JobSampleMetaService extends WaspService<JobSampleMeta> {

  public void setJobSampleMetaDao(JobSampleMetaDao jobSampleMetaDao);
  public JobSampleMetaDao getJobSampleMetaDao();

  public JobSampleMeta getJobSampleMetaByJobSampleMetaId (final int jobSampleMetaId);

  public JobSampleMeta getJobSampleMetaByKJobsampleId (final String k, final int jobsampleId);

  public void updateByJobsampleId (final int jobsampleId, final List<JobSampleMeta> metaList);

}

