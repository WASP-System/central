
/**
 *
 * AcctQuoteUserService.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuoteUserService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.AcctQuoteUserService;
import edu.yu.einstein.wasp.dao.AcctQuoteUserDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.AcctQuoteUser;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AcctQuoteUserServiceImpl extends WaspServiceImpl<AcctQuoteUser> implements AcctQuoteUserService {

  private AcctQuoteUserDao acctQuoteUserDao;
  @Autowired
  public void setAcctQuoteUserDao(AcctQuoteUserDao acctQuoteUserDao) {
    this.acctQuoteUserDao = acctQuoteUserDao;
    this.setWaspDao(acctQuoteUserDao);
  }
  public AcctQuoteUserDao getAcctQuoteUserDao() {
    return this.acctQuoteUserDao;
  }

  // **

  
  public AcctQuoteUser getAcctQuoteUserByQuoteUserId (final int quoteUserId) {
    return this.getAcctQuoteUserDao().getAcctQuoteUserByQuoteUserId(quoteUserId);
  }
}

