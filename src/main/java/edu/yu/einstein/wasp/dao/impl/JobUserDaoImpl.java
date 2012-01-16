
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
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.JobUser;

@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
	@Transactional
	public JobUser getJobUserByJobUserId (final int jobUserId) {
    		HashMap m = new HashMap();
		m.put("jobUserId", jobUserId);

		List<JobUser> results = (List<JobUser>) this.findByMap((Map) m);

		if (results.size() == 0) {
			JobUser rt = new JobUser();
			return rt;
		}
		return (JobUser) results.get(0);
	}



	/**
	 * getJobUserByJobIdUserId(final int jobId, final int UserId)
	 *
	 * @param final int jobId, final int UserId
	 *
	 * @return jobUser
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public JobUser getJobUserByJobIdUserId (final int jobId, final int UserId) {
    		HashMap m = new HashMap();
		m.put("jobId", jobId);
		m.put("UserId", UserId);

		List<JobUser> results = (List<JobUser>) this.findByMap((Map) m);

		if (results.size() == 0) {
			JobUser rt = new JobUser();
			return rt;
		}
		return (JobUser) results.get(0);
	}



}

