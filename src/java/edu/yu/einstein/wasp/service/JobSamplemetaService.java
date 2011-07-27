
/**
 *
 * JobSamplemetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSamplemetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.JobSamplemetaDao;
import edu.yu.einstein.wasp.model.JobSamplemeta;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface JobSamplemetaService extends WaspService<JobSamplemeta> {

  public void setJobSamplemetaDao(JobSamplemetaDao jobSamplemetaDao);
  public JobSamplemetaDao getJobSamplemetaDao();

  public JobSamplemeta getJobSamplemetaByJobSamplemetaId (final int jobSamplemetaId);

  public JobSamplemeta getJobSamplemetaByKJobsampleId (final String k, final int jobsampleId);

  public void updateByJobsampleId (final int jobsampleId, final List<JobSamplemeta> metaList);

}

