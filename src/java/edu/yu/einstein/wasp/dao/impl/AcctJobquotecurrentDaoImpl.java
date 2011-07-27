
/**
 *
 * AcctJobquotecurrentImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctJobquotecurrent object
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

import edu.yu.einstein.wasp.model.AcctJobquotecurrent;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class AcctJobquotecurrentDaoImpl extends WaspDaoImpl<AcctJobquotecurrent> implements edu.yu.einstein.wasp.dao.AcctJobquotecurrentDao {

  public AcctJobquotecurrentDaoImpl() {
    super();
    this.entityClass = AcctJobquotecurrent.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public AcctJobquotecurrent getAcctJobquotecurrentByJobId (final int jobId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM AcctJobquotecurrent a WHERE "
       + "a.jobId = :jobId";
     Query query = em.createQuery(queryString);
      query.setParameter("jobId", jobId);

    return query.getResultList();
  }
  });
    List<AcctJobquotecurrent> results = (List<AcctJobquotecurrent>) res;
    if (results.size() == 0) {
      AcctJobquotecurrent rt = new AcctJobquotecurrent();
      return rt;
    }
    return (AcctJobquotecurrent) results.get(0);
  }


}

