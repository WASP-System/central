
/**
 *
 * TypeSampleImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSample object
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

import edu.yu.einstein.wasp.model.TypeSample;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class TypeSampleDaoImpl extends WaspDaoImpl<TypeSample> implements edu.yu.einstein.wasp.dao.TypeSampleDao {

  public TypeSampleDaoImpl() {
    super();
    this.entityClass = TypeSample.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public TypeSample getTypeSampleByTypeSampleId (final int typeSampleId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM TypeSample a WHERE "
       + "a.typeSampleId = :typeSampleId";
     Query query = em.createQuery(queryString);
      query.setParameter("typeSampleId", typeSampleId);

    return query.getResultList();
  }
  });
    List<TypeSample> results = (List<TypeSample>) res;
    if (results.size() == 0) {
      TypeSample rt = new TypeSample();
      return rt;
    }
    return (TypeSample) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public TypeSample getTypeSampleByIName (final String iName) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM TypeSample a WHERE "
       + "a.iName = :iName";
     Query query = em.createQuery(queryString);
      query.setParameter("iName", iName);

    return query.getResultList();
  }
  });
    List<TypeSample> results = (List<TypeSample>) res;
    if (results.size() == 0) {
      TypeSample rt = new TypeSample();
      return rt;
    }
    return (TypeSample) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public TypeSample getTypeSampleByName (final String name) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM TypeSample a WHERE "
       + "a.name = :name";
     Query query = em.createQuery(queryString);
      query.setParameter("name", name);

    return query.getResultList();
  }
  });
    List<TypeSample> results = (List<TypeSample>) res;
    if (results.size() == 0) {
      TypeSample rt = new TypeSample();
      return rt;
    }
    return (TypeSample) results.get(0);
  }


}

