
/**
 *
 * WorkflowImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflow object
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

import edu.yu.einstein.wasp.model.Workflow;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class WorkflowDaoImpl extends WaspDaoImpl<Workflow> implements edu.yu.einstein.wasp.dao.WorkflowDao {

  public WorkflowDaoImpl() {
    super();
    this.entityClass = Workflow.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Workflow getWorkflowByWorkflowId (final int workflowId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Workflow a WHERE "
       + "a.workflowId = :workflowId";
     Query query = em.createQuery(queryString);
      query.setParameter("workflowId", workflowId);

    return query.getResultList();
  }
  });
    List<Workflow> results = (List<Workflow>) res;
    if (results.size() == 0) {
      Workflow rt = new Workflow();
      return rt;
    }
    return (Workflow) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Workflow getWorkflowByIName (final String iName) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Workflow a WHERE "
       + "a.iName = :iName";
     Query query = em.createQuery(queryString);
      query.setParameter("iName", iName);

    return query.getResultList();
  }
  });
    List<Workflow> results = (List<Workflow>) res;
    if (results.size() == 0) {
      Workflow rt = new Workflow();
      return rt;
    }
    return (Workflow) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Workflow getWorkflowByName (final String name) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Workflow a WHERE "
       + "a.name = :name";
     Query query = em.createQuery(queryString);
      query.setParameter("name", name);

    return query.getResultList();
  }
  });
    List<Workflow> results = (List<Workflow>) res;
    if (results.size() == 0) {
      Workflow rt = new Workflow();
      return rt;
    }
    return (Workflow) results.get(0);
  }


}

