
/**
 *
 * AcctInvoiceService.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctInvoiceService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.AcctInvoiceDao;
import edu.yu.einstein.wasp.model.AcctInvoice;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface AcctInvoiceService extends WaspService<AcctInvoice> {

  public void setAcctInvoiceDao(AcctInvoiceDao acctInvoiceDao);
  public AcctInvoiceDao getAcctInvoiceDao();

  public AcctInvoice getAcctInvoiceByInvoiceId (final int invoiceId);

}

