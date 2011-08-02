
/**
 *
 * MetaImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Meta object
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

import edu.yu.einstein.wasp.model.Meta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class MetaDaoImpl extends WaspDaoImpl<Meta> implements edu.yu.einstein.wasp.dao.MetaDao {

  public MetaDaoImpl() {
    super();
    this.entityClass = Meta.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Meta getMetaByMetaId (final int metaId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Meta a WHERE "
       + "a.metaId = :metaId";
     Query query = em.createQuery(queryString);
      query.setParameter("metaId", metaId);

    return query.getResultList();
  }
  });
    List<Meta> results = (List<Meta>) res;
    if (results.size() == 0) {
      Meta rt = new Meta();
      return rt;
    }
    return (Meta) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Meta getMetaByPropertyK (final String property, final String k) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Meta a WHERE "
       + "a.property = :property"
       + " AND "+ "a.k = :k";
     Query query = em.createQuery(queryString);
      query.setParameter("property", property);
      query.setParameter("k", k);

    return query.getResultList();
  }
  });
    List<Meta> results = (List<Meta>) res;
    if (results.size() == 0) {
      Meta rt = new Meta();
      return rt;
    }
    return (Meta) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Meta getMetaByPropertyV (final String property, final String v) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Meta a WHERE "
       + "a.property = :property"
       + " AND "+ "a.v = :v";
     Query query = em.createQuery(queryString);
      query.setParameter("property", property);
      query.setParameter("v", v);

    return query.getResultList();
  }
  });
    List<Meta> results = (List<Meta>) res;
    if (results.size() == 0) {
      Meta rt = new Meta();
      return rt;
    }
    return (Meta) results.get(0);
  }


}

