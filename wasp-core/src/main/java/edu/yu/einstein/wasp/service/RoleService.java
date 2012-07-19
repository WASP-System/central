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
import java.util.Map;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.exception.FileMoveException;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.Sample;
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
}
