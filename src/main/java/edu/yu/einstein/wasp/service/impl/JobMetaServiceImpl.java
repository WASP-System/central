
/**
 *
 * JobMetaServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobMetaService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobMetaDao;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.service.JobMetaService;

@Service
public class JobMetaServiceImpl extends WaspMetaServiceImpl<JobMeta> implements JobMetaService {

	/**
	 * jobMetaDao;
	 *
	 */
	private JobMetaDao jobMetaDao;

	/**
	 * setJobMetaDao(JobMetaDao jobMetaDao)
	 *
	 * @param jobMetaDao
	 *
	 */
	@Autowired
	public void setJobMetaDao(JobMetaDao jobMetaDao) {
		this.jobMetaDao = jobMetaDao;
		this.setWaspDao(jobMetaDao);
	}

	/**
	 * getJobMetaDao();
	 *
	 * @return jobMetaDao
	 *
	 */
	public JobMetaDao getJobMetaDao() {
		return this.jobMetaDao;
	}


  public JobMeta getJobMetaByJobMetaId (final int jobMetaId) {
    return this.getJobMetaDao().getJobMetaByJobMetaId(jobMetaId);
  }

  public JobMeta getJobMetaByKJobId (final String k, final int jobId) {
    return this.getJobMetaDao().getJobMetaByKJobId(k, jobId);
  }

  public void updateByJobId (final String area, final int jobId, final List<JobMeta> metaList) {
    this.getJobMetaDao().updateByJobId(area, jobId, metaList); 
  }

  public void updateByJobId (final int jobId, final List<JobMeta> metaList) {
    this.getJobMetaDao().updateByJobId(jobId, metaList); 
  }


}

