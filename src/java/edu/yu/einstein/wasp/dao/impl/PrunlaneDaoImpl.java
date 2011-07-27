
/**
 *
 * Prunlane.java 
 * @author echeng (table2type.pl)
 *  
 * the Prunlane object
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.*;
import java.util.*;
import edu.yu.einstein.wasp.model.*;


import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;


public class PrunlaneDaoImpl extends WaspDaoImpl<Prunlane> implements edu.yu.einstein.wasp.dao.PrunlaneDao {

  @SuppressWarnings("unchecked")
  @Transactional
  public Prunlane getPrunlaneByPrunlaneId (final int prunlaneId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Prunlane a WHERE "
       + "a.prunlaneId = :prunlaneId";
     Query query = em.createQuery(queryString);
      query.setParameter("prunlaneId", prunlaneId);

    return query.getResultList();
  }
  });
    List<Prunlane> results = (List<Prunlane>) res;
    if (results.size() == 0) {
      Prunlane rt = new Prunlane();
      return rt;
    }
    return (Prunlane) results.get(0);
  }


}

