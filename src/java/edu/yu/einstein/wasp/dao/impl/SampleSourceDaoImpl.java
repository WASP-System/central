
/**
 *
 * SampleSourceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSource object
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

import edu.yu.einstein.wasp.model.SampleSource;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleSourceDaoImpl extends WaspDaoImpl<SampleSource> implements edu.yu.einstein.wasp.dao.SampleSourceDao {

  public SampleSourceDaoImpl() {
    super();
    this.entityClass = SampleSource.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public SampleSource getSampleSourceBySampleSourceId (final int sampleSourceId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM SampleSource a WHERE "
       + "a.sampleSourceId = :sampleSourceId";
     Query query = em.createQuery(queryString);
      query.setParameter("sampleSourceId", sampleSourceId);

    return query.getResultList();
  }
  });
    List<SampleSource> results = (List<SampleSource>) res;
    if (results.size() == 0) {
      SampleSource rt = new SampleSource();
      return rt;
    }
    return (SampleSource) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public SampleSource getSampleSourceBySampleIdMultiplexindex (final int sampleId, final int multiplexindex) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM SampleSource a WHERE "
       + "a.sampleId = :sampleId"
       + "AND "+ "a.multiplexindex = :multiplexindex";
     Query query = em.createQuery(queryString);
      query.setParameter("sampleId", sampleId);
      query.setParameter("multiplexindex", multiplexindex);

    return query.getResultList();
  }
  });
    List<SampleSource> results = (List<SampleSource>) res;
    if (results.size() == 0) {
      SampleSource rt = new SampleSource();
      return rt;
    }
    return (SampleSource) results.get(0);
  }


}

