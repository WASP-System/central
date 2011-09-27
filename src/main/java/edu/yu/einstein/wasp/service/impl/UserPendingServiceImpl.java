
/**
 *
 * UserPendingServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the UserPendingService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.UserPendingService;
import edu.yu.einstein.wasp.dao.UserPendingDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.UserPending;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserPendingServiceImpl extends WaspServiceImpl<UserPending> implements UserPendingService {

	/**
	 * userPendingDao;
	 *
	 */
	private UserPendingDao userPendingDao;

	/**
	 * setUserPendingDao(UserPendingDao userPendingDao)
	 *
	 * @param userPendingDao
	 *
	 */
	@Autowired
	public void setUserPendingDao(UserPendingDao userPendingDao) {
		this.userPendingDao = userPendingDao;
		this.setWaspDao(userPendingDao);
	}

	/**
	 * getUserPendingDao();
	 *
	 * @return userPendingDao
	 *
	 */
	public UserPendingDao getUserPendingDao() {
		return this.userPendingDao;
	}


  public UserPending getUserPendingByUserPendingId (final int userPendingId) {
    return this.getUserPendingDao().getUserPendingByUserPendingId(userPendingId);
  }

}

