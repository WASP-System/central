
/**
 *
 * JobMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobMetaService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobMetaDao;
import edu.yu.einstein.wasp.model.JobMeta;

@Service
public interface JobMetaService extends WaspMetaService<JobMeta> {

	/**
	 * setJobMetaDao(JobMetaDao jobMetaDao)
	 *
	 * @param jobMetaDao
	 *
	 */
	public void setJobMetaDao(JobMetaDao jobMetaDao);

	/**
	 * getJobMetaDao();
	 *
	 * @return jobMetaDao
	 *
	 */
	public JobMetaDao getJobMetaDao();

  public JobMeta getJobMetaByJobMetaId (final int jobMetaId);

  public JobMeta getJobMetaByKJobId (final String k, final int jobId);


  public void updateByJobId (final String area, final int jobId, final List<JobMeta> metaList);

  public void updateByJobId (final int jobId, final List<JobMeta> metaList);


}

