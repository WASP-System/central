
/**
 *
 * SampleDraftMetaImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleDraftMeta object
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

import edu.yu.einstein.wasp.model.SampleDraftMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleDraftMetaDaoImpl extends WaspDaoImpl<SampleDraftMeta> implements edu.yu.einstein.wasp.dao.SampleDraftMetaDao {

  public SampleDraftMetaDaoImpl() {
    super();
    this.entityClass = SampleDraftMeta.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public SampleDraftMeta getSampleDraftMetaBySampleDraftMetaId (final int sampleDraftMetaId) {
    HashMap m = new HashMap();
    m.put("sampleDraftMetaId", sampleDraftMetaId);
    List<SampleDraftMeta> results = (List<SampleDraftMeta>) this.findByMap((Map) m);
    if (results.size() == 0) {
      SampleDraftMeta rt = new SampleDraftMeta();
      return rt;
    }
    return (SampleDraftMeta) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public SampleDraftMeta getSampleDraftMetaByKSampledraftId (final String k, final int sampledraftId) {
    HashMap m = new HashMap();
    m.put("k", k);
    m.put("sampledraftId", sampledraftId);
    List<SampleDraftMeta> results = (List<SampleDraftMeta>) this.findByMap((Map) m);
    if (results.size() == 0) {
      SampleDraftMeta rt = new SampleDraftMeta();
      return rt;
    }
    return (SampleDraftMeta) results.get(0);
  }



  @SuppressWarnings("unchecked")
  @Transactional
  public void updateBySampledraftId (final int sampledraftId, final List<SampleDraftMeta> metaList) {

    getJpaTemplate().execute(new JpaCallback() {

      public Object doInJpa(EntityManager em) throws PersistenceException {
        em.createNativeQuery("delete from sampledraftmeta where sampledraftId=:sampledraftId").setParameter("sampledraftId", sampledraftId).executeUpdate();

        for (SampleDraftMeta m:metaList) {
          em.persist(m);
        }

        return null;
      }
    });
  }

  
}

