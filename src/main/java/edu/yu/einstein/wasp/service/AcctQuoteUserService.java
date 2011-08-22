
/**
 *
 * AcctQuoteUserService.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuoteUserService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.AcctQuoteUserDao;
import edu.yu.einstein.wasp.model.AcctQuoteUser;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface AcctQuoteUserService extends WaspService<AcctQuoteUser> {

  public void setAcctQuoteUserDao(AcctQuoteUserDao acctQuoteUserDao);
  public AcctQuoteUserDao getAcctQuoteUserDao();

  public AcctQuoteUser getAcctQuoteUserByQuoteUserId (final int quoteUserId);

}

