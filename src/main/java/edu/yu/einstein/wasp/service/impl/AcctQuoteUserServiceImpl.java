
/**
 *
 * AcctQuoteUserServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuoteUserService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.AcctQuoteUserDao;
import edu.yu.einstein.wasp.model.AcctQuoteUser;
import edu.yu.einstein.wasp.service.AcctQuoteUserService;

@Service
public class AcctQuoteUserServiceImpl extends WaspServiceImpl<AcctQuoteUser> implements AcctQuoteUserService {

	/**
	 * acctQuoteUserDao;
	 *
	 */
	private AcctQuoteUserDao acctQuoteUserDao;

	/**
	 * setAcctQuoteUserDao(AcctQuoteUserDao acctQuoteUserDao)
	 *
	 * @param acctQuoteUserDao
	 *
	 */
	@Autowired
	public void setAcctQuoteUserDao(AcctQuoteUserDao acctQuoteUserDao) {
		this.acctQuoteUserDao = acctQuoteUserDao;
		this.setWaspDao(acctQuoteUserDao);
	}

	/**
	 * getAcctQuoteUserDao();
	 *
	 * @return acctQuoteUserDao
	 *
	 */
	public AcctQuoteUserDao getAcctQuoteUserDao() {
		return this.acctQuoteUserDao;
	}


  public AcctQuoteUser getAcctQuoteUserByQuoteUserId (final int quoteUserId) {
    return this.getAcctQuoteUserDao().getAcctQuoteUserByQuoteUserId(quoteUserId);
  }

}

