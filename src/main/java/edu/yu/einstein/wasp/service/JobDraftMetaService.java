
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

import edu.yu.einstein.wasp.dao.JobDraftMetaDao;
import edu.yu.einstein.wasp.model.JobDraftMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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

  public JobDraftMeta getJobDraftMetaByJobDraftMetaId (final int jobDraftMetaId);

  public JobDraftMeta getJobDraftMetaByKJobdraftId (final String k, final int jobdraftId);


  public void updateByJobdraftId (final String area, final int jobdraftId, final List<JobDraftMeta> metaList);

  public void updateByJobdraftId (final int jobdraftId, final List<JobDraftMeta> metaList);


}

