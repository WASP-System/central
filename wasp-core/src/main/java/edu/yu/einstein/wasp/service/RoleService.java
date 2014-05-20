/**
 *
 * RoleService.java 
 * @author dubin
 *  
 * the RoleService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.User;

@Service
public interface RoleService extends WaspService {

	
	/**
	 * getUniqueRoleList() returns list of unique roles for a user
	 * SuperUser, Facility Manager, Facility Tech, System Admin, Facility Admin, PI, Lab Manager, Lab Member, Department Admin.
	 * @param User
	 * @return List<String>
	 * 
	 */
	public List<String> getUniqueSortedRoleList(User user);
	
	/**
	 * getCompleteRoleList() returns list of unique roles for a user
	 * SuperUser, Facility Manager, Facility Tech, System Admin, Facility Admin, PI (with lab name as PI's last name), Lab Manager (with PI's last name), Lab Member (with PI's last name), Department Admin (departments NOT listed as could be long list).
	 * @param User
	 * @return List<String>
	 * 
	 */
	public List<String> getCompleteSortedRoleList(User user);
	
	/**
	 * convertRoleVisibilityDelimitedStringToRoleList() returns list of roles given a roleVisibility (obtained from some Meta)
	 * @param String roleVisibility (example: "fm;ft;sa;ga")
	 * @return List<Role>
	 * 
	 */
	public List<Role> convertMetaRoleVisibilityDelimitedStringToRoleList(String roleVisibility);

	/**
	 * convertRoleListToMetaRoleVisibilityDelimitedString() returns a meta.roleVisibility given a list of roles
	 * @param List<Role> roleList 
	 * @return String roleVisibility (example: "fm;ft;sa;ga")
	 * 
	 */
	public String convertRoleListToMetaRoleVisibilityDelimitedString(List<Role> roleList);

	
	/**
	 * getRoleByRolename(String roleName) returns a Role given a roleName (such as fm)
	 * @param String roleName (example: "fm" which stands for Facility Manager)
	 * @return Role
	 * 
	 */
	public Role getRoleByRolename(String roleName);
	
	/**
	 * metaRoleVisibilityDelimitedStringContainsRole(finalString roleVisibility, Role role) returns true if role is encoded within the string roleVisibility
	 * @param String roleVisibility (example: "fm; ft;" which stands for Facility Manager and Facility Tech)
	 * @param Role role 
	 * @return boolean
	 * 
	 */
	public boolean metaRoleVisibilityDelimitedStringContainsRole(String roleVisibility, Role role);

	/**
	 * addRoleToMetaRoleVisibility(String roleVisibility, Role role) returns roleVisibilityString containing the new role (if it was not there before)
	 * @param String roleVisibility (example: "fm; ft;" which stands for Facility Manager and Facility Tech)
	 * @param Role role 
	 * @return String roleVisibility
	 * 
	 */
	public String addRoleToMetaRoleVisibility(String roleVisibility, Role role);
	
	/**
	 * removeRoleFromMetaRoleVisibility(String roleVisibility, Role role) returns roleVisibilityString without role encoded into it (if the role was present)
	 * @param String roleVisibility (example: "fm; ft;" which stands for Facility Manager and Facility Tech)
	 * @param Role role 
	 * @return String roleVisibility
	 * 
	 */
	public String removeRoleFromMetaRoleVisibility(String roleVisibility, Role role);

}
