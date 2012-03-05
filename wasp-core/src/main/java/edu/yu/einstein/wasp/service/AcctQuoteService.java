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

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AcctQuoteDao;
import edu.yu.einstein.wasp.model.AcctQuote;

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

	public AcctQuote getAcctQuoteByQuoteId(final int quoteId);

	List<AcctQuote> getJobQuotesByJobId(Integer jobId);

	List<AcctQuote> getJobQuotesByUserId(Integer userId);

}
