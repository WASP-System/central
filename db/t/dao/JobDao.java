
/**
 *
 * JobDao.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface JobDao extends WaspDao<Job> {

  public Job getJobByJobId (final int jobId);

  public Job getJobByNameLabId (final String name, final int labId);

}

