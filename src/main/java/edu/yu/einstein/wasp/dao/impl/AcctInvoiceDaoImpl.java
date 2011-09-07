
/**
 *
 * AcctInvoiceDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctInvoice Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.AcctInvoice;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class AcctInvoiceDaoImpl extends WaspDaoImpl<AcctInvoice> implements edu.yu.einstein.wasp.dao.AcctInvoiceDao {

	/**
	 * AcctInvoiceDaoImpl() Constructor
	 *
	 *
	 */
	public AcctInvoiceDaoImpl() {
		super();
		this.entityClass = AcctInvoice.class;
	}


	/**
	 * getAcctInvoiceByInvoiceId(final int invoiceId)
	 *
	 * @param final int invoiceId
	 *
	 * @return acctInvoice
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public AcctInvoice getAcctInvoiceByInvoiceId (final int invoiceId) {
    		HashMap m = new HashMap();
		m.put("invoiceId", invoiceId);

		List<AcctInvoice> results = (List<AcctInvoice>) this.findByMap((Map) m);

		if (results.size() == 0) {
			AcctInvoice rt = new AcctInvoice();
			return rt;
		}
		return (AcctInvoice) results.get(0);
	}



}

