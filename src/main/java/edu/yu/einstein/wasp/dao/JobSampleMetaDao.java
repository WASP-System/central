
/**
 *
 * JobSampleMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSampleMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface JobSampleMetaDao extends WaspDao<JobSampleMeta> {

  public JobSampleMeta getJobSampleMetaByJobSampleMetaId (final int jobSampleMetaId);

  public JobSampleMeta getJobSampleMetaByKJobsampleId (final String k, final int jobsampleId);

  public void updateByJobsampleId (final int jobsampleId, final List<JobSampleMeta> metaList);


}

