
/**
 *
 * UserService.java 
 * @author echeng (table2type.pl)
 *  
 * the UserService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.model.WUser;
import edu.yu.einstein.wasp.model.UserPending;

@Service
public interface UserService extends WaspService {

  public void setUserDao(UserDao userDao);
  public UserDao getUserDao();

  public String getUniqueLoginName(final WUser user);
  public String getNewAuthcodeForUserPending(UserPending userpending);
  public String getNewAuthcodeForUser(WUser user);
  
  /**
	 * reverse sort a List<WUser> by UserId so that the user with highest Id is first.
	 * @param List<WUser>
	 * @return void
	 */
  public void reverseSortUsersByUserId(List<WUser> users);
  
  /**
	 * get list of facility technicians (distinct list of users with role of fm or ft).
	 * @return List<WUser>
	 */
  public List<WUser> getFacilityTechnicians();
  
  /**
 	 * get user by unique login. If login is null, empty, or login.trim is empty, return new WUser();
 	 * @param String login
 	 * @return WUser 
 	 */
  public WUser getUserByLogin(String login);

  /**
 	 * get user by unique email address. If email is null, empty, or login.trim is empty, return new WUser();
 	 * @param String emailAddress
 	 * @return WUser 
 	 */
  public WUser getUserByEmail(String emailAddress);

}

