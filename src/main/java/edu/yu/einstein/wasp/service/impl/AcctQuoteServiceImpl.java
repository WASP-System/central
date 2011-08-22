
/**
 *
 * AcctQuoteService.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuoteService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.AcctQuoteService;
import edu.yu.einstein.wasp.dao.AcctQuoteDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.AcctQuote;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AcctQuoteServiceImpl extends WaspServiceImpl<AcctQuote> implements AcctQuoteService {

  private AcctQuoteDao acctQuoteDao;
  @Autowired
  public void setAcctQuoteDao(AcctQuoteDao acctQuoteDao) {
    this.acctQuoteDao = acctQuoteDao;
    this.setWaspDao(acctQuoteDao);
  }
  public AcctQuoteDao getAcctQuoteDao() {
    return this.acctQuoteDao;
  }

  // **

  
  public AcctQuote getAcctQuoteByQuoteId (final int quoteId) {
    return this.getAcctQuoteDao().getAcctQuoteByQuoteId(quoteId);
  }
}

