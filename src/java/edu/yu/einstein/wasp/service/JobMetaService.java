
/**
 *
 * JobMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the JobMetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.JobMetaDao;
import edu.yu.einstein.wasp.model.JobMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface JobMetaService extends WaspService<JobMeta> {

  public void setJobMetaDao(JobMetaDao jobMetaDao);
  public JobMetaDao getJobMetaDao();

  public JobMeta getJobMetaByJobMetaId (final int jobMetaId);

  public JobMeta getJobMetaByKJobId (final String k, final int jobId);

  public void updateByJobId (final int jobId, final List<JobMeta> metaList);

}

