
/**
 *
 * JobSampleMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSampleMetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.JobSampleMetaService;
import edu.yu.einstein.wasp.dao.JobSampleMetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.JobSampleMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobSampleMetaServiceImpl extends WaspServiceImpl<JobSampleMeta> implements JobSampleMetaService {

  private JobSampleMetaDao jobSampleMetaDao;
  @Autowired
  public void setJobSampleMetaDao(JobSampleMetaDao jobSampleMetaDao) {
    this.jobSampleMetaDao = jobSampleMetaDao;
    this.setWaspDao(jobSampleMetaDao);
  }
  public JobSampleMetaDao getJobSampleMetaDao() {
    return this.jobSampleMetaDao;
  }

  // **

  
  public JobSampleMeta getJobSampleMetaByJobSampleMetaId (final int jobSampleMetaId) {
    return this.getJobSampleMetaDao().getJobSampleMetaByJobSampleMetaId(jobSampleMetaId);
  }

  public JobSampleMeta getJobSampleMetaByKJobsampleId (final String k, final int jobsampleId) {
    return this.getJobSampleMetaDao().getJobSampleMetaByKJobsampleId(k, jobsampleId);
  }

  public void updateByJobsampleId (final int jobsampleId, final List<JobSampleMeta> metaList) {
    this.getJobSampleMetaDao().updateByJobsampleId(jobsampleId, metaList); 
  }

}

