
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

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.AcctInvoice;


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

	@Override
	@Transactional
	public AcctInvoice getAcctInvoiceByInvoiceId (final int invoiceId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("invoiceId", invoiceId);

		List<AcctInvoice> results = this.findByMap(m);

		if (results.size() == 0) {
			AcctInvoice rt = new AcctInvoice();
			return rt;
		}
		return results.get(0);
	}



}

