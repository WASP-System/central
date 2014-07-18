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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.LabPendingDao;
import edu.yu.einstein.wasp.dao.LabUserDao;
import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.dao.UserPendingDao;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.TaskService;

@Service
@Transactional("entityManager")
public class TaskServiceImpl extends WaspServiceImpl implements TaskService {


	@Autowired
	private UserPendingDao			userPendingDao;

	@Autowired
	private RoleDao					roleDao;

	@Autowired
	private LabUserDao				labUserDao;

	@Autowired
	private LabDao					labDao;

	@Autowired
	private LabPendingDao			labPendingDao;

	@Autowired
	private AuthenticationService	authenticationService;
	
	@Autowired
	private JobService jobService;
	
	@Override
	public boolean isLabManagerPendingTasks(List<Job> activeJobs) {
		List<Job> allJobsAwaitingLmApproval = new ArrayList<Job>();
		Role pendingLabmemberRole = roleDao.getRoleByName("Lab Member Pending");
		for (Job job: activeJobs){
			if (jobService.isJobAwaitingPiApproval(job)){
				if (authenticationService.isSuperUser())
					return true;
				allJobsAwaitingLmApproval.add(job);
			}
		}
		Map<String, Object> userSearchMap = new HashMap<String, Object>();
		userSearchMap.put("status", "PENDING");
		
		List<UserPending> newUsersPendingLmApprovalList = new ArrayList<UserPending>();
		for (UserPending up : userPendingDao.findByMap(userSearchMap)){
			if (up.getLabId() != null){
				if (authenticationService.isSuperUser())
					return true;
				newUsersPendingLmApprovalList.add(up);
			}
		}
		
		Map<String, Object> roleSearchMap = new HashMap<String, Object>();
		roleSearchMap.put("roleId", pendingLabmemberRole.getRoleId());
		if (authenticationService.isSuperUser() && !labUserDao.findByMap(roleSearchMap).isEmpty())
			return true;
		
		for(int labId : authenticationService.idsOfLabsManagedByCurrentUser()){
			Lab lab = labDao.getLabByLabId(labId);
			for (UserPending userPending : lab.getUserPending()) 
				if ("PENDING".equals(userPending.getStatus())) 
					return true;
			for (LabUser labUser : lab.getLabUser())
				if (labUser.getRole().getRoleName().equals(pendingLabmemberRole.getRoleName()))
					return true;
			for (Job job: allJobsAwaitingLmApproval){
				if (job.getLabId().intValue() == labId)
					return true;
			}
		}
		return false;
	}
	
	@Override
	public int getLabManagerPendingTasks() {
		List<UserPending> newUsersPendingLmApprovalList = new ArrayList<UserPending>();
		List<LabUser> existingUsersPendingLmApprovalList = new ArrayList<LabUser>();
		List<Job> jobsPendingLmApprovalList = new ArrayList<Job>();
		return getLabManagerPendingTasks(newUsersPendingLmApprovalList, existingUsersPendingLmApprovalList, jobsPendingLmApprovalList);

	}

	@Override
	public int getLabManagerPendingTasks(List<UserPending> newUsersPendingLmApprovalList, List<LabUser> existingUsersPendingLmApprovalList, List<Job> jobsPendingLmApprovalList) {

		// three tasks awaiting Lab Manager or PI from lab where labid = labId
		// 1. approve or reject new users that have applied to join a lab
		// 2. approve or reject existing users that have applied to join a lab
		// 3. approve or reject a new job submission
		// however if the user is superuser, ft, fm, and ga, then don't filter by lab_id
		Role pendingLabmemberRole = roleDao.getRoleByName("Lab Member Pending");
		
		List<Job> allJobsAwaitingLmApproval = jobService.getJobsAwaitingPiLmApproval();
		
		if (authenticationService.isSuperUser()) {
		
			// remove those with labId being NULL (as these are new PI's and will be
			// dealt with at the DepartmentAdmin level (approving a new lab)
			Map<String, Object> userSearchMap = new HashMap<String, Object>();
			userSearchMap.put("status", "PENDING");
			
			for (UserPending up : userPendingDao.findByMap(userSearchMap))
				if (up.getLabId() != null)
					newUsersPendingLmApprovalList.add(up);

			// 2 pending existing users
			Map<String, Object> roleSearchMap = new HashMap<String, Object>();
			roleSearchMap.put("roleId", pendingLabmemberRole.getRoleId());
			existingUsersPendingLmApprovalList.addAll(labUserDao.findByMap(roleSearchMap));

			// 3 jobs awaiting PI approval
			jobsPendingLmApprovalList.addAll(allJobsAwaitingLmApproval);

		}else{
			for(int labId : authenticationService.idsOfLabsManagedByCurrentUser()){
				// 1 pending new users
				Lab lab = labDao.getLabByLabId(labId);
				
				// usersPendingLmApprovalList.addAll(lab.getUserPending());
				for (UserPending userPending : lab.getUserPending()) 
					if ("PENDING".equals(userPending.getStatus())) 
						newUsersPendingLmApprovalList.add(userPending);

				// 2 pending existing users
				for (LabUser labUser : lab.getLabUser())
					if (labUser.getRole().getRoleName().equals(pendingLabmemberRole.getRoleName()))
						existingUsersPendingLmApprovalList.add(labUser);

				// 3 pending jobs
				for (Job job: allJobsAwaitingLmApproval){
					if (job.getLabId().intValue() == labId)
						jobsPendingLmApprovalList.add(job);
				}
			}
		}
		return newUsersPendingLmApprovalList.size() + existingUsersPendingLmApprovalList.size() + jobsPendingLmApprovalList.size();
	}
	
	@Override
	public boolean isDepartmentAdminPendingTasks(List<Job> activeJobs) {
		Map<String, Object> labPendingSearchMap = new HashMap<String, Object>();
		labPendingSearchMap.put("status", "PENDING");
		if ( (authenticationService.isSuperUser() || authenticationService.hasRole("ga")) && 
				!labPendingDao.findByMap(labPendingSearchMap).isEmpty() )
			return true;
		Set<Integer> departmentIdList = authenticationService.idsOfDepartmentsManagedByCurrentUser();
		for (int departmentId : departmentIdList) {
			labPendingSearchMap.put("departmentId", departmentId); 
			if (!labPendingDao.findByMap(labPendingSearchMap).isEmpty())
				return true;
		}
		for (Job job : activeJobs){
			if (!jobService.isJobAwaitingDaApproval(job))
				continue;
			if (authenticationService.isSuperUser() || authenticationService.hasRole("ga"))
				return true;
			for (int departmentId : departmentIdList) {
				if (job.getLab().getDepartmentId().intValue() == departmentId)
					return true;
			}
		}
		return false;
	}

	@Override
	public int getDepartmentAdminPendingTasks() {
		List<LabPending> labsPendingDaApprovalList = new ArrayList<LabPending>();
		List<Job> jobsPendingDaApprovalList = new ArrayList<Job>();
		return getDepartmentAdminPendingTasks(labsPendingDaApprovalList, jobsPendingDaApprovalList);
	}

	@Override
	public int getDepartmentAdminPendingTasks(List<LabPending> labsPendingDaApprovalList, List<Job> jobsPendingDaApprovalList) {
		List<Job> allJobsPendingDaApproval = jobService.getJobsAwaitingDaApproval();
		Map<String, Object> labPendingSearchMap = new HashMap<String, Object>();
		labPendingSearchMap.put("status", "PENDING");
		
		if (authenticationService.isSuperUser() || authenticationService.hasRole("ga")){
			jobsPendingDaApprovalList.addAll(allJobsPendingDaApproval);
			labsPendingDaApprovalList.addAll(labPendingDao.findByMap(labPendingSearchMap));
		} else {
			// get list of departmentId values for this authenticated user
			Set<Integer> departmentIdList = authenticationService.idsOfDepartmentsManagedByCurrentUser();
			if (! departmentIdList.isEmpty()){
				for (int departmentId : departmentIdList) {
					labPendingSearchMap.put("departmentId", departmentId); 
					labsPendingDaApprovalList.addAll(labPendingDao.findByMap(labPendingSearchMap));
					for (Job job : allJobsPendingDaApproval){
						if (job.getLab().getDepartmentId().intValue() == departmentId) {
							jobsPendingDaApprovalList.add(job);
						}
					}
				}
			}
		}
		// total number of tasksPendingDaApproval
		return labsPendingDaApprovalList.size() + jobsPendingDaApprovalList.size();
	}
	
	@Override
	public boolean isFmPendingTasks(List<Job> activeJobs) {
		for (Job job : activeJobs)
			if (jobService.isJobAwaitingFmApproval(job))
				return true;
		return false;
	}
	
	
}
