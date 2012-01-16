
/**
 *
 * UserpasswordauthServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the UserpasswordauthService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.UserpasswordauthDao;
import edu.yu.einstein.wasp.model.Userpasswordauth;
import edu.yu.einstein.wasp.service.UserpasswordauthService;

@Service
public class UserpasswordauthServiceImpl extends WaspServiceImpl<Userpasswordauth> implements UserpasswordauthService {

	/**
	 * userpasswordauthDao;
	 *
	 */
	private UserpasswordauthDao userpasswordauthDao;

	/**
	 * setUserpasswordauthDao(UserpasswordauthDao userpasswordauthDao)
	 *
	 * @param userpasswordauthDao
	 *
	 */
	@Autowired
	public void setUserpasswordauthDao(UserpasswordauthDao userpasswordauthDao) {
		this.userpasswordauthDao = userpasswordauthDao;
		this.setWaspDao(userpasswordauthDao);
	}

	/**
	 * getUserpasswordauthDao();
	 *
	 * @return userpasswordauthDao
	 *
	 */
	public UserpasswordauthDao getUserpasswordauthDao() {
		return this.userpasswordauthDao;
	}


  public Userpasswordauth getUserpasswordauthByUserId (final int UserId) {
    return this.getUserpasswordauthDao().getUserpasswordauthByUserId(UserId);
  }

  public Userpasswordauth getUserpasswordauthByAuthcode (final String authcode) {
    return this.getUserpasswordauthDao().getUserpasswordauthByAuthcode(authcode);
  }

}

