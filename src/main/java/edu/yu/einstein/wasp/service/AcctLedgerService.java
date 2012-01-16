
/**
 *
 * AcctLedgerService.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctLedgerService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AcctLedgerDao;
import edu.yu.einstein.wasp.model.AcctLedger;

@Service
public interface AcctLedgerService extends WaspService<AcctLedger> {

	/**
	 * setAcctLedgerDao(AcctLedgerDao acctLedgerDao)
	 *
	 * @param acctLedgerDao
	 *
	 */
	public void setAcctLedgerDao(AcctLedgerDao acctLedgerDao);

	/**
	 * getAcctLedgerDao();
	 *
	 * @return acctLedgerDao
	 *
	 */
	public AcctLedgerDao getAcctLedgerDao();

  public AcctLedger getAcctLedgerByLedgerId (final int ledgerId);


}

