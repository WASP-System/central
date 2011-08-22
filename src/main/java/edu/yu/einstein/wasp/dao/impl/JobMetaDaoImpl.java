
/**
 *
 * JobMetaImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobMeta object
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

import edu.yu.einstein.wasp.model.JobMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class JobMetaDaoImpl extends WaspDaoImpl<JobMeta> implements edu.yu.einstein.wasp.dao.JobMetaDao {

  public JobMetaDaoImpl() {
    super();
    this.entityClass = JobMeta.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public JobMeta getJobMetaByJobMetaId (final int jobMetaId) {
    HashMap m = new HashMap();
    m.put("jobMetaId", jobMetaId);
    List<JobMeta> results = (List<JobMeta>) this.findByMap((Map) m);
    if (results.size() == 0) {
      JobMeta rt = new JobMeta();
      return rt;
    }
    return (JobMeta) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public JobMeta getJobMetaByKJobId (final String k, final int jobId) {
    HashMap m = new HashMap();
    m.put("k", k);
    m.put("jobId", jobId);
    List<JobMeta> results = (List<JobMeta>) this.findByMap((Map) m);
    if (results.size() == 0) {
      JobMeta rt = new JobMeta();
      return rt;
    }
    return (JobMeta) results.get(0);
  }



  @SuppressWarnings("unchecked")
  @Transactional
  public void updateByJobId (final int jobId, final List<JobMeta> metaList) {

    getJpaTemplate().execute(new JpaCallback() {

      public Object doInJpa(EntityManager em) throws PersistenceException {
        em.createNativeQuery("delete from jobMeta where jobId=:jobId").setParameter("jobId", jobId).executeUpdate();

        for (JobMeta m:metaList) {
          em.persist(m);
        }

        return null;
      }
    });

  }
}

