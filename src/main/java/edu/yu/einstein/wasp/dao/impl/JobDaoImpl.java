
/**
 *
 * JobDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Job Dao Impl
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

import edu.yu.einstein.wasp.model.Job;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class JobDaoImpl extends WaspDaoImpl<Job> implements edu.yu.einstein.wasp.dao.JobDao {

	/**
	 * JobDaoImpl() Constructor
	 *
	 *
	 */
	public JobDaoImpl() {
		super();
		this.entityClass = Job.class;
	}


	/**
	 * getJobByJobId(final int jobId)
	 *
	 * @param final int jobId
	 *
	 * @return job
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public Job getJobByJobId (final int jobId) {
    		HashMap m = new HashMap();
		m.put("jobId", jobId);

		List<Job> results = (List<Job>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Job rt = new Job();
			return rt;
		}
		return (Job) results.get(0);
	}



	/**
	 * getJobByNameLabId(final String name, final int labId)
	 *
	 * @param final String name, final int labId
	 *
	 * @return job
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public Job getJobByNameLabId (final String name, final int labId) {
    		HashMap m = new HashMap();
		m.put("name", name);
		m.put("labId", labId);

		List<Job> results = (List<Job>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Job rt = new Job();
			return rt;
		}
		return (Job) results.get(0);
	}



}

