package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.dao.UserPendingDao;
import edu.yu.einstein.wasp.model.Department;
import edu.yu.einstein.wasp.model.DepartmentUser;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.FilterService;
import edu.yu.einstein.wasp.service.MessageService;


@Service
@Transactional("entityManager")
public class FilterServiceImpl implements FilterService {
	
	@Autowired
	private UserDao userDao;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	
	@Autowired
	private UserPendingDao userPendingDao;
	
	public void setUserPendingDao(UserPendingDao userPendingDao) {
		this.userPendingDao = userPendingDao;
	}
	
	@Autowired
	private MessageService messageService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private AuthenticationService authenticationService;
	
	@Override
	public List<Job> filterJobListForDA(List<Job> jobList){
		
		List<Job> jobsToRetain = new ArrayList<Job>();
		if(authenticationService.isOnlyDepartmentAdministrator()){
			User da = authenticationService.getAuthenticatedUser();
			List<DepartmentUser> departmentUserList = da.getDepartmentUser();
			List<Integer> departmentIdList = new ArrayList<Integer>();
			for(DepartmentUser du : departmentUserList){
				departmentIdList.add(du.getDepartmentId());
			}
			for(Job job : jobList){
				for(Integer deptId : departmentIdList){
					if(job.getLab().getDepartmentId().intValue() == deptId.intValue()){
						jobsToRetain.add(job);
						break;
					}
				}
			}
		}
		else{
			jobsToRetain.addAll(jobList);
		}
		return jobsToRetain;
	}
	
	@Override
	public List<Lab> filterLabListForDA(List<Lab> labList){
		
		List<Lab> labsToRetain = new ArrayList<Lab>();
		if(authenticationService.isOnlyDepartmentAdministrator()){
			User da = authenticationService.getAuthenticatedUser();//get the web viewer who is a DA
			List<DepartmentUser> departmentUserList = da.getDepartmentUser();
			List<Integer> departmentIdList = new ArrayList<Integer>();
			for(DepartmentUser du : departmentUserList){
				departmentIdList.add(du.getDepartmentId());
			}
			for(Lab lab : labList){
				for(Integer deptId : departmentIdList){
					if(lab.getDepartmentId().intValue() == deptId.intValue()){
						labsToRetain.add(lab);
						break;
					}
				}
			}
		}
		else{
			labsToRetain.addAll(labList);
		}
		
		return labsToRetain;
	}
	
	@Override
	public List<Department> filterDepartmentListForDA(List<Department> departmentList){
		
		List<Department> departmentsToRetain = new ArrayList<Department>();
		if(authenticationService.isOnlyDepartmentAdministrator()){
			User da = authenticationService.getAuthenticatedUser();//get the web viewer who is a DA
			List<DepartmentUser> departmentUserList = da.getDepartmentUser();
			List<Integer> departmentIdList = new ArrayList<Integer>();
			for(DepartmentUser du : departmentUserList){
				departmentIdList.add(du.getDepartmentId());
			}
			for(Department department : departmentList){
				for(Integer deptId : departmentIdList){
					if(department.getDepartmentId().intValue() == deptId.intValue()){
						departmentsToRetain.add(department);
						break;
					}
				}
			}
		}
		else{
			departmentsToRetain.addAll(departmentList);
		}
		
		return departmentsToRetain;
		
	}
	
	@Override
	public List<User> filterUserListForDA(List<User> userList){
		
		List<User> usersToRetain = new ArrayList<User>();
		if(authenticationService.isOnlyDepartmentAdministrator()){
			User da = authenticationService.getAuthenticatedUser();//get the web viewer who is a DA
			List<DepartmentUser> departmentUserList = da.getDepartmentUser();
			List<Integer> departmentIdList = new ArrayList<Integer>();
			for(DepartmentUser du : departmentUserList){
				departmentIdList.add(du.getDepartmentId());
			}
			
			for(User user : userList){
				List<LabUser> labUserList = user.getLabUser();//a user can be in multiple labs; note: do NOT use method user.getLab()
				boolean foundUser = false;
				for(LabUser labUser : labUserList){
					if(foundUser==true){break;}
					Lab lab = labUser.getLab();
					for(Integer deptId : departmentIdList){
						if(lab.getDepartmentId().intValue() == deptId.intValue()){
							usersToRetain.add(user);
							foundUser=true;
							break;
						}
					}
				}
			}
		}
		else{
			usersToRetain.addAll(userList);
		}		
		return usersToRetain;
	}
	
}
