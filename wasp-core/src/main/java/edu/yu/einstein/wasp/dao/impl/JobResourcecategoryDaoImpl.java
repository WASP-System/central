
/**
 *
 * JobResourcecategoryDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobResourcecategory Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.JobResourcecategory;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class JobResourcecategoryDaoImpl extends WaspDaoImpl<JobResourcecategory> implements edu.yu.einstein.wasp.dao.JobResourcecategoryDao {

	/**
	 * JobResourcecategoryDaoImpl() Constructor
	 *
	 *
	 */
	public JobResourcecategoryDaoImpl() {
		super();
		this.entityClass = JobResourcecategory.class;
	}


	/**
	 * getJobResourcecategoryByJobResourcecategoryId(final Integer jobResourcecategoryId)
	 *
	 * @param final Integer jobResourcecategoryId
	 *
	 * @return jobResourcecategory
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public JobResourcecategory getJobResourcecategoryByJobResourcecategoryId (final Integer jobResourcecategoryId) {
    		HashMap m = new HashMap();
		m.put("jobResourcecategoryId", jobResourcecategoryId);

		List<JobResourcecategory> results = this.findByMap(m);

		if (results.size() == 0) {
			JobResourcecategory rt = new JobResourcecategory();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getJobResourcecategoryByResourcecategoryIdJobId(final Integer resourcecategoryId, final Integer jobId)
	 *
	 * @param final Integer resourcecategoryId, final Integer jobId
	 *
	 * @return jobResourcecategory
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public JobResourcecategory getJobResourcecategoryByResourcecategoryIdJobId (final Integer resourcecategoryId, final Integer jobId) {
    		HashMap m = new HashMap();
		m.put("resourcecategoryId", resourcecategoryId);
		m.put("jobId", jobId);

		List<JobResourcecategory> results = this.findByMap(m);

		if (results.size() == 0) {
			JobResourcecategory rt = new JobResourcecategory();
			return rt;
		}
		return results.get(0);
	}



}

