
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
import edu.yu.einstein.wasp.dao.UserMetaDao;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserPending;

@Service
public interface UserService extends WaspService {

  public void setUserDao(UserDao userDao);
  public UserDao getUserDao();

  public String getUniqueLoginName(final User user);
  public String getNewAuthcodeForUserPending(UserPending userpending);
  public String getNewAuthcodeForUser(User user);
  
  /**
	 * reverse sort a List<User> by UserId so that the user with highest Id is first.
	 * @param List<User>
	 * @return void
	 */
  public void reverseSortUsersByUserId(List<User> users);
  
  /**
	 * get list of facility technicians (distinct list of users with role of fm or ft).
	 * @return List<User>
	 */
  public List<User> getFacilityTechnicians();
  
  /**
 	 * get user by unique login. If login is null, empty, or login.trim is empty, return new User();
 	 * @param String login
 	 * @return User 
 	 */
  public User getUserByLogin(String login);

  /**
 	 * get user by unique email address. If email is null, empty, or login.trim is empty, return new User();
 	 * @param String emailAddress
 	 * @return User 
 	 */
  public User getUserByEmail(String emailAddress);
  
  /**
   * @return
   */
  public UserMetaDao getUserMetaDao();

  	/**
	 * get list of facility managers (distinct list of users with role of fm ).
	 * @return List<User>
	 */
  public List<User> getFacilityManagers();
	
  public List<Lab> getLabsForUser(User user);

}

