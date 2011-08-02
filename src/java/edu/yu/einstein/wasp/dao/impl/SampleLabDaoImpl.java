
/**
 *
 * SampleLabImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleLab object
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

import edu.yu.einstein.wasp.model.SampleLab;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleLabDaoImpl extends WaspDaoImpl<SampleLab> implements edu.yu.einstein.wasp.dao.SampleLabDao {

  public SampleLabDaoImpl() {
    super();
    this.entityClass = SampleLab.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public SampleLab getSampleLabBySampleLabId (final int sampleLabId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM SampleLab a WHERE "
       + "a.sampleLabId = :sampleLabId";
     Query query = em.createQuery(queryString);
      query.setParameter("sampleLabId", sampleLabId);

    return query.getResultList();
  }
  });
    List<SampleLab> results = (List<SampleLab>) res;
    if (results.size() == 0) {
      SampleLab rt = new SampleLab();
      return rt;
    }
    return (SampleLab) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public SampleLab getSampleLabBySampleIdLabId (final int sampleId, final int labId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM SampleLab a WHERE "
       + "a.sampleId = :sampleId"
       + " AND "+ "a.labId = :labId";
     Query query = em.createQuery(queryString);
      query.setParameter("sampleId", sampleId);
      query.setParameter("labId", labId);

    return query.getResultList();
  }
  });
    List<SampleLab> results = (List<SampleLab>) res;
    if (results.size() == 0) {
      SampleLab rt = new SampleLab();
      return rt;
    }
    return (SampleLab) results.get(0);
  }


}

