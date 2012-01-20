
/**
 *
 * TaskServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the TaskService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import util.spring.SecurityUtil;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.model.TaskMapping;
import edu.yu.einstein.wasp.service.TaskService;

@Service
public class TaskServiceImpl extends WaspServiceImpl<Task> implements TaskService {

	/**
	 * taskDao;
	 *
	 */
	private TaskDao taskDao;

	/**
	 * setTaskDao(TaskDao taskDao)
	 *
	 * @param taskDao
	 *
	 */
	@Override
	@Autowired
	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
		this.setWaspDao(taskDao);
	}

	/**
	 * getTaskDao();
	 *
	 * @return taskDao
	 *
	 */
	@Override
	public TaskDao getTaskDao() {
		return this.taskDao;
	}

	
	
  @Override
public Task getTaskByTaskId (final int taskId) {
    return this.getTaskDao().getTaskByTaskId(taskId);
  }

  @Override
public Task getTaskByIName (final String iName) {
    return this.getTaskDao().getTaskByIName(iName);
  }


  @Override
public List<State> getStatesByTaskIName (final String iName, final String status) {
    return this.getTaskDao().getStatesByTaskIName(iName, status);
  }

  @Override
public List<State> getJobCreatedStates () {
    return getStatesByTaskIName("Start Job", "CREATED");
  }

  @Override
public List<State> getQuoteJobStates () {
    return getStatesByTaskIName("Quote Job", "QUOTED");
  }

  @Override
public List<State> getPiApprovedStates () {
    return getStatesByTaskIName("PI Approval", "APPROVED");
  }

  @Override
public List<State> getDaApprovedStates () {
    return getStatesByTaskIName("DA Approval", "APPROVED");
  }

  @Override
  public List<State> getSampleReceivedStates () {
    return getStatesByTaskIName("Receive Sample", "RECEIVED");
  }
  
  
  //returns list of states for the task in given status.
  //consults taskmapping table to see if currently logged in user is authorized to see them
  //only states that pass authorization are returned,. 
  public List<State> getStatesByTaskMappingRule(Task task, String status)  {
	  
	  List<State> result = new ArrayList<State>();
	  
	  for (State s:task.getState()) {
		  if (s.getStatus().equals(status)) {
			  result.add(s);			  
		  }
	  }
	  
	  if (result.isEmpty()) return result;
	  
	  for (TaskMapping m:getTaskMappings()) {
		  if (!m.getTaskId().equals(task.getTaskId())) continue;
		  
		  if (!m.getStatus().equals(status)) continue;
		  
		  
		  try {
			  if (!SecurityUtil.isAuthorized(m.getPermission())){
				  
				  continue;
			  }
		  } catch (Throwable e) {
			  throw new IllegalStateException("Cant authorize access "+m.getPermission(),e);
		  }
		  
		  return result;
		  
	  }
	  
	  return new ArrayList<State>();
  }

  public List<TaskMapping> getTaskMappings() {
		return this.taskDao.getTaskMappings();
  }
}

