
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.UserPendingDao;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.service.UserPendingService;

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
	@Override
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
	@Override
	public UserPendingDao getUserPendingDao() {
		return this.userPendingDao;
	}


  @Override
public UserPending getUserPendingByUserPendingId (final int userPendingId) {
    return this.getUserPendingDao().getUserPendingByUserPendingId(userPendingId);
  }

}

