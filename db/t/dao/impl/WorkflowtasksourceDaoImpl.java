
/**
 *
 * WorkflowtasksourceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowtasksource object
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

import edu.yu.einstein.wasp.model.Workflowtasksource;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class WorkflowtasksourceDaoImpl extends WaspDaoImpl<Workflowtasksource> implements edu.yu.einstein.wasp.dao.WorkflowtasksourceDao {

  public WorkflowtasksourceDaoImpl() {
    super();
    this.entityClass = Workflowtasksource.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Workflowtasksource getWorkflowtasksourceByWorkflowtasksourceId (final int workflowtasksourceId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Workflowtasksource a WHERE "
       + "a.workflowtasksourceId = :workflowtasksourceId";
     Query query = em.createQuery(queryString);
      query.setParameter("workflowtasksourceId", workflowtasksourceId);

    return query.getResultList();
  }
  });
    List<Workflowtasksource> results = (List<Workflowtasksource>) res;
    if (results.size() == 0) {
      Workflowtasksource rt = new Workflowtasksource();
      return rt;
    }
    return (Workflowtasksource) results.get(0);
  }


}

