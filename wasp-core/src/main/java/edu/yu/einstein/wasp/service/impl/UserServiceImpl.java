
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.service.ConfirmEmailAuthService;
import edu.yu.einstein.wasp.service.UserService;

@Service
public class UserServiceImpl extends WaspServiceImpl<User> implements UserService {
	
  @Autowired
  private ConfirmEmailAuthService confirmEmailAuthService;
  
//  @Autowired
//  private EmailService emailService;
	
  private UserDao userDao;
  @Override
@Autowired
  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
    this.setWaspDao(userDao);
  }
  @Override
public UserDao getUserDao() {
    return this.userDao;
  }

  // **

  
  @Override
public User getUserByUserId (final int UserId) {
    return this.getUserDao().getUserByUserId(UserId);
  }

  @Override
public User getUserByLogin (final String login) {
    return this.getUserDao().getUserByLogin(login);
  }

  @Override
public User getUserByEmail (final String email) {
    return this.getUserDao().getUserByEmail(email);
  }
  
 /* public boolean loginExists(final String login, final Integer excludeUserId){
	  return  this.getUserDao().loginExists(login, excludeUserId);
  }*/
  
  @Override
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
		while (this.getUserByLogin(login).getUserId() != null) {
			login = loginBase + c;
			c++;
		}
		return login;
  }
  
  @Override
  public List<User> getActiveUsers(){
	  Map queryMap = new HashMap();
	  queryMap.put("isActive", 1);
	  return this.findByMap(queryMap);
  }

  
}

