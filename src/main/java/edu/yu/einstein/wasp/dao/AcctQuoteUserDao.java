
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

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface AcctQuoteUserDao extends WaspDao<AcctQuoteUser> {

  public AcctQuoteUser getAcctQuoteUserByQuoteUserId (final int quoteUserId);


}

