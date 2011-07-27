
/**
 *
 * JobTask.java 
 * @author echeng (table2type.pl)
 *  
 * the JobTask object
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.*;
import java.util.*;
import edu.yu.einstein.wasp.model.*;


import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;


public class JobTaskDaoImpl extends WaspDaoImpl<JobTask> implements edu.yu.einstein.wasp.dao.JobTaskDao {

  @SuppressWarnings("unchecked")
  @Transactional
  public JobTask getJobTaskByJobTaskId (final int jobTaskId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM JobTask a WHERE "
       + "a.jobTaskId = :jobTaskId";
     Query query = em.createQuery(queryString);
      query.setParameter("jobTaskId", jobTaskId);

    return query.getResultList();
  }
  });
    List<JobTask> results = (List<JobTask>) res;
    if (results.size() == 0) {
      JobTask rt = new JobTask();
      return rt;
    }
    return (JobTask) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public JobTask getJobTaskByINameJobId (final String iName, final int jobId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM JobTask a WHERE "
       + "a.iName = :iName"
       + "AND "+ "a.jobId = :jobId";
     Query query = em.createQuery(queryString);
      query.setParameter("iName", iName);
      query.setParameter("jobId", jobId);

    return query.getResultList();
  }
  });
    List<JobTask> results = (List<JobTask>) res;
    if (results.size() == 0) {
      JobTask rt = new JobTask();
      return rt;
    }
    return (JobTask) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public JobTask getJobTaskByNameJobId (final String name, final int jobId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM JobTask a WHERE "
       + "a.name = :name"
       + "AND "+ "a.jobId = :jobId";
     Query query = em.createQuery(queryString);
      query.setParameter("name", name);
      query.setParameter("jobId", jobId);

    return query.getResultList();
  }
  });
    List<JobTask> results = (List<JobTask>) res;
    if (results.size() == 0) {
      JobTask rt = new JobTask();
      return rt;
    }
    return (JobTask) results.get(0);
  }


}

