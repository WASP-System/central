
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

import edu.yu.einstein.wasp.service.ConfirmEmailAuthService;
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.util.AuthCode;
import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.ConfirmEmailAuth;
import edu.yu.einstein.wasp.model.User;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl extends WaspServiceImpl<User> implements UserService {
	
  @Autowired
  private ConfirmEmailAuthService confirmEmailAuthService;
  
  @Autowired
  private EmailService emailService;
	
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
  
 /* public boolean loginExists(final String login, final Integer excludeUserId){
	  return  this.getUserDao().loginExists(login, excludeUserId);
  }*/
  
  public String getUniqueLoginName(final User user){
	    if (user == null || user.getUserId() == null){
	    	return null;
	    }
	    String loginBase = user.getFirstName().substring(0, 1);
		String loginLastName = user.getLastName();
		loginLastName = loginLastName.replaceAll(" ", "");
		int posOfQuote = StringUtils.indexOf(loginLastName, "'"); // e.g. O'Broin
		if (posOfQuote != -1 && (posOfQuote - 1) != -1 && (posOfQuote < loginLastName.length())){
			loginBase += loginLastName.substring(posOfQuote -1, posOfQuote);
			loginBase += loginLastName.substring(posOfQuote + 1);
		} else {
			loginBase += loginLastName;
		}
		loginBase = loginBase.replaceAll("[^\\w-]", "").toLowerCase();
		String login = loginBase;
		int c = 1;
		while (this.getUserByLogin(login).getUserId() > 0) {
			login = loginBase + c;
			c++;
		}
		return login;
  }

  
}

