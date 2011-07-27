
/**
 *
 * AcctQuoteUserDao.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuoteUserDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface AcctQuoteUserDao extends WaspDao<AcctQuoteUser> {

  public AcctQuoteUser getAcctQuoteUserByQuoteUserId (final int quoteUserId);

}

