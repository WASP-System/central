
/**
 *
 * AcctQuoteMetaServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuoteMetaService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.AcctQuoteMetaService;
import edu.yu.einstein.wasp.dao.AcctQuoteMetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.AcctQuoteMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AcctQuoteMetaServiceImpl extends WaspServiceImpl<AcctQuoteMeta> implements AcctQuoteMetaService {

	/**
	 * acctQuoteMetaDao;
	 *
	 */
	private AcctQuoteMetaDao acctQuoteMetaDao;

	/**
	 * setAcctQuoteMetaDao(AcctQuoteMetaDao acctQuoteMetaDao)
	 *
	 * @param acctQuoteMetaDao
	 *
	 */
	@Autowired
	public void setAcctQuoteMetaDao(AcctQuoteMetaDao acctQuoteMetaDao) {
		this.acctQuoteMetaDao = acctQuoteMetaDao;
		this.setWaspDao(acctQuoteMetaDao);
	}

	/**
	 * getAcctQuoteMetaDao();
	 *
	 * @return acctQuoteMetaDao
	 *
	 */
	public AcctQuoteMetaDao getAcctQuoteMetaDao() {
		return this.acctQuoteMetaDao;
	}


  public AcctQuoteMeta getAcctQuoteMetaByQuotemetaId (final Integer quotemetaId) {
    return this.getAcctQuoteMetaDao().getAcctQuoteMetaByQuotemetaId(quotemetaId);
  }

  public AcctQuoteMeta getAcctQuoteMetaByKQuoteId (final String k, final Integer quoteId) {
    return this.getAcctQuoteMetaDao().getAcctQuoteMetaByKQuoteId(k, quoteId);
  }

  public void updateByQuoteId (final String area, final int quoteId, final List<AcctQuoteMeta> metaList) {
    this.getAcctQuoteMetaDao().updateByQuoteId(area, quoteId, metaList); 
  }

  public void updateByQuoteId (final int quoteId, final List<AcctQuoteMeta> metaList) {
    this.getAcctQuoteMetaDao().updateByQuoteId(quoteId, metaList); 
  }


}

