
/**
 *
 * Prun.java 
 * @author echeng (table2type.pl)
 *  
 * the Prun object
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.*;
import java.util.*;
import edu.yu.einstein.wasp.model.*;


import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;


public class PrunDaoImpl extends WaspDaoImpl<Prun> implements edu.yu.einstein.wasp.dao.PrunDao {

  @SuppressWarnings("unchecked")
  @Transactional
  public Prun getPrunByPrunId (final int prunId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Prun a WHERE "
       + "a.prunId = :prunId";
     Query query = em.createQuery(queryString);
      query.setParameter("prunId", prunId);

    return query.getResultList();
  }
  });
    List<Prun> results = (List<Prun>) res;
    if (results.size() == 0) {
      Prun rt = new Prun();
      return rt;
    }
    return (Prun) results.get(0);
  }


}

