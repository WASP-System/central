
/**
 *
 * JobSampleImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSample object
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

import edu.yu.einstein.wasp.model.JobSample;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class JobSampleDaoImpl extends WaspDaoImpl<JobSample> implements edu.yu.einstein.wasp.dao.JobSampleDao {

  public JobSampleDaoImpl() {
    super();
    this.entityClass = JobSample.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public JobSample getJobSampleByJobSampleId (final int jobSampleId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM JobSample a WHERE "
       + "a.jobSampleId = :jobSampleId";
     Query query = em.createQuery(queryString);
      query.setParameter("jobSampleId", jobSampleId);

    return query.getResultList();
  }
  });
    List<JobSample> results = (List<JobSample>) res;
    if (results.size() == 0) {
      JobSample rt = new JobSample();
      return rt;
    }
    return (JobSample) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public JobSample getJobSampleByJobIdSampleId (final int jobId, final int sampleId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM JobSample a WHERE "
       + "a.jobId = :jobId"
       + " AND "+ "a.sampleId = :sampleId";
     Query query = em.createQuery(queryString);
      query.setParameter("jobId", jobId);
      query.setParameter("sampleId", sampleId);

    return query.getResultList();
  }
  });
    List<JobSample> results = (List<JobSample>) res;
    if (results.size() == 0) {
      JobSample rt = new JobSample();
      return rt;
    }
    return (JobSample) results.get(0);
  }


}

