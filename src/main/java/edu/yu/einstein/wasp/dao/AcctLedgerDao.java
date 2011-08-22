
/**
 *
 * AcctLedgerDao.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctLedgerDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface AcctLedgerDao extends WaspDao<AcctLedger> {

  public AcctLedger getAcctLedgerByLedgerId (final int ledgerId);

}

