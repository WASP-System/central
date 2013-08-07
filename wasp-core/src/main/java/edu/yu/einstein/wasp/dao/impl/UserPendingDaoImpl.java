
/**
 *
 * UserPendingImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the UserPending object
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.UserPending;


@Transactional("entityManager")
@Repository
public class UserPendingDaoImpl extends WaspDaoImpl<UserPending> implements edu.yu.einstein.wasp.dao.UserPendingDao {

  public UserPendingDaoImpl() {
    super();
    this.entityClass = UserPending.class;
  }

  @Override
  @Transactional("entityManager")
  public UserPending getUserPendingByUserPendingId (final int userPendingId) {
    HashMap<String, Integer> m = new HashMap<String, Integer>();
    m.put("id", userPendingId);
    List<UserPending> results = this.findByMap(m);
    if (results.size() == 0) {
      UserPending rt = new UserPending();
      return rt;
    }
    return results.get(0);
  }

}

