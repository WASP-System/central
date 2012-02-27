
/**
 *
 * AcctQuoteMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuoteMetaService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.AcctQuoteMetaDao;
import edu.yu.einstein.wasp.model.AcctQuoteMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface AcctQuoteMetaService extends WaspService<AcctQuoteMeta> {

	/**
	 * setAcctQuoteMetaDao(AcctQuoteMetaDao acctQuoteMetaDao)
	 *
	 * @param acctQuoteMetaDao
	 *
	 */
	public void setAcctQuoteMetaDao(AcctQuoteMetaDao acctQuoteMetaDao);

	/**
	 * getAcctQuoteMetaDao();
	 *
	 * @return acctQuoteMetaDao
	 *
	 */
	public AcctQuoteMetaDao getAcctQuoteMetaDao();

  public AcctQuoteMeta getAcctQuoteMetaByQuotemetaId (final Integer quotemetaId);

  public AcctQuoteMeta getAcctQuoteMetaByKQuoteId (final String k, final Integer quoteId);


  public void updateByQuoteId (final String area, final int quoteId, final List<AcctQuoteMeta> metaList);

  public void updateByQuoteId (final int quoteId, final List<AcctQuoteMeta> metaList);


}

