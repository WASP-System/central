
/**
 *
 * StaterunImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Staterun object
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

import edu.yu.einstein.wasp.model.Staterun;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class StaterunDaoImpl extends WaspDaoImpl<Staterun> implements edu.yu.einstein.wasp.dao.StaterunDao {

  public StaterunDaoImpl() {
    super();
    this.entityClass = Staterun.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Staterun getStaterunByStaterunId (final int staterunId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Staterun a WHERE "
       + "a.staterunId = :staterunId";
     Query query = em.createQuery(queryString);
      query.setParameter("staterunId", staterunId);

    return query.getResultList();
  }
  });
    List<Staterun> results = (List<Staterun>) res;
    if (results.size() == 0) {
      Staterun rt = new Staterun();
      return rt;
    }
    return (Staterun) results.get(0);
  }


}

