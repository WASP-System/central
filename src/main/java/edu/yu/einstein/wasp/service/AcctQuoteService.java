
/**
 *
 * AcctQuoteService.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuoteService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.AcctQuoteDao;
import edu.yu.einstein.wasp.model.AcctQuote;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface AcctQuoteService extends WaspService<AcctQuote> {

	/**
	 * setAcctQuoteDao(AcctQuoteDao acctQuoteDao)
	 *
	 * @param acctQuoteDao
	 *
	 */
	public void setAcctQuoteDao(AcctQuoteDao acctQuoteDao);

	/**
	 * getAcctQuoteDao();
	 *
	 * @return acctQuoteDao
	 *
	 */
	public AcctQuoteDao getAcctQuoteDao();

  public AcctQuote getAcctQuoteByQuoteId (final int quoteId);


}

