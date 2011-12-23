
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

import edu.yu.einstein.wasp.service.LabService;
import edu.yu.einstein.wasp.service.TaskService;
import edu.yu.einstein.wasp.service.StateService;
import edu.yu.einstein.wasp.service.RoleService;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.Role;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LabServiceImpl extends WaspServiceImpl<Lab> implements LabService {

	 @Autowired
	  private TaskService taskService;
	 @Autowired
	  private StateService stateService;
	 @Autowired
	  private RoleService roleService;
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
	public LabDao getLabDao() {
		return this.labDao;
	}


  public Lab getLabByLabId (final int labId) {
    return this.getLabDao().getLabByLabId(labId);
  }

  public Lab getLabByName (final String name) {
    return this.getLabDao().getLabByName(name);
  }

  public Lab getLabByPrimaryUserId (final int primaryUserId) {
    return this.getLabDao().getLabByPrimaryUserId(primaryUserId);
  }

  public int getLabManagerPendingTasks(int labId){
	  List<UserPending> newUsersPendingLmApprovalList = new ArrayList<UserPending>();
	  List<LabUser> existingUsersPendingLmApprovalList = new ArrayList<LabUser>();
	  List<Job> jobsPendingLmApprovalList = new ArrayList<Job>();
	  return getLabManagerPendingTasks(labId, newUsersPendingLmApprovalList, existingUsersPendingLmApprovalList, jobsPendingLmApprovalList);
  }
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
		for (LabUser labUser: (List<LabUser>) lab.getLabUser()){
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

  
}

