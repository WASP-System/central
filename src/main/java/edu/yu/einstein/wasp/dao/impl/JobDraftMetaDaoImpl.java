
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
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
	 * getJobDraftMetaByJobDraftMetaId(final int jobDraftMetaId)
	 *
	 * @param final int jobDraftMetaId
	 *
	 * @return jobDraftMeta
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public JobDraftMeta getJobDraftMetaByJobDraftMetaId (final int jobDraftMetaId) {
    		HashMap m = new HashMap();
		m.put("jobDraftMetaId", jobDraftMetaId);

		List<JobDraftMeta> results = (List<JobDraftMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			JobDraftMeta rt = new JobDraftMeta();
			return rt;
		}
		return (JobDraftMeta) results.get(0);
	}



	/**
	 * getJobDraftMetaByKJobdraftId(final String k, final int jobdraftId)
	 *
	 * @param final String k, final int jobdraftId
	 *
	 * @return jobDraftMeta
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public JobDraftMeta getJobDraftMetaByKJobdraftId (final String k, final int jobdraftId) {
    		HashMap m = new HashMap();
		m.put("k", k);
		m.put("jobdraftId", jobdraftId);

		List<JobDraftMeta> results = (List<JobDraftMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			JobDraftMeta rt = new JobDraftMeta();
			return rt;
		}
		return (JobDraftMeta) results.get(0);
	}



	/**
	 * updateByJobdraftId (final string area, final int jobdraftId, final List<JobDraftMeta> metaList)
	 *
	 * @param jobdraftId
	 * @param metaList
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByJobdraftId (final String area, final int jobdraftId, final List<JobDraftMeta> metaList) {

		getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				em.createNativeQuery("delete from jobdraftmeta where jobdraftId=:jobdraftId and k like :area").setParameter("jobdraftId", jobdraftId).setParameter("area", area + ".%").executeUpdate();

				for (JobDraftMeta m:metaList) {
					m.setJobdraftId(jobdraftId);
					em.persist(m);
				}
        			return null;
			}
		});
	}


	/**
	 * updateByJobdraftId (final int jobdraftId, final List<JobDraftMeta> metaList)
	 *
	 * @param jobdraftId
	 * @param metaList
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByJobdraftId (final int jobdraftId, final List<JobDraftMeta> metaList) {

		getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				em.createNativeQuery("delete from jobdraftmeta where jobdraftId=:jobdraftId").setParameter("jobdraftId", jobdraftId).executeUpdate();

				for (JobDraftMeta m:metaList) {
					m.setJobdraftId(jobdraftId);
					em.persist(m);
				}
        			return null;
			}
		});
	}



}

