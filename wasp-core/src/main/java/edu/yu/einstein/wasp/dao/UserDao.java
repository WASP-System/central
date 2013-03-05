
/**
 *
 * UserDao.java 
 * @author echeng (table2type.pl)
 *  
 * the UserDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.User;


public interface UserDao extends WaspDao<User> {

  public User getUserByUserId (final int userId);

  public User getUserByLogin (final String login);

  public User getUserByEmail (final String email);

  public List<User> getActiveUsers();
  
 

}

