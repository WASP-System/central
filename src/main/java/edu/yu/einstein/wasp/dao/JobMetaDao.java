
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

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface JobMetaDao extends WaspDao<JobMeta> {

  public JobMeta getJobMetaByJobMetaId (final int jobMetaId);

  public JobMeta getJobMetaByKJobId (final String k, final int jobId);


  public void updateByJobId (final String area, final int jobId, final List<JobMeta> metaList);

  public void updateByJobId (final int jobId, final List<JobMeta> metaList);




}

