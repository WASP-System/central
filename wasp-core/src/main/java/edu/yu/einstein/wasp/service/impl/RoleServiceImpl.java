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
import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.dao.DepartmentUserDao;
import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.model.DepartmentUser;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.WRole;
import edu.yu.einstein.wasp.model.WUser;
import edu.yu.einstein.wasp.model.Userrole;
import edu.yu.einstein.wasp.service.RoleService;

@Service
@Transactional("entityManager")
public class RoleServiceImpl extends WaspServiceImpl implements RoleService {
	
	private final String DELIMITER = ";";

	public void setDepartmentUserDao(DepartmentUserDao departmentUserDao) {
		this.departmentUserDao = departmentUserDao;
	}

	@Autowired
	private DepartmentUserDao departmentUserDao;
	@Autowired
	private RoleDao roleDao;
	


	 /**
	   * {@inheritDoc}
	   */
	@Override
	//SuperUser, Facility Manager, Facility Tech, System Admin, Facility Admin, PI, Lab Manager, Lab Member, Department Admin.
	public List<String> getUniqueSortedRoleList(WUser user){
		
		Set<String> rolesAsSet = new HashSet<String>();
		
		List<Userrole> userroles = user.getUserrole();
		
		for(Userrole userrole : userroles){//picks up Super WUser, Facility Manager, Facility Tech, Facility Admin, System Admin
			rolesAsSet.add(userrole.getRole().getName());
		}
		List<LabUser> labUsers = user.getLabUser();//picks up PI, LabManager, LabMember 
		for(LabUser labUser : labUsers){
			rolesAsSet.add(labUser.getRole().getName());
		}
		
		//pick up Department Administrator (via departmentuser table; interestingly, no roleId in this table, since entries can only mean that user is a Depart. Admin)
		Map<String, Integer> departmentuserQueryMap = new HashMap<String, Integer>();
		departmentuserQueryMap.put("userId", user.getId());
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
	public List<String> getCompleteSortedRoleList(WUser user){
		
		Set<String> rolesAsSet = new HashSet<String>();
		
		List<Userrole> userroles = user.getUserrole();
		
		for(Userrole userrole : userroles){//picks up Super WUser, Facility Manager, Facility Tech, Facility Admin, System Admin
			rolesAsSet.add(userrole.getRole().getName());
		}
		List<LabUser> labUsers = user.getLabUser();//picks up PI, LabManager, LabMember 
		for(LabUser labUser : labUsers){
			String piLastName = labUser.getLab().getUser().getLastName();
			String roleName = labUser.getRole().getRoleName();
			if(roleName.equals("lm") || roleName.equals("lu")){//lab manager (lm) or lab member (lu)
				rolesAsSet.add(labUser.getRole().getName() + " (" + piLastName + " Lab)");
			}
			else{ rolesAsSet.add(labUser.getRole().getName()); }
		}
		
		//pick up Department Administrator (via departmentuser table; interestingly, no roleId in this table, since entries can only mean that user is a Depart. Admin)
		Map<String, Integer> departmentuserQueryMap = new HashMap<String, Integer>();
		departmentuserQueryMap.put("userId", user.getId());
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
	public List<WRole> convertMetaRoleVisibilityDelimitedStringToRoleList(String roleVisibility){
		List<WRole> roleList = new ArrayList<WRole>();
		if(roleVisibility != null){
			String[] rolenamesAsStringArray = StringUtils.delimitedListToStringArray(roleVisibility, DELIMITER);
			for(String rolename : rolenamesAsStringArray){
				WRole role = this.getRoleByRolename(rolename);
				if(role!=null && role.getId() !=null && role.getId().intValue()>0){
					roleList.add(role);
				}
			}
		}			
		return roleList;
	}
	
	 /**
	   * {@inheritDoc}
	   */
	@Override
	public WRole getRoleByRolename(String roleName){
		return roleDao.getRoleByRoleName(roleName);
	}
	
	 /**
	   * {@inheritDoc}
	   */
	@Override
	public String convertRoleListToMetaRoleVisibilityDelimitedString(List<WRole> roleList){
		
		StringBuilder roleVisibilitySB = new StringBuilder("");		
		for(WRole role : roleList){
			if(role.getRoleName() != null){
				roleVisibilitySB.append(role.getRoleName() + DELIMITER);
			}
		}
		return new String(roleVisibilitySB);		
	}

	 /**
	   * {@inheritDoc}
	   */
	@Override
	public boolean metaRoleVisibilityDelimitedStringContainsRole(String roleVisibility, WRole role){
		
		if(roleVisibility == null || role == null || role.getRoleName() == null){ return false; }
		
		List<WRole> roleList = this.convertMetaRoleVisibilityDelimitedStringToRoleList(roleVisibility);
		return roleInRoleList(role, roleList);
	}
	
	 /**
	   * {@inheritDoc}
	   */
	@Override
	public String addRoleToMetaRoleVisibility(String roleVisibility, WRole role){

		if(roleVisibility == null || role == null || role.getRoleName() == null || metaRoleVisibilityDelimitedStringContainsRole(roleVisibility, role)){
			return roleVisibility;
		}
		
		return new String(roleVisibility + role.getRoleName() + DELIMITER);		
	}

	 /**
	   * {@inheritDoc}
	   */
	@Override
	public String removeRoleFromMetaRoleVisibility(String roleVisibility, WRole role){

		if(roleVisibility == null || role == null || role.getRoleName() == null || ! metaRoleVisibilityDelimitedStringContainsRole(roleVisibility, role)){
			return roleVisibility;
		}
		
		List<WRole> roleList = convertMetaRoleVisibilityDelimitedStringToRoleList(roleVisibility);
		StringBuilder modifiedRoleVisibilitySB = new StringBuilder("");
		for(WRole roleInList : roleList){
			if(!role.getRoleName().equals(roleInList.getRoleName())){
				modifiedRoleVisibilitySB.append(roleInList.getRoleName() + DELIMITER);
			}
		}		
		return new String(modifiedRoleVisibilitySB);		
	}
	
	private boolean roleInRoleList(WRole role, List<WRole> roleList){
		for(WRole roleFromList : roleList){
			if(role.getRoleName().equals(roleFromList.getRoleName())){
				return true;
			}
		}
		return false;
	}
}
