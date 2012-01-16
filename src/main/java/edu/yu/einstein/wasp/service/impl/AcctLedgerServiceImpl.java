
/**
 *
 * AcctLedgerServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctLedgerService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AcctLedgerDao;
import edu.yu.einstein.wasp.model.AcctLedger;
import edu.yu.einstein.wasp.service.AcctLedgerService;

@Service
public class AcctLedgerServiceImpl extends WaspServiceImpl<AcctLedger> implements AcctLedgerService {

	/**
	 * acctLedgerDao;
	 *
	 */
	private AcctLedgerDao acctLedgerDao;

	/**
	 * setAcctLedgerDao(AcctLedgerDao acctLedgerDao)
	 *
	 * @param acctLedgerDao
	 *
	 */
	@Autowired
	public void setAcctLedgerDao(AcctLedgerDao acctLedgerDao) {
		this.acctLedgerDao = acctLedgerDao;
		this.setWaspDao(acctLedgerDao);
	}

	/**
	 * getAcctLedgerDao();
	 *
	 * @return acctLedgerDao
	 *
	 */
	public AcctLedgerDao getAcctLedgerDao() {
		return this.acctLedgerDao;
	}


  public AcctLedger getAcctLedgerByLedgerId (final int ledgerId) {
    return this.getAcctLedgerDao().getAcctLedgerByLedgerId(ledgerId);
  }

}

