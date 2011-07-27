
/**
 *
 * JobTaskAChipseqArun.java 
 * @author echeng (table2type.pl)
 *  
 * the JobTaskAChipseqArun object
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.*;
import java.util.*;
import edu.yu.einstein.wasp.model.*;


import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;


public class JobTaskAChipseqArunDaoImpl extends WaspDaoImpl<JobTaskAChipseqArun> implements edu.yu.einstein.wasp.dao.JobTaskAChipseqArunDao {

  @SuppressWarnings("unchecked")
  @Transactional
  public JobTaskAChipseqArun getJobTaskAChipseqArunByJobtaskArunId (final int jobtaskArunId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM JobTaskAChipseqArun a WHERE "
       + "a.jobtaskArunId = :jobtaskArunId";
     Query query = em.createQuery(queryString);
      query.setParameter("jobtaskArunId", jobtaskArunId);

    return query.getResultList();
  }
  });
    List<JobTaskAChipseqArun> results = (List<JobTaskAChipseqArun>) res;
    if (results.size() == 0) {
      JobTaskAChipseqArun rt = new JobTaskAChipseqArun();
      return rt;
    }
    return (JobTaskAChipseqArun) results.get(0);
  }


}

