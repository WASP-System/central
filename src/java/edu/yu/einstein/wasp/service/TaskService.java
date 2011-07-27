
/**
 *
 * TaskService.java 
 * @author echeng (table2type.pl)
 *  
 * the TaskService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.model.Task;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface TaskService extends WaspService<Task> {

  public void setTaskDao(TaskDao taskDao);
  public TaskDao getTaskDao();

  public Task getTaskByTaskId (final int taskId);

  public Task getTaskByIName (final String iName);

}

