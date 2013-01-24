
/**
 *
 * JobUserDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobUser Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.JobUser;


@Transactional
@Repository
public class JobUserDaoImpl extends WaspDaoImpl<JobUser> implements edu.yu.einstein.wasp.dao.JobUserDao {

	/**
	 * JobUserDaoImpl() Constructor
	 *
	 *
	 */
	public JobUserDaoImpl() {
		super();
		this.entityClass = JobUser.class;
	}


	/**
	 * getJobUserByJobUserId(final int jobUserId)
	 *
	 * @param final int jobUserId
	 *
	 * @return jobUser
	 */

	@Override
	@Transactional
	public JobUser getJobUserByJobUserId (final int jobUserId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("jobUserId", jobUserId);

		List<JobUser> results = this.findByMap(m);

		if (results.size() == 0) {
			JobUser rt = new JobUser();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getJobUserByJobIdUserId(final int jobId, final int UserId)
	 *
	 * @param final int jobId, final int UserId
	 *
	 * @return jobUser
	 */

	@Override
	@Transactional
	public JobUser getJobUserByJobIdUserId (final int jobId, final int UserId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("jobId", jobId);
		m.put("UserId", UserId);

		List<JobUser> results = this.findByMap(m);

		if (results.size() == 0) {
			JobUser rt = new JobUser();
			return rt;
		}
		return results.get(0);
	}



}

