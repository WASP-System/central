
/**
 *
 * SampleImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Sample object
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

import edu.yu.einstein.wasp.model.Sample;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleDaoImpl extends WaspDaoImpl<Sample> implements edu.yu.einstein.wasp.dao.SampleDao {

  public SampleDaoImpl() {
    super();
    this.entityClass = Sample.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Sample getSampleBySampleId (final int sampleId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Sample a WHERE "
       + "a.sampleId = :sampleId";
     Query query = em.createQuery(queryString);
      query.setParameter("sampleId", sampleId);

    return query.getResultList();
  }
  });
    List<Sample> results = (List<Sample>) res;
    if (results.size() == 0) {
      Sample rt = new Sample();
      return rt;
    }
    return (Sample) results.get(0);
  }


}

