
/**
 *
 * AcctQuoteServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuoteService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AcctQuoteDao;
import edu.yu.einstein.wasp.model.AcctQuote;
import edu.yu.einstein.wasp.service.AcctQuoteService;

@Service
public class AcctQuoteServiceImpl extends WaspServiceImpl<AcctQuote> implements AcctQuoteService {

	/**
	 * acctQuoteDao;
	 *
	 */
	private AcctQuoteDao acctQuoteDao;

	/**
	 * setAcctQuoteDao(AcctQuoteDao acctQuoteDao)
	 *
	 * @param acctQuoteDao
	 *
	 */
	@Autowired
	public void setAcctQuoteDao(AcctQuoteDao acctQuoteDao) {
		this.acctQuoteDao = acctQuoteDao;
		this.setWaspDao(acctQuoteDao);
	}

	/**
	 * getAcctQuoteDao();
	 *
	 * @return acctQuoteDao
	 *
	 */
	public AcctQuoteDao getAcctQuoteDao() {
		return this.acctQuoteDao;
	}


  public AcctQuote getAcctQuoteByQuoteId (final int quoteId) {
    return this.getAcctQuoteDao().getAcctQuoteByQuoteId(quoteId);
  }

}

