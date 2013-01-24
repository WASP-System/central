
/**
 *
 * AcctQuoteMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuoteMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.AcctQuoteMeta;


public interface AcctQuoteMetaDao extends WaspMetaDao<AcctQuoteMeta> {

  public AcctQuoteMeta getAcctQuoteMetaByQuotemetaId (final Integer quotemetaId);

  public AcctQuoteMeta getAcctQuoteMetaByKQuoteId (final String k, final Integer quoteId);





}

