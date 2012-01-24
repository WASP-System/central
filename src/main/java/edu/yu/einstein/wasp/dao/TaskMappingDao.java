
/**
 *
 * TaskMappingDao.java 
 * @author echeng (table2type.pl)
 *  
 * the TaskMapping Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface TaskMappingDao extends WaspDao<TaskMapping> {

  public TaskMapping getTaskMappingByTaskMappingId (final int taskMappingId);

  public TaskMapping getTaskMappingByTaskIdStatus (final int taskId, final String status);


}

