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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import util.spring.SecurityUtil;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.LabPendingDao;
import edu.yu.einstein.wasp.dao.LabUserDao;
import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.dao.TaskMappingDao;
import edu.yu.einstein.wasp.dao.UserPendingDao;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.model.TaskMapping;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.TaskService;

@Service
public class TaskServiceImpl extends WaspServiceImpl implements TaskService {

	/**
	 * taskDao;
	 * 
	 */
	private TaskDao	taskDao;

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

	@Autowired
	private UserPendingDao			userPendingDao;

	@Autowired
	private TaskMappingDao			taskMappingDao;

	@Autowired
	private StateDao				stateDao;

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

	@Override
	public List<State> getJobCreatedStates() {
		return taskDao.getStatesByTaskIName("Start Job", "CREATED");
	}

	@Override
	public List<State> getQuoteJobStates() {
		return taskDao.getStatesByTaskIName("Quote Job", "COMPLETED");
	}

	@Override
	public List<State> getJob2QuoteStates() {
		return taskDao.getStatesByTaskIName("Quote Job", "CREATED");
	}

	@Override
	public List<State> getPiApprovedStates() {
		return taskDao.getStatesByTaskIName("PI Approval", "COMPLETED");
	}

	@Override
	public List<State> getDaApprovedStates() {
		return taskDao.getStatesByTaskIName("DA Approval", "COMPLETED");
	}

	@Override
	public List<State> getSampleReceivedStates() {
		return taskDao.getStatesByTaskIName("Receive Sample", "COMPLETED");
	}

	private String expand(String perm, State state) {

		if (state == null || state.getStatejob() == null || state.getStatejob().isEmpty() || state.getStatejob().get(0).getJob() == null || state.getStatejob().get(0).getJob().getLab() == null)
			return perm;

		perm = perm.replaceAll("#jobId", state.getStatejob().get(0).getJobId() + "");
		perm = perm.replaceAll("#labId", state.getStatejob().get(0).getJob().getLabId() + "");
		perm = perm.replaceAll("#departmentId", state.getStatejob().get(0).getJob().getLab().getDepartmentId() + "");

		return perm;

	}

	// returns list of states for the task in given status.
	// consults taskmapping table to see if currently logged in user is
	// authorized to see them
	// only states that pass authorization are returned,.
	@Override
	public List<State> getStatesByTaskMappingRule(Task task, String status) {

		List<State> candidates = new ArrayList<State>();

		// filter out states that are not in right status
		for (State s : task.getState()) {
			if (s.getStatus().equals(status)) {
				candidates.add(s);
			}
		}

		if (candidates.isEmpty())
			return candidates;

		List<State> result = new ArrayList<State>();

		// further filter out states that do not match any of the task mappings
		for (TaskMapping m : taskMappingDao.getTaskMappingByTaskId(task.getTaskId())) {
			if (!m.getStatus().equals(status))
				continue;
 
			// let's just stick to jobId -> stateJob.jobId,
			// labId->stateJob.job.labId, departmentId
			// stateJob.job.lab.departmentId

			for (Iterator<State> it = candidates.iterator(); it.hasNext();) {
				State state = it.next();
				String perm = expand(m.getPermission(), state);
				try {
					if (SecurityUtil.isAuthorized(perm)) {// check if user can
						// see this state
						result.add(state); // yes, he can! add it to result
						it.remove(); // and remove from the list of candidates
					}
				} catch (Throwable e) {
					throw new IllegalStateException("Cant authorize access " + perm + "|" + m.getPermission(), e);
				}
			}

			if (candidates.isEmpty())
				return result;

		}

		return candidates;

	}

	// returns states that
	// a) are in the given status
	// b) are accessible by current user according to the given permission
	@Override
	public List<State> filterStatesByStatusAndPermission(List<State> states, String status, String permission) {

		List<State> candidates = new ArrayList<State>();

		// filter out states that are not in right status
		for (State s : states) {
			if (s.getStatus().equals(status)) {
				candidates.add(s);
			}
		}

		if (candidates.isEmpty())
			return candidates;

		List<State> result = new ArrayList<State>();

		if (permission.indexOf('#') == -1) {// no '#' means static permission.
			// no needs to call "expand"
			// function.
			try {
				return SecurityUtil.isAuthorized(permission) ? candidates : result;
			} catch (Throwable e) {
				throw new IllegalStateException("Cant authorize access " + permission, e);
			}
		}

		// let's just stick to jobId -> stateJob.jobId,
		// labId->stateJob.job.labId, departmentId stateJob.job.lab.departmentId

		for (Iterator<State> it = candidates.iterator(); it.hasNext();) {
			State state = it.next();
			String perm = expand(permission, state);
			try {
				if (SecurityUtil.isAuthorized(perm)) {
					// check if user can see this state
					result.add(state); // yes, he can! add it to result
					it.remove(); // and remove from the list of candidates
				}
			} catch (Throwable e) {
				throw new IllegalStateException("Cant authorize access " + perm + "|" + permission, e);
			}
		}

		return result;
	}

	@Override
	public int getLabManagerPendingTasks(int labId) {
		List<UserPending> newUsersPendingLmApprovalList = new ArrayList<UserPending>();
		List<LabUser> existingUsersPendingLmApprovalList = new ArrayList<LabUser>();
		List<Job> jobsPendingLmApprovalList = new ArrayList<Job>();
		return getLabManagerPendingTasks(labId, newUsersPendingLmApprovalList, existingUsersPendingLmApprovalList, jobsPendingLmApprovalList);
	}

	@Override
	public int getLabManagerPendingTasks(int labId, List<UserPending> newUsersPendingLmApprovalList, List<LabUser> existingUsersPendingLmApprovalList, List<Job> jobsPendingLmApprovalList) {

		// three tasks awaiting Lab Manager or PI from lab where labid = labId
		// 1. approve or reject new users that have applied to join a lab
		// 2. approve or reject existing users that have applied to join a lab
		// 3. approve or reject a new job submission

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
		Map themap = new HashMap();
		Task task = taskDao.getTaskByIName("PI Approval");
		if (task.getTaskId() == null) {// unexpectedly not found
			// TODO: throw exception
		}
		themap.put("taskId", task.getTaskId());
		themap.put("status", "CREATED");
		// may need to be iterated over a few times
		List<State> stateList = stateDao.findByMap(themap);
		for (int i = 0; i < stateList.size(); i++) {
			List<Statejob> statejobList = stateList.get(i).getStatejob();
			for (Statejob stateJob : statejobList) {
				if (stateJob.getJob().getLab().getLabId().intValue() == labId) {
					jobsPendingLmApprovalList.add(stateJob.getJob());
				}
			}
		}

		return newUsersPendingLmApprovalList.size() + existingUsersPendingLmApprovalList.size() + jobsPendingLmApprovalList.size();
	}

	@Override
	public int getLabManagerPendingTasks2(List<UserPending> newUsersPendingLmApprovalList, List<LabUser> existingUsersPendingLmApprovalList, List<Job> jobsPendingLmApprovalList) {

		// three tasks awaiting Lab Manager or PI from lab where labid = labId
		// 1. approve or reject new users that have applied to join a lab
		// 2. approve or reject existing users that have applied to join a lab
		// 3. approve or reject a new job submission
		// however if the user is superuser, ft, fm, and ga, then don't filter by lab_id
		
		if (authenticationService.isSuperUser() || authenticationService.hasRole("fm-*") 
				|| authenticationService.hasRole("ft-*") || authenticationService.hasRole("lm-*") 
				|| authenticationService.hasRole("pi-*") || authenticationService.hasRole("ga-*")) {

			Map themap = new HashMap();
			themap.put("status", "PENDING");
			// themap.put("labId", "NOT NULL");//this won't work
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
			Task task = taskDao.getTaskByIName("PI Approval");
			themap.clear();
			themap.put("taskId", task.getTaskId());
			themap.put("status", "CREATED");
			List<State> stateList = stateDao.findByMap(themap);
			for (int i = 0; i < stateList.size(); i++) {
				List<Statejob> statejobList = stateList.get(i).getStatejob();
				for (Statejob stateJob : statejobList) {
					jobsPendingLmApprovalList.add(stateJob.getJob());
				}
			}

		}else if (authenticationService.hasRole("pi-*") || authenticationService.hasRole("lm-*") ){
			//determine the lab(s) these PI's or lm represent and get info according to their lab(s)
			//recall that any PI also has role of lm, so it is sufficient to ask for lm
		
			// get list of departmentId values for this authenticated user
			List<Integer> labIdList = new ArrayList<Integer>();
			for (String role : authenticationService.getRoles()) {
				String[] splitRole = role.split("-");
				if (splitRole.length != 2) {
					continue;
				}
				if (splitRole[1].equals("*")) {
					continue;
				}
				if ("lm".equals(splitRole[0])) {// lab manager (which also includes any PIs)
					try {
						labIdList.add(Integer.parseInt(splitRole[1]));
					} catch (Exception e) {
						continue;
					}
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
				Map themap = new HashMap();
				Task task = taskDao.getTaskByIName("PI Approval");
				if (task.getTaskId() == null) {// unexpectedly not found
					// TODO: throw exception
				}
				themap.put("taskId", task.getTaskId());
				themap.put("status", "CREATED");
				// may need to be iterated over a few times
				List<State> stateList = stateDao.findByMap(themap);
				for (int i = 0; i < stateList.size(); i++) {
					List<Statejob> statejobList = stateList.get(i).getStatejob();
					for (Statejob stateJob : statejobList) {
						if (stateJob.getJob().getLab().getLabId().intValue() == labId) {
							jobsPendingLmApprovalList.add(stateJob.getJob());
						}
					}
				}
			}
		}
		return newUsersPendingLmApprovalList.size() + existingUsersPendingLmApprovalList.size() + jobsPendingLmApprovalList.size();
	}
	
	@Override
	public int getAllLabManagerPendingTasks() {
		List<UserPending> newUsersPendingLmApprovalList = new ArrayList<UserPending>();
		List<LabUser> existingUsersPendingLmApprovalList = new ArrayList<LabUser>();
		List<Job> jobsPendingLmApprovalList = new ArrayList<Job>();
		return getAllLabManagerPendingTasks(newUsersPendingLmApprovalList, existingUsersPendingLmApprovalList, jobsPendingLmApprovalList);

	}

	@Override
	public int getAllLabManagerPendingTasks(List<UserPending> newUsersPendingLmApprovalList, List<LabUser> existingUsersPendingLmApprovalList, List<Job> jobsPendingLmApprovalList) {
/****
		Map themap = new HashMap();
		themap.put("status", "PENDING");
		// themap.put("labId", "NOT NULL");//this won't work
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
		Task task = taskDao.getTaskByIName("PI Approval");
		themap.clear();
		themap.put("taskId", task.getTaskId());
		themap.put("status", "CREATED");
		List<State> stateList = stateDao.findByMap(themap);
		for (int i = 0; i < stateList.size(); i++) {
			List<Statejob> statejobList = stateList.get(i).getStatejob();
			for (Statejob stateJob : statejobList) {
				jobsPendingLmApprovalList.add(stateJob.getJob());
			}
		}

		return newUsersPendingLmApprovalList.size() + existingUsersPendingLmApprovalList.size() + jobsPendingLmApprovalList.size();
**********/
		
		// three tasks awaiting Lab Manager or PI from lab where labid = labId
		// 1. approve or reject new users that have applied to join a lab
		// 2. approve or reject existing users that have applied to join a lab
		// 3. approve or reject a new job submission
		// however if the user is superuser, ft, fm, and ga, then don't filter by lab_id
		
		if (authenticationService.isSuperUser() || authenticationService.hasRole("fm-*") 
				|| authenticationService.hasRole("ft-*") || authenticationService.hasRole("ga-*")) {

			Map themap = new HashMap();
			themap.put("status", "PENDING");
			// themap.put("labId", "NOT NULL");//this won't work
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
			Task task = taskDao.getTaskByIName("PI Approval");
			themap.clear();
			themap.put("taskId", task.getTaskId());
			themap.put("status", "CREATED");
			List<State> stateList = stateDao.findByMap(themap);
			for (int i = 0; i < stateList.size(); i++) {
				List<Statejob> statejobList = stateList.get(i).getStatejob();
				for (Statejob stateJob : statejobList) {
					jobsPendingLmApprovalList.add(stateJob.getJob());
				}
			}

		}else if (authenticationService.hasRole("pi-*") || authenticationService.hasRole("lm-*") ){
			//determine the lab(s) these PI's or lm represent and get info according to their lab(s)
			//recall that any PI also has role of lm, so it is sufficient to ask for lm
		
			// get list of departmentId values for this authenticated user
			List<Integer> labIdList = new ArrayList<Integer>();
			for (String role : authenticationService.getRoles()) {
				String[] splitRole = role.split("-");
				if (splitRole.length != 2) {
					continue;
				}
				if (splitRole[1].equals("*")) {
					continue;
				}
				if ("lm".equals(splitRole[0])) {// lab manager (which also includes any PIs)
					try {
						labIdList.add(Integer.parseInt(splitRole[1]));
					} catch (Exception e) {
						continue;
					}
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
				Map themap = new HashMap();
				Task task = taskDao.getTaskByIName("PI Approval");
				if (task.getTaskId() == null) {// unexpectedly not found
					// TODO: throw exception
				}
				themap.put("taskId", task.getTaskId());
				themap.put("status", "CREATED");
				// may need to be iterated over a few times
				List<State> stateList = stateDao.findByMap(themap);
				for (int i = 0; i < stateList.size(); i++) {
					List<Statejob> statejobList = stateList.get(i).getStatejob();
					for (Statejob stateJob : statejobList) {
						if (stateJob.getJob().getLab().getLabId().intValue() == labId) {
							jobsPendingLmApprovalList.add(stateJob.getJob());
						}
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

		Map themap = new HashMap();
		Task task = taskDao.getTaskByIName("DA Approval");

		if (task.getTaskId() == null) {// unexpectedly not found
			// TODO: throw exception
		}

		if (authenticationService.isSuperUser() || authenticationService.hasRole("fm-*") || authenticationService.hasRole("ft-*") || authenticationService.hasRole("ga")) {

			themap.put("status", "PENDING");
			labsPendingDaApprovalList.addAll(labPendingDao.findByMap(themap));	// returns a list
			themap.clear();
			themap.put("taskId", task.getTaskId());
			themap.put("status", "CREATED");
			List<State> stateList = stateDao.findByMap(themap);
			for (State state : stateList) {
				// stateList was created and filled above
				List<Statejob> statejobList = state.getStatejob(); // this should be one:one
				for (Statejob stateJob : statejobList) {
					jobsPendingDaApprovalList.add(stateJob.getJob());
				}
			}
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

				themap.clear();
				themap.put("taskId", task.getTaskId());
				themap.put("status", "CREATED");
				// may need to be iterated over a few times
				List<State> stateList = stateDao.findByMap(themap);

				for (int departmentId : departmentIdList) {
					themap.clear();
					themap.put("departmentId", departmentId); // this is the new
					// line
					themap.put("status", "PENDING");
					labsPendingDaApprovalList.addAll(labPendingDao.findByMap(themap));

					for (int i = 0; i < stateList.size(); i++) {
						List<Statejob> statejobList = stateList.get(i).getStatejob();
						for (Statejob stateJob : statejobList) {
							if (stateJob.getJob().getLab().getDepartmentId().intValue() == departmentId) {
								jobsPendingDaApprovalList.add(stateJob.getJob());
							}
						}
					}
				}
			}
		}
		// total number of tasksPendingDaApproval
		return labsPendingDaApprovalList.size() + jobsPendingDaApprovalList.size();
	}
}
