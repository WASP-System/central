
/**
 *
 * LabUserImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the LabUser object
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

import edu.yu.einstein.wasp.model.LabUser;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class LabUserDaoImpl extends WaspDaoImpl<LabUser> implements edu.yu.einstein.wasp.dao.LabUserDao {

  public LabUserDaoImpl() {
    super();
    this.entityClass = LabUser.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public LabUser getLabUserByLabUserId (final int labUserId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM LabUser a WHERE "
       + "a.labUserId = :labUserId";
     Query query = em.createQuery(queryString);
      query.setParameter("labUserId", labUserId);

    return query.getResultList();
  }
  });
    List<LabUser> results = (List<LabUser>) res;
    if (results.size() == 0) {
      LabUser rt = new LabUser();
      return rt;
    }
    return (LabUser) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public LabUser getLabUserByLabIdUserId (final int labId, final int UserId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM LabUser a WHERE "
       + "a.labId = :labId"
       + " AND "+ "a.UserId = :UserId";
     Query query = em.createQuery(queryString);
      query.setParameter("labId", labId);
      query.setParameter("UserId", UserId);

    return query.getResultList();
  }
  });
    List<LabUser> results = (List<LabUser>) res;
    if (results.size() == 0) {
      LabUser rt = new LabUser();
      return rt;
    }
    return (LabUser) results.get(0);
  }


}

