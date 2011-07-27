
/**
 *
 * UsermetaImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Usermeta object
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Usermeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class UsermetaDaoImpl extends WaspDaoImpl<Usermeta> implements edu.yu.einstein.wasp.dao.UsermetaDao {

  public UsermetaDaoImpl() {
    super();
    this.entityClass = Usermeta.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Usermeta getUsermetaByUsermetaId (final int usermetaId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Usermeta a WHERE "
       + "a.usermetaId = :usermetaId";
     Query query = em.createQuery(queryString);
      query.setParameter("usermetaId", usermetaId);

    return query.getResultList();
  }
  });
    List<Usermeta> results = (List<Usermeta>) res;
    if (results.size() == 0) {
      Usermeta rt = new Usermeta();
      return rt;
    }
    return (Usermeta) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Usermeta getUsermetaByKUserId (final String k, final int UserId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Usermeta a WHERE "
       + "a.k = :k"
       + "AND "+ "a.UserId = :UserId";
     Query query = em.createQuery(queryString);
      query.setParameter("k", k);
      query.setParameter("UserId", UserId);

    return query.getResultList();
  }
  });
    List<Usermeta> results = (List<Usermeta>) res;
    if (results.size() == 0) {
      Usermeta rt = new Usermeta();
      return rt;
    }
    return (Usermeta) results.get(0);
  }



  @SuppressWarnings("unchecked")
  @Transactional
  public void updateByUserId (final int UserId, final List<Usermeta> metaList) {

    getJpaTemplate().execute(new JpaCallback() {

      public Object doInJpa(EntityManager em) throws PersistenceException {
        em.createNativeQuery("delete from usermeta where UserId=:UserId").setParameter("UserId", UserId).executeUpdate();

        for (Usermeta m:metaList) {
          em.persist(m);
        }

        return null;
      }
    });

  }
}

