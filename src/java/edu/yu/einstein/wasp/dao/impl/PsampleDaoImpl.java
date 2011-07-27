
/**
 *
 * Psample.java 
 * @author echeng (table2type.pl)
 *  
 * the Psample object
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.*;
import java.util.*;
import edu.yu.einstein.wasp.model.*;


import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;


public class PsampleDaoImpl extends WaspDaoImpl<Psample> implements edu.yu.einstein.wasp.dao.PsampleDao {

  @SuppressWarnings("unchecked")
  @Transactional
  public Psample getPsampleByPsampleId (final int psampleId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Psample a WHERE "
       + "a.psampleId = :psampleId";
     Query query = em.createQuery(queryString);
      query.setParameter("psampleId", psampleId);

    return query.getResultList();
  }
  });
    List<Psample> results = (List<Psample>) res;
    if (results.size() == 0) {
      Psample rt = new Psample();
      return rt;
    }
    return (Psample) results.get(0);
  }


}

