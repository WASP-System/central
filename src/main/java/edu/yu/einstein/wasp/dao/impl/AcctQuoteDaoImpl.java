
/**
 *
 * AcctQuoteImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuote object
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.AcctQuote;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class AcctQuoteDaoImpl extends WaspDaoImpl<AcctQuote> implements edu.yu.einstein.wasp.dao.AcctQuoteDao {

  public AcctQuoteDaoImpl() {
    super();
    this.entityClass = AcctQuote.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public AcctQuote getAcctQuoteByQuoteId (final int quoteId) {
    HashMap m = new HashMap();
    m.put("quoteId", quoteId);
    List<AcctQuote> results = (List<AcctQuote>) this.findByMap((Map) m);
    if (results.size() == 0) {
      AcctQuote rt = new AcctQuote();
      return rt;
    }
    return (AcctQuote) results.get(0);
  }


}

