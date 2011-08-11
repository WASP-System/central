
/**
 *
 * StatemetaImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Statemeta object
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

import edu.yu.einstein.wasp.model.Statemeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class StatemetaDaoImpl extends WaspDaoImpl<Statemeta> implements edu.yu.einstein.wasp.dao.StatemetaDao {

  public StatemetaDaoImpl() {
    super();
    this.entityClass = Statemeta.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Statemeta getStatemetaByStatemetaId (final int statemetaId) {
    HashMap m = new HashMap();
    m.put("statemetaId", statemetaId);
    List<Statemeta> results = (List<Statemeta>) this.findByMap((Map) m);
    if (results.size() == 0) {
      Statemeta rt = new Statemeta();
      return rt;
    }
    return (Statemeta) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Statemeta getStatemetaByKStateId (final String k, final int stateId) {
    HashMap m = new HashMap();
    m.put("k", k);
    m.put("stateId", stateId);
    List<Statemeta> results = (List<Statemeta>) this.findByMap((Map) m);
    if (results.size() == 0) {
      Statemeta rt = new Statemeta();
      return rt;
    }
    return (Statemeta) results.get(0);
  }



  @SuppressWarnings("unchecked")
  @Transactional
  public void updateByStateId (final int stateId, final List<Statemeta> metaList) {

    getJpaTemplate().execute(new JpaCallback() {

      public Object doInJpa(EntityManager em) throws PersistenceException {
        em.createNativeQuery("delete from statemeta where stateId=:stateId").setParameter("stateId", stateId).executeUpdate();

        for (Statemeta m:metaList) {
          em.persist(m);
        }

        return null;
      }
    });

  }
}

