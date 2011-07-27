
/**
 *
 * DepartmentImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Department object
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

import edu.yu.einstein.wasp.model.Department;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class DepartmentDaoImpl extends WaspDaoImpl<Department> implements edu.yu.einstein.wasp.dao.DepartmentDao {

  public DepartmentDaoImpl() {
    super();
    this.entityClass = Department.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Department getDepartmentByDepartmentId (final int departmentId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Department a WHERE "
       + "a.departmentId = :departmentId";
     Query query = em.createQuery(queryString);
      query.setParameter("departmentId", departmentId);

    return query.getResultList();
  }
  });
    List<Department> results = (List<Department>) res;
    if (results.size() == 0) {
      Department rt = new Department();
      return rt;
    }
    return (Department) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Department getDepartmentByName (final String name) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Department a WHERE "
       + "a.name = :name";
     Query query = em.createQuery(queryString);
      query.setParameter("name", name);

    return query.getResultList();
  }
  });
    List<Department> results = (List<Department>) res;
    if (results.size() == 0) {
      Department rt = new Department();
      return rt;
    }
    return (Department) results.get(0);
  }


}

