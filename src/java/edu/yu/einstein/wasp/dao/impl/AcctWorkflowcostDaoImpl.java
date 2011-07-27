
/**
 *
 * AcctWorkflowcostImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctWorkflowcost object
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

import edu.yu.einstein.wasp.model.AcctWorkflowcost;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class AcctWorkflowcostDaoImpl extends WaspDaoImpl<AcctWorkflowcost> implements edu.yu.einstein.wasp.dao.AcctWorkflowcostDao {

  public AcctWorkflowcostDaoImpl() {
    super();
    this.entityClass = AcctWorkflowcost.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public AcctWorkflowcost getAcctWorkflowcostByWorkflowId (final int workflowId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM AcctWorkflowcost a WHERE "
       + "a.workflowId = :workflowId";
     Query query = em.createQuery(queryString);
      query.setParameter("workflowId", workflowId);

    return query.getResultList();
  }
  });
    List<AcctWorkflowcost> results = (List<AcctWorkflowcost>) res;
    if (results.size() == 0) {
      AcctWorkflowcost rt = new AcctWorkflowcost();
      return rt;
    }
    return (AcctWorkflowcost) results.get(0);
  }


}

