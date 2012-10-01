/**
 *
 * RoleServiceImpl.java 
 * @author dubin
 *  
 * the RoleService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.DepartmentUserDao;
import edu.yu.einstein.wasp.dao.FileDao;
import edu.yu.einstein.wasp.dao.JobCellSelectionDao;
import edu.yu.einstein.wasp.dao.JobDraftDao;
import edu.yu.einstein.wasp.dao.JobFileDao;
import edu.yu.einstein.wasp.dao.JobMetaDao;
import edu.yu.einstein.wasp.dao.JobResourcecategoryDao;
import edu.yu.einstein.wasp.dao.JobSampleDao;
import edu.yu.einstein.wasp.dao.JobSoftwareDao;
import edu.yu.einstein.wasp.dao.JobUserDao;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.dao.ResourceTypeDao;
import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleFileDao;
import edu.yu.einstein.wasp.dao.SampleJobCellSelectionDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.dao.SoftwareDao;
import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.StatejobDao;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.model.DepartmentUser;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Userrole;
import edu.yu.einstein.wasp.service.RoleService;
import edu.yu.einstein.wasp.service.TaskService;

@Service
@Transactional
public class RoleServiceImpl extends WaspServiceImpl implements RoleService {

	@Autowired
	private DepartmentUserDao departmentUserDao;
	
	@Autowired
	private JobDraftDao jobDraftDao;
	
	@Autowired
	private SampleMetaDao sampleMetaDao;
	
	@Autowired
	private TaskDao taskDao;

	@Autowired
	private TaskService taskService;
	 
	@Autowired
	private JobMetaDao jobMetaDao;

	@Autowired
	protected LabDao labDao;

	@Autowired
	protected JobUserDao jobUserDao;

	@Autowired
	protected RoleDao roleDao;

	@Autowired
	protected ResourceDao resourceDao;

	@Autowired
	protected ResourceCategoryDao resourceCategoryDao;

	@Autowired
	protected SoftwareDao softwareDao;

	@Autowired
	protected ResourceTypeDao resourceTypeDao;

	@Autowired
	protected SampleDao sampleDao;

	@Autowired
	protected SampleFileDao sampleFileDao;

	@Autowired
	protected JobSampleDao jobSampleDao;
	
	@Autowired
	protected SampleTypeDao sampleTypeDao;
	
	@Autowired
	protected SampleSubtypeDao sampleSubtypeDao;

	@Autowired
	protected StatejobDao statejobDao;

	@Autowired
	protected StateDao stateDao;

	@Autowired
	protected SampleSubtypeDao subSampleTypeDao;

	@Autowired
	protected FileDao fileDao;

	@Autowired
	protected JobCellSelectionDao jobCellSelectionDao;
	
	@Autowired
	protected SampleJobCellSelectionDao sampleJobCellSelectionDao;
	
	@Autowired
	protected JobSoftwareDao jobSoftwareDao;
	
	@Autowired
	protected JobResourcecategoryDao jobResourcecategoryDao;
	
	@Autowired
	protected JobFileDao jobFileDao;

	 /**
	   * {@inheritDoc}
	   */
	@Override
	//SuperUser, Facility Manager, Facility Tech, System Admin, Facility Admin, PI, Lab Manager, Lab Member, Department Admin.
	public List<String> getUniqueSortedRoleList(User user){
		
		Set<String> rolesAsSet = new HashSet<String>();
		
		List<Userrole> userroles = user.getUserrole();
		
		for(Userrole userrole : userroles){//picks up Super User, Facility Manager, Facility Tech, Facility Admin, System Admin
			rolesAsSet.add(userrole.getRole().getName());
		}
		List<LabUser> labUsers = user.getLabUser();//picks up PI, LabManager, LabMember 
		for(LabUser labUser : labUsers){
			rolesAsSet.add(labUser.getRole().getName());
		}
		
		//pick up Department Administrator (via departmentuser table; interestingly, no roleId in this table, since entries can only mean that user is a Depart. Admin)
		Map<String, Integer> departmentuserQueryMap = new HashMap<String, Integer>();
		departmentuserQueryMap.put("UserId", user.getUserId());
		List<DepartmentUser> departmentUsers = departmentUserDao.findByMap(departmentuserQueryMap);
		if(departmentUsers.size() > 0){
			rolesAsSet.add("Dep't. Admin.");
		}
						
		List<String> roles = new ArrayList<String>(rolesAsSet);
		Collections.sort(roles);
		return roles;
	}
	
	 /**
	   * {@inheritDoc}
	   */
	@Override
	//SuperUser, Facility Manager, Facility Tech, System Admin, Facility Admin, PI (with lab name as PI's last name), Lab Manager (with PI's last name), Lab Member (with PI's last name), Department Admin (departments NOT listed as could be long list).
	public List<String> getCompleteSortedRoleList(User user){
		
		Set<String> rolesAsSet = new HashSet<String>();
		
		List<Userrole> userroles = user.getUserrole();
		
		for(Userrole userrole : userroles){//picks up Super User, Facility Manager, Facility Tech, Facility Admin, System Admin
			rolesAsSet.add(userrole.getRole().getName());
		}
		List<LabUser> labUsers = user.getLabUser();//picks up PI, LabManager, LabMember 
		for(LabUser labUser : labUsers){
			String piLastName = labUser.getLab().getUser().getLastName();
			String labName = labUser.getLab().getName();//not used
			String roleName = labUser.getRole().getRoleName();
			if(roleName.equals("lm") || roleName.equals("lu")){//lab manager (lm) or lab member (lu)
				rolesAsSet.add(labUser.getRole().getName() + " (" + piLastName + " Lab)");
			}
			else{ rolesAsSet.add(labUser.getRole().getName()); }
		}
		
		//pick up Department Administrator (via departmentuser table; interestingly, no roleId in this table, since entries can only mean that user is a Depart. Admin)
		Map<String, Integer> departmentuserQueryMap = new HashMap<String, Integer>();
		departmentuserQueryMap.put("UserId", user.getUserId());
		List<DepartmentUser> departmentUsers = departmentUserDao.findByMap(departmentuserQueryMap);
		if(departmentUsers.size() > 0){
			rolesAsSet.add("Dep't. Admin.");
		}
						
		List<String> roles = new ArrayList<String>(rolesAsSet);
		Collections.sort(roles);
		return roles;
	}
}
