
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserPending;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class UserPendingDaoImpl extends WaspDaoImpl<UserPending> implements edu.yu.einstein.wasp.dao.UserPendingDao {

  public UserPendingDaoImpl() {
    super();
    this.entityClass = UserPending.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public UserPending getUserPendingByUserPendingId (final int userPendingId) {
    HashMap m = new HashMap();
    m.put("userPendingId", userPendingId);
    List<UserPending> results = (List<UserPending>) this.findByMap((Map) m);
    if (results.size() == 0) {
      UserPending rt = new UserPending();
      return rt;
    }
    return (UserPending) results.get(0);
  }

}

