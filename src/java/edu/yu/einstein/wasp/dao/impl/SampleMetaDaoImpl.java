
/**
 *
 * SampleMetaImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleMeta object
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

import edu.yu.einstein.wasp.model.SampleMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleMetaDaoImpl extends WaspDaoImpl<SampleMeta> implements edu.yu.einstein.wasp.dao.SampleMetaDao {

  public SampleMetaDaoImpl() {
    super();
    this.entityClass = SampleMeta.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public SampleMeta getSampleMetaBySampleMetaId (final int sampleMetaId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM SampleMeta a WHERE "
       + "a.sampleMetaId = :sampleMetaId";
     Query query = em.createQuery(queryString);
      query.setParameter("sampleMetaId", sampleMetaId);

    return query.getResultList();
  }
  });
    List<SampleMeta> results = (List<SampleMeta>) res;
    if (results.size() == 0) {
      SampleMeta rt = new SampleMeta();
      return rt;
    }
    return (SampleMeta) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public SampleMeta getSampleMetaByKSampleId (final String k, final int sampleId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM SampleMeta a WHERE "
       + "a.k = :k"
       + "AND "+ "a.sampleId = :sampleId";
     Query query = em.createQuery(queryString);
      query.setParameter("k", k);
      query.setParameter("sampleId", sampleId);

    return query.getResultList();
  }
  });
    List<SampleMeta> results = (List<SampleMeta>) res;
    if (results.size() == 0) {
      SampleMeta rt = new SampleMeta();
      return rt;
    }
    return (SampleMeta) results.get(0);
  }



  @SuppressWarnings("unchecked")
  @Transactional
  public void updateBySampleId (final int sampleId, final List<SampleMeta> metaList) {

    getJpaTemplate().execute(new JpaCallback() {

      public Object doInJpa(EntityManager em) throws PersistenceException {
        em.createNativeQuery("delete from sampleMeta where sampleId=:sampleId").setParameter("sampleId", sampleId).executeUpdate();

        for (SampleMeta m:metaList) {
          em.persist(m);
        }

        return null;
      }
    });

  }
}

