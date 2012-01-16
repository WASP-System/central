
/**
 *
 * AcctInvoiceServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctInvoiceService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AcctInvoiceDao;
import edu.yu.einstein.wasp.model.AcctInvoice;
import edu.yu.einstein.wasp.service.AcctInvoiceService;

@Service
public class AcctInvoiceServiceImpl extends WaspServiceImpl<AcctInvoice> implements AcctInvoiceService {

	/**
	 * acctInvoiceDao;
	 *
	 */
	private AcctInvoiceDao acctInvoiceDao;

	/**
	 * setAcctInvoiceDao(AcctInvoiceDao acctInvoiceDao)
	 *
	 * @param acctInvoiceDao
	 *
	 */
	@Override
	@Autowired
	public void setAcctInvoiceDao(AcctInvoiceDao acctInvoiceDao) {
		this.acctInvoiceDao = acctInvoiceDao;
		this.setWaspDao(acctInvoiceDao);
	}

	/**
	 * getAcctInvoiceDao();
	 *
	 * @return acctInvoiceDao
	 *
	 */
	@Override
	public AcctInvoiceDao getAcctInvoiceDao() {
		return this.acctInvoiceDao;
	}


  @Override
public AcctInvoice getAcctInvoiceByInvoiceId (final int invoiceId) {
    return this.getAcctInvoiceDao().getAcctInvoiceByInvoiceId(invoiceId);
  }

}

