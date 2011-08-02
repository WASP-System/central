
/**
 *
 * RolesetImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Roleset object
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

import edu.yu.einstein.wasp.model.Roleset;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class RolesetDaoImpl extends WaspDaoImpl<Roleset> implements edu.yu.einstein.wasp.dao.RolesetDao {

  public RolesetDaoImpl() {
    super();
    this.entityClass = Roleset.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Roleset getRolesetByRolesetId (final int rolesetId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Roleset a WHERE "
       + "a.rolesetId = :rolesetId";
     Query query = em.createQuery(queryString);
      query.setParameter("rolesetId", rolesetId);

    return query.getResultList();
  }
  });
    List<Roleset> results = (List<Roleset>) res;
    if (results.size() == 0) {
      Roleset rt = new Roleset();
      return rt;
    }
    return (Roleset) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Roleset getRolesetByParentroleIdChildroleId (final int parentroleId, final int childroleId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Roleset a WHERE "
       + "a.parentroleId = :parentroleId"
       + " AND "+ "a.childroleId = :childroleId";
     Query query = em.createQuery(queryString);
      query.setParameter("parentroleId", parentroleId);
      query.setParameter("childroleId", childroleId);

    return query.getResultList();
  }
  });
    List<Roleset> results = (List<Roleset>) res;
    if (results.size() == 0) {
      Roleset rt = new Roleset();
      return rt;
    }
    return (Roleset) results.get(0);
  }


}

