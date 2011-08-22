
/**
 *
 * UserService.java 
 * @author echeng (table2type.pl)
 *  
 * the UserService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.User;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl extends WaspServiceImpl<User> implements UserService {

  private UserDao userDao;
  @Autowired
  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
    this.setWaspDao(userDao);
  }
  public UserDao getUserDao() {
    return this.userDao;
  }

  // **

  
  public User getUserByUserId (final int UserId) {
    return this.getUserDao().getUserByUserId(UserId);
  }

  public User getUserByLogin (final String login) {
    return this.getUserDao().getUserByLogin(login);
  }

  public User getUserByEmail (final String email) {
    return this.getUserDao().getUserByEmail(email);
  }
  
  public boolean loginExists(final String login, final Integer excludeUserId){
	  return  this.getUserDao().loginExists(login, excludeUserId);
  }
}

