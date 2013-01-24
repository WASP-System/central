
/**
 *
 * UserMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the UserMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.UserMeta;


public interface UserMetaDao extends WaspMetaDao<UserMeta> {

  public UserMeta getUserMetaByUserMetaId (final int userMetaId);

  public UserMeta getUserMetaByKUserId (final String k, final int UserId);





}

