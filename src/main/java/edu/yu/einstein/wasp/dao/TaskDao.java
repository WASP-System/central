
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

import java.util.List;

import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.model.TaskMapping;


public interface TaskDao extends WaspDao<Task> {

  public Task getTaskByTaskId (final int taskId);

  public Task getTaskByIName (final String iName);
 
  public List<State> getStatesByTaskIName (final String iName, final String status);

  List<TaskMapping> getTaskMappings();
  
}

