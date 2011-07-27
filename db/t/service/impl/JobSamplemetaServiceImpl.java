
/**
 *
 * JobSamplemetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSamplemetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.JobSamplemetaService;
import edu.yu.einstein.wasp.dao.JobSamplemetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.JobSamplemeta;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobSamplemetaServiceImpl extends WaspServiceImpl<JobSamplemeta> implements JobSamplemetaService {

  private JobSamplemetaDao jobSamplemetaDao;
  @Autowired
  public void setJobSamplemetaDao(JobSamplemetaDao jobSamplemetaDao) {
    this.jobSamplemetaDao = jobSamplemetaDao;
    this.setWaspDao(jobSamplemetaDao);
  }
  public JobSamplemetaDao getJobSamplemetaDao() {
    return this.jobSamplemetaDao;
  }

  // **

  
  public JobSamplemeta getJobSamplemetaByJobSamplemetaId (final int jobSamplemetaId) {
    return this.getJobSamplemetaDao().getJobSamplemetaByJobSamplemetaId(jobSamplemetaId);
  }

  public JobSamplemeta getJobSamplemetaByKJobsampleId (final String k, final int jobsampleId) {
    return this.getJobSamplemetaDao().getJobSamplemetaByKJobsampleId(k, jobsampleId);
  }

  public void updateByJobsampleId (final int jobsampleId, final List<JobSamplemeta> metaList) {
    this.getJobSamplemetaDao().updateByJobsampleId(jobsampleId, metaList); 
  }

}

