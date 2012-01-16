
/**
 *
 * AcctQuoteUserDao.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuoteUser Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.AcctQuoteUser;


public interface AcctQuoteUserDao extends WaspDao<AcctQuoteUser> {

  public AcctQuoteUser getAcctQuoteUserByQuoteUserId (final int quoteUserId);


}

