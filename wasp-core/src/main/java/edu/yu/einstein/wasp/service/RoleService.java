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

import edu.yu.einstein.wasp.model.WUser;
import edu.yu.einstein.wasp.model.WRole;

@Service
public interface RoleService extends WaspService {

	
	/**
	 * getUniqueRoleList() returns list of unique roles for a user
	 * SuperUser, Facility Manager, Facility Tech, System Admin, Facility Admin, PI, Lab Manager, Lab Member, Department Admin.
	 * @param WUser
	 * @return List<String>
	 * 
	 */
	public List<String> getUniqueSortedRoleList(WUser user);
	
	/**
	 * getCompleteRoleList() returns list of unique roles for a user
	 * SuperUser, Facility Manager, Facility Tech, System Admin, Facility Admin, PI (with lab name as PI's last name), Lab Manager (with PI's last name), Lab Member (with PI's last name), Department Admin (departments NOT listed as could be long list).
	 * @param WUser
	 * @return List<String>
	 * 
	 */
	public List<String> getCompleteSortedRoleList(WUser user);
	
	/**
	 * convertRoleVisibilityDelimitedStringToRoleList() returns list of roles given a roleVisibility (obtained from some Meta)
	 * @param String roleVisibility (example: "fm;ft;sa;ga")
	 * @return List<WRole>
	 * 
	 */
	public List<WRole> convertMetaRoleVisibilityDelimitedStringToRoleList(String roleVisibility);

	/**
	 * convertRoleListToMetaRoleVisibilityDelimitedString() returns a meta.roleVisibility given a list of roles
	 * @param List<WRole> roleList 
	 * @return String roleVisibility (example: "fm;ft;sa;ga")
	 * 
	 */
	public String convertRoleListToMetaRoleVisibilityDelimitedString(List<WRole> roleList);

	
	/**
	 * getRoleByRolename(String roleName) returns a WRole given a roleName (such as fm)
	 * @param String roleName (example: "fm" which stands for Facility Manager)
	 * @return WRole
	 * 
	 */
	public WRole getRoleByRolename(String roleName);
	
	/**
	 * metaRoleVisibilityDelimitedStringContainsRole(finalString roleVisibility, WRole role) returns true if role is encoded within the string roleVisibility
	 * @param String roleVisibility (example: "fm; ft;" which stands for Facility Manager and Facility Tech)
	 * @param WRole role 
	 * @return boolean
	 * 
	 */
	public boolean metaRoleVisibilityDelimitedStringContainsRole(String roleVisibility, WRole role);

	/**
	 * addRoleToMetaRoleVisibility(String roleVisibility, WRole role) returns roleVisibilityString containing the new role (if it was not there before)
	 * @param String roleVisibility (example: "fm; ft;" which stands for Facility Manager and Facility Tech)
	 * @param WRole role 
	 * @return String roleVisibility
	 * 
	 */
	public String addRoleToMetaRoleVisibility(String roleVisibility, WRole role);
	
	/**
	 * removeRoleFromMetaRoleVisibility(String roleVisibility, WRole role) returns roleVisibilityString without role encoded into it (if the role was present)
	 * @param String roleVisibility (example: "fm; ft;" which stands for Facility Manager and Facility Tech)
	 * @param WRole role 
	 * @return String roleVisibility
	 * 
	 */
	public String removeRoleFromMetaRoleVisibility(String roleVisibility, WRole role);

}
