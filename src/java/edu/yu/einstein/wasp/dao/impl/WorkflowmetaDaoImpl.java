
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
import java.util.HashMap;
import java.util.Map;

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
    HashMap m = new HashMap();
    m.put("workflowmetaId", workflowmetaId);
    List<Workflowmeta> results = (List<Workflowmeta>) this.findByMap((Map) m);
    if (results.size() == 0) {
      Workflowmeta rt = new Workflowmeta();
      return rt;
    }
    return (Workflowmeta) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Workflowmeta getWorkflowmetaByKWorkflowId (final String k, final int workflowId) {
    HashMap m = new HashMap();
    m.put("k", k);
    m.put("workflowId", workflowId);
    List<Workflowmeta> results = (List<Workflowmeta>) this.findByMap((Map) m);
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

