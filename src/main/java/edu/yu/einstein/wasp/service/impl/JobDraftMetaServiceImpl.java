
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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobDraftMetaDao;
import edu.yu.einstein.wasp.model.JobDraftMeta;
import edu.yu.einstein.wasp.service.JobDraftMetaService;

@Service
public class JobDraftMetaServiceImpl extends WaspMetaServiceImpl<JobDraftMeta> implements JobDraftMetaService {

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
	@Override
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
	@Override
	public JobDraftMetaDao getJobDraftMetaDao() {
		return this.jobDraftMetaDao;
	}


  @Override
public JobDraftMeta getJobDraftMetaByJobDraftMetaId (final int jobDraftMetaId) {
    return this.getJobDraftMetaDao().getJobDraftMetaByJobDraftMetaId(jobDraftMetaId);
  }

  @Override
public JobDraftMeta getJobDraftMetaByKJobdraftId (final String k, final int jobdraftId) {
    return this.getJobDraftMetaDao().getJobDraftMetaByKJobdraftId(k, jobdraftId);
  }

  @Override
public void updateByJobdraftId (final String area, final int jobdraftId, final List<JobDraftMeta> metaList) {
    this.getJobDraftMetaDao().updateByJobdraftId(area, jobdraftId, metaList); 
  }

  @Override
public void updateByJobdraftId (final int jobdraftId, final List<JobDraftMeta> metaList) {
    this.getJobDraftMetaDao().updateByJobdraftId(jobdraftId, metaList); 
  }


}

