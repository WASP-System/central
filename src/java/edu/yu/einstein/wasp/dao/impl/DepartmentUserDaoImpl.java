
/**
 *
 * DepartmentUserImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the DepartmentUser object
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

import edu.yu.einstein.wasp.model.DepartmentUser;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class DepartmentUserDaoImpl extends WaspDaoImpl<DepartmentUser> implements edu.yu.einstein.wasp.dao.DepartmentUserDao {

  public DepartmentUserDaoImpl() {
    super();
    this.entityClass = DepartmentUser.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public DepartmentUser getDepartmentUserByDepartmentUserId (final int departmentUserId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM DepartmentUser a WHERE "
       + "a.departmentUserId = :departmentUserId";
     Query query = em.createQuery(queryString);
      query.setParameter("departmentUserId", departmentUserId);

    return query.getResultList();
  }
  });
    List<DepartmentUser> results = (List<DepartmentUser>) res;
    if (results.size() == 0) {
      DepartmentUser rt = new DepartmentUser();
      return rt;
    }
    return (DepartmentUser) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public DepartmentUser getDepartmentUserByDepartmentIdUserId (final int departmentId, final int UserId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM DepartmentUser a WHERE "
       + "a.departmentId = :departmentId"
       + " AND "+ "a.UserId = :UserId";
     Query query = em.createQuery(queryString);
      query.setParameter("departmentId", departmentId);
      query.setParameter("UserId", UserId);

    return query.getResultList();
  }
  });
    List<DepartmentUser> results = (List<DepartmentUser>) res;
    if (results.size() == 0) {
      DepartmentUser rt = new DepartmentUser();
      return rt;
    }
    return (DepartmentUser) results.get(0);
  }


}

