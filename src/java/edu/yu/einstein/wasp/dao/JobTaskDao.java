
/**
 *
 * JobTask.java 
 * @author echeng (table2type.pl)
 *  
 * the JobTask object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.*;
import edu.yu.einstein.wasp.model.*;


import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;


public interface JobTaskDao extends WaspDao<JobTask> {

  public JobTask getJobTaskByJobTaskId (final int jobTaskId);

  public JobTask getJobTaskByINameJobId (final String iName, final int jobId);

  public JobTask getJobTaskByNameJobId (final String name, final int jobId);

}

