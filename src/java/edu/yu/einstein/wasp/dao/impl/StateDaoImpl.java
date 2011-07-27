
/**
 *
 * StateImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the State object
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

import edu.yu.einstein.wasp.model.State;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class StateDaoImpl extends WaspDaoImpl<State> implements edu.yu.einstein.wasp.dao.StateDao {

  public StateDaoImpl() {
    super();
    this.entityClass = State.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public State getStateByStateId (final int stateId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM State a WHERE "
       + "a.stateId = :stateId";
     Query query = em.createQuery(queryString);
      query.setParameter("stateId", stateId);

    return query.getResultList();
  }
  });
    List<State> results = (List<State>) res;
    if (results.size() == 0) {
      State rt = new State();
      return rt;
    }
    return (State) results.get(0);
  }


}

