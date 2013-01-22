
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

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.JobSoftware;


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

	@Override
	@Transactional
	public JobSoftware getJobSoftwareByJobSoftwareId (final Integer jobSoftwareId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("jobSoftwareId", jobSoftwareId);

		List<JobSoftware> results = this.findByMap(m);

		if (results.size() == 0) {
			JobSoftware rt = new JobSoftware();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getJobSoftwareBySoftwareIdJobId(final Integer softwareId, final Integer jobId)
	 *
	 * @param final Integer softwareId, final Integer jobId
	 *
	 * @return jobSoftware
	 */

	@Override
	@Transactional
	public JobSoftware getJobSoftwareBySoftwareIdJobId (final Integer softwareId, final Integer jobId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("softwareId", softwareId);
		m.put("jobId", jobId);

		List<JobSoftware> results = this.findByMap(m);

		if (results.size() == 0) {
			JobSoftware rt = new JobSoftware();
			return rt;
		}
		return results.get(0);
	}



}

