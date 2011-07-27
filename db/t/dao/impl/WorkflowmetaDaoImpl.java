
/**
 *
 * WorkflowmetaImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowmeta object
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

import edu.yu.einstein.wasp.model.Workflowmeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class WorkflowmetaDaoImpl extends WaspDaoImpl<Workflowmeta> implements edu.yu.einstein.wasp.dao.WorkflowmetaDao {

  public WorkflowmetaDaoImpl() {
    super();
    this.entityClass = Workflowmeta.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Workflowmeta getWorkflowmetaByWorkflowmetaId (final int workflowmetaId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Workflowmeta a WHERE "
       + "a.workflowmetaId = :workflowmetaId";
     Query query = em.createQuery(queryString);
      query.setParameter("workflowmetaId", workflowmetaId);

    return query.getResultList();
  }
  });
    List<Workflowmeta> results = (List<Workflowmeta>) res;
    if (results.size() == 0) {
      Workflowmeta rt = new Workflowmeta();
      return rt;
    }
    return (Workflowmeta) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Workflowmeta getWorkflowmetaByKWorkflowId (final String k, final int workflowId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Workflowmeta a WHERE "
       + "a.k = :k"
       + "AND "+ "a.workflowId = :workflowId";
     Query query = em.createQuery(queryString);
      query.setParameter("k", k);
      query.setParameter("workflowId", workflowId);

    return query.getResultList();
  }
  });
    List<Workflowmeta> results = (List<Workflowmeta>) res;
    if (results.size() == 0) {
      Workflowmeta rt = new Workflowmeta();
      return rt;
    }
    return (Workflowmeta) results.get(0);
  }



  @SuppressWarnings("unchecked")
  @Transactional
  public void updateByWorkflowId (final int workflowId, final List<Workflowmeta> metaList) {

    getJpaTemplate().execute(new JpaCallback() {

      public Object doInJpa(EntityManager em) throws PersistenceException {
        em.createNativeQuery("delete from workflowmeta where workflowId=:workflowId").setParameter("workflowId", workflowId).executeUpdate();

        for (Workflowmeta m:metaList) {
          em.persist(m);
        }

        return null;
      }
    });

  }
}

