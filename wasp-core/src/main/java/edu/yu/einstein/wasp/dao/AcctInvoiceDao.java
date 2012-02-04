
/**
 *
 * AcctInvoiceDao.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctInvoice Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.AcctInvoice;


public interface AcctInvoiceDao extends WaspDao<AcctInvoice> {

  public AcctInvoice getAcctInvoiceByInvoiceId (final int invoiceId);


}

