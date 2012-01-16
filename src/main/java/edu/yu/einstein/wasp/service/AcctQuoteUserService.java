
/**
 *
 * AcctQuoteUserService.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuoteUserService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AcctQuoteUserDao;
import edu.yu.einstein.wasp.model.AcctQuoteUser;

@Service
public interface AcctQuoteUserService extends WaspService<AcctQuoteUser> {

	/**
	 * setAcctQuoteUserDao(AcctQuoteUserDao acctQuoteUserDao)
	 *
	 * @param acctQuoteUserDao
	 *
	 */
	public void setAcctQuoteUserDao(AcctQuoteUserDao acctQuoteUserDao);

	/**
	 * getAcctQuoteUserDao();
	 *
	 * @return acctQuoteUserDao
	 *
	 */
	public AcctQuoteUserDao getAcctQuoteUserDao();

  public AcctQuoteUser getAcctQuoteUserByQuoteUserId (final int quoteUserId);


}

