
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
public class JobDraftMetaDaoImpl extends WaspMetaDaoImpl<JobDraftMeta> implements edu.yu.einstein.wasp.dao.JobDraftMetaDao {

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
	 * getJobDraftMetaByKJobDraftId(final String k, final Integer jobDraftId)
	 *
	 * @param final String k, final Integer jobDraftId
	 *
	 * @return jobDraftMeta
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public JobDraftMeta getJobDraftMetaByKJobDraftId (final String k, final Integer jobDraftId) {
    		HashMap m = new HashMap();
		m.put("k", k);
		m.put("jobDraftId", jobDraftId);

		List<JobDraftMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			JobDraftMeta rt = new JobDraftMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * updateByJobDraftId (final string area, final int jobDraftId, final List<JobDraftMeta> metaList)
	 *
	 * @param jobDraftId
	 * @param metaList
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public void replaceByJobDraftId (final String area, final int jobDraftId, final List<JobDraftMeta> metaList) {
		entityManager.createNativeQuery("delete from jobdraftmeta where jobDraftId=:jobDraftId and k like :area").setParameter("jobDraftId", jobDraftId).setParameter("area", area + ".%").executeUpdate();

		for (JobDraftMeta m:metaList) {
			m.setJobDraftId(jobDraftId);
			entityManager.persist(m);
		}
	}


}

