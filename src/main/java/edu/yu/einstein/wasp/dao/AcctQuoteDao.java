
/**
 *
 * AcctQuoteDao.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuote Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.AcctQuote;


public interface AcctQuoteDao extends WaspDao<AcctQuote> {

  public AcctQuote getAcctQuoteByQuoteId (final int quoteId);


}

