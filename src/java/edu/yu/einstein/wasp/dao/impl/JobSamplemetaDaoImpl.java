
/**
 *
 * JobSamplemetaImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSamplemeta object
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

import edu.yu.einstein.wasp.model.JobSamplemeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class JobSamplemetaDaoImpl extends WaspDaoImpl<JobSamplemeta> implements edu.yu.einstein.wasp.dao.JobSamplemetaDao {

  public JobSamplemetaDaoImpl() {
    super();
    this.entityClass = JobSamplemeta.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public JobSamplemeta getJobSamplemetaByJobSamplemetaId (final int jobSamplemetaId) {
    HashMap m = new HashMap();
    m.put("jobSamplemetaId", jobSamplemetaId);
    List<JobSamplemeta> results = (List<JobSamplemeta>) this.findByMap((Map) m);
    if (results.size() == 0) {
      JobSamplemeta rt = new JobSamplemeta();
      return rt;
    }
    return (JobSamplemeta) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public JobSamplemeta getJobSamplemetaByKJobsampleId (final String k, final int jobsampleId) {
    HashMap m = new HashMap();
    m.put("k", k);
    m.put("jobsampleId", jobsampleId);
    List<JobSamplemeta> results = (List<JobSamplemeta>) this.findByMap((Map) m);
    if (results.size() == 0) {
      JobSamplemeta rt = new JobSamplemeta();
      return rt;
    }
    return (JobSamplemeta) results.get(0);
  }



  @SuppressWarnings("unchecked")
  @Transactional
  public void updateByJobsampleId (final int jobsampleId, final List<JobSamplemeta> metaList) {

    getJpaTemplate().execute(new JpaCallback() {

      public Object doInJpa(EntityManager em) throws PersistenceException {
        em.createNativeQuery("delete from jobSamplemeta where jobsampleId=:jobsampleId").setParameter("jobsampleId", jobsampleId).executeUpdate();

        for (JobSamplemeta m:metaList) {
          em.persist(m);
        }

        return null;
      }
    });

  }
}

