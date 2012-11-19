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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.batch.core.extension.JobExplorerWasp;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.LabPendingDao;
import edu.yu.einstein.wasp.dao.LabUserDao;
import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.dao.TaskMappingDao;
import edu.yu.einstein.wasp.dao.UserPendingDao;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.TaskMapping;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.TaskService;

@Service
@Transactional("entityManager")
public class TaskServiceImpl extends WaspServiceImpl implements TaskService {

	private TaskMappingDao	taskMappingDao;
	
	private JobExplorerWasp jobExplorer;
	
	/**
	 * Set JobExplorerWasp
	 * @param jobExplorer
	 */
	@Autowired
	void setJobExplorer(JobExplorer jobExplorer){
		this.jobExplorer = (JobExplorerWasp) jobExplorer;
	}

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
		List<Job> allJobsAwaitingLmApproval = new ArrayList<Job>();
		for (Job job: jobService.getActiveJobs())
			if (jobService.isJobAwaitingPiApproval(job))
				allJobsAwaitingLmApproval.add(job);
		
		if (authenticationService.isSuperUser() || authenticationService.hasRole("fm-*") || authenticationService.hasRole("ft-*") || authenticationService.hasRole("ga-*")) {

			Map<String, Object> themap = new HashMap<String, Object>();
			themap.put("status", "PENDING");
			List<UserPending> tempNewUsersPendingLmApprovalList = userPendingDao.findByMap(themap);
			// remove those with labId being NULL (as these are new PI's and will be
			// dealt with at the DepartmentAdmin level (approving a new lab)
			for (Iterator<UserPending> iter = tempNewUsersPendingLmApprovalList.iterator(); iter.hasNext();) {
				// must go through iterator if want to remove object, or else get
				// exception (http://www.rgagnon.com/javadetails/java-0619.html)
				UserPending up = iter.next();
				if (up.getLabId() != null) {
					newUsersPendingLmApprovalList.add(up);
				}
			}

			// 2 pending existing users
			Role role = roleDao.getRoleByName("Lab Member Pending");
			themap.clear();
			themap.put("roleId", role.getRoleId());
			List<LabUser> tempExistingUsersPendingLmApprovalList = labUserDao.findByMap(themap);
			for (LabUser lu : tempExistingUsersPendingLmApprovalList) {
				existingUsersPendingLmApprovalList.add(lu);
			}

			// 3 jobs awaiting PI approval
			jobsPendingLmApprovalList.addAll(allJobsAwaitingLmApproval);

		}else if (authenticationService.hasRole("pi-*") || authenticationService.hasRole("lm-*") ){
			//determine the lab(s) these PI's or lm represent and get info according to their lab(s)
			//recall that any PI also has role of lm, so it is sufficient to ask for lm
		
			// get list of departmentId values for this authenticated user
			List<Integer> labIdList = new ArrayList<Integer>();
			for (String role : authenticationService.getRoles()) {
				if (role.startsWith("lm-")){
					Integer labId = authenticationService.getRoleValue(role);
					if (labId != null)
						labIdList.add(labId);
				}
			}
			
			for(int labId : labIdList){
				// 1 pending new users
				Lab lab = labDao.getLabByLabId(labId);
				// usersPendingLmApprovalList.addAll(lab.getUserPending());
				for (UserPending userPending : lab.getUserPending()) {
					if ("PENDING".equals(userPending.getStatus())) {
						newUsersPendingLmApprovalList.add(userPending);
					}
				}

				// 2 pending existing users
				Role role = roleDao.getRoleByName("Lab Member Pending");
				if (role.getRoleId() == null) {
					// TODO: throw exception
				}
				for (LabUser labUser : lab.getLabUser()) {
					if (labUser.getRole().getRoleName().equals(role.getRoleName())) {
						existingUsersPendingLmApprovalList.add(labUser);
					}
				}

				// 3 pending jobs
				for (Job job: allJobsAwaitingLmApproval){
					if (job.getLabId().intValue() == labId) {
						jobsPendingLmApprovalList.add(job);
					}
				}
			}
		}
		return newUsersPendingLmApprovalList.size() + existingUsersPendingLmApprovalList.size() + jobsPendingLmApprovalList.size();
	}

	@Override
	public int getDepartmentAdminPendingTasks() {
		List<LabPending> labsPendingDaApprovalList = new ArrayList<LabPending>();
		List<Job> jobsPendingDaApprovalList = new ArrayList<Job>();
		return getDepartmentAdminPendingTasks(labsPendingDaApprovalList, jobsPendingDaApprovalList);
	}

	@Override
	public int getDepartmentAdminPendingTasks(List<LabPending> labsPendingDaApprovalList, List<Job> jobsPendingDaApprovalList) {

		List<Job> allJobsPendingDaApproval = new ArrayList<Job>();
		for (Job job : jobService.getActiveJobs())
			if (jobService.isJobAwaitingDaApproval(job))
				allJobsPendingDaApproval.add(job);
		if (authenticationService.isSuperUser() || authenticationService.hasRole("fm-*") || authenticationService.hasRole("ft-*") || authenticationService.hasRole("ga")) {
			jobsPendingDaApprovalList.addAll(allJobsPendingDaApproval);
		} else if (authenticationService.hasRole("da-*") ){//if a departmental administrator
			// get list of departmentId values for this authenticated user
			List<Integer> departmentIdList = new ArrayList<Integer>();
			for (String role : authenticationService.getRoles()) {
				String[] splitRole = role.split("-");
				if (splitRole.length != 2) {
					continue;
				}
				if (splitRole[1].equals("*")) {
					continue;
				}
				if ("da".equals(splitRole[0])) {// department administrator
					try {
						departmentIdList.add(Integer.parseInt(splitRole[1]));
					} catch (Exception e) {
						continue;
					}
				}
			}
			if (departmentIdList.size() > 0) {
				for (int departmentId : departmentIdList) {
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
	
	// TODO: Everything above here needs to be refactored
	
		

	
	
}
