
/**
 *
 * JobDraftresourceDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftresource Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.JobDraftresource;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class JobDraftresourceDaoImpl extends WaspDaoImpl<JobDraftresource> implements edu.yu.einstein.wasp.dao.JobDraftresourceDao {

	/**
	 * JobDraftresourceDaoImpl() Constructor
	 *
	 *
	 */
	public JobDraftresourceDaoImpl() {
		super();
		this.entityClass = JobDraftresource.class;
	}


	/**
	 * getJobDraftresourceByJobDraftresourceId(final int jobDraftresourceId)
	 *
	 * @param final int jobDraftresourceId
	 *
	 * @return jobDraftresource
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public JobDraftresource getJobDraftresourceByJobDraftresourceId (final int jobDraftresourceId) {
    		HashMap m = new HashMap();
		m.put("jobDraftresourceId", jobDraftresourceId);

		List<JobDraftresource> results = this.findByMap(m);

		if (results.size() == 0) {
			JobDraftresource rt = new JobDraftresource();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getJobDraftresourceByResourceIdJobdraftId(final int resourceId, final int jobdraftId)
	 *
	 * @param final int resourceId, final int jobdraftId
	 *
	 * @return jobDraftresource
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public JobDraftresource getJobDraftresourceByResourceIdJobdraftId (final int resourceId, final int jobdraftId) {
    		HashMap m = new HashMap();
		m.put("resourceId", resourceId);
		m.put("jobdraftId", jobdraftId);

		List<JobDraftresource> results = this.findByMap(m);

		if (results.size() == 0) {
			JobDraftresource rt = new JobDraftresource();
			return rt;
		}
		return results.get(0);
	}



}

