
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.ConfirmEmailAuthDao;
import edu.yu.einstein.wasp.dao.LabUserDao;
import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.dao.UserMetaDao;
import edu.yu.einstein.wasp.dao.UserroleDao;
import edu.yu.einstein.wasp.model.ConfirmEmailAuth;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.model.Userrole;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.util.AuthCode;

@Service
@Transactional("entityManager")
public class UserServiceImpl extends WaspServiceImpl implements UserService {
	
 private UserDao userDao;
  @Override
@Autowired
  public void setUserDao(UserDao userDao) {
      System.out.println("SETTING USERDAO");
    this.userDao = userDao;
  }
  @Override
public UserDao getUserDao() {
    return this.userDao;
  }
  
  @Autowired
  private ConfirmEmailAuthDao confirmEmailAuthDao;

  public void setconfirmEmailAuthDao(ConfirmEmailAuthDao confirmEmailAuthDao) {
	    this.confirmEmailAuthDao = confirmEmailAuthDao;
  }
  
  @Autowired
  private UserroleDao userroleDao;
  
  @Autowired
  private UserMetaDao userMetaDao;
  
  @Autowired
  private LabUserDao labUserDao;


  
  @Override
public String getUniqueLoginName(final User user){
	    if (user == null || user.getUserId() == null){
	    	return null;
	    }
	    String loginBase = user.getFirstName().substring(0, 1);
		String loginLastName = user.getLastName();
		loginLastName = loginLastName.replaceAll(" ", "");
		int posOfQuote = StringUtils.indexOf(loginLastName, "'"); // e.g. O'Broin
		int posOfHyphen = StringUtils.indexOf(loginLastName, "-");
		if (posOfQuote != -1 && (posOfQuote - 1) != -1 && (posOfQuote < loginLastName.length())){
			//loginBase += loginLastName.substring(posOfQuote -1, posOfQuote);
			//loginBase += loginLastName.substring(posOfQuote + 1);
			loginBase += loginLastName.substring(0, posOfQuote);
			loginBase += loginLastName.substring(posOfQuote + 1);
		}
		else if (posOfHyphen != -1 && (posOfHyphen - 1) != -1 && (posOfHyphen < loginLastName.length())){
			loginBase += loginLastName.substring(0, posOfHyphen);
			loginBase += loginLastName.substring(posOfHyphen + 1);
		}else {
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
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<User> getFacilityTechnicians(){
		  
		  Set<User> theSet = new HashSet<User>();
		  List<User> facilityTechnicians = new ArrayList<User>();
		  for(Userrole userRole : userroleDao.findAll()){			  
			  if(userRole.getRole().getRoleName().equals("fm")||userRole.getRole().getRoleName().equals("ft")){
				  theSet.add(userRole.getUser());//for distinct
			  }
		  }
		  class LastNameFirstNameComparator implements Comparator<User> {
				@Override
				public int compare(User arg0, User arg1) {
					return arg0.getLastName().concat(arg0.getFirstName()).compareToIgnoreCase(arg1.getLastName().concat(arg1.getFirstName()));
				}
		  }
		  facilityTechnicians.addAll(theSet);
		  Collections.sort(facilityTechnicians, new LastNameFirstNameComparator());//asc
		  return facilityTechnicians;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public User getUserByLogin(String login){
		  if(login==null || login.isEmpty() || login.trim().isEmpty()){
			  return new User();
		  }
		  else{ return userDao.getUserByLogin(login.trim());}
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public User getUserByEmail(String emailAddress){
		  if(emailAddress==null || emailAddress.isEmpty() || emailAddress.trim().isEmpty()){
			  return new User();
		  }
		  else{ return userDao.getUserByEmail(emailAddress);}
	  }

	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<User> getFacilityManagers(){
		  
		  Set<User> theSet = new HashSet<User>();
		  List<User> facilityManagers = new ArrayList<User>();
		  for(Userrole userRole : userroleDao.findAll()){			  
			  if(userRole.getRole().getRoleName().equals("fm")){
				  theSet.add(userRole.getUser());//for distinct
			  }
		  }
		  class LastNameFirstNameComparator implements Comparator<User> {
				@Override
				public int compare(User arg0, User arg1) {
					return arg0.getLastName().concat(arg0.getFirstName()).compareToIgnoreCase(arg1.getLastName().concat(arg1.getFirstName()));
				}
		  }
		  facilityManagers.addAll(theSet);
		  Collections.sort(facilityManagers, new LastNameFirstNameComparator());//asc
		  return facilityManagers;
	  }

	@Override
	public UserMetaDao getUserMetaDao() {
		return this.userMetaDao;
	}
	
	@Override
	public List<Lab> getLabsForUser(User user){
		Map<String, Integer> m = new HashMap<>();
		m.put("userId", user.getId());
		List<Lab> labs = new ArrayList<>();
		for (LabUser lu : labUserDao.findByMap(m))
			labs.add(lu.getLab());
		return labs;
	}

}

