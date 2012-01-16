
/**
 *
 * UserPendingMetaServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the UserPendingMetaService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.UserPendingMetaDao;
import edu.yu.einstein.wasp.model.UserPendingMeta;
import edu.yu.einstein.wasp.service.UserPendingMetaService;

@Service
public class UserPendingMetaServiceImpl extends WaspMetaServiceImpl<UserPendingMeta> implements UserPendingMetaService {

	/**
	 * userPendingMetaDao;
	 *
	 */
	private UserPendingMetaDao userPendingMetaDao;

	/**
	 * setUserPendingMetaDao(UserPendingMetaDao userPendingMetaDao)
	 *
	 * @param userPendingMetaDao
	 *
	 */
	@Override
	@Autowired
	public void setUserPendingMetaDao(UserPendingMetaDao userPendingMetaDao) {
		this.userPendingMetaDao = userPendingMetaDao;
		this.setWaspDao(userPendingMetaDao);
	}

	/**
	 * getUserPendingMetaDao();
	 *
	 * @return userPendingMetaDao
	 *
	 */
	@Override
	public UserPendingMetaDao getUserPendingMetaDao() {
		return this.userPendingMetaDao;
	}


  @Override
public UserPendingMeta getUserPendingMetaByUserPendingMetaId (final int userPendingMetaId) {
    return this.getUserPendingMetaDao().getUserPendingMetaByUserPendingMetaId(userPendingMetaId);
  }

  @Override
public UserPendingMeta getUserPendingMetaByKUserpendingId (final String k, final int userpendingId) {
    return this.getUserPendingMetaDao().getUserPendingMetaByKUserpendingId(k, userpendingId);
  }

  @Override
public void updateByUserpendingId (final String area, final int userpendingId, final List<UserPendingMeta> metaList) {
    this.getUserPendingMetaDao().updateByUserpendingId(area, userpendingId, metaList); 
  }

  @Override
public void updateByUserpendingId (final int userpendingId, final List<UserPendingMeta> metaList) {
    this.getUserPendingMetaDao().updateByUserpendingId(userpendingId, metaList); 
  }


}

