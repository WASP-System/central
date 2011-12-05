
/**
 *
 * JobResourceDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobResource Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface JobResourceDao extends WaspDao<JobResource> {

  public JobResource getJobResourceByJobResourceId (final int jobResourceId);

  public JobResource getJobResourceByResourceIdJobId (final int resourceId, final int jobId);


}

