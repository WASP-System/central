
/**
 *
 * LabmetaImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Labmeta object
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

import edu.yu.einstein.wasp.model.Labmeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class LabmetaDaoImpl extends WaspDaoImpl<Labmeta> implements edu.yu.einstein.wasp.dao.LabmetaDao {

  public LabmetaDaoImpl() {
    super();
    this.entityClass = Labmeta.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Labmeta getLabmetaByLabmetaId (final int labmetaId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Labmeta a WHERE "
       + "a.labmetaId = :labmetaId";
     Query query = em.createQuery(queryString);
      query.setParameter("labmetaId", labmetaId);

    return query.getResultList();
  }
  });
    List<Labmeta> results = (List<Labmeta>) res;
    if (results.size() == 0) {
      Labmeta rt = new Labmeta();
      return rt;
    }
    return (Labmeta) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Labmeta getLabmetaByKLabId (final String k, final int labId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Labmeta a WHERE "
       + "a.k = :k"
       + " AND "+ "a.labId = :labId";
     Query query = em.createQuery(queryString);
      query.setParameter("k", k);
      query.setParameter("labId", labId);

    return query.getResultList();
  }
  });
    List<Labmeta> results = (List<Labmeta>) res;
    if (results.size() == 0) {
      Labmeta rt = new Labmeta();
      return rt;
    }
    return (Labmeta) results.get(0);
  }



  @SuppressWarnings("unchecked")
  @Transactional
  public void updateByLabId (final int labId, final List<Labmeta> metaList) {

    getJpaTemplate().execute(new JpaCallback() {

      public Object doInJpa(EntityManager em) throws PersistenceException {
        em.createNativeQuery("delete from labmeta where labId=:labId").setParameter("labId", labId).executeUpdate();

        for (Labmeta m:metaList) {
          em.persist(m);
        }

        return null;
      }
    });

  }
}

