
/**
 *
 * LabMetaImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the LabMeta object
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

import edu.yu.einstein.wasp.model.LabMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class LabMetaDaoImpl extends WaspDaoImpl<LabMeta> implements edu.yu.einstein.wasp.dao.LabMetaDao {

  public LabMetaDaoImpl() {
    super();
    this.entityClass = LabMeta.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public LabMeta getLabMetaByLabMetaId (final int labMetaId) {
    HashMap m = new HashMap();
    m.put("labMetaId", labMetaId);
    List<LabMeta> results = (List<LabMeta>) this.findByMap((Map) m);
    if (results.size() == 0) {
      LabMeta rt = new LabMeta();
      return rt;
    }
    return (LabMeta) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public LabMeta getLabMetaByKLabId (final String k, final int labId) {
    HashMap m = new HashMap();
    m.put("k", k);
    m.put("labId", labId);
    List<LabMeta> results = (List<LabMeta>) this.findByMap((Map) m);
    if (results.size() == 0) {
      LabMeta rt = new LabMeta();
      return rt;
    }
    return (LabMeta) results.get(0);
  }



  @SuppressWarnings("unchecked")
  @Transactional
  public void updateByLabId (final int labId, final List<LabMeta> metaList) {

    getJpaTemplate().execute(new JpaCallback() {

      public Object doInJpa(EntityManager em) throws PersistenceException {
        em.createNativeQuery("delete from labMeta where labId=:labId").setParameter("labId", labId).executeUpdate();

        for (LabMeta m:metaList) {
          em.persist(m);
        }

        return null;
      }
    });

  }
}

