
/**
 *
 * JobUserImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobUser object
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

import edu.yu.einstein.wasp.model.JobUser;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class JobUserDaoImpl extends WaspDaoImpl<JobUser> implements edu.yu.einstein.wasp.dao.JobUserDao {

  public JobUserDaoImpl() {
    super();
    this.entityClass = JobUser.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public JobUser getJobUserByJobUserId (final int jobUserId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM JobUser a WHERE "
       + "a.jobUserId = :jobUserId";
     Query query = em.createQuery(queryString);
      query.setParameter("jobUserId", jobUserId);

    return query.getResultList();
  }
  });
    List<JobUser> results = (List<JobUser>) res;
    if (results.size() == 0) {
      JobUser rt = new JobUser();
      return rt;
    }
    return (JobUser) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public JobUser getJobUserByJobIdUserId (final int jobId, final int UserId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM JobUser a WHERE "
       + "a.jobId = :jobId"
       + "AND "+ "a.UserId = :UserId";
     Query query = em.createQuery(queryString);
      query.setParameter("jobId", jobId);
      query.setParameter("UserId", UserId);

    return query.getResultList();
  }
  });
    List<JobUser> results = (List<JobUser>) res;
    if (results.size() == 0) {
      JobUser rt = new JobUser();
      return rt;
    }
    return (JobUser) results.get(0);
  }


}

