
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

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Task;

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

  public List<State> getStatesByTaskIName (final String iName, final String status);

  public List<State> getJobCreatedStates();

  public List<State> getQuoteJobStates();

  public List<State> getPiApprovedStates();

  public List<State> getDaApprovedStates();

  public List<State> getSampleReceivedStates();


}

