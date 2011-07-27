
/**
 *
 * WorkflowtaskImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowtask object
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

import edu.yu.einstein.wasp.model.Workflowtask;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class WorkflowtaskDaoImpl extends WaspDaoImpl<Workflowtask> implements edu.yu.einstein.wasp.dao.WorkflowtaskDao {

  public WorkflowtaskDaoImpl() {
    super();
    this.entityClass = Workflowtask.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Workflowtask getWorkflowtaskByWorkflowtaskId (final int workflowtaskId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Workflowtask a WHERE "
       + "a.workflowtaskId = :workflowtaskId";
     Query query = em.createQuery(queryString);
      query.setParameter("workflowtaskId", workflowtaskId);

    return query.getResultList();
  }
  });
    List<Workflowtask> results = (List<Workflowtask>) res;
    if (results.size() == 0) {
      Workflowtask rt = new Workflowtask();
      return rt;
    }
    return (Workflowtask) results.get(0);
  }


}

