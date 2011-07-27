
/**
 *
 * JobTaskmeta.java 
 * @author echeng (table2type.pl)
 *  
 * the JobTaskmeta object
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.*;
import java.util.*;
import edu.yu.einstein.wasp.model.*;


import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;


public class JobTaskmetaDaoImpl extends WaspDaoImpl<JobTaskmeta> implements edu.yu.einstein.wasp.dao.JobTaskmetaDao {

  @SuppressWarnings("unchecked")
  @Transactional
  public JobTaskmeta getJobTaskmetaByJobTaskmetaId (final int jobTaskmetaId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM JobTaskmeta a WHERE "
       + "a.jobTaskmetaId = :jobTaskmetaId";
     Query query = em.createQuery(queryString);
      query.setParameter("jobTaskmetaId", jobTaskmetaId);

    return query.getResultList();
  }
  });
    List<JobTaskmeta> results = (List<JobTaskmeta>) res;
    if (results.size() == 0) {
      JobTaskmeta rt = new JobTaskmeta();
      return rt;
    }
    return (JobTaskmeta) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public JobTaskmeta getJobTaskmetaByKJobtaskId (final String k, final int jobtaskId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM JobTaskmeta a WHERE "
       + "a.k = :k"
       + "AND "+ "a.jobtaskId = :jobtaskId";
     Query query = em.createQuery(queryString);
      query.setParameter("k", k);
      query.setParameter("jobtaskId", jobtaskId);

    return query.getResultList();
  }
  });
    List<JobTaskmeta> results = (List<JobTaskmeta>) res;
    if (results.size() == 0) {
      JobTaskmeta rt = new JobTaskmeta();
      return rt;
    }
    return (JobTaskmeta) results.get(0);
  }


}

