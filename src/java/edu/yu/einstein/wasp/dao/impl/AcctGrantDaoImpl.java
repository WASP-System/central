
/**
 *
 * AcctGrantImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctGrant object
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

import edu.yu.einstein.wasp.model.AcctGrant;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class AcctGrantDaoImpl extends WaspDaoImpl<AcctGrant> implements edu.yu.einstein.wasp.dao.AcctGrantDao {

  public AcctGrantDaoImpl() {
    super();
    this.entityClass = AcctGrant.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public AcctGrant getAcctGrantByGrantId (final int grantId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM AcctGrant a WHERE "
       + "a.grantId = :grantId";
     Query query = em.createQuery(queryString);
      query.setParameter("grantId", grantId);

    return query.getResultList();
  }
  });
    List<AcctGrant> results = (List<AcctGrant>) res;
    if (results.size() == 0) {
      AcctGrant rt = new AcctGrant();
      return rt;
    }
    return (AcctGrant) results.get(0);
  }


}

