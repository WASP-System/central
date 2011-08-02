
/**
 *
 * UserroleImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Userrole object
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

import edu.yu.einstein.wasp.model.Userrole;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class UserroleDaoImpl extends WaspDaoImpl<Userrole> implements edu.yu.einstein.wasp.dao.UserroleDao {

  public UserroleDaoImpl() {
    super();
    this.entityClass = Userrole.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Userrole getUserroleByUserroleId (final int userroleId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Userrole a WHERE "
       + "a.userroleId = :userroleId";
     Query query = em.createQuery(queryString);
      query.setParameter("userroleId", userroleId);

    return query.getResultList();
  }
  });
    List<Userrole> results = (List<Userrole>) res;
    if (results.size() == 0) {
      Userrole rt = new Userrole();
      return rt;
    }
    return (Userrole) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Userrole getUserroleByUserIdRoleId (final int UserId, final int roleId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Userrole a WHERE "
       + "a.UserId = :UserId"
       + " AND "+ "a.roleId = :roleId";
     Query query = em.createQuery(queryString);
      query.setParameter("UserId", UserId);
      query.setParameter("roleId", roleId);

    return query.getResultList();
  }
  });
    List<Userrole> results = (List<Userrole>) res;
    if (results.size() == 0) {
      Userrole rt = new Userrole();
      return rt;
    }
    return (Userrole) results.get(0);
  }


}

