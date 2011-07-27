
/**
 *
 * LabImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Lab object
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

import edu.yu.einstein.wasp.model.Lab;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class LabDaoImpl extends WaspDaoImpl<Lab> implements edu.yu.einstein.wasp.dao.LabDao {

  public LabDaoImpl() {
    super();
    this.entityClass = Lab.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Lab getLabByLabId (final int labId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Lab a WHERE "
       + "a.labId = :labId";
     Query query = em.createQuery(queryString);
      query.setParameter("labId", labId);

    return query.getResultList();
  }
  });
    List<Lab> results = (List<Lab>) res;
    if (results.size() == 0) {
      Lab rt = new Lab();
      return rt;
    }
    return (Lab) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Lab getLabByName (final String name) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Lab a WHERE "
       + "a.name = :name";
     Query query = em.createQuery(queryString);
      query.setParameter("name", name);

    return query.getResultList();
  }
  });
    List<Lab> results = (List<Lab>) res;
    if (results.size() == 0) {
      Lab rt = new Lab();
      return rt;
    }
    return (Lab) results.get(0);
  }


}

