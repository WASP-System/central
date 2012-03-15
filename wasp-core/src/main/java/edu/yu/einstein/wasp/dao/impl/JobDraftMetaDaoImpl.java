
/**
 *
 * JobDraftMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftMeta Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.JobDraftMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class JobDraftMetaDaoImpl extends WaspDaoImpl<JobDraftMeta> implements edu.yu.einstein.wasp.dao.JobDraftMetaDao {

	/**
	 * JobDraftMetaDaoImpl() Constructor
	 *
	 *
	 */
	public JobDraftMetaDaoImpl() {
		super();
		this.entityClass = JobDraftMeta.class;
	}


	/**
	 * getJobDraftMetaByJobDraftMetaId(final Integer jobDraftMetaId)
	 *
	 * @param final Integer jobDraftMetaId
	 *
	 * @return jobDraftMeta
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public JobDraftMeta getJobDraftMetaByJobDraftMetaId (final Integer jobDraftMetaId) {
    		HashMap m = new HashMap();
		m.put("jobDraftMetaId", jobDraftMetaId);

		List<JobDraftMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			JobDraftMeta rt = new JobDraftMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getJobDraftMetaByKJobdraftId(final String k, final Integer jobdraftId)
	 *
	 * @param final String k, final Integer jobdraftId
	 *
	 * @return jobDraftMeta
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public JobDraftMeta getJobDraftMetaByKJobdraftId (final String k, final Integer jobdraftId) {
    		HashMap m = new HashMap();
		m.put("k", k);
		m.put("jobdraftId", jobdraftId);

		List<JobDraftMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			JobDraftMeta rt = new JobDraftMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * updateByJobdraftId (final string area, final int jobdraftId, final List<JobDraftMeta> metaList)
	 *
	 * @param jobdraftId
	 * @param metaList
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByJobdraftId (final String area, final int jobdraftId, final List<JobDraftMeta> metaList) {
		entityManager.createNativeQuery("delete from jobdraftmeta where jobdraftId=:jobdraftId and k like :area").setParameter("jobdraftId", jobdraftId).setParameter("area", area + ".%").executeUpdate();

		for (JobDraftMeta m:metaList) {
			m.setJobdraftId(jobdraftId);
			entityManager.persist(m);
		}
	}


	/**
	 * updateByJobdraftId (final int jobdraftId, final List<JobDraftMeta> metaList)
	 *
	 * @param jobdraftId
	 * @param metaList
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByJobdraftId (final int jobdraftId, final List<JobDraftMeta> metaList) {
		entityManager.createNativeQuery("delete from jobdraftmeta where jobdraftId=:jobdraftId").setParameter("jobdraftId", jobdraftId).executeUpdate();

		for (JobDraftMeta m:metaList) {
			m.setJobdraftId(jobdraftId);
			entityManager.persist(m);
		}
	}



}

