
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

import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.WRole;
import edu.yu.einstein.wasp.model.WUser;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.model.Lab;

@Service
public interface LabService extends WaspService {

  
  /**
	 * Get Lab by PI. If pi is null or pi.getUserId==null, then return new Lab();
	 * @param WUser pi
	 * @return Lab
	 */
	public Lab getLabByPI(WUser pi);
  
 /**
	 * is a WUser a Member of a lab (true if entry is in the labuser table for this lab, having ANY role: PI, Lab Manager, Lab Member, Lab Member Pending, Lab Member Inactive)
	 * @param Lab lab
	 * @param WUser user
	 * @return boolean
	 */
	public boolean isUserLabMember(Lab lab, WUser user);
	
 /**
	 * get a WUser's role in a particular lab (PI, Lab Manager, Lab Member, Lab Member Pending, Lab Member Inactive)
	 * if the user is not in the specified lab at all, return new WRole();
	 * @param Lab lab
	 * @param WUser user
	 * @return WRole
	 */
	public WRole getUserRoleInLab(Lab lab, WUser user);
	
 /**
	 * add an existing user to a new lab, with role of Lab Member Pending
	 * 	 
	 * @param Lab lab
	 * @param WUser user
	 * @return LabUser
	 */
	public LabUser addExistingUserToLabAsLabMemberPending(Lab lab, WUser user);
}

