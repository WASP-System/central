
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

import edu.yu.einstein.wasp.service.UserPendingMetaService;
import edu.yu.einstein.wasp.dao.UserPendingMetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.UserPendingMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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
	public UserPendingMetaDao getUserPendingMetaDao() {
		return this.userPendingMetaDao;
	}


  public UserPendingMeta getUserPendingMetaByUserPendingMetaId (final int userPendingMetaId) {
    return this.getUserPendingMetaDao().getUserPendingMetaByUserPendingMetaId(userPendingMetaId);
  }

  public UserPendingMeta getUserPendingMetaByKUserpendingId (final String k, final int userpendingId) {
    return this.getUserPendingMetaDao().getUserPendingMetaByKUserpendingId(k, userpendingId);
  }

  public void updateByUserpendingId (final String area, final int userpendingId, final List<UserPendingMeta> metaList) {
    this.getUserPendingMetaDao().updateByUserpendingId(area, userpendingId, metaList); 
  }

  public void updateByUserpendingId (final int userpendingId, final List<UserPendingMeta> metaList) {
    this.getUserPendingMetaDao().updateByUserpendingId(userpendingId, metaList); 
  }


}

