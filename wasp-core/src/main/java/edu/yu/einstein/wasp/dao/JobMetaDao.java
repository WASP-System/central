
/**
 *
 * JobMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.JobMeta;


public interface JobMetaDao extends WaspDao<JobMeta> {

  public JobMeta getJobMetaByJobMetaId (final int jobMetaId);

  public JobMeta getJobMetaByKJobId (final String k, final int jobId);

  public void updateByJobId (final int jobId, final List<JobMeta> metaList);




}

