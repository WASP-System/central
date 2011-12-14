
/**
 *
 * JobMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobMeta Dao Impl
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

	/**
	 * JobMetaDaoImpl() Constructor
	 *
	 *
	 */
	public JobMetaDaoImpl() {
		super();
		this.entityClass = JobMeta.class;
	}


	/**
	 * getJobMetaByJobMetaId(final int jobMetaId)
	 *
	 * @param final int jobMetaId
	 *
	 * @return jobMeta
	 */

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



	/**
	 * getJobMetaByKJobId(final String k, final int jobId)
	 *
	 * @param final String k, final int jobId
	 *
	 * @return jobMeta
	 */

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



	/**
	 * updateByJobId (final string area, final int jobId, final List<JobMeta> metaList)
	 *
	 * @param jobId
	 * @param metaList
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByJobId (final String area, final int jobId, final List<JobMeta> metaList) {
		entityManager.createNativeQuery("delete from jobmeta where jobId=:jobId and k like :area").setParameter("jobId", jobId).setParameter("area", area + ".%").executeUpdate();

		for (JobMeta m:metaList) {
			m.setJobId(jobId);
			entityManager.persist(m);
		}
	}


	/**
	 * updateByJobId (final int jobId, final List<JobMeta> metaList)
	 *
	 * @param jobId
	 * @param metaList
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByJobId (final int jobId, final List<JobMeta> metaList) {
		entityManager.createNativeQuery("delete from jobmeta where jobId=:jobId").setParameter("jobId", jobId).executeUpdate();

		for (JobMeta m:metaList) {
			m.setJobId(jobId);
			entityManager.persist(m);
		}
  	}



}

