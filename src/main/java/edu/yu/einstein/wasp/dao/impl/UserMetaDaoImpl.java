
/**
 *
 * UserMetaImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the UserMeta object
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

import edu.yu.einstein.wasp.model.UserMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class UserMetaDaoImpl extends WaspDaoImpl<UserMeta> implements edu.yu.einstein.wasp.dao.UserMetaDao {

  public UserMetaDaoImpl() {
    super();
    this.entityClass = UserMeta.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public UserMeta getUserMetaByUserMetaId (final int userMetaId) {
    HashMap m = new HashMap();
    m.put("userMetaId", userMetaId);
    List<UserMeta> results = (List<UserMeta>) this.findByMap((Map) m);
    if (results.size() == 0) {
      UserMeta rt = new UserMeta();
      return rt;
    }
    return (UserMeta) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public UserMeta getUserMetaByKUserId (final String k, final int UserId) {
    HashMap m = new HashMap();
    m.put("k", k);
    m.put("UserId", UserId);
    List<UserMeta> results = (List<UserMeta>) this.findByMap((Map) m);
    if (results.size() == 0) {
      UserMeta rt = new UserMeta();
      return rt;
    }
    return (UserMeta) results.get(0);
  }



  @SuppressWarnings("unchecked")
  @Transactional
  public void updateByUserId (final int UserId, final List<UserMeta> metaList) {

    getJpaTemplate().execute(new JpaCallback() {

      public Object doInJpa(EntityManager em) throws PersistenceException {
        em.createNativeQuery("delete from userMeta where UserId=:UserId").setParameter("UserId", UserId).executeUpdate();

        for (UserMeta m:metaList) {
          em.persist(m);
        }

        return null;
      }
    });

  }
}

