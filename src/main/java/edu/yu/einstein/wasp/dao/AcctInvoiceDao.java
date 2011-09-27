
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

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface AcctInvoiceDao extends WaspDao<AcctInvoice> {

  public AcctInvoice getAcctInvoiceByInvoiceId (final int invoiceId);


}

