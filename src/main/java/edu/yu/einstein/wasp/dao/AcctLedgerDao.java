
/**
 *
 * AcctLedgerDao.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctLedger Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.AcctLedger;


public interface AcctLedgerDao extends WaspDao<AcctLedger> {

  public AcctLedger getAcctLedgerByLedgerId (final int ledgerId);


}

