
/**
 *
 * PAChipseqArun.java 
 * @author echeng (table2type.pl)
 *  
 * the PAChipseqArun object
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.*;
import java.util.*;
import edu.yu.einstein.wasp.model.*;


import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;


public class PAChipseqArunDaoImpl extends WaspDaoImpl<PAChipseqArun> implements edu.yu.einstein.wasp.dao.PAChipseqArunDao {

  @SuppressWarnings("unchecked")
  @Transactional
  public PAChipseqArun getPAChipseqArunByPArunId (final int pArunId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM PAChipseqArun a WHERE "
       + "a.pArunId = :pArunId";
     Query query = em.createQuery(queryString);
      query.setParameter("pArunId", pArunId);

    return query.getResultList();
  }
  });
    List<PAChipseqArun> results = (List<PAChipseqArun>) res;
    if (results.size() == 0) {
      PAChipseqArun rt = new PAChipseqArun();
      return rt;
    }
    return (PAChipseqArun) results.get(0);
  }


}

