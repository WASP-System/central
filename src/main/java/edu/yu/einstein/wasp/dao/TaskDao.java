
/**
 *
 * TaskDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Task Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface TaskDao extends WaspDao<Task> {

  public Task getTaskByTaskId (final int taskId);

  public Task getTaskByIName (final String iName);


}

