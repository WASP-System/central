
/**
 *
 * JobSoftwareDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSoftware Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface JobSoftwareDao extends WaspDao<JobSoftware> {

  public JobSoftware getJobSoftwareByJobSoftwareId (final Integer jobSoftwareId);

  public JobSoftware getJobSoftwareBySoftwareIdJobId (final Integer softwareId, final Integer jobId);


}

