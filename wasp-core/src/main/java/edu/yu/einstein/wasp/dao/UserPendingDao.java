
/**
 *
 * UserPendingDao.java 
 * @author echeng (table2type.pl)
 *  
 * the UserPendingDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.UserPending;


public interface UserPendingDao extends WaspDao<UserPending> {

  public UserPending getUserPendingByUserPendingId (final int userPendingId);

}

