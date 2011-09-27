
/**
 *
 * TaskService.java 
 * @author echeng (table2type.pl)
 *  
 * the TaskService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.model.Task;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface TaskService extends WaspService<Task> {

	/**
	 * setTaskDao(TaskDao taskDao)
	 *
	 * @param taskDao
	 *
	 */
	public void setTaskDao(TaskDao taskDao);

	/**
	 * getTaskDao();
	 *
	 * @return taskDao
	 *
	 */
	public TaskDao getTaskDao();

  public Task getTaskByTaskId (final int taskId);

  public Task getTaskByIName (final String iName);


}

