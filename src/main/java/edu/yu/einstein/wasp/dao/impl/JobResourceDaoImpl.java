
/**
 *
 * JobResourceDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobResource Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.JobResource;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class JobResourceDaoImpl extends WaspDaoImpl<JobResource> implements edu.yu.einstein.wasp.dao.JobResourceDao {

	/**
	 * JobResourceDaoImpl() Constructor
	 *
	 *
	 */
	public JobResourceDaoImpl() {
		super();
		this.entityClass = JobResource.class;
	}


	/**
	 * getJobResourceByJobResourceId(final int jobResourceId)
	 *
	 * @param final int jobResourceId
	 *
	 * @return jobResource
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public JobResource getJobResourceByJobResourceId (final int jobResourceId) {
    		HashMap m = new HashMap();
		m.put("jobResourceId", jobResourceId);

		List<JobResource> results = this.findByMap(m);

		if (results.size() == 0) {
			JobResource rt = new JobResource();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getJobResourceByResourceIdJobId(final int resourceId, final int jobId)
	 *
	 * @param final int resourceId, final int jobId
	 *
	 * @return jobResource
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public JobResource getJobResourceByResourceIdJobId (final int resourceId, final int jobId) {
    		HashMap m = new HashMap();
		m.put("resourceId", resourceId);
		m.put("jobId", jobId);

		List<JobResource> results = this.findByMap(m);

		if (results.size() == 0) {
			JobResource rt = new JobResource();
			return rt;
		}
		return results.get(0);
	}



}

