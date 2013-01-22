
/**
 *
 * JobDraftMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.JobDraftMeta;


public interface JobDraftMetaDao extends WaspMetaDao<JobDraftMeta> {

  public JobDraftMeta getJobDraftMetaByJobDraftMetaId (final Integer jobDraftMetaId);

  public JobDraftMeta getJobDraftMetaByKJobDraftId (final String k, final Integer jobDraftId);

   public void replaceByJobDraftId(String area, int jobDraftId, List<JobDraftMeta> metaList);




}

