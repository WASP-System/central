
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
import java.util.HashMap;
import java.util.Map;

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
    HashMap m = new HashMap();
    m.put("runLaneId", runLaneId);
    List<RunLane> results = (List<RunLane>) this.findByMap((Map) m);
    if (results.size() == 0) {
      RunLane rt = new RunLane();
      return rt;
    }
    return (RunLane) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public RunLane getRunLaneByRunIdResourcelaneId (final int runId, final int resourcelaneId) {
    HashMap m = new HashMap();
    m.put("runId", runId);
    m.put("resourcelaneId", resourcelaneId);
    List<RunLane> results = (List<RunLane>) this.findByMap((Map) m);
    if (results.size() == 0) {
      RunLane rt = new RunLane();
      return rt;
    }
    return (RunLane) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public RunLane getRunLaneBySampleIdRunId (final int sampleId, final int runId) {
    HashMap m = new HashMap();
    m.put("sampleId", sampleId);
    m.put("runId", runId);
    List<RunLane> results = (List<RunLane>) this.findByMap((Map) m);
    if (results.size() == 0) {
      RunLane rt = new RunLane();
      return rt;
    }
    return (RunLane) results.get(0);
  }


}

