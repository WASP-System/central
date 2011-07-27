
/**
 *
 * StaterunlaneImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Staterunlane object
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

import edu.yu.einstein.wasp.model.Staterunlane;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class StaterunlaneDaoImpl extends WaspDaoImpl<Staterunlane> implements edu.yu.einstein.wasp.dao.StaterunlaneDao {

  public StaterunlaneDaoImpl() {
    super();
    this.entityClass = Staterunlane.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Staterunlane getStaterunlaneByStaterunlaneId (final int staterunlaneId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Staterunlane a WHERE "
       + "a.staterunlaneId = :staterunlaneId";
     Query query = em.createQuery(queryString);
      query.setParameter("staterunlaneId", staterunlaneId);

    return query.getResultList();
  }
  });
    List<Staterunlane> results = (List<Staterunlane>) res;
    if (results.size() == 0) {
      Staterunlane rt = new Staterunlane();
      return rt;
    }
    return (Staterunlane) results.get(0);
  }


}

