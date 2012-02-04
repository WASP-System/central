
/**
 *
 * JobDraftMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftMetaService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobDraftMetaDao;
import edu.yu.einstein.wasp.model.JobDraftMeta;

@Service
public interface JobDraftMetaService extends WaspService<JobDraftMeta> {

	/**
	 * setJobDraftMetaDao(JobDraftMetaDao jobDraftMetaDao)
	 *
	 * @param jobDraftMetaDao
	 *
	 */
	public void setJobDraftMetaDao(JobDraftMetaDao jobDraftMetaDao);

	/**
	 * getJobDraftMetaDao();
	 *
	 * @return jobDraftMetaDao
	 *
	 */
	public JobDraftMetaDao getJobDraftMetaDao();

  public JobDraftMeta getJobDraftMetaByJobDraftMetaId (final Integer jobDraftMetaId);

  public JobDraftMeta getJobDraftMetaByKJobdraftId (final String k, final Integer jobdraftId);


  public void updateByJobdraftId (final String area, final int jobdraftId, final List<JobDraftMeta> metaList);

  public void updateByJobdraftId (final int jobdraftId, final List<JobDraftMeta> metaList);


}

