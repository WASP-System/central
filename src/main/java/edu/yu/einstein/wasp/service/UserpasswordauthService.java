
/**
 *
 * UserpasswordauthService.java 
 * @author echeng (table2type.pl)
 *  
 * the UserpasswordauthService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.UserpasswordauthDao;
import edu.yu.einstein.wasp.model.Userpasswordauth;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface UserpasswordauthService extends WaspService<Userpasswordauth> {

	/**
	 * setUserpasswordauthDao(UserpasswordauthDao userpasswordauthDao)
	 *
	 * @param userpasswordauthDao
	 *
	 */
	public void setUserpasswordauthDao(UserpasswordauthDao userpasswordauthDao);

	/**
	 * getUserpasswordauthDao();
	 *
	 * @return userpasswordauthDao
	 *
	 */
	public UserpasswordauthDao getUserpasswordauthDao();

  public Userpasswordauth getUserpasswordauthByUserId (final int UserId);

  public Userpasswordauth getUserpasswordauthByAuthcode (final String authcode);


}

