
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.ConfirmEmailAuthDao;
import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.model.ConfirmEmailAuth;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.util.AuthCode;

@Service
@Transactional
public class UserServiceImpl extends WaspServiceImpl implements UserService {
	
 private UserDao userDao;
  @Override
@Autowired
  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }
  @Override
public UserDao getUserDao() {
    return this.userDao;
  }
  
  @Autowired
  private ConfirmEmailAuthDao confirmEmailAuthDao;

  
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
		while (userDao.getUserByLogin(login).getUserId() != null) {
			login = loginBase + c;
			c++;
		}
		return login;
  }
  
	@Override
	public String getNewAuthcodeForUser(User user) {
		String authcode = AuthCode.create(20);
		ConfirmEmailAuth confirmEmailAuth = confirmEmailAuthDao.getConfirmEmailAuthByUserId(user.getUserId());
		confirmEmailAuth.setAuthcode(authcode);
		confirmEmailAuth.setUserId(user.getUserId());
		confirmEmailAuthDao.save(confirmEmailAuth);
		return authcode;
	}
	
	@Override
	public String getNewAuthcodeForUserPending(UserPending userpending) {
		String authcode = AuthCode.create(20);
		ConfirmEmailAuth confirmEmailAuth = confirmEmailAuthDao.getConfirmEmailAuthByUserpendingId(userpending.getUserPendingId());
		confirmEmailAuth.setAuthcode(authcode);
		confirmEmailAuth.setUserpendingId(userpending.getUserPendingId());
		confirmEmailAuthDao.save(confirmEmailAuth);
		return authcode;
	}

	/**
	   * {@inheritDoc}
	   */
	  @Override
	  public void reverseSortUsersByUserId(List<User> users){
		  class UserIdComparator implements Comparator<User> {
			    @Override
			    public int compare(User arg0, User arg1) {
			        return arg1.getUserId().compareTo(arg0.getUserId());
			    }
		  }
		  Collections.sort(users, new UserIdComparator());//reverse sort by job ID 
	  }
}

