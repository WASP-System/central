
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

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.JobSample;


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

	@Override
	@Transactional
	public JobSample getJobSampleByJobSampleId (final int jobSampleId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("jobSampleId", jobSampleId);

		List<JobSample> results = this.findByMap(m);

		if (results.size() == 0) {
			JobSample rt = new JobSample();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getJobSampleByJobIdSampleId(final int jobId, final int sampleId)
	 *
	 * @param final int jobId, final int sampleId
	 *
	 * @return jobSample
	 */

	@Override
	@Transactional
	public JobSample getJobSampleByJobIdSampleId (final int jobId, final int sampleId) {
    	HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("jobId", jobId);
		m.put("sampleId", sampleId);

		List<JobSample> results = this.findByMap(m);

		if (results.size() == 0) {
			JobSample rt = new JobSample();
			return rt;
		}
		return results.get(0);
	}
	
	/**
	 * getJobSampleByJobId(final int jobId)
	 *
	 * @param final int jobId
	 *
	 * @return jobSample
	 */

	@Override
	@Transactional
	public List<JobSample> getJobSampleByJobId(final int jobId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("jobId", jobId);

		return (List<JobSample>) this.findByMap(m);

	}

	
}

