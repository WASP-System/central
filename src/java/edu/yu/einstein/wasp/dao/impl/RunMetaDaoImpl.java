
/**
 *
 * RunMetaImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the RunMeta object
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

import edu.yu.einstein.wasp.model.RunMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class RunMetaDaoImpl extends WaspDaoImpl<RunMeta> implements edu.yu.einstein.wasp.dao.RunMetaDao {

  public RunMetaDaoImpl() {
    super();
    this.entityClass = RunMeta.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public RunMeta getRunMetaByRunMetaId (final int runMetaId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM RunMeta a WHERE "
       + "a.runMetaId = :runMetaId";
     Query query = em.createQuery(queryString);
      query.setParameter("runMetaId", runMetaId);

    return query.getResultList();
  }
  });
    List<RunMeta> results = (List<RunMeta>) res;
    if (results.size() == 0) {
      RunMeta rt = new RunMeta();
      return rt;
    }
    return (RunMeta) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public RunMeta getRunMetaByKRunId (final String k, final int runId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM RunMeta a WHERE "
       + "a.k = :k"
       + " AND "+ "a.runId = :runId";
     Query query = em.createQuery(queryString);
      query.setParameter("k", k);
      query.setParameter("runId", runId);

    return query.getResultList();
  }
  });
    List<RunMeta> results = (List<RunMeta>) res;
    if (results.size() == 0) {
      RunMeta rt = new RunMeta();
      return rt;
    }
    return (RunMeta) results.get(0);
  }



  @SuppressWarnings("unchecked")
  @Transactional
  public void updateByRunId (final int runId, final List<RunMeta> metaList) {

    getJpaTemplate().execute(new JpaCallback() {

      public Object doInJpa(EntityManager em) throws PersistenceException {
        em.createNativeQuery("delete from runMeta where runId=:runId").setParameter("runId", runId).executeUpdate();

        for (RunMeta m:metaList) {
          em.persist(m);
        }

        return null;
      }
    });

  }
}

