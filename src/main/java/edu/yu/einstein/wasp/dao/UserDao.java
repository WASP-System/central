
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

import edu.yu.einstein.wasp.model.User;


public interface UserDao extends WaspDao<User> {

  public User getUserByUserId (final int UserId);

  public User getUserByLogin (final String login);

  public User getUserByEmail (final String email);
  
 // boolean loginExists(final String login, final Integer excludeUserId);

}

