
/**
 *
 * LabmetaImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Labmeta object
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

import edu.yu.einstein.wasp.model.Labmeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class LabmetaDaoImpl extends WaspDaoImpl<Labmeta> implements edu.yu.einstein.wasp.dao.LabmetaDao {

  public LabmetaDaoImpl() {
    super();
    this.entityClass = Labmeta.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Labmeta getLabmetaByLabmetaId (final int labmetaId) {
    HashMap m = new HashMap();
    m.put("labmetaId", labmetaId);
    List<Labmeta> results = (List<Labmeta>) this.findByMap((Map) m);
    if (results.size() == 0) {
      Labmeta rt = new Labmeta();
      return rt;
    }
    return (Labmeta) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Labmeta getLabmetaByKLabId (final String k, final int labId) {
    HashMap m = new HashMap();
    m.put("k", k);
    m.put("labId", labId);
    List<Labmeta> results = (List<Labmeta>) this.findByMap((Map) m);
    if (results.size() == 0) {
      Labmeta rt = new Labmeta();
      return rt;
    }
    return (Labmeta) results.get(0);
  }



  @SuppressWarnings("unchecked")
  @Transactional
  public void updateByLabId (final int labId, final List<Labmeta> metaList) {

    getJpaTemplate().execute(new JpaCallback() {

      public Object doInJpa(EntityManager em) throws PersistenceException {
        em.createNativeQuery("delete from labmeta where labId=:labId").setParameter("labId", labId).executeUpdate();

        for (Labmeta m:metaList) {
          em.persist(m);
        }

        return null;
      }
    });

  }
}

