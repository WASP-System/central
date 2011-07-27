
/**
 *
 * AcctInvoiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctInvoice object
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

import edu.yu.einstein.wasp.model.AcctInvoice;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class AcctInvoiceDaoImpl extends WaspDaoImpl<AcctInvoice> implements edu.yu.einstein.wasp.dao.AcctInvoiceDao {

  public AcctInvoiceDaoImpl() {
    super();
    this.entityClass = AcctInvoice.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public AcctInvoice getAcctInvoiceByInvoiceId (final int invoiceId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM AcctInvoice a WHERE "
       + "a.invoiceId = :invoiceId";
     Query query = em.createQuery(queryString);
      query.setParameter("invoiceId", invoiceId);

    return query.getResultList();
  }
  });
    List<AcctInvoice> results = (List<AcctInvoice>) res;
    if (results.size() == 0) {
      AcctInvoice rt = new AcctInvoice();
      return rt;
    }
    return (AcctInvoice) results.get(0);
  }


}

