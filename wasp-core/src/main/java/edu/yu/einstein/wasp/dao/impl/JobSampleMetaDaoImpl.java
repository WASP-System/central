
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

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.JobSampleMeta;


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

	@Override
	@Transactional
	public JobSampleMeta getJobSampleMetaByJobSampleMetaId (final int jobSampleMetaId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("jobSampleMetaId", jobSampleMetaId);

		List<JobSampleMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			JobSampleMeta rt = new JobSampleMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getJobSampleMetaByKJobsampleId(final String k, final int jobsampleId)
	 *
	 * @param final String k, final int jobsampleId
	 *
	 * @return jobSampleMeta
	 */

	@Override
	@Transactional
	public JobSampleMeta getJobSampleMetaByKJobsampleId (final String k, final int jobsampleId) {
    		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("k", k);
		m.put("jobsampleId", jobsampleId);

		List<JobSampleMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			JobSampleMeta rt = new JobSampleMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * updateByJobsampleId (final int jobsampleId, final List<JobSampleMeta> metaList)
	 *
	 * @param jobsampleId
	 * @param metaList
	 *
	 */
	@Override
	@Transactional
	public void updateByJobsampleId (final int jobsampleId, final List<JobSampleMeta> metaList) {
		for (JobSampleMeta m:metaList) {
			JobSampleMeta currentMeta = getJobSampleMetaByKJobsampleId(m.getK(), jobsampleId);
			if (currentMeta.getJobSampleMetaId() == null){
				// metadata value not in database yet
				m.setJobsampleId(jobsampleId);
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

