
/**
 *
 * RunLaneImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the RunLane object
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

import edu.yu.einstein.wasp.model.RunLane;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class RunLaneDaoImpl extends WaspDaoImpl<RunLane> implements edu.yu.einstein.wasp.dao.RunLaneDao {

  public RunLaneDaoImpl() {
    super();
    this.entityClass = RunLane.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public RunLane getRunLaneByRunLaneId (final int runLaneId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM RunLane a WHERE "
       + "a.runLaneId = :runLaneId";
     Query query = em.createQuery(queryString);
      query.setParameter("runLaneId", runLaneId);

    return query.getResultList();
  }
  });
    List<RunLane> results = (List<RunLane>) res;
    if (results.size() == 0) {
      RunLane rt = new RunLane();
      return rt;
    }
    return (RunLane) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public RunLane getRunLaneByRunIdResourcelaneId (final int runId, final int resourcelaneId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM RunLane a WHERE "
       + "a.runId = :runId"
       + "AND "+ "a.resourcelaneId = :resourcelaneId";
     Query query = em.createQuery(queryString);
      query.setParameter("runId", runId);
      query.setParameter("resourcelaneId", resourcelaneId);

    return query.getResultList();
  }
  });
    List<RunLane> results = (List<RunLane>) res;
    if (results.size() == 0) {
      RunLane rt = new RunLane();
      return rt;
    }
    return (RunLane) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public RunLane getRunLaneBySampleIdRunId (final int sampleId, final int runId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM RunLane a WHERE "
       + "a.sampleId = :sampleId"
       + "AND "+ "a.runId = :runId";
     Query query = em.createQuery(queryString);
      query.setParameter("sampleId", sampleId);
      query.setParameter("runId", runId);

    return query.getResultList();
  }
  });
    List<RunLane> results = (List<RunLane>) res;
    if (results.size() == 0) {
      RunLane rt = new RunLane();
      return rt;
    }
    return (RunLane) results.get(0);
  }


}

