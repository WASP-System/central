
/**
 *
 * AcctInvoiceService.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctInvoiceService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.AcctInvoiceDao;
import edu.yu.einstein.wasp.model.AcctInvoice;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface AcctInvoiceService extends WaspService<AcctInvoice> {

	/**
	 * setAcctInvoiceDao(AcctInvoiceDao acctInvoiceDao)
	 *
	 * @param acctInvoiceDao
	 *
	 */
	public void setAcctInvoiceDao(AcctInvoiceDao acctInvoiceDao);

	/**
	 * getAcctInvoiceDao();
	 *
	 * @return acctInvoiceDao
	 *
	 */
	public AcctInvoiceDao getAcctInvoiceDao();

  public AcctInvoice getAcctInvoiceByInvoiceId (final int invoiceId);


}

