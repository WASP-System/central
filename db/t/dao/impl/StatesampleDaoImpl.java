
/**
 *
 * StatesampleImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Statesample object
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

import edu.yu.einstein.wasp.model.Statesample;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class StatesampleDaoImpl extends WaspDaoImpl<Statesample> implements edu.yu.einstein.wasp.dao.StatesampleDao {

  public StatesampleDaoImpl() {
    super();
    this.entityClass = Statesample.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Statesample getStatesampleByStatesampleId (final int statesampleId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Statesample a WHERE "
       + "a.statesampleId = :statesampleId";
     Query query = em.createQuery(queryString);
      query.setParameter("statesampleId", statesampleId);

    return query.getResultList();
  }
  });
    List<Statesample> results = (List<Statesample>) res;
    if (results.size() == 0) {
      Statesample rt = new Statesample();
      return rt;
    }
    return (Statesample) results.get(0);
  }


}

