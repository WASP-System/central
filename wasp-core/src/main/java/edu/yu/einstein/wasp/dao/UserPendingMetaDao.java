
/**
 *
 * UserPendingMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the UserPendingMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.UserPendingMeta;


public interface UserPendingMetaDao extends WaspMetaDao<UserPendingMeta> {

  public UserPendingMeta getUserPendingMetaByUserPendingMetaId (final int userPendingMetaId);

  public UserPendingMeta getUserPendingMetaByKUserpendingId (final String k, final int userPendingId);





}

