
/**
 *
 * AcctQuoteService.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuoteService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.AcctQuoteDao;
import edu.yu.einstein.wasp.model.AcctQuote;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface AcctQuoteService extends WaspService<AcctQuote> {

  public void setAcctQuoteDao(AcctQuoteDao acctQuoteDao);
  public AcctQuoteDao getAcctQuoteDao();

  public AcctQuote getAcctQuoteByQuoteId (final int quoteId);

}

