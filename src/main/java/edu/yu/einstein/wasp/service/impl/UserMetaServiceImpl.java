
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

import edu.yu.einstein.wasp.service.UserMetaService;
import edu.yu.einstein.wasp.dao.UserMetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.UserMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserMetaServiceImpl extends WaspServiceImpl<UserMeta> implements UserMetaService {

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
	public UserMetaDao getUserMetaDao() {
		return this.userMetaDao;
	}


  public UserMeta getUserMetaByUserMetaId (final int userMetaId) {
    return this.getUserMetaDao().getUserMetaByUserMetaId(userMetaId);
  }

  public UserMeta getUserMetaByKUserId (final String k, final int UserId) {
    return this.getUserMetaDao().getUserMetaByKUserId(k, UserId);
  }

  public void updateByUserId (final int UserId, final List<UserMeta> metaList) {
    this.getUserMetaDao().updateByUserId(UserId, metaList); 
  }


}

