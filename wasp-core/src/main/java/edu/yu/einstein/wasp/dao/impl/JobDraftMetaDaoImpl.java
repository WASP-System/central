
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
	@Transactional
	public JobDraftMeta getJobDraftMetaByJobDraftMetaId (final Integer jobDraftMetaId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
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
	@Transactional
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
	@Transactional
	public void replaceByJobDraftId (final String area, final int jobDraftId, final List<JobDraftMeta> metaList) {
		entityManager.createNativeQuery("delete from jobdraftmeta where jobDraftId=:jobDraftId and k like :area").setParameter("jobDraftId", jobDraftId).setParameter("area", area + ".%").executeUpdate();

		for (JobDraftMeta m:metaList) {
			m.setJobDraftId(jobDraftId);
			entityManager.persist(m);
		}
	}


	/**
	 * updateByJobDraftId (final int jobDraftId, final List<JobDraftMeta> metaList)
	 *
	 * @param jobDraftId
	 * @param metaList
	 *
	 */
	@Override
	@Transactional
	public void updateByJobDraftId (final int jobDraftId, final List<JobDraftMeta> metaList) {
		for (JobDraftMeta m:metaList) {
			JobDraftMeta currentMeta = getJobDraftMetaByKJobDraftId(m.getK(), jobDraftId);
			if (currentMeta.getJobDraftMetaId() == null){
				// metadata value not in database yet
				m.setJobDraftId(jobDraftId);
				entityManager.persist(m);
			} else if (!currentMeta.getV().equals(m.getV())){
				// meta exists already but value has changed
				currentMeta.setV(m.getV());
				entityManager.merge(currentMeta);
			} else{
				// no change to meta so do nothing
			}
		}
	}



}

