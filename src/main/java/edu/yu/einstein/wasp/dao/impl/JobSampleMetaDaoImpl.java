
/**
 *
 * JobSampleMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSampleMeta Dao Impl
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

import edu.yu.einstein.wasp.model.JobSampleMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class JobSampleMetaDaoImpl extends WaspDaoImpl<JobSampleMeta> implements edu.yu.einstein.wasp.dao.JobSampleMetaDao {

	/**
	 * JobSampleMetaDaoImpl() Constructor
	 *
	 *
	 */
	public JobSampleMetaDaoImpl() {
		super();
		this.entityClass = JobSampleMeta.class;
	}


	/**
	 * getJobSampleMetaByJobSampleMetaId(final int jobSampleMetaId)
	 *
	 * @param final int jobSampleMetaId
	 *
	 * @return jobSampleMeta
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public JobSampleMeta getJobSampleMetaByJobSampleMetaId (final int jobSampleMetaId) {
    		HashMap m = new HashMap();
		m.put("jobSampleMetaId", jobSampleMetaId);

		List<JobSampleMeta> results = (List<JobSampleMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			JobSampleMeta rt = new JobSampleMeta();
			return rt;
		}
		return (JobSampleMeta) results.get(0);
	}



	/**
	 * getJobSampleMetaByKJobsampleId(final String k, final int jobsampleId)
	 *
	 * @param final String k, final int jobsampleId
	 *
	 * @return jobSampleMeta
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public JobSampleMeta getJobSampleMetaByKJobsampleId (final String k, final int jobsampleId) {
    		HashMap m = new HashMap();
		m.put("k", k);
		m.put("jobsampleId", jobsampleId);

		List<JobSampleMeta> results = (List<JobSampleMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			JobSampleMeta rt = new JobSampleMeta();
			return rt;
		}
		return (JobSampleMeta) results.get(0);
	}



	/**
	 * updateByJobsampleId (final int jobsampleId, final List<JobSampleMeta> metaList)
	 *
	 * @param jobsampleId
	 * @param metaList
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByJobsampleId (final int jobsampleId, final List<JobSampleMeta> metaList) {

		getJpaTemplate().execute(new JpaCallback() {

			public Object doInJpa(EntityManager em) throws PersistenceException {
				em.createNativeQuery("delete from jobSampleMeta where jobsampleId=:jobsampleId").setParameter("jobsampleId", jobsampleId).executeUpdate();

				for (JobSampleMeta m:metaList) {
					m.setJobsampleId(jobsampleId);
					em.persist(m);
				}
        			return null;
			}
		});
	}



}

