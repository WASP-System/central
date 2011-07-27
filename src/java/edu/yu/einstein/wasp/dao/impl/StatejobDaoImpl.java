
/**
 *
 * StatejobImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Statejob object
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

import edu.yu.einstein.wasp.model.Statejob;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class StatejobDaoImpl extends WaspDaoImpl<Statejob> implements edu.yu.einstein.wasp.dao.StatejobDao {

  public StatejobDaoImpl() {
    super();
    this.entityClass = Statejob.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Statejob getStatejobByStatejobId (final int statejobId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Statejob a WHERE "
       + "a.statejobId = :statejobId";
     Query query = em.createQuery(queryString);
      query.setParameter("statejobId", statejobId);

    return query.getResultList();
  }
  });
    List<Statejob> results = (List<Statejob>) res;
    if (results.size() == 0) {
      Statejob rt = new Statejob();
      return rt;
    }
    return (Statejob) results.get(0);
  }


}

