
/**
 *
 * JobDraftMetaServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftMetaService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.JobDraftMetaService;
import edu.yu.einstein.wasp.dao.JobDraftMetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.JobDraftMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobDraftMetaServiceImpl extends WaspServiceImpl<JobDraftMeta> implements JobDraftMetaService {

	/**
	 * jobDraftMetaDao;
	 *
	 */
	private JobDraftMetaDao jobDraftMetaDao;

	/**
	 * setJobDraftMetaDao(JobDraftMetaDao jobDraftMetaDao)
	 *
	 * @param jobDraftMetaDao
	 *
	 */
	@Autowired
	public void setJobDraftMetaDao(JobDraftMetaDao jobDraftMetaDao) {
		this.jobDraftMetaDao = jobDraftMetaDao;
		this.setWaspDao(jobDraftMetaDao);
	}

	/**
	 * getJobDraftMetaDao();
	 *
	 * @return jobDraftMetaDao
	 *
	 */
	public JobDraftMetaDao getJobDraftMetaDao() {
		return this.jobDraftMetaDao;
	}


  public JobDraftMeta getJobDraftMetaByJobDraftMetaId (final int jobDraftMetaId) {
    return this.getJobDraftMetaDao().getJobDraftMetaByJobDraftMetaId(jobDraftMetaId);
  }

  public JobDraftMeta getJobDraftMetaByKJobdraftId (final String k, final int jobdraftId) {
    return this.getJobDraftMetaDao().getJobDraftMetaByKJobdraftId(k, jobdraftId);
  }

  public void updateByJobdraftId (final int jobdraftId, final List<JobDraftMeta> metaList) {
    this.getJobDraftMetaDao().updateByJobdraftId(jobdraftId, metaList); 
  }


}

