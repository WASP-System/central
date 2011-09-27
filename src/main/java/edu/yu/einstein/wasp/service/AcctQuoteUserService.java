
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

import edu.yu.einstein.wasp.dao.AcctQuoteUserDao;
import edu.yu.einstein.wasp.model.AcctQuoteUser;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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

