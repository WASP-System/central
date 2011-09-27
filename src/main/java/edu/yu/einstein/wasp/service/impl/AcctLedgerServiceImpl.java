
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

import edu.yu.einstein.wasp.service.AcctLedgerService;
import edu.yu.einstein.wasp.dao.AcctLedgerDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.AcctLedger;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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

