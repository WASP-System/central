
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

import edu.yu.einstein.wasp.service.AcctInvoiceService;
import edu.yu.einstein.wasp.dao.AcctInvoiceDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.AcctInvoice;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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
	public AcctInvoiceDao getAcctInvoiceDao() {
		return this.acctInvoiceDao;
	}


  public AcctInvoice getAcctInvoiceByInvoiceId (final int invoiceId) {
    return this.getAcctInvoiceDao().getAcctInvoiceByInvoiceId(invoiceId);
  }

}

