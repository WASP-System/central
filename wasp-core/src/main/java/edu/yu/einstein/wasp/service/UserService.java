
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

@Service
public interface UserService extends WaspService<User> {

  public void setUserDao(UserDao userDao);
  public UserDao getUserDao();

  public User getUserByUserId (final int UserId);

  public User getUserByLogin (final String login);

  public User getUserByEmail (final String email);
  
 // boolean loginExists(final String login, final Integer excludeUserId);
  
  public String getUniqueLoginName(final User user);

  public List<User> getActiveUsers();

}

