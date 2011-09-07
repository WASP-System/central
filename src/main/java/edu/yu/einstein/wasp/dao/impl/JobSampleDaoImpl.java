
/**
 *
 * JobSampleDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSample Dao Impl
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

import edu.yu.einstein.wasp.model.JobSample;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class JobSampleDaoImpl extends WaspDaoImpl<JobSample> implements edu.yu.einstein.wasp.dao.JobSampleDao {

	/**
	 * JobSampleDaoImpl() Constructor
	 *
	 *
	 */
	public JobSampleDaoImpl() {
		super();
		this.entityClass = JobSample.class;
	}


	/**
	 * getJobSampleByJobSampleId(final int jobSampleId)
	 *
	 * @param final int jobSampleId
	 *
	 * @return jobSample
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public JobSample getJobSampleByJobSampleId (final int jobSampleId) {
    		HashMap m = new HashMap();
		m.put("jobSampleId", jobSampleId);

		List<JobSample> results = (List<JobSample>) this.findByMap((Map) m);

		if (results.size() == 0) {
			JobSample rt = new JobSample();
			return rt;
		}
		return (JobSample) results.get(0);
	}



	/**
	 * getJobSampleByJobIdSampleId(final int jobId, final int sampleId)
	 *
	 * @param final int jobId, final int sampleId
	 *
	 * @return jobSample
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public JobSample getJobSampleByJobIdSampleId (final int jobId, final int sampleId) {
    		HashMap m = new HashMap();
		m.put("jobId", jobId);
		m.put("sampleId", sampleId);

		List<JobSample> results = (List<JobSample>) this.findByMap((Map) m);

		if (results.size() == 0) {
			JobSample rt = new JobSample();
			return rt;
		}
		return (JobSample) results.get(0);
	}



}

