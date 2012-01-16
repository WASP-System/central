
/**
 *
 * JobSampleMetaServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSampleMetaService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobSampleMetaDao;
import edu.yu.einstein.wasp.model.JobSampleMeta;
import edu.yu.einstein.wasp.service.JobSampleMetaService;

@Service
public class JobSampleMetaServiceImpl extends WaspMetaServiceImpl<JobSampleMeta> implements JobSampleMetaService {

	/**
	 * jobSampleMetaDao;
	 *
	 */
	private JobSampleMetaDao jobSampleMetaDao;

	/**
	 * setJobSampleMetaDao(JobSampleMetaDao jobSampleMetaDao)
	 *
	 * @param jobSampleMetaDao
	 *
	 */
	@Override
	@Autowired
	public void setJobSampleMetaDao(JobSampleMetaDao jobSampleMetaDao) {
		this.jobSampleMetaDao = jobSampleMetaDao;
		this.setWaspDao(jobSampleMetaDao);
	}

	/**
	 * getJobSampleMetaDao();
	 *
	 * @return jobSampleMetaDao
	 *
	 */
	@Override
	public JobSampleMetaDao getJobSampleMetaDao() {
		return this.jobSampleMetaDao;
	}


  @Override
public JobSampleMeta getJobSampleMetaByJobSampleMetaId (final int jobSampleMetaId) {
    return this.getJobSampleMetaDao().getJobSampleMetaByJobSampleMetaId(jobSampleMetaId);
  }

  @Override
public JobSampleMeta getJobSampleMetaByKJobsampleId (final String k, final int jobsampleId) {
    return this.getJobSampleMetaDao().getJobSampleMetaByKJobsampleId(k, jobsampleId);
  }

  @Override
public void updateByJobsampleId (final String area, final int jobsampleId, final List<JobSampleMeta> metaList) {
    this.getJobSampleMetaDao().updateByJobsampleId(area, jobsampleId, metaList); 
  }

  @Override
public void updateByJobsampleId (final int jobsampleId, final List<JobSampleMeta> metaList) {
    this.getJobSampleMetaDao().updateByJobsampleId(jobsampleId, metaList); 
  }


}

