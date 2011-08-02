
/**
 *
 * JobImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Job object
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

import edu.yu.einstein.wasp.model.Job;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class JobDaoImpl extends WaspDaoImpl<Job> implements edu.yu.einstein.wasp.dao.JobDao {

  public JobDaoImpl() {
    super();
    this.entityClass = Job.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Job getJobByJobId (final int jobId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Job a WHERE "
       + "a.jobId = :jobId";
     Query query = em.createQuery(queryString);
      query.setParameter("jobId", jobId);

    return query.getResultList();
  }
  });
    List<Job> results = (List<Job>) res;
    if (results.size() == 0) {
      Job rt = new Job();
      return rt;
    }
    return (Job) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Job getJobByNameLabId (final String name, final int labId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Job a WHERE "
       + "a.name = :name"
       + " AND "+ "a.labId = :labId";
     Query query = em.createQuery(queryString);
      query.setParameter("name", name);
      query.setParameter("labId", labId);

    return query.getResultList();
  }
  });
    List<Job> results = (List<Job>) res;
    if (results.size() == 0) {
      Job rt = new Job();
      return rt;
    }
    return (Job) results.get(0);
  }


}

