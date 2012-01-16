
/**
 *
 * UserPendingMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the UserPendingMetaService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.UserPendingMetaDao;
import edu.yu.einstein.wasp.model.UserPendingMeta;

@Service
public interface UserPendingMetaService extends WaspMetaService<UserPendingMeta> {

	/**
	 * setUserPendingMetaDao(UserPendingMetaDao userPendingMetaDao)
	 *
	 * @param userPendingMetaDao
	 *
	 */
	public void setUserPendingMetaDao(UserPendingMetaDao userPendingMetaDao);

	/**
	 * getUserPendingMetaDao();
	 *
	 * @return userPendingMetaDao
	 *
	 */
	public UserPendingMetaDao getUserPendingMetaDao();

  public UserPendingMeta getUserPendingMetaByUserPendingMetaId (final int userPendingMetaId);

  public UserPendingMeta getUserPendingMetaByKUserpendingId (final String k, final int userpendingId);


  public void updateByUserpendingId (final String area, final int userpendingId, final List<UserPendingMeta> metaList);

  public void updateByUserpendingId (final int userpendingId, final List<UserPendingMeta> metaList);


}

