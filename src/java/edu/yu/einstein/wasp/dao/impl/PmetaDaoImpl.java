
/**
 *
 * Pmeta.java 
 * @author echeng (table2type.pl)
 *  
 * the Pmeta object
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.*;
import java.util.*;
import edu.yu.einstein.wasp.model.*;


import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;


public class PmetaDaoImpl extends WaspDaoImpl<Pmeta> implements edu.yu.einstein.wasp.dao.PmetaDao {

  @SuppressWarnings("unchecked")
  @Transactional
  public Pmeta getPmetaByPmetaId (final int pmetaId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Pmeta a WHERE "
       + "a.pmetaId = :pmetaId";
     Query query = em.createQuery(queryString);
      query.setParameter("pmetaId", pmetaId);

    return query.getResultList();
  }
  });
    List<Pmeta> results = (List<Pmeta>) res;
    if (results.size() == 0) {
      Pmeta rt = new Pmeta();
      return rt;
    }
    return (Pmeta) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Pmeta getPmetaByKPId (final String k, final int pId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Pmeta a WHERE "
       + "a.k = :k"
       + "AND "+ "a.pId = :pId";
     Query query = em.createQuery(queryString);
      query.setParameter("k", k);
      query.setParameter("pId", pId);

    return query.getResultList();
  }
  });
    List<Pmeta> results = (List<Pmeta>) res;
    if (results.size() == 0) {
      Pmeta rt = new Pmeta();
      return rt;
    }
    return (Pmeta) results.get(0);
  }


}

