
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


@Transactional("entityManager")
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
	@Transactional("entityManager")
	public JobDraftMeta getJobDraftMetaByJobDraftMetaId (final Integer jobDraftMetaId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", jobDraftMetaId);

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
	@Transactional("entityManager")
	public JobDraftMeta getJobDraftMetaByKJobDraftId (final String k, final Integer jobDraftId) {
    		HashMap<String, Object> m = new HashMap<String, Object>();
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
	@Transactional("entityManager")
	public void replaceByJobDraftId (final String area, final int jobDraftId, final List<JobDraftMeta> metaList) {
		entityManager.createQuery("DELETE from JobDraftMeta jdm WHERE jdm.jobDraftId=:jobDraftId AND jdm.k LIKE :area").setParameter("jobDraftId", jobDraftId).setParameter("area", area + ".%").executeUpdate();

		for (JobDraftMeta m:metaList) {
			m.setJobDraftId(jobDraftId);
			persist(m);
		}
	}

}

