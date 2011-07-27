
/**
 *
 * P.java 
 * @author echeng (table2type.pl)
 *  
 * the P object
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.*;
import java.util.*;
import edu.yu.einstein.wasp.model.*;


import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;


public class PDaoImpl extends WaspDaoImpl<P> implements edu.yu.einstein.wasp.dao.PDao {

  @SuppressWarnings("unchecked")
  @Transactional
  public P getPByPId (final int pId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM P a WHERE "
       + "a.pId = :pId";
     Query query = em.createQuery(queryString);
      query.setParameter("pId", pId);

    return query.getResultList();
  }
  });
    List<P> results = (List<P>) res;
    if (results.size() == 0) {
      P rt = new P();
      return rt;
    }
    return (P) results.get(0);
  }


}

