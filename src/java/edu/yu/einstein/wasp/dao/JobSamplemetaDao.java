
/**
 *
 * JobSamplemetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSamplemetaDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface JobSamplemetaDao extends WaspDao<JobSamplemeta> {

  public JobSamplemeta getJobSamplemetaByJobSamplemetaId (final int jobSamplemetaId);

  public JobSamplemeta getJobSamplemetaByKJobsampleId (final String k, final int jobsampleId);

  public void updateByJobsampleId (final int jobsampleId, final List<JobSamplemeta> metaList);

}

