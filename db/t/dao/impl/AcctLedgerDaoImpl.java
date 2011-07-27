
/**
 *
 * AcctLedgerImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctLedger object
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

import edu.yu.einstein.wasp.model.AcctLedger;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class AcctLedgerDaoImpl extends WaspDaoImpl<AcctLedger> implements edu.yu.einstein.wasp.dao.AcctLedgerDao {

  public AcctLedgerDaoImpl() {
    super();
    this.entityClass = AcctLedger.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public AcctLedger getAcctLedgerByLedgerId (final int ledgerId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM AcctLedger a WHERE "
       + "a.ledgerId = :ledgerId";
     Query query = em.createQuery(queryString);
      query.setParameter("ledgerId", ledgerId);

    return query.getResultList();
  }
  });
    List<AcctLedger> results = (List<AcctLedger>) res;
    if (results.size() == 0) {
      AcctLedger rt = new AcctLedger();
      return rt;
    }
    return (AcctLedger) results.get(0);
  }


}

