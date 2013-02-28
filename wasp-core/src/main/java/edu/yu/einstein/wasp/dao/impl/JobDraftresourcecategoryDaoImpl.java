
/**
 *
 * JobDraftresourcecategoryDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftresourcecategory Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.JobDraftresourcecategory;


@Transactional
@Repository
public class JobDraftresourcecategoryDaoImpl extends WaspDaoImpl<JobDraftresourcecategory> implements edu.yu.einstein.wasp.dao.JobDraftresourcecategoryDao {

	/**
	 * JobDraftresourcecategoryDaoImpl() Constructor
	 *
	 *
	 */
	public JobDraftresourcecategoryDaoImpl() {
		super();
		this.entityClass = JobDraftresourcecategory.class;
	}


	/**
	 * getJobDraftresourcecategoryByJobDraftresourcecategoryId(final Integer jobDraftresourcecategoryId)
	 *
	 * @param final Integer jobDraftresourcecategoryId
	 *
	 * @return jobDraftresourcecategory
	 */

	@Override
	@Transactional
	public JobDraftresourcecategory getJobDraftresourcecategoryByJobDraftresourcecategoryId (final Integer jobDraftresourcecategoryId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", jobDraftresourcecategoryId);

		List<JobDraftresourcecategory> results = this.findByMap(m);

		if (results.size() == 0) {
			JobDraftresourcecategory rt = new JobDraftresourcecategory();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getJobDraftresourcecategoryByResourcecategoryIdJobDraftId(final Integer resourcecategoryId, final Integer jobDraftId)
	 *
	 * @param final Integer resourcecategoryId, final Integer jobDraftId
	 *
	 * @return jobDraftresourcecategory
	 */

	@Override
	@Transactional
	public JobDraftresourcecategory getJobDraftresourcecategoryByResourcecategoryIdJobDraftId (final Integer resourcecategoryId, final Integer jobDraftId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("resourcecategoryId", resourcecategoryId);
		m.put("jobDraftId", jobDraftId);

		List<JobDraftresourcecategory> results = this.findByMap(m);

		if (results.size() == 0) {
			JobDraftresourcecategory rt = new JobDraftresourcecategory();
			return rt;
		}
		return results.get(0);
	}



}

