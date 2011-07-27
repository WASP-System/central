
/**
 *
 * RunImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Run object
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

import edu.yu.einstein.wasp.model.Run;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class RunDaoImpl extends WaspDaoImpl<Run> implements edu.yu.einstein.wasp.dao.RunDao {

  public RunDaoImpl() {
    super();
    this.entityClass = Run.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Run getRunByRunId (final int runId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Run a WHERE "
       + "a.runId = :runId";
     Query query = em.createQuery(queryString);
      query.setParameter("runId", runId);

    return query.getResultList();
  }
  });
    List<Run> results = (List<Run>) res;
    if (results.size() == 0) {
      Run rt = new Run();
      return rt;
    }
    return (Run) results.get(0);
  }


}

