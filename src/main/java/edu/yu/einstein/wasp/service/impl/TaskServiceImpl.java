
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
import java.util.Iterator;
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
  
  private String expand(String perm, State state) {
	  
	  if (
			  state==null 
			  || state.getStatejob()==null 
			  || state.getStatejob().isEmpty() 			  
			  ||state.getStatejob().get(0).getJob()==null 
			  || state.getStatejob().get(0).getJob().getLab()==null	  
	  ) return perm;
	  
	  perm=perm.replaceAll("#jobId", state.getStatejob().get(0).getJobId()+"");
	  perm=perm.replaceAll("#labId", state.getStatejob().get(0).getJob().getLabId()+"");
	  perm=perm.replaceAll("#departmentId", state.getStatejob().get(0).getJob().getLab().getDepartmentId()+"");
	  
	  return perm;
	  
  }
  
  //returns list of states for the task in given status.
  //consults taskmapping table to see if currently logged in user is authorized to see them
  //only states that pass authorization are returned,. 
  public List<State> getStatesByTaskMappingRule(Task task, String status)  {
	  
	  List<State> candidates = new ArrayList<State>();
	  
	  //filter out states that are not in right status
	  for (State s:task.getState()) {
		  if (s.getStatus().equals(status)) {
			  candidates.add(s);			  
		  }
	  }
	  
	  if (candidates.isEmpty()) return candidates;
	  
	  List<State> result = new ArrayList<State>();
	  
	  //further filter out states that do not match any of the task mappings
	  for (TaskMapping m:getTaskMappings()) {
		  if (!m.getTaskId().equals(task.getTaskId())) continue;
		  
		  if (!m.getStatus().equals(status)) continue;
		  
		  // let's just stick to jobId -> stateJob.jobId, labId->stateJob.job.labId,  departmentId stateJob.job.lab.departmentId
		  
		  
		  for(Iterator<State> it=candidates.iterator();it.hasNext();) {
			  State state=it.next();
			  String perm = expand(m.getPermission(), state);
			  try {
			  if (SecurityUtil.isAuthorized(perm)) {//check if user can see this state
				  result.add(state); 				//yes, he can! add it to result 
				  it.remove();						//and remove from the list of candidates
			  }
			  } catch (Throwable e) {
				  throw new IllegalStateException("Cant authorize access "+perm+"|"+m.getPermission(),e);
			  }
		  }
		  
		  if (candidates.isEmpty()) return result;
		 
		  
	  }
	  
	  return candidates;
	  
  }
  
  // returns states that
  // a) are in the given status
  // b) are accessible by current user according to the given permission   
  public List<State> filterStatesByStatusAndPermission(List<State> states, String status,  String permission)  {
	  
	  List<State> candidates = new ArrayList<State>();
	  
	  //filter out states that are not in right status
	  for (State s:states) {
		  if (s.getStatus().equals(status)) {
			  candidates.add(s);			  
		  }
	  }
	  
	  if (candidates.isEmpty()) return candidates;
	  
	  List<State> result = new ArrayList<State>();	  
	  
      if (permission.indexOf('#') == -1) {//no '#' means static permission. no needs to call "expand" function.   
			try {
				return SecurityUtil.isAuthorized(permission) ? candidates : result;
			} catch (Throwable e) {
				throw new IllegalStateException("Cant authorize access " + permission, e);
			}
	  }
	  
	 
	   // let's just stick to jobId -> stateJob.jobId, labId->stateJob.job.labId,  departmentId stateJob.job.lab.departmentId
		  		  
	  for(Iterator<State> it=candidates.iterator();it.hasNext();) {
		  State state=it.next();
			  String perm = expand(permission, state);
			  try {
			  if (SecurityUtil.isAuthorized(perm)) {//check if user can see this state
				  result.add(state); 				//yes, he can! add it to result 
				  it.remove();						//and remove from the list of candidates
			  }
			  } catch (Throwable e) {
				  throw new IllegalStateException("Cant authorize access "+perm+"|"+permission,e);
			  }
	  }
		  
	  return result;
  }
		  
	  
	  

  public List<TaskMapping> getTaskMappings() {
		return this.taskDao.getTaskMappings();
  }
}

