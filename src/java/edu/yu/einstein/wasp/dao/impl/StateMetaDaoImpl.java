
/**
 *
 * StateMetaImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the StateMeta object
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

import edu.yu.einstein.wasp.model.StateMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class StateMetaDaoImpl extends WaspDaoImpl<StateMeta> implements edu.yu.einstein.wasp.dao.StateMetaDao {

  public StateMetaDaoImpl() {
    super();
    this.entityClass = StateMeta.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public StateMeta getStateMetaByStateMetaId (final int stateMetaId) {
    HashMap m = new HashMap();
    m.put("stateMetaId", stateMetaId);
    List<StateMeta> results = (List<StateMeta>) this.findByMap((Map) m);
    if (results.size() == 0) {
      StateMeta rt = new StateMeta();
      return rt;
    }
    return (StateMeta) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public StateMeta getStateMetaByKStateId (final String k, final int stateId) {
    HashMap m = new HashMap();
    m.put("k", k);
    m.put("stateId", stateId);
    List<StateMeta> results = (List<StateMeta>) this.findByMap((Map) m);
    if (results.size() == 0) {
      StateMeta rt = new StateMeta();
      return rt;
    }
    return (StateMeta) results.get(0);
  }



  @SuppressWarnings("unchecked")
  @Transactional
  public void updateByStateId (final int stateId, final List<StateMeta> metaList) {

    getJpaTemplate().execute(new JpaCallback() {

      public Object doInJpa(EntityManager em) throws PersistenceException {
        em.createNativeQuery("delete from stateMeta where stateId=:stateId").setParameter("stateId", stateId).executeUpdate();

        for (StateMeta m:metaList) {
          em.persist(m);
        }

        return null;
      }
    });

  }
}

