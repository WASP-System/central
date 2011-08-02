
/**
 *
 * AChipseqArunargsImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the AChipseqArunargs object
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

import edu.yu.einstein.wasp.model.AChipseqArunargs;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class AChipseqArunargsDaoImpl extends WaspDaoImpl<AChipseqArunargs> implements edu.yu.einstein.wasp.dao.AChipseqArunargsDao {

  public AChipseqArunargsDaoImpl() {
    super();
    this.entityClass = AChipseqArunargs.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public AChipseqArunargs getAChipseqArunargsByArunargsId (final int arunargsId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM AChipseqArunargs a WHERE "
       + "a.arunargsId = :arunargsId";
     Query query = em.createQuery(queryString);
      query.setParameter("arunargsId", arunargsId);

    return query.getResultList();
  }
  });
    List<AChipseqArunargs> results = (List<AChipseqArunargs>) res;
    if (results.size() == 0) {
      AChipseqArunargs rt = new AChipseqArunargs();
      return rt;
    }
    return (AChipseqArunargs) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public AChipseqArunargs getAChipseqArunargsByArunIdArgc (final Integer arunId, final int argc) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM AChipseqArunargs a WHERE "
       + "a.arunId = :arunId"
       + " AND "+ "a.argc = :argc";
     Query query = em.createQuery(queryString);
      query.setParameter("arunId", arunId);
      query.setParameter("argc", argc);

    return query.getResultList();
  }
  });
    List<AChipseqArunargs> results = (List<AChipseqArunargs>) res;
    if (results.size() == 0) {
      AChipseqArunargs rt = new AChipseqArunargs();
      return rt;
    }
    return (AChipseqArunargs) results.get(0);
  }


}

