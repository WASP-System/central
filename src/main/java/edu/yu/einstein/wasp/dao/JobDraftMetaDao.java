
/**
 *
 * JobDraftMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftMetaDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface JobDraftMetaDao extends WaspDao<JobDraftMeta> {

  public JobDraftMeta getJobDraftMetaByJobDraftMetaId (final int jobDraftMetaId);

  public JobDraftMeta getJobDraftMetaByKJobdraftId (final String k, final int jobdraftId);

  public void updateByJobdraftId (final int jobdraftId, final List<JobDraftMeta> metaList);

}

