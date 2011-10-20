
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

import edu.yu.einstein.wasp.service.JobMetaService;
import edu.yu.einstein.wasp.dao.JobMetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.JobMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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

