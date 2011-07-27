
/**
 *
 * AcctQuoteDao.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuoteDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface AcctQuoteDao extends WaspDao<AcctQuote> {

  public AcctQuote getAcctQuoteByQuoteId (final int quoteId);

}

