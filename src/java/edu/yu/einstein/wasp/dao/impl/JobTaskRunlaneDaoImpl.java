
/**
 *
 * JobTaskRunlane.java 
 * @author echeng (table2type.pl)
 *  
 * the JobTaskRunlane object
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.*;
import java.util.*;
import edu.yu.einstein.wasp.model.*;


import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;


public class JobTaskRunlaneDaoImpl extends WaspDaoImpl<JobTaskRunlane> implements edu.yu.einstein.wasp.dao.JobTaskRunlaneDao {

  @SuppressWarnings("unchecked")
  @Transactional
  public JobTaskRunlane getJobTaskRunlaneByJobtaskrunlaneId (final int jobtaskrunlaneId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM JobTaskRunlane a WHERE "
       + "a.jobtaskrunlaneId = :jobtaskrunlaneId";
     Query query = em.createQuery(queryString);
      query.setParameter("jobtaskrunlaneId", jobtaskrunlaneId);

    return query.getResultList();
  }
  });
    List<JobTaskRunlane> results = (List<JobTaskRunlane>) res;
    if (results.size() == 0) {
      JobTaskRunlane rt = new JobTaskRunlane();
      return rt;
    }
    return (JobTaskRunlane) results.get(0);
  }


}

