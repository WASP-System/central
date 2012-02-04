
/**
 *
 * UserMetaServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the UserMetaService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.UserMetaDao;
import edu.yu.einstein.wasp.model.UserMeta;
import edu.yu.einstein.wasp.service.UserMetaService;

@Service
public class UserMetaServiceImpl extends WaspMetaServiceImpl<UserMeta> implements UserMetaService {

	/**
	 * userMetaDao;
	 *
	 */
	private UserMetaDao userMetaDao;

	/**
	 * setUserMetaDao(UserMetaDao userMetaDao)
	 *
	 * @param userMetaDao
	 *
	 */
	@Override
	@Autowired
	public void setUserMetaDao(UserMetaDao userMetaDao) {
		this.userMetaDao = userMetaDao;
		this.setWaspDao(userMetaDao);
	}

	/**
	 * getUserMetaDao();
	 *
	 * @return userMetaDao
	 *
	 */
	@Override
	public UserMetaDao getUserMetaDao() {
		return this.userMetaDao;
	}


  @Override
public UserMeta getUserMetaByUserMetaId (final int userMetaId) {
    return this.getUserMetaDao().getUserMetaByUserMetaId(userMetaId);
  }

  @Override
public UserMeta getUserMetaByKUserId (final String k, final int UserId) {
    return this.getUserMetaDao().getUserMetaByKUserId(k, UserId);
  }

  @Override
public void updateByUserId (final String area, final int UserId, final List<UserMeta> metaList) {
    this.getUserMetaDao().updateByUserId(area, UserId, metaList); 
  }

  @Override
public void updateByUserId (final int UserId, final List<UserMeta> metaList) {
    this.getUserMetaDao().updateByUserId(UserId, metaList); 
  }


}

