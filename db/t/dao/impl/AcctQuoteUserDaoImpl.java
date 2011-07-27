
/**
 *
 * AcctQuoteUserImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuoteUser object
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.AcctQuoteUser;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class AcctQuoteUserDaoImpl extends WaspDaoImpl<AcctQuoteUser> implements edu.yu.einstein.wasp.dao.AcctQuoteUserDao {

  public AcctQuoteUserDaoImpl() {
    super();
    this.entityClass = AcctQuoteUser.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public AcctQuoteUser getAcctQuoteUserByQuoteUserId (final int quoteUserId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM AcctQuoteUser a WHERE "
       + "a.quoteUserId = :quoteUserId";
     Query query = em.createQuery(queryString);
      query.setParameter("quoteUserId", quoteUserId);

    return query.getResultList();
  }
  });
    List<AcctQuoteUser> results = (List<AcctQuoteUser>) res;
    if (results.size() == 0) {
      AcctQuoteUser rt = new AcctQuoteUser();
      return rt;
    }
    return (AcctQuoteUser) results.get(0);
  }


}

