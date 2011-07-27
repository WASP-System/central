
/**
 *
 * Pjob.java 
 * @author echeng (table2type.pl)
 *  
 * the Pjob object
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.*;
import java.util.*;
import edu.yu.einstein.wasp.model.*;


import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;


public class PjobDaoImpl extends WaspDaoImpl<Pjob> implements edu.yu.einstein.wasp.dao.PjobDao {

  @SuppressWarnings("unchecked")
  @Transactional
  public Pjob getPjobByPjobId (final int pjobId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Pjob a WHERE "
       + "a.pjobId = :pjobId";
     Query query = em.createQuery(queryString);
      query.setParameter("pjobId", pjobId);

    return query.getResultList();
  }
  });
    List<Pjob> results = (List<Pjob>) res;
    if (results.size() == 0) {
      Pjob rt = new Pjob();
      return rt;
    }
    return (Pjob) results.get(0);
  }


}

