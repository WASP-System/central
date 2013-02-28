
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

import edu.yu.einstein.wasp.model.WUser;


public interface UserDao extends WaspDao<WUser> {

  public WUser getUserByUserId (final int userId);

  public WUser getUserByLogin (final String login);

  public WUser getUserByEmail (final String email);

  public List<WUser> getActiveUsers();
  
 

}

