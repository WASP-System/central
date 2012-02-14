
/**
 *
 * LabServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the LabService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.model.Department;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.service.LabService;
import edu.yu.einstein.wasp.service.LabUserService;
import edu.yu.einstein.wasp.service.RoleService;
import edu.yu.einstein.wasp.service.StateService;
import edu.yu.einstein.wasp.service.TaskService;
import edu.yu.einstein.wasp.service.UserPendingService;

@Service
public class LabServiceImpl extends WaspServiceImpl<Lab> implements LabService {

	 @Autowired
	  private TaskService taskService;
	 @Autowired
	  private StateService stateService;
	 @Autowired
	  private RoleService roleService;
	 @Autowired
	  private UserPendingService userPendingService;
	 @Autowired
	  private LabUserService labUserService;
	/**
	 * labDao;
	 *
	 */
	private LabDao labDao;

	/**
	 * setLabDao(LabDao labDao)
	 *
	 * @param labDao
	 *
	 */
	@Override
	@Autowired
	public void setLabDao(LabDao labDao) {
		this.labDao = labDao;
		this.setWaspDao(labDao);
	}

	/**
	 * getLabDao();
	 *
	 * @return labDao
	 *
	 */
	@Override
	public LabDao getLabDao() {
		return this.labDao;
	}


  @Override
public Lab getLabByLabId (final int labId) {
    return this.getLabDao().getLabByLabId(labId);
  }

  @Override
public Lab getLabByName (final String name) {
    return this.getLabDao().getLabByName(name);
  }

  @Override
public Lab getLabByPrimaryUserId (final int primaryUserId) {
    return this.getLabDao().getLabByPrimaryUserId(primaryUserId);
  }

  @Override
public int getLabManagerPendingTasks(int labId){
	  List<UserPending> newUsersPendingLmApprovalList = new ArrayList<UserPending>();
	  List<LabUser> existingUsersPendingLmApprovalList = new ArrayList<LabUser>();
	  List<Job> jobsPendingLmApprovalList = new ArrayList<Job>();
	  return getLabManagerPendingTasks(labId, newUsersPendingLmApprovalList, existingUsersPendingLmApprovalList, jobsPendingLmApprovalList);
  }
  @Override
public int getLabManagerPendingTasks(int labId, List<UserPending> newUsersPendingLmApprovalList, List<LabUser> existingUsersPendingLmApprovalList, List<Job> jobsPendingLmApprovalList){
	  
	  //three tasks awaiting Lab Manager or PI from lab where labid = labId
	  //1. approve or reject new users that have applied to join a lab 
	  //2. approve or reject existing users that have applied to join a lab 
	  //3. approve or reject a new job submission
	  
	  //1 pending new users
	  Lab lab = getLabByLabId(labId);
	  //usersPendingLmApprovalList.addAll(lab.getUserPending());
	  for(UserPending userPending : lab.getUserPending()){
		  if( "PENDING".equals(userPending.getStatus()) ){
			  newUsersPendingLmApprovalList.add(userPending);
		  }
	  }	  
	  
	  //2 pending existing users
	  Role role = roleService.getRoleByName("Lab Member Pending");
	  if(role.getRoleId()==null){
		  //TODO: throw exception
	  }
		for (LabUser labUser: lab.getLabUser()){
			if (labUser.getRole().getRoleName().equals(role.getRoleName())){
				existingUsersPendingLmApprovalList.add(labUser);
			}
		}  
	  
	  //3 pending jobs
	  Map themap = new HashMap();
	  Task task = taskService.getTaskByIName("PI Approval");
	  if(task.getTaskId()==null){//unexpectedly not found
	    	//TODO: throw exception
	  }
	  themap.put("taskId", task.getTaskId());
	  themap.put("status", "CREATED");
	  List<State> stateList = stateService.findByMap(themap);//may need to be iterated over a few times
	  for( int i = 0; i < stateList.size(); i++){
			List<Statejob> statejobList = stateList.get(i).getStatejob();
			for(Statejob stateJob : statejobList){
				if(stateJob.getJob().getLab().getLabId().intValue() == labId){
					jobsPendingLmApprovalList.add(stateJob.getJob());
				}
			}
		}
	  
	  
	  return newUsersPendingLmApprovalList.size() + existingUsersPendingLmApprovalList.size() + jobsPendingLmApprovalList.size();
  }

  @Override
public int getAllLabManagerPendingTasks(){
	  List<UserPending> newUsersPendingLmApprovalList = new ArrayList<UserPending>();
	  List<LabUser> existingUsersPendingLmApprovalList = new ArrayList<LabUser>();
	  List<Job> jobsPendingLmApprovalList = new ArrayList<Job>();
	  return getAllLabManagerPendingTasks(newUsersPendingLmApprovalList, existingUsersPendingLmApprovalList, jobsPendingLmApprovalList);
  
  }
  @Override
public int getAllLabManagerPendingTasks(List<UserPending> newUsersPendingLmApprovalList, List<LabUser> existingUsersPendingLmApprovalList, List<Job> jobsPendingLmApprovalList){
	  
	  Map themap = new HashMap();
	  themap.put("status", "PENDING");
	  //themap.put("labId", "NOT NULL");//this won't work
	  List<UserPending> tempNewUsersPendingLmApprovalList = userPendingService.findByMap(themap);
	  //remove those with labId being NULL (as these are new PI's and will be dealt with at the DepartmentAdmin level (approving a new lab)
	  for(Iterator<UserPending> iter = tempNewUsersPendingLmApprovalList.iterator(); iter.hasNext();){//must go through iterator if want to remove object, or else get exception (http://www.rgagnon.com/javadetails/java-0619.html)
		  UserPending up = iter.next();
		  if(up.getLabId() != null){ 
			  newUsersPendingLmApprovalList.add(up);
		  }
	  }
	  
	  //2 pending existing users
	  Role role = roleService.getRoleByName("Lab Member Pending");	  
	  themap.clear();
	  themap.put("roleId", role.getRoleId());
	  List<LabUser> tempExistingUsersPendingLmApprovalList = labUserService.findByMap(themap);
	  for(LabUser lu : tempExistingUsersPendingLmApprovalList){
		  existingUsersPendingLmApprovalList.add(lu);
	  }
	  
	  //3 jobs awaiting PI approval	  
	  Task task = taskService.getTaskByIName("PI Approval");
	  themap.clear();
	  themap.put("taskId", task.getTaskId());
	  themap.put("status", "CREATED");
	  List<State> stateList = stateService.findByMap(themap);
	  for( int i = 0; i < stateList.size(); i++){
			List<Statejob> statejobList = stateList.get(i).getStatejob();
			for(Statejob stateJob : statejobList){				
				jobsPendingLmApprovalList.add(stateJob.getJob());
			}
		}
	
	  return newUsersPendingLmApprovalList.size() + existingUsersPendingLmApprovalList.size() + jobsPendingLmApprovalList.size();

  }
  
  @Override
  public List<Lab> getActiveLabs(){
	  Map queryMap = new HashMap();
	  queryMap.put("isActive", 1);
	  return this.findByMap(queryMap);
  }
  
}

