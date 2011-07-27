
/**
 *
 * JobSampleDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSampleDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface JobSampleDao extends WaspDao<JobSample> {

  public JobSample getJobSampleByJobSampleId (final int jobSampleId);

  public JobSample getJobSampleByJobIdSampleId (final int jobId, final int sampleId);

}

