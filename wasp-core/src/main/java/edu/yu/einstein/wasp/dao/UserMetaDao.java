
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

import java.util.List;

import edu.yu.einstein.wasp.model.UserMeta;


public interface UserMetaDao extends WaspDao<UserMeta> {

  public UserMeta getUserMetaByUserMetaId (final int userMetaId);

  public UserMeta getUserMetaByKUserId (final String k, final int UserId);


  public void updateByUserId (final String area, final int UserId, final List<UserMeta> metaList);

  public void updateByUserId (final int UserId, final List<UserMeta> metaList);




}

