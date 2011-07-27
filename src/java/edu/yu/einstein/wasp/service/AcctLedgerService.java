
/**
 *
 * AcctLedgerService.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctLedgerService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.AcctLedgerDao;
import edu.yu.einstein.wasp.model.AcctLedger;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface AcctLedgerService extends WaspService<AcctLedger> {

  public void setAcctLedgerDao(AcctLedgerDao acctLedgerDao);
  public AcctLedgerDao getAcctLedgerDao();

  public AcctLedger getAcctLedgerByLedgerId (final int ledgerId);

}

