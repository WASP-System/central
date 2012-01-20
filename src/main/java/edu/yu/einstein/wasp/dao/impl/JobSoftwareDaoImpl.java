
/**
 *
 * JobSoftwareDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSoftware Dao Impl
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
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.JobSoftware;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class JobSoftwareDaoImpl extends WaspDaoImpl<JobSoftware> implements edu.yu.einstein.wasp.dao.JobSoftwareDao {

	/**
	 * JobSoftwareDaoImpl() Constructor
	 *
	 *
	 */
	public JobSoftwareDaoImpl() {
		super();
		this.entityClass = JobSoftware.class;
	}


	/**
	 * getJobSoftwareByJobSoftwareId(final Integer jobSoftwareId)
	 *
	 * @param final Integer jobSoftwareId
	 *
	 * @return jobSoftware
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public JobSoftware getJobSoftwareByJobSoftwareId (final Integer jobSoftwareId) {
    		HashMap m = new HashMap();
		m.put("jobSoftwareId", jobSoftwareId);

		List<JobSoftware> results = (List<JobSoftware>) this.findByMap((Map) m);

		if (results.size() == 0) {
			JobSoftware rt = new JobSoftware();
			return rt;
		}
		return (JobSoftware) results.get(0);
	}



	/**
	 * getJobSoftwareBySoftwareIdJobId(final Integer softwareId, final Integer jobId)
	 *
	 * @param final Integer softwareId, final Integer jobId
	 *
	 * @return jobSoftware
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public JobSoftware getJobSoftwareBySoftwareIdJobId (final Integer softwareId, final Integer jobId) {
    		HashMap m = new HashMap();
		m.put("softwareId", softwareId);
		m.put("jobId", jobId);

		List<JobSoftware> results = (List<JobSoftware>) this.findByMap((Map) m);

		if (results.size() == 0) {
			JobSoftware rt = new JobSoftware();
			return rt;
		}
		return (JobSoftware) results.get(0);
	}



}

