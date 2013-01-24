
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

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.JobMeta;


@Transactional
@Repository
public class JobMetaDaoImpl extends WaspMetaDaoImpl<JobMeta> implements edu.yu.einstein.wasp.dao.JobMetaDao {

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

	@Override
	@Transactional
	public JobMeta getJobMetaByJobMetaId (final int jobMetaId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("jobMetaId", jobMetaId);

		List<JobMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			JobMeta rt = new JobMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getJobMetaByKJobId(final String k, final int jobId)
	 *
	 * @param final String k, final int jobId
	 *
	 * @return jobMeta
	 */

	@Override
	@Transactional
	public JobMeta getJobMetaByKJobId (final String k, final int jobId) {
    		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("k", k);
		m.put("jobId", jobId);

		List<JobMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			JobMeta rt = new JobMeta();
			return rt;
		}
		return results.get(0);
	}

}

