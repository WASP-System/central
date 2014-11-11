
/**
 *
 * LabService.java 
 * @author RDubin (01-30-13)
 *  
 * the LabService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.User;

@Service
public interface LabService extends WaspService {

  
  /**
	 * Get Lab by PI. If pi is null or pi.getUserId==null, then return new Lab();
	 * @param User pi
	 * @return Lab
	 */
	public Lab getLabByPI(User pi);
  
 /**
	 * is a User a Member of a lab (true if entry is in the labuser table for this lab, having ANY role: PI, Lab Manager, Lab Member, Lab Member Pending, Lab Member Inactive)
	 * @param Lab lab
	 * @param User user
	 * @return boolean
	 */
	public boolean isUserLabMember(Lab lab, User user);
	
 /**
	 * get a User's role in a particular lab (PI, Lab Manager, Lab Member, Lab Member Pending, Lab Member Inactive)
	 * if the user is not in the specified lab at all, return new Role();
	 * @param Lab lab
	 * @param User user
	 * @return Role
	 */
	public Role getUserRoleInLab(Lab lab, User user);
	
 /**
	 * add an existing user to a new lab, with role of Lab Member Pending
	 * 	 
	 * @param Lab lab
	 * @param User user
	 * @return LabUser
	 */
	public LabUser addExistingUserToLabAsLabMemberPending(Lab lab, User user);
	
	/**
	 * has an existing user applied to become a PI and is that request pending approval
	 *
	 * @param User user
	 * @return boolean
	 */
	public boolean isExistingUserPIPending(User user);

	public List<LabUser> getAllLabUsers();
	
	public String getInstitutionOfLabPI(Lab lab);
}

