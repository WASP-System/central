
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
	 * reverse sort a List<User> by userId so that the user with highest Id is first.
	 * @param List<User>
	 * @return void
	 */
  public void reverseSortUsersByUserId(List<User> users);
  
}

